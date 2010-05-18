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
        Hierarchy original = data.newHierarchy("Original", true);
        Hierarchy type = data.newHierarchy("Type", true);
        Category view = type.newCategory(type.getRoot(), "View");
        Category create = type.newCategory(type.getRoot(), "Create");
        Category delete = type.newCategory(type.getRoot(), "Delete");
        Category settings = type.newCategory(type.getRoot(), "Settings");
        Category createMessage = original.newCategory(original.getRoot(), "Create message");
        type.add(create,
                original.newItem(createMessage, "Message", 0.282),
                original.newItem(createMessage, "Voice notes", 0.0282),
                original.newItem(createMessage, "Templates", 0.0113)
        );
        Category email = original.newCategory(original.getRoot(), "Email");
        type.add(view,
                original.newItem(email, "Inbox", 0.009)
        );
        type.add(create,
                original.newItem(email, "Compose", 0.0056)
        );
        type.add(view,
                original.newItem(email, "Drafts", 0.0023),
                original.newItem(email, "Outbox", 0.0023),
                original.newItem(email, "Sent items", 0.0042)
        );
        type.add(settings,
                original.newItem(email, "Select a service provider", 0.0011)
        );
        type.add(view,
                original.newItem(original.getRoot(), "Inbox", 0.282)
        );
        Category instantMessaging = original.newCategory(original.getRoot(), "Instant Messaging");
        type.add(view,
                original.newItem(instantMessaging, "AIM", 0.0056),
                original.newItem(instantMessaging, "ICQ", 0.0056),
                original.newItem(instantMessaging, "Windows Live Messenger", 0.0056),
                original.newItem(instantMessaging, "Yahoo! Messenger", 0.0056)
        );
        type.add(view,
                original.newItem(original.getRoot(), "Drafts", 0.0423),
                original.newItem(original.getRoot(), "Outbox", 0.0282),
                original.newItem(original.getRoot(), "Sent items", 0.0846)
        );
        Category savedItems = original.newCategory(original.getRoot(), "Saved items");
        type.add(view,
                original.newItem(savedItems, "Saved Messages", 0.0169),
                original.newItem(savedItems, "Templates", 0.0113)
        );
        Category voiceMail = original.newCategory(original.getRoot(), "Voice mail");
        type.add(view,
                original.newItem(voiceMail, "Listen to voice mails", 0.0451)
        );
        type.add(settings,
                original.newItem(voiceMail, "Voice mailbox no.", 0.0006)
        );
        type.add(view,
                original.newItem(original.getRoot(), "Delivery reports", 0.0338),
                original.newItem(original.getRoot(), "Serv. commands", 0.0071));
        Category deleteMessages = original.newCategory(original.getRoot(), "Delete messages");
        type.add(delete,
                original.newItem(deleteMessages, "By message", 0.0113),
                original.newItem(deleteMessages, "By folder", 0.0113),
                original.newItem(deleteMessages, "All messages", 0.0113)
        );
        Category messageSettings = original.newCategory(original.getRoot(), "Message settings");
        Category generalSettings = original.newCategory(messageSettings, "General settings");
        Category textMessages = original.newCategory(messageSettings, "Text messages");
        Category pictureMessages = original.newCategory(messageSettings, "Picture messages");
        Category serviceMessages = original.newCategory(messageSettings, "Service messages");
        Category messageLog = original.newCategory(original.getRoot(), "Message log");
        type.add(settings,
                original.newItem(generalSettings, "Save sent messages", 0.0017),
                original.newItem(generalSettings, "Overwriting in Sent", 0.0011),
                original.newItem(generalSettings, "Default destination", 0.0034),
                original.newItem(generalSettings, "Font size", 0.0028),
                original.newItem(generalSettings, "Graphical smileys", 0.0028),
                original.newItem(textMessages, "Delivery reports", 0.0017),
                original.newItem(textMessages, "Message centers", 0.0006),
                original.newItem(textMessages, "Msg. center in use", 0.0006),
                original.newItem(textMessages, "Use packet data", 0.0006),
                original.newItem(textMessages, "Character support", 0.0006),
                original.newItem(textMessages, "Reply via same center", 0.0006),
                original.newItem(pictureMessages, "Request reports", 0.0008),
                original.newItem(pictureMessages, "Allow read report", 0.0008),
                original.newItem(pictureMessages, "Image size (pic.msg)", 0.0014),
                original.newItem(pictureMessages, "Default slide timing", 0.0011),
                original.newItem(pictureMessages, "MMS reception", 0.0011),
                original.newItem(pictureMessages, "Allow ads", 0.0011),
                original.newItem(pictureMessages, "Configuration sett.", 0.0011),
                original.newItem(serviceMessages, "t-zones messages", 0.0008),
                original.newItem(serviceMessages, "Message filter", 0.0008),
                original.newItem(serviceMessages, "Autom. connection", 0.0008),
                original.newItem(messageLog, "Sent text messages", 0.0056),
                original.newItem(messageLog, "Sent picture msgs.", 0.0028),
                original.newItem(messageLog, "Received text msgs.", 0.0056),
                original.newItem(messageLog, "Received pic msgs.", 0.0028),
                original.newItem(messageLog, "Clear all counters", 0.0023)
        );
        return data;
    }
}
