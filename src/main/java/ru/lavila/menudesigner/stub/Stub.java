package ru.lavila.menudesigner.stub;

import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Hierarchy;
import ru.lavila.menudesigner.models.ItemsList;
import ru.lavila.menudesigner.models.impl.ItemsListImpl;

public class Stub
{
    public static ItemsList getSourceData()
    {
        ItemsListImpl data = new ItemsListImpl();
        Hierarchy original = data.newHierarchy("Original Menu", true);
        Hierarchy type = data.newHierarchy("Action Type", true);
        Category view = type.newCategory(type.getRoot(), "View");
        Category create = type.newCategory(type.getRoot(), "Create");
        Category delete = type.newCategory(type.getRoot(), "Delete");
        Category settings = type.newCategory(type.getRoot(), "Settings");
        Category createMessage = original.newCategory(original.getRoot(), "Create message");
        type.add(create,
                original.newItem(createMessage, "Message", 2000),
                original.newItem(createMessage, "Voice notes", 200),
                original.newItem(createMessage, "Templates", 80)
        );
        type.add(view,
                original.newItem(original.getRoot(), "Inbox", 2000)
        );
        Category email = original.newCategory(original.getRoot(), "Email");
        type.add(view,
                original.newItem(email, "Inbox", 64)
        );
        type.add(create,
                original.newItem(email, "Compose", 40)
        );
        type.add(view,
                original.newItem(email, "Drafts", 16),
                original.newItem(email, "Outbox", 16),
                original.newItem(email, "Sent items", 30)
        );
        type.add(settings,
                original.newItem(email, "Select a service provider", 8)
        );
        Category instantMessaging = original.newCategory(original.getRoot(), "Instant Messaging");
        type.add(view,
                original.newItem(instantMessaging, "AIM", 40),
                original.newItem(instantMessaging, "ICQ", 40),
                original.newItem(instantMessaging, "Windows Live Messenger", 40),
                original.newItem(instantMessaging, "Yahoo! Messenger", 40)
        );
        type.add(view,
                original.newItem(original.getRoot(), "Drafts", 300),
                original.newItem(original.getRoot(), "Outbox", 200),
                original.newItem(original.getRoot(), "Sent items", 600)
        );
        Category savedItems = original.newCategory(original.getRoot(), "Saved items");
        type.add(view,
                original.newItem(savedItems, "Saved Messages", 120),
                original.newItem(savedItems, "Templates", 80)
        );
        Category voiceMail = original.newCategory(original.getRoot(), "Voice mail");
        type.add(view,
                original.newItem(voiceMail, "Listen to voice mails", 320)
        );
        type.add(settings,
                original.newItem(voiceMail, "Voice mailbox no.", 4)
        );
        type.add(view,
                original.newItem(original.getRoot(), "Delivery reports", 240),
                original.newItem(original.getRoot(), "Serv. commands", 50));
        Category deleteMessages = original.newCategory(original.getRoot(), "Delete messages");
        type.add(delete,
                original.newItem(deleteMessages, "By message", 80),
                original.newItem(deleteMessages, "By folder", 80),
                original.newItem(deleteMessages, "All messages", 80)
        );
        Category messageSettings = original.newCategory(original.getRoot(), "Message settings");
        Category generalSettings = original.newCategory(messageSettings, "General settings");
        Category textMessages = original.newCategory(messageSettings, "Text messages");
        Category pictureMessages = original.newCategory(messageSettings, "Picture messages");
        Category serviceMessages = original.newCategory(messageSettings, "Service messages");
        type.add(settings,
                original.newItem(generalSettings, "Save sent messages", 12),
                original.newItem(generalSettings, "Overwriting in Sent", 8),
                original.newItem(generalSettings, "Default destination", 24),
                original.newItem(generalSettings, "Font size", 20),
                original.newItem(generalSettings, "Graphical smileys", 20),
                original.newItem(textMessages, "Delivery reports", 12),
                original.newItem(textMessages, "Message centers", 4),
                original.newItem(textMessages, "Msg. center in use", 4),
                original.newItem(textMessages, "Use packet data", 4),
                original.newItem(textMessages, "Character support", 4),
                original.newItem(textMessages, "Reply via same center", 4),
                original.newItem(pictureMessages, "Request reports", 6),
                original.newItem(pictureMessages, "Allow read report", 6),
                original.newItem(pictureMessages, "Image size (pic.msg)", 10),
                original.newItem(pictureMessages, "Default slide timing", 8),
                original.newItem(pictureMessages, "MMS reception", 8),
                original.newItem(pictureMessages, "Allow ads", 8),
                original.newItem(pictureMessages, "Configuration sett.", 8),
                original.newItem(serviceMessages, "t-zones messages", 6),
                original.newItem(serviceMessages, "Message filter", 6),
                original.newItem(serviceMessages, "Autom. connection", 6)
        );
        Category messageLog = original.newCategory(original.getRoot(), "Message log");
        type.add(view,
                original.newItem(messageLog, "Sent text messages", 40),
                original.newItem(messageLog, "Sent picture msgs.", 20),
                original.newItem(messageLog, "Received text msgs.", 40),
                original.newItem(messageLog, "Received pic msgs.", 20)
        );
        type.add(delete,
                original.newItem(messageLog, "Clear all counters", 16)
        );
        return data;
    }
}
