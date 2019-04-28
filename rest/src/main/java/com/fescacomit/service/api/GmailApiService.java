package com.fescacomit.service.api;

import com.fescacomit.service.exceptions.DownloadException;
import com.fescacomit.service.exceptions.LinkNotFoundException;
import com.fescacomit.service.gmail.messages.GmailMessageService;
import com.fescacomit.service.gmail.utilties.Utilities;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Label;
import com.google.api.services.gmail.model.Message;

import java.io.IOException;
import java.util.List;

public interface GmailApiService {

    List<Message> listMessagesMatchingQuery(Gmail service,
                                            String userId,
                                            String query) throws IOException;

    List<Message> listMessagesWithLabelsAndQueries(Gmail service,
                                                   String userId,
                                                   String query,
                                                   List<String> labelIds) throws IOException;

    void getAttachmentByLinkText(Gmail service,
                                 GmailMessageService messageService,
                                 Utilities utilities, String linkText,
                                 String label,
                                 String userId,
                                 String messageId,
                                 int monthOffset) throws LinkNotFoundException, DownloadException;

    List<Label> fetchLabels(Gmail service,
                            String userId);

    List<Message> listMessagesWithLabels(Gmail service,
                                         String userId,
                                         List<String> labelIds) throws IOException;
}
