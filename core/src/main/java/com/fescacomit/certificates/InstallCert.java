package com.fescacomit.certificates;
/*
 * Copyright 2006 Sun Microsystems, Inc.  All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Sun Microsystems nor the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.*;
import java.net.Socket;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class InstallCert {

    public static final boolean AUTOCLOSE_TRUE = true;

    private static final Logger log = LoggerFactory.getLogger(InstallCert.class);

    private static final char[] HEXDIGITS = "0123456789abcdef".toCharArray();

    private static class SavingTrustManager implements X509TrustManager {

        private final X509TrustManager tm;

        private X509Certificate[] chain;

        SavingTrustManager(X509TrustManager tm) {
            this.tm = tm;
        }

        public X509Certificate[] getAcceptedIssuers() {
            throw new UnsupportedOperationException();
        }

        public void checkClientTrusted(X509Certificate[] chain, String authType) {
            throw new UnsupportedOperationException();
        }

        public void checkServerTrusted(X509Certificate[] chain, String authType)
                throws CertificateException {
            this.chain = chain;
            tm.checkServerTrusted(chain, authType);
        }
    }

    public static void main(String[] args) throws Exception {
        String host = "doc.my-documents.be";
        int port = 443;
        char[] passphrase = "changeit".toCharArray();

        System.setProperty("javax.net.debug", "all");
        System.setProperty("com.sun.net.ssl.rsaPreMasterSecretFix", "true");

        File file = new File("jssecacerts");
        if (file.isFile() == false) {
            char SEP = File.separatorChar;
            File dir = new File(System.getProperty("java.home") + SEP
                                        + "lib" + SEP + "security");
            file = new File(dir, "jssecacerts");
            if (file.isFile() == false) {
                file = new File(dir, "cacerts");
            }
        }
        log.info("Loading KeyStore " + file + "...");
        InputStream in = new FileInputStream(file);
        KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
        ks.load(in, passphrase);
        in.close();

        SSLContext context = SSLContext.getInstance("TLS");
        TrustManagerFactory tmf =
                TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(ks);
        X509TrustManager defaultTrustManager = (X509TrustManager) tmf.getTrustManagers()[0];
        SavingTrustManager tm = new SavingTrustManager(defaultTrustManager);
        context.init(null, new TrustManager[] { tm }, null);

        SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        final SSLSocket  socket  = getSslSocket(host, port, context, factory);
        try {
            log.info("Starting SSL handshake...");
            socket.startHandshake();
            socket.close();

            log.info("No errors, certificate is already trusted");
        }
        catch (SSLException e) {
            log.error(e.getMessage());
        }

        X509Certificate[] chain = tm.chain;
        if (chain == null) {
            log.info("Could not obtain server certificate chain");
            return;
        }

        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));

        log.info("Server sent " + chain.length + " certificate(s):");
        MessageDigest sha1 = MessageDigest.getInstance("SHA1");
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        for (int i = 0; i < chain.length; i++) {
            X509Certificate cert = chain[i];
            log.info
                    (" " + (i + 1) + " Subject " + cert.getSubjectDN());
            log.info("   Issuer  " + cert.getIssuerDN());
            sha1.update(cert.getEncoded());
            log.info("   sha1    " + toHexString(sha1.digest()));
            md5.update(cert.getEncoded());
            log.info("   md5     " + toHexString(md5.digest()));
        }

        log.info("Enter certificate to add to trusted keystore or 'q' to quit: [1]");
        String line = reader.readLine().trim();
        int k;
        try {
            k = (line.length() == 0) ? 0 : Integer.parseInt(line) - 1;
        }
        catch (NumberFormatException e) {
            log.info("KeyStore not changed");
            return;
        }

        X509Certificate cert = chain[k];
        String alias = host + "-" + (k + 1);
        ks.setCertificateEntry(alias, cert);

        OutputStream out = new FileOutputStream("jssecacerts");
        ks.store(out, passphrase);
        out.close();

        log.info(cert.toString());
        log.info
                ("Added certificate to keystore 'jssecacerts' using alias '"
                         + alias + "'");
    }

    private static SSLSocket getSslSocket(String host, int port, SSLContext context, SSLSocketFactory factory)
            throws IOException {
        SSLSocket     socket;
        final String  proxyHost = System.getProperty("https.proxyHost");
        final Integer proxyPort = Integer.getInteger("https.proxyPort");
        if (proxyHost != null && proxyPort != null) {
            final Socket tunnel        = new Socket(proxyHost, proxyPort);
            final String proxyUser = System.getProperty("https.proxyUser");
            final String proxyPassword = System.getProperty("https.proxyPassword");
            doTunnelHandshake(tunnel, proxyHost, proxyPort, proxyUser, proxyPassword);

            socket = (SSLSocket) factory.createSocket(tunnel, host, port, AUTOCLOSE_TRUE);

            /*
             * register a callback for handshaking completion event
             */
            socket.addHandshakeCompletedListener(
                    new HandshakeCompletedListener() {
                        public void handshakeCompleted(
                                HandshakeCompletedEvent event) {
                            System.out.println("Handshake finished!");
                            System.out.println(
                                    "\t CipherSuite:" + event.getCipherSuite());
                            System.out.println(
                                    "\t SessionId " + event.getSession());
                            System.out.println(
                                    "\t PeerHost " + event.getSession().getPeerHost());
                        }
                    }
                                                );
        }
        else {
            factory = context.getSocketFactory();
            log.info("Opening connection to " + host + ":" + port + "...");
            socket = (SSLSocket) factory.createSocket(host, port);
        }
        socket.setSoTimeout(10000);
        return socket;
    }

    private static String toHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * 3);
        for (int b : bytes) {
            b &= 0xff;
            sb.append(HEXDIGITS[b >> 4]);
            sb.append(HEXDIGITS[b & 15]);
            sb.append(' ');
        }
        return sb.toString();
    }

    private static void doTunnelHandshake(Socket tunnel, String proxyHost, int proxyPort, String username, String password)
            throws IOException {

        String encoded = Base64.encodeBase64String((username + ":" + password).getBytes()).replace("\r\n", "");
        OutputStream out = tunnel.getOutputStream();
        StringBuilder msg = new StringBuilder();
        msg.append("CONNECT ").append(proxyHost).append(":").append(proxyPort).append(" HTTP/1.0\n");
        msg.append("Proxy-Authorization").append(": ").append("Basic ").append(encoded);
        msg.append("\r\n\r\n");

        byte b[];
        try {
            /*
             * We really do want ASCII7 -- the http protocol doesn't change
             * with locale.
             */
            b = msg.toString().getBytes("ASCII7");
        }
        catch (UnsupportedEncodingException ignored) {
            /*
             * If ASCII7 isn't there, something serious is wrong, but
             * Paranoia Is Good (tm)
             */
            b = msg.toString().getBytes();
        }
        out.write(b);
        out.flush();

        /*
         * We need to store the reply so we can create a detailed
         * error message to the user.
         */
        byte reply[] = new byte[200];
        int replyLen = 0;
        int newlinesSeen = 0;
        boolean headerDone = false;     /* Done on first newline */

        InputStream in = tunnel.getInputStream();

        while (newlinesSeen < 2) {
            int i = in.read();
            if (i < 0) {
                throw new IOException("Unexpected EOF from proxy");
            }
            if (i == '\n') {
                headerDone = true;
                ++newlinesSeen;
            }
            else if (i != '\r') {
                newlinesSeen = 0;
                if (!headerDone && replyLen < reply.length) {
                    reply[replyLen++] = (byte) i;
                }
            }
        }

        /*
         * Converting the byte array to a string is slightly wasteful
         * in the case where the connection was successful, but it's
         * insignificant compared to the network overhead.
         */
        String replyStr;
        try {
            replyStr = new String(reply, 0, replyLen, "ASCII7");
        }
        catch (UnsupportedEncodingException ignored) {
            replyStr = new String(reply, 0, replyLen);
        }

        /* We check for Connection Established because our proxy returns
         * HTTP/1.1 instead of 1.0 */
        //if (!replyStr.startsWith("HTTP/1.0 200")) {
        if (!replyStr.toLowerCase().contains("200 connection established")) {
            throw new IOException("Unable to tunnel through "
                                          + proxyHost + ":" + proxyPort
                                          + ".  Proxy returns \"" + replyStr + "\"");
        }

        /* tunneling Handshake was successful! */
    }

}