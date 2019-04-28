package com.fescacomit.service.gmail.auth;

import com.google.api.services.gmail.Gmail;

public interface GmailAuthorizationService {
    Gmail getGmailService();
}
