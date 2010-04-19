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
        createMessage.add(new ItemImpl("Message", 0.28), new ItemImpl("Voice notes", 0.025), new ItemImpl("Templates", 0.01));
        Category instantMessaging = new CategoryImpl("Instant Messaging");
        instantMessaging.add(new ItemImpl("AIM", 0.005), new ItemImpl("ICQ", 0.005), new ItemImpl("Windows Live Messenger", 0.005), new ItemImpl("Yahoo! Messenger", 0.005));
        Category savedItems = new CategoryImpl("Saved items");
        savedItems.add(new ItemImpl("Saved Messages", 0.02), new ItemImpl("Templates", 0.01));
        Category voiceMail = new CategoryImpl("Voice mail");
        voiceMail.add(new ItemImpl("Listen to voice mails", 0.045), new ItemImpl("Voice mailbox no.", 0.0005));
        Category messageSettings = new CategoryImpl("Message settings");
        messageSettings.add(new ItemImpl("General settings", 0.01), new ItemImpl("Text messages", 0.005), new ItemImpl("Picture messages", 0.008), new ItemImpl("Service messages", 0.003));
        root.add(
                createMessage,
                new ItemImpl("Inbox", 0.28),
                new ItemImpl("E-mail", 0.02),
                instantMessaging,
                new ItemImpl("Drafts", 0.04),
                new ItemImpl("Outbox", 0.03),
                new ItemImpl("Sent items", 0.08),
                savedItems,
                voiceMail,
                new ItemImpl("Delivery reports", 0.03),
                new ItemImpl("Serv. commands", 0.007),
                new ItemImpl("Delete messages", 0.03),
                messageSettings,
                new ItemImpl("Message log", 0.02)
        );
    }
}
