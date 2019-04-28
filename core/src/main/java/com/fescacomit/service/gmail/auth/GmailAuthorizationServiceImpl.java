package com.fescacomit.service.gmail.auth;

import com.fescacomit.service.gmail.utilties.Utilities;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.Gmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;

import static com.fescacomit.service.gmail.constants.GmailConstants.*;

@Service
public class GmailAuthorizationServiceImpl implements GmailAuthorizationService {

    private static final Logger log = LoggerFactory.getLogger(GmailAuthorizationServiceImpl.class);

    private Gmail gmailService;

    public GmailAuthorizationServiceImpl() {
        // Start Authorization process
        try {
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();
            dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
            gmailService = new Gmail(httpTransport, JSON_FACTORY, authorize());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Authorizes the installed application to access user's protected data.
     */
    private static Credential authorize() throws Exception {
        log.info("Starting the authorization process");
        // initialize client secrets object
        GoogleClientSecrets clientSecrets;
        Utilities.initializeProxyAuthenticator();

        // load client secrets
        InputStreamReader credentialReader = new InputStreamReader(
                GmailAuthorizationServiceImpl.class
                        .getResourceAsStream("/client_id.json"));
        clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, credentialReader);
        exitIfSecretsIsNotOk(clientSecrets);

        // set up authorization code flow
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow
                                                       .Builder(
                httpTransport,
                JSON_FACTORY,
                clientSecrets,
                SCOPES)
                                                   .setDataStoreFactory(dataStoreFactory)
                                                   .build();
        // authorize
        log.info("Finishing the authorization process");
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    private static void exitIfSecretsIsNotOk(GoogleClientSecrets clientSecrets) {
        if (clientSecrets.getDetails().getClientId().startsWith("Enter")
            || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
            //TODO: Add a proper log here
            System.out.println("Enter Client ID and Secret from https://code.google.com/apis/console/ "
                               + "into compute-engine-cmdline-sample/src/main/resources/client_id.json");
            System.exit(1);
        }
    }

    @Override
    public Gmail getGmailService() {
        return gmailService;
    }
}
