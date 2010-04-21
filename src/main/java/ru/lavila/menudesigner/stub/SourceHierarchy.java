package ru.lavila.menudesigner.stub;

import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.CategoryImpl;
import ru.lavila.menudesigner.models.Hierarchy;
import ru.lavila.menudesigner.models.ItemImpl;

public class SourceHierarchy extends Hierarchy
{
    public SourceHierarchy()
    {
        populateStub();
    }

    private void populateStub()
    {
        Category createMessage = new CategoryImpl("Create message");
        createMessage.add(new ItemImpl("Message", 0.282), new ItemImpl("Voice notes", 0.0282), new ItemImpl("Templates", 0.0113));
        Category email = new CategoryImpl("Email");
        email.add(new ItemImpl("Inbox", 0.009), new ItemImpl("Compose", 0.0056), new ItemImpl("Drafts", 0.0023), new ItemImpl("Outbox", 0.0023), new ItemImpl("Sent items", 0.0042), new ItemImpl("Select a service provider", 0.0011));
        Category instantMessaging = new CategoryImpl("Instant Messaging");
        instantMessaging.add(new ItemImpl("AIM", 0.0056), new ItemImpl("ICQ", 0.0056), new ItemImpl("Windows Live Messenger", 0.0056), new ItemImpl("Yahoo! Messenger", 0.0056));
        Category savedItems = new CategoryImpl("Saved items");
        savedItems.add(new ItemImpl("Saved Messages", 0.0169), new ItemImpl("Templates", 0.0113));
        Category voiceMail = new CategoryImpl("Voice mail");
        voiceMail.add(new ItemImpl("Listen to voice mails", 0.0451), new ItemImpl("Voice mailbox no.", 0.0006));
        Category deleteMessages = new CategoryImpl("Delete messages");
        deleteMessages.add(new ItemImpl("By message", 0.0113), new ItemImpl("By folder", 0.0113), new ItemImpl("All messages", 0.0113));
        Category messageSettings = new CategoryImpl("Message settings");
        Category generalSettings = new CategoryImpl("General settings");
        generalSettings.add(new ItemImpl("Save sent messages", 0.0017), new ItemImpl("Overwriting in Sent", 0.0011), new ItemImpl("Default destination", 0.0034), new ItemImpl("Font size", 0.0028), new ItemImpl("Graphical smileys", 0.0028));
        Category textMessages = new CategoryImpl("Text messages");
        textMessages.add(new ItemImpl("Delivery reports", 0.0017), new ItemImpl("Message centers", 0.0006), new ItemImpl("Msg. center in use", 0.0006), new ItemImpl("Use packet data", 0.0006), new ItemImpl("Character support", 0.0006), new ItemImpl("Reply via same center", 0.0006));
        Category pictureMessages = new CategoryImpl("Picture messages");
        pictureMessages.add(new ItemImpl("Request reports", 0.0008), new ItemImpl("Allow read report", 0.0008), new ItemImpl("Image size (pic.msg)", 0.0014), new ItemImpl("Default slide timing", 0.0011), new ItemImpl("MMS reception", 0.0011), new ItemImpl("Allow ads", 0.0011), new ItemImpl("Configuration sett.", 0.0011));
        Category serviceMessages = new CategoryImpl("Service messages");
        serviceMessages.add(new ItemImpl("t-zones messages", 0.0008), new ItemImpl("Message filter", 0.0008), new ItemImpl("Autom. connection", 0.0008));
        messageSettings.add(generalSettings, textMessages, pictureMessages, serviceMessages);
        Category messageLog = new CategoryImpl("Message log");
        messageLog.add(new ItemImpl("Sent text messages", 0.0056), new ItemImpl("Sent picture msgs.", 0.0028), new ItemImpl("Received text msgs.", 0.0056), new ItemImpl("Received pic msgs.", 0.0028), new ItemImpl("Clear all counters", 0.0023));
        root.add(
                createMessage,
                new ItemImpl("Inbox", 0.282),
                email,
                instantMessaging,
                new ItemImpl("Drafts", 0.0423),
                new ItemImpl("Outbox", 0.0282),
                new ItemImpl("Sent items", 0.0846),
                savedItems,
                voiceMail,
                new ItemImpl("Delivery reports", 0.0338),
                new ItemImpl("Serv. commands", 0.0071),
                deleteMessages,
                messageSettings,
                messageLog
        );
    }
}
