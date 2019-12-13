package com.fescacomit.service.api;

import com.fescacomit.service.exceptions.DownloadException;
import com.fescacomit.service.exceptions.LabelsNotFoundException;
import com.fescacomit.service.exceptions.LinkNotFoundException;
import com.fescacomit.service.gmail.messages.GmailMessageService;
import com.fescacomit.service.gmail.utilties.Utilities;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.client.repackaged.org.apache.commons.codec.binary.StringUtils;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static com.fescacomit.service.gmail.constants.GmailConstants.ATTACHMENT_STORE_DIR;

@Component
public class GmailApiServiceImpl implements GmailApiService {
    private static final Logger log = LoggerFactory.getLogger(GmailApiServiceImpl.class);


    /**
     * Get Message with given ID.
     *
     * @param service     Authorized Gmail API instance.
     * @param utilities
     * @param linkText    The text value of the Href.
     * @param label       The label to apply to the filename.
     * @param userId      User's email address. The special value "me"
     *                    can be used to indicate the authenticated user.
     * @param messageId   ID of Message to retrieve.
     * @param monthOffset usually when the invoice arrives refers not at the same month of the message,this parameter
     */
    public void getAttachmentByLinkText(
            Gmail service,
            GmailMessageService messageService,
            Utilities utilities,
            String linkText,
            String label,
            String userId,
            String messageId,
            int monthOffset) throws LinkNotFoundException, DownloadException {

        Message messageFull = messageService.getMessage(service, userId, messageId);
        String hrefLinkByHrefValue = getHrefLinkByHrefValue(linkText, messageFull);
        String lastHrefRedirected = followRedirects(hrefLinkByHrefValue);
        String localFilename = createFilename(label, monthOffset, messageFull);

        utilities.downloadAttachment(lastHrefRedirected, localFilename);
    }


