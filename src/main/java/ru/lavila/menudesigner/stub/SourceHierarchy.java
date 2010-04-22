package ru.lavila.menudesigner.stub;

import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Hierarchy;

public class SourceHierarchy extends Hierarchy
{
    public SourceHierarchy()
    {
        populateStub();
    }

    private void populateStub()
    {
        Category createMessage = newCategory(root, "Create message");
        newItem(createMessage, "Message", 0.282);
        newItem(createMessage, "Voice notes", 0.0282);
        newItem(createMessage, "Templates", 0.0113);
        Category email = newCategory(root, "Email");
        newItem(email, "Inbox", 0.009);
        newItem(email, "Compose", 0.0056);
        newItem(email, "Drafts", 0.0023);
        newItem(email, "Outbox", 0.0023);
        newItem(email, "Sent items", 0.0042);
        newItem(email, "Select a service provider", 0.0011);
        newItem(root, "Inbox", 0.282);
        Category instantMessaging = newCategory(root, "Instant Messaging");
        newItem(instantMessaging, "AIM", 0.0056);
        newItem(instantMessaging, "ICQ", 0.0056);
        newItem(instantMessaging, "Windows Live Messenger", 0.0056);
        newItem(instantMessaging, "Yahoo! Messenger", 0.0056);
        newItem(root, "Drafts", 0.0423);
        newItem(root, "Outbox", 0.0282);
        newItem(root, "Sent items", 0.0846);
        Category savedItems = newCategory(root, "Saved items");
        newItem(savedItems, "Saved Messages", 0.0169);
        newItem(savedItems, "Templates", 0.0113);
        Category voiceMail = newCategory(root, "Voice mail");
        newItem(voiceMail, "Listen to voice mails", 0.0451);
        newItem(voiceMail, "Voice mailbox no.", 0.0006);
        newItem(root, "Delivery reports", 0.0338);
        newItem(root, "Serv. commands", 0.0071);
        Category deleteMessages = newCategory(root, "Delete messages");
        newItem(deleteMessages, "By message", 0.0113);
        newItem(deleteMessages, "By folder", 0.0113);
        newItem(deleteMessages, "All messages", 0.0113);
        Category messageSettings = newCategory(root, "Message settings");
        Category generalSettings = newCategory(messageSettings, "General settings");
        newItem(generalSettings, "Save sent messages", 0.0017);
        newItem(generalSettings, "Overwriting in Sent", 0.0011);
        newItem(generalSettings, "Default destination", 0.0034);
        newItem(generalSettings, "Font size", 0.0028);
        newItem(generalSettings, "Graphical smileys", 0.0028);
        Category textMessages = newCategory(messageSettings, "Text messages");
        newItem(textMessages, "Delivery reports", 0.0017);
        newItem(textMessages, "Message centers", 0.0006);
        newItem(textMessages, "Msg. center in use", 0.0006);
        newItem(textMessages, "Use packet data", 0.0006);
        newItem(textMessages, "Character support", 0.0006);
        newItem(textMessages, "Reply via same center", 0.0006);
        Category pictureMessages = newCategory(messageSettings, "Picture messages");
        newItem(pictureMessages, "Request reports", 0.0008);
        newItem(pictureMessages, "Allow read report", 0.0008);
        newItem(pictureMessages, "Image size (pic.msg)", 0.0014);
        newItem(pictureMessages, "Default slide timing", 0.0011);
        newItem(pictureMessages, "MMS reception", 0.0011);
        newItem(pictureMessages, "Allow ads", 0.0011);
        newItem(pictureMessages, "Configuration sett.", 0.0011);
        Category serviceMessages = newCategory(messageSettings, "Service messages");
        newItem(serviceMessages, "t-zones messages", 0.0008);
        newItem(serviceMessages, "Message filter", 0.0008);
        newItem(serviceMessages, "Autom. connection", 0.0008);
        Category messageLog = newCategory(root, "Message log");
        newItem(messageLog, "Sent text messages", 0.0056);
        newItem(messageLog, "Sent picture msgs.", 0.0028);
        newItem(messageLog, "Received text msgs.", 0.0056);
        newItem(messageLog, "Received pic msgs.", 0.0028);
        newItem(messageLog, "Clear all counters", 0.0023);
    }
}
