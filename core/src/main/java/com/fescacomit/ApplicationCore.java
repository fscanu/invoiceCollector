package com.fescacomit;

import com.fescacomit.service.api.GmailApiService;
import com.fescacomit.service.exceptions.DownloadException;
import com.fescacomit.service.exceptions.LinkNotFoundException;
import com.fescacomit.service.gmail.auth.GmailAuthorizationService;
import com.fescacomit.service.gmail.messages.GmailMessageService;
import com.fescacomit.service.gmail.utilties.Utilities;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.List;

import static com.fescacomit.service.gmail.constants.GmailConstants.*;

@SpringBootApplication
public class ApplicationCore {

    private static final Logger log = LoggerFactory.getLogger(ApplicationCore.class);

    public static void main(String[] args) {
        SpringApplication.run(ApplicationCore.class);
    }

    @Bean
    public CommandLineRunner demo(GmailApiService gmailApiService, GmailMessageService gmailMessageService,
                                  GmailAuthorizationService authorizationService, Utilities utilities) {
        return args -> {

            //load the trusted keystore
            System.setProperty("javax.net.ssl.trustStore", "jssecacerts");

            Utilities.initializeProxyAuthenticator();
            Gmail         authService = authorizationService.getGmailService();
            List<Message> messages;

            /*Get messages by Label ID*/
            List<String> labelIds = new ArrayList<>();
            labelIds.add("Label_2");
            final String label = "GOOGLE";
            messages = gmailApiService.listMessagesWithLabelsAndQueries(authService, USER_ID, "filename:pdf", labelIds);
            messages.forEach(message -> {
                gmailMessageService.getAttachments(authService, USER_ID, label, message.getId());
            });

            /*Get messages with Label DATS 24 and text contained in the body*/
            final String linkDats24Text  = "Consultez ici votre aperçu";
            final String labelDats24Text = "DATS 24";
            final String queryDats24     = "label:" + labelDats24Text + " AND " + linkDats24Text;
            messages = gmailApiService.listMessagesMatchingQuery(authService, USER_ID, queryDats24);

            messages.forEach(message -> {
                try {
                    gmailApiService
                            .getAttachmentByLinkText(authService,
                                                     gmailMessageService,
                                                     utilities,
                                                     linkDats24Text,
                                                     labelDats24Text,
                                                     USER_ID,
                                                     message.getId(),
                                                     PREVIOUS_MONTH);
                } catch (LinkNotFoundException | DownloadException e) {
                    e.printStackTrace();
                }
            });

            /*Get messages with Label UBER and text contained in the body*/
            final String linkUberText  = "Download PDF";
            final String labelUberText = "UBER";
            final String queryUber     = "label:" + labelUberText + " AND " + linkUberText;
            messages = gmailApiService.listMessagesMatchingQuery(authService, USER_ID, queryUber);

            forEachMessageGetAttachment(gmailApiService,
                                        gmailMessageService,
                                        utilities,
                                        authService,
                                        messages,
                                        linkUberText,
                                        labelUberText);
            /*Get messages with Label VOO and text contained in the body*/

            final String labelVOO = "VOO";

            final String linkVOOText    = "Télécharger ma facture Pack";
            final String prefixFilename = "VOO internet";
            final String queryVOO       = "label:" + labelVOO + " AND " + linkVOOText;

            messages = gmailApiService.listMessagesMatchingQuery(authService, USER_ID, queryVOO);

            forEachMessageGetAttachment(gmailApiService,
                                        gmailMessageService,
                                        utilities,
                                        authService,
                                        messages,
                                        linkVOOText,
                                        prefixFilename);


            /*Get messages with Label VOO and text contained in the body*/
            final String linkVOOMobileText  = "Télécharger ma facture Mobile";
            final String labelVOOMobileText = "VOO mobile";
            final String queryMobileVOO     = "label:" + labelVOO + " AND " + linkVOOMobileText;

            messages = gmailApiService.listMessagesMatchingQuery(authService, USER_ID, queryMobileVOO);

            forEachMessageGetAttachment(gmailApiService,
                                        gmailMessageService,
                                        utilities,
                                        authService,
                                        messages,
                                        linkVOOMobileText,
                                        labelVOOMobileText);
        };
    }

    private void forEachMessageGetAttachment(GmailApiService gmailApiService, GmailMessageService gmailMessageService, Utilities utilities, Gmail authService, List<Message> messages, String linkVOOText, String prefixFilename) {
        messages.forEach(message -> {
            try {
                gmailApiService.getAttachmentByLinkText(authService,
                                                        gmailMessageService,
                                                        utilities,
                                                        linkVOOText,
                                                        prefixFilename,
                                                        USER_ID,
                                                        message.getId(),
                                                        SAME_MONTH);
            } catch (LinkNotFoundException | DownloadException e) {
                e.printStackTrace();
            }
        });
    }
}
