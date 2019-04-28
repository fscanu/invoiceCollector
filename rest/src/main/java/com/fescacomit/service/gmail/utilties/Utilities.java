package com.fescacomit.service.gmail.utilties;

import com.fescacomit.service.exceptions.DownloadException;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.*;

import static com.fescacomit.service.gmail.constants.GmailConstants.CONNECT_TIMEOUT;
import static com.fescacomit.service.gmail.constants.GmailConstants.READ_TIMEOUT;
import static org.apache.commons.io.FileUtils.copyInputStreamToFile;

@Component
public class Utilities {

    private static final Logger log = LoggerFactory.getLogger(Utilities.class);

    public void downloadAttachment(String url, String localFilename) throws DownloadException {
        try {
            final URL source = new URL(url);
            final File destination = new File(localFilename);

            final String proxyHost = System.getProperty("https.proxyHost");
            final String proxyPort = System.getProperty("https.proxyPort");

            if (proxyHost != null && proxyPort != null) {
                initializeProxyAuthenticator();
                copyUrlToFileThroughProxy(source, destination, proxyHost, proxyPort);
            } else {
                log.info(" NO proxy");
                FileUtils.copyURLToFile(source, destination, CONNECT_TIMEOUT, READ_TIMEOUT);
            }

        } catch (IOException e) {
            throw new DownloadException(e.getMessage());
        }

    }

    private static void copyUrlToFileThroughProxy(URL source, File destination, String proxyHost, String proxyPort) throws IOException {
        log.info("going through proxy");
        SocketAddress addr = new InetSocketAddress(proxyHost, Integer.valueOf(proxyPort));
        Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
        URLConnection connection = source.openConnection(proxy);
        connection.setConnectTimeout(CONNECT_TIMEOUT);
        connection.setReadTimeout(READ_TIMEOUT);
        copyInputStreamToFile(connection.getInputStream(), destination);
    }

    public static void initializeProxyAuthenticator() {
        final String proxyUser = System.getProperty("https.proxyUser");
        final String proxyPassword = System.getProperty("https.proxyPassword");

        /*Now, proxies requiring Basic authentication when setting up a tunnel for HTTPS will no longer succeed by default.
        If required, this authentication scheme can be reactivated by removing Basic from the
        jdk.http.auth.tunneling.disabledSchemes networking property, or by setting a system property of the same name to ""
        ( empty ) on the command line.*/
        System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "");
        System.setProperty("jdk.http.auth.proxying.disabledSchemes", "");

        if (proxyUser != null && proxyPassword != null) {
            Authenticator.setDefault(
                    new Authenticator() {
                        public PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(
                                    proxyUser, proxyPassword.toCharArray()
                            );
                        }
                    });
        }
    }
}
