package com.fescacomit.service.api;

import com.fescacomit.service.exceptions.DownloadException;
import com.fescacomit.service.exceptions.LinkNotFoundException;
import com.fescacomit.service.gmail.messages.GmailMessageService;
import com.fescacomit.service.gmail.utilties.Utilities;
import com.google.api.client.util.Base64;
import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.model.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GmailApiServiceImplTest {
    private static final int    MONTH_OFFSET = 0;
    private static final String MESSAGE_ID   = "message_id";
    private static final String USER_ID      = "user_id";
    private static final String TEST_LABEL   = "testLabel";
    private static final String LINK_TEXT    = "testData";

    @InjectMocks
    private GmailApiServiceImpl gmailApiService;

    @Mock
    private Utilities           utilities;
    @Mock
    private Gmail               gmailService;
    @Mock
    private GmailMessageService gmailMessageService;

    @Test(expected = LinkNotFoundException.class)
    public void getAttachmentByLinkText_with_message_without_link() throws LinkNotFoundException, DownloadException {
        //given a message with no links inside
        doReturn(getMessageWithoutLink()).when(gmailMessageService).getMessage(gmailService, USER_ID, MESSAGE_ID);
        //then i will expect a LinkNotFoundException
        gmailApiService.getAttachmentByLinkText(
                gmailService,
                gmailMessageService,
                utilities,
                LINK_TEXT,
                TEST_LABEL,
                USER_ID,
                MESSAGE_ID, MONTH_OFFSET);
    }

    @Test(expected = LinkNotFoundException.class)
    public void getAttachmentByLinkText_with_full_message_link_do_not_match()
            throws LinkNotFoundException, DownloadException {
        //given a message with link that does not correspond to the one we are looking for
        doReturn(getMessageWithLink()).when(gmailMessageService).getMessage(gmailService, USER_ID, MESSAGE_ID);

        //then i will expect a LinkNotFoundException
        gmailApiService.getAttachmentByLinkText(
                gmailService,
                gmailMessageService,
                utilities,
                LINK_TEXT,
                TEST_LABEL,
                USER_ID,
                MESSAGE_ID, MONTH_OFFSET);
    }

    @Test
    public void getAttachmentByLinkText_with_full_message_link_match() {
        //given a message with a valid link inside
        doReturn(getMessageWithLink()).when(gmailMessageService).getMessage(gmailService, USER_ID, MESSAGE_ID);

        //then i will expect a LinkNotFoundException
        try {
            gmailApiService.getAttachmentByLinkText(
                    gmailService,
                    gmailMessageService,
                    utilities,
                    "Consultez ici votre aperçu",
                    TEST_LABEL,
                    USER_ID,
                    MESSAGE_ID,
                    MONTH_OFFSET);
        } catch (LinkNotFoundException | DownloadException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = java.lang.NullPointerException.class)
    public void fetchLabelsWithEmptyListReturnedFromService() throws IOException {
        //given an empty list
        Gmail.Users.Labels.List list = getMockedLabelList();
        when(list.execute()).thenReturn(new ListLabelsResponse());

        //when we will expect a NullPointerException
        gmailApiService.fetchLabels(gmailService, USER_ID);

    }

    @Test
    public void fetchLabelsWithNotEmptyList() throws IOException {
        //given a non empty list
        List<Label> labelList = new ArrayList<>();
        Label       label     = new Label();
        label.setId("1");
        label.setName("TEST");
        labelList.add(label);
        when(getMockedLabelList().execute()).thenReturn(new ListLabelsResponse().setLabels(labelList));

        //when we will expect a NullPointerException
        List<Label> labelsList = gmailApiService.fetchLabels(gmailService, USER_ID);

        //then
        assertNotNull(labelsList);
        assertEquals("The list contains one label", 1, labelsList.size());
        assertEquals("The label has ID: 1", "1", labelsList.get(0).getId());
        assertEquals("The label has name: TEST", "TEST", labelsList.get(0).getName());

    }

    @Test
    public void listMessagesMatchingQuery() {
    }

    @Test
    public void listMessagesWithLabels() {
    }

    @Test
    public void listMessagesWithLabelsAndQueries() {
    }

    private Message getMessageWithoutLink() {
        Message         messageEmptyLink = new Message();
        MessagePartBody partBodyData     = (new MessagePartBody()).setData("<a href=\"\">" + "TestData" + "</a>");
        MessagePart     payload          = (new MessagePart()).setBody(partBodyData);
        messageEmptyLink.setPayload(payload);
        return messageEmptyLink;
    }

    private Message getMessageWithLink() {
        Message message = new Message();
        String
                bodyText =
                "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title>Dats24</title><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /></head><body>\n" +
                "\t<table width=\"640\" border=\"0\" align=\"left\" cellpadding=\"0\" cellspacing=\"0\"><tr align=\"left\"><td>\n" +
                "\t\t\t\t<div><p><span style=\"font-size: 12px;\"><span style=\"font-family: Arial;\"><span style=\"color: #000000;\"><img alt=\"\" border=\"0\" height=\"68\" \n" +
                "src=\"http://cdn.mi.addemar.com/files/a_dats24/data/Image/Dats24/dats_new.jpg\" width=\"204\" /><br /><br /><br />\n" +
                "\tCher client, chère cliente,<br /><br /><br />\n" +
                "\tEn cliquant sur le lien ci-dessous, vous trouverez un aperçu de vos pleins du mois précédent.<br /><br /><a \n" +
                "href=\"http://dats24.fb.mi.addemar.com/c659/e7772820/h87b94/l9787/index.html\"><strong>Consultez ici votre aperçu</strong></a><br /><br />\n" +
                "\tVous n&rsquo;arrivez pas à ouvrir ce lien ?<br /><a \n" +
                "href=\"http://dats24.fb.mi.addemar.com/c659/e7772820/h87b94/l9788/index.html\" target=\"_blank\">Cliquez ici</a> et suivez les instructions.<br /><br /><span style=\"color: #00b0f0;\"><strong>Grâce à la domiciliation européenne, le montant sera prélevé automatiquement de votre compte bancaire. Il n'est pas nécessaire de réaliser un virement bancaire.</strong></span><br /><br />\n" +
                "\tUn petit conseil : sauvegarder le fichier PDF de votre aperçu mensuel afin de pouvoir le consulter quand vous le souhaitez.<br /><span style=\"font-size: 12px;\"><span style=\"font-family: Arial;\"><span style=\"color: #000000;\">    </span><br /><br />\n" +
                "\tCordiales salutations,<br /><br />\n" +
                "\tHerman Longin</span><br /><span style=\"color: #666666;\"><span style=\"font-size: 10px;\">Customer service</span></span><br />\n" +
                "\tTel : +32 2 3635152</span></span></span></span></p></div></td>\n" +
                "\t\t</tr></table><img height=\"6\" width=\"200\" \n" +
                "src=\"http://dats24.fb.mi.addemar.com/c659/e7772820/h87b94/l0/logo.gif\" border=\"0\" alt=\"\"" +
                " /></body></html>";
        MessagePartBody
                partBodyData =
                (new MessagePartBody()).setData(Base64.encodeBase64URLSafeString(bodyText.getBytes()));
        MessagePart payload = (new MessagePart()).setBody(partBodyData);
        message.setPayload(payload);
        message.setInternalDate(System.currentTimeMillis());
        return message;
    }

    private Gmail.Users.Labels.List getMockedLabelList() throws IOException {
        Gmail.Users             users  = mock(Gmail.Users.class);
        Gmail.Users.Labels      labels = mock(Gmail.Users.Labels.class);
        Gmail.Users.Labels.List list   = mock(Gmail.Users.Labels.List.class);
        when(gmailService.users()).thenReturn(users);
        when(users.labels()).thenReturn(labels);
        when(labels.list(USER_ID)).thenReturn(list);
        return list;
    }

}