    /**
     * List all Labels of the user's mailbox.
     *
     * @param service Authorized Gmail API instance.
     * @param userId  User's email address. The special value "me"
     *                can be used to indicate the authenticated user.
     * @throws IOException
     */
    @Override
    public List<Label> fetchLabels(Gmail service, String userId) throws LabelsNotFoundException {
        List<Label> labels;
        List<Label> result = new ArrayList<>();
        try {
            //Service
            ListLabelsResponse response = service.users().labels().list(userId).execute();
            labels = response.getLabels();
            Optional.ofNullable(labels).orElseThrow(() -> new LabelsNotFoundException("Labels not found"));
            for (Label label : labels) {
                Optional.ofNullable(label.getType()).orElseThrow(() -> new LabelsNotFoundException("Label type not found"));
                if (label.getType().equals("user")) result.add(label);
            }
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        return result;
    }

    /**
     * List all Messages of the user's mailbox matching the query.
     *
     * @param service Authorized Gmail API instance.
     * @param userId  User's email address. The special value "me"
     *                can be used to indicate the authenticated user.
     * @param query   String used to filter the Messages listed.
     * @throws IOException
     */
    @Override
    public List<Message> listMessagesMatchingQuery(Gmail service,
                                                   String userId,
                                                   String query)
            throws IOException {
        ListMessagesResponse
                response =
                service.users().messages().list(userId).setQ(query).execute();

        List<Message> messages = new ArrayList<Message>();
        while (response.getMessages() != null) {
            messages.addAll(response.getMessages());
            if (response.getNextPageToken() != null) {
                String pageToken = response.getNextPageToken();
                response = service.users().messages().list(userId).setQ(query)
                        .setPageToken(pageToken).execute();
            } else {
                break;
            }
        }

        for (Message message : messages) {
            log.info(message.toPrettyString());
        }

        return messages;
    }

    /**
     * List all Messages of the user's mailbox with labelIds applied.
     *
     * @param service  Authorized Gmail API instance.
     * @param userId   User's email address. The special value "me"
     *                 can be used to indicate the authenticated user.
     * @param labelIds Only return Messages with these labelIds applied.
     * @throws IOException
     */
    @Override
    public List<Message> listMessagesWithLabels(Gmail service,
                                                String userId,
                                                List<String> labelIds)
            throws IOException {
        ListMessagesResponse response = service.users().messages().list(userId)
                .setLabelIds(labelIds).execute();

        List<Message> messages = new ArrayList<Message>();
        while (response.getMessages() != null) {
            messages.addAll(response.getMessages());
            if (response.getNextPageToken() != null) {
                String pageToken = response.getNextPageToken();
                response =
                        service.users()
                                .messages()
                                .list(userId)
                                .setLabelIds(labelIds)
                                .setPageToken(pageToken)
                                .execute();
            } else {
                break;
            }
        }

        for (Message message : messages) {
            log.info(message.toPrettyString());
        }

        return messages;
    }


    /**
     * List all Messages of the user's mailbox with labelIds applied.
     *
     * @param service  Authorized Gmail API instance.
     * @param userId   User's email address. The special value "me"
     *                 can be used to indicate the authenticated user.
     * @param query    String used to filter the Messages listed.
     * @param labelIds Only return Messages with these labelIds applied.
     * @throws IOException
     */
    @Override
    public List<Message> listMessagesWithLabelsAndQueries(
            Gmail service,
            String userId,
            String query,
            List<String> labelIds) throws IOException {

        Gmail.Users.Messages.List list = service.users()
                .messages()
                .list(userId)
                .setLabelIds(labelIds)
                .setQ(query);
        ListMessagesResponse response = list.execute();

        List<Message> messages = new ArrayList<Message>();
        while (response.getMessages() != null) {
            messages.addAll(response.getMessages());
            if (response.getNextPageToken() != null) {
                String pageToken = response.getNextPageToken();
                response = list
                        .setPageToken(pageToken)
                        .execute();
            } else {
                break;
            }
        }

        for (Message message : messages) {
            log.info(message.toPrettyString());
        }

        return messages;
    }

    private String getHrefLinkByHrefValue(String linkText, Message messageFull) throws LinkNotFoundException {
        String body = extractBodyData(messageFull);
        Document document = Jsoup.parse(body);
        Elements links = document.select("a[href]");

        linksNotEmptyCheck(linkText, links);
        String href = null;
        Boolean linkFound = Boolean.FALSE;
        for (Element link : links) {

            href = link.attr("href");
            String text = link.text();
            if (text.equals(linkText)) {
                linkFound = Boolean.TRUE;
                break;
            }
        }
        linkFoundCheck(linkFound, linkText);
        return href;
    }

    private void linkFoundCheck(Boolean linkFound, String linkText) throws LinkNotFoundException {
        if (!linkFound) {
            throw new LinkNotFoundException(String.format("There are links but none of them matches the criteria: %s",
                    linkText));
        }
    }

    private void linksNotEmptyCheck(String linkText, Elements links) throws LinkNotFoundException {
        if (links.isEmpty()) {
            throw new LinkNotFoundException(String.format("The link %s was not found in the message, there are no links",
                    linkText));
        }
    }

    private String createFilename(String label, int monthOffset, Message messageFull) {
        Long internalDate = messageFull.getInternalDate();
        Calendar messageDate = Calendar.getInstance();
        messageDate.setTimeInMillis(internalDate);
        messageDate.add(Calendar.MONTH, monthOffset);
        int month =
                messageDate.get(Calendar.MONTH) + 1; // beware of month indexing from zero
        int year = messageDate.get(Calendar.YEAR);
        String
                monthFormatted =
                month < 10
                        ? "0".concat(String.valueOf(month))
                        : String.valueOf(month);
        String formattedDate = "" + year + monthFormatted;
        return ATTACHMENT_STORE_DIR + File.separator + label + " " + formattedDate + ".pdf";
    }

    private String extractBodyData(Message messageFull) {
        String body = StringUtils.newStringUtf8(Base64.decodeBase64(messageFull.getPayload()
                .getBody()
                .getData()));
        if (body == null) {
            body = "";
            List<MessagePart> parts = messageFull.getPayload().getParts();
            for (MessagePart part : parts) {
                Base64 base64Url = new Base64(true);
                byte[] data = Base64.decodeBase64(part.getBody().getData());
                body += data != null ? body.concat(StringUtils.newStringUtf8(data)) : "";
            }
        }
        return body;
    }

    private String followRedirects(String href) {
        HttpURLConnection connection = null;
        for (; ; ) {
            URL url;
            try {
                url = new URL(href);
                connection = (HttpURLConnection) url.openConnection();
                connection.setInstanceFollowRedirects(false);
                String redirectLocation = connection.getHeaderField("Location");
                if (redirectLocation == null) break;
                href = redirectLocation;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return href;
    }
}