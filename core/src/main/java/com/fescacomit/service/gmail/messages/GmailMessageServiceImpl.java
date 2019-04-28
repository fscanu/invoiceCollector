package com.fescacomit.service.gmail.messages;

import com.google.api.client.repackaged.org.apache.commons.codec.binary.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.Message;
import com.google.api.services.gmail.model.MessagePart;
import com.google.api.services.gmail.model.MessagePartBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import static com.fescacomit.service.gmail.constants.GmailConstants.ATTACHMENT_STORE_DIR;

@Service
public class GmailMessageServiceImpl implements GmailMessageService {
    private static final Logger log = LoggerFactory.getLogger(GmailMessageServiceImpl.class);

    /**
     * Get Message with given ID.
     *
     * @param service   Authorized Gmail API instance.
     * @param userId    User's email address. The special value "me"
     *                  can be used to indicate the authenticated user.
     * @param messageId ID of Message to retrieve.
     * @return Message Retrieved Message.
     * @throws IOException
     */
    public Message getMessage(Gmail service, String userId, String messageId) {
        Message message = null;

        try {
            message = service.users().messages().get(userId, messageId).execute();

        } catch (IOException e) {
            log.error(e.getMessage());
        }

        return message;
    }

    /**
     * Get the attachments in a given email.
     *
     * @param service   Authorized Gmail API instance.
     * @param userId    User's email address. The special value "me"
     *                  can be used to indicate the authenticated user.
     * @param messageId ID of Message containing attachment..
     * @throws IOException
     */
    @Override
    public void getAttachments(Gmail service, String userId, String label, String messageId) {
        try {
            Message
                    message =
                    service.users().messages().get(userId, messageId).execute();
            List<MessagePart> parts = message.getPayload().getParts();
            for (MessagePart part : parts) {
                if (part.getFilename() != null && part.getFilename()
                                                      .length() > 0) {
                    String attId = part.getBody().getAttachmentId();
                    MessagePartBody
                            attachPart =
                            service.users()
                                   .messages()
                                   .attachments()
                                   .
                                           get(userId,
                                               messageId,
                                               attId)
                                   .execute();

                    Base64 base64Url = new Base64(true);
                    byte[]
                            fileByteArray =
                            Base64.decodeBase64(attachPart.getData());

                    Long     internalDate = message.getInternalDate();
                    Calendar messageDate  = Calendar.getInstance();
                    messageDate.setTimeInMillis(internalDate);
                    messageDate.add(Calendar.MONTH, -1);
                    int
                            month =
                            messageDate.get(Calendar.MONTH) + 1; // beware of month indexing from zero
                    int year = messageDate.get(Calendar.YEAR);
                    String
                            monthFormatted =
                            month < 10
                            ? "0".concat(String.valueOf(month))
                            : String.valueOf(month);
                    String formattedDate = "" + year + monthFormatted;
                    new File(ATTACHMENT_STORE_DIR.getAbsolutePath()).mkdirs();
                    FileOutputStream fileOutFile =
                            new FileOutputStream(ATTACHMENT_STORE_DIR + System.getProperty(
                                    "file.separator") + label + " " + formattedDate + ".pdf");
                    fileOutFile.write(fileByteArray);
                    fileOutFile.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
