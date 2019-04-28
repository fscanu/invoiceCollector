package com.fescacomit.service.gmail.messages;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;

public interface GmailMessageService {

    Message getMessage(Gmail service, String userId, String messageId);


    void getAttachments(Gmail service, String userId, String label, String messageId);
}
