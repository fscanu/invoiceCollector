package com.fescacomit.service.gmail.constants;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.gmail.GmailScopes;

import java.util.Arrays;
import java.util.List;

public class GmailConstants {

    /**
     * Directory to store user credentials.
     */
    public static final java.io.File DATA_STORE_DIR =
            new java.io.File(System.getProperty("java.io.tmpdir"), ".store/compute_engine_sample");

    /**
     * Directory to store attachments.
     */
    public static final java.io.File ATTACHMENT_STORE_DIR =
            new java.io.File(System.getProperty("java.io.tmpdir"), "attachments");

    /**
     * Default user id
     */
    public static final String USER_ID = "me";

    /**
     * Global instance of the JSON factory.
     */
    public static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

    /**
     * OAuth 2.0 scopes
     */
    public static final List<String> SCOPES = Arrays.asList(GmailScopes.GMAIL_READONLY);

    public static final int CONNECT_TIMEOUT = 10000;
    public static final int READ_TIMEOUT = 10000;

    public static final int SAME_MONTH = 0;
    public static final int PREVIOUS_MONTH = -1;

    /**
     * Global instance of the {@link DataStoreFactory}. The best practice is to make it a single
     * globally shared instance across your application.
     */
    public static FileDataStoreFactory dataStoreFactory;

    /**
     * Global instance of the HTTP transport.
     */
    public static HttpTransport httpTransport;
}
