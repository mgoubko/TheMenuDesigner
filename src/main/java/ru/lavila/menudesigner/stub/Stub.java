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
        Hierarchy hierarchy = data.newHierarchy("Original", true);
        Category createMessage = hierarchy.newCategory(hierarchy.getRoot(), "Create message");
        hierarchy.newItem(createMessage, "Message", 0.282);
        hierarchy.newItem(createMessage, "Voice notes", 0.0282);
        hierarchy.newItem(createMessage, "Templates", 0.0113);
        Category email = hierarchy.newCategory(hierarchy.getRoot(), "Email");
        hierarchy.newItem(email, "Inbox", 0.009);
        hierarchy.newItem(email, "Compose", 0.0056);
        hierarchy.newItem(email, "Drafts", 0.0023);
        hierarchy.newItem(email, "Outbox", 0.0023);
        hierarchy.newItem(email, "Sent items", 0.0042);
        hierarchy.newItem(email, "Select a service provider", 0.0011);
        hierarchy.newItem(hierarchy.getRoot(), "Inbox", 0.282);
        Category instantMessaging = hierarchy.newCategory(hierarchy.getRoot(), "Instant Messaging");
        hierarchy.newItem(instantMessaging, "AIM", 0.0056);
        hierarchy.newItem(instantMessaging, "ICQ", 0.0056);
        hierarchy.newItem(instantMessaging, "Windows Live Messenger", 0.0056);
        hierarchy.newItem(instantMessaging, "Yahoo! Messenger", 0.0056);
        hierarchy.newItem(hierarchy.getRoot(), "Drafts", 0.0423);
        hierarchy.newItem(hierarchy.getRoot(), "Outbox", 0.0282);
        hierarchy.newItem(hierarchy.getRoot(), "Sent items", 0.0846);
        Category savedItems = hierarchy.newCategory(hierarchy.getRoot(), "Saved items");
        hierarchy.newItem(savedItems, "Saved Messages", 0.0169);
        hierarchy.newItem(savedItems, "Templates", 0.0113);
        Category voiceMail = hierarchy.newCategory(hierarchy.getRoot(), "Voice mail");
        hierarchy.newItem(voiceMail, "Listen to voice mails", 0.0451);
        hierarchy.newItem(voiceMail, "Voice mailbox no.", 0.0006);
        hierarchy.newItem(hierarchy.getRoot(), "Delivery reports", 0.0338);
        hierarchy.newItem(hierarchy.getRoot(), "Serv. commands", 0.0071);
        Category deleteMessages = hierarchy.newCategory(hierarchy.getRoot(), "Delete messages");
        hierarchy.newItem(deleteMessages, "By message", 0.0113);
        hierarchy.newItem(deleteMessages, "By folder", 0.0113);
        hierarchy.newItem(deleteMessages, "All messages", 0.0113);
        Category messageSettings = hierarchy.newCategory(hierarchy.getRoot(), "Message settings");
        Category generalSettings = hierarchy.newCategory(messageSettings, "General settings");
        hierarchy.newItem(generalSettings, "Save sent messages", 0.0017);
        hierarchy.newItem(generalSettings, "Overwriting in Sent", 0.0011);
        hierarchy.newItem(generalSettings, "Default destination", 0.0034);
        hierarchy.newItem(generalSettings, "Font size", 0.0028);
        hierarchy.newItem(generalSettings, "Graphical smileys", 0.0028);
        Category textMessages = hierarchy.newCategory(messageSettings, "Text messages");
        hierarchy.newItem(textMessages, "Delivery reports", 0.0017);
        hierarchy.newItem(textMessages, "Message centers", 0.0006);
        hierarchy.newItem(textMessages, "Msg. center in use", 0.0006);
        hierarchy.newItem(textMessages, "Use packet data", 0.0006);
        hierarchy.newItem(textMessages, "Character support", 0.0006);
        hierarchy.newItem(textMessages, "Reply via same center", 0.0006);
        Category pictureMessages = hierarchy.newCategory(messageSettings, "Picture messages");
        hierarchy.newItem(pictureMessages, "Request reports", 0.0008);
        hierarchy.newItem(pictureMessages, "Allow read report", 0.0008);
        hierarchy.newItem(pictureMessages, "Image size (pic.msg)", 0.0014);
        hierarchy.newItem(pictureMessages, "Default slide timing", 0.0011);
        hierarchy.newItem(pictureMessages, "MMS reception", 0.0011);
        hierarchy.newItem(pictureMessages, "Allow ads", 0.0011);
        hierarchy.newItem(pictureMessages, "Configuration sett.", 0.0011);
        Category serviceMessages = hierarchy.newCategory(messageSettings, "Service messages");
        hierarchy.newItem(serviceMessages, "t-zones messages", 0.0008);
        hierarchy.newItem(serviceMessages, "Message filter", 0.0008);
        hierarchy.newItem(serviceMessages, "Autom. connection", 0.0008);
        Category messageLog = hierarchy.newCategory(hierarchy.getRoot(), "Message log");
        hierarchy.newItem(messageLog, "Sent text messages", 0.0056);
        hierarchy.newItem(messageLog, "Sent picture msgs.", 0.0028);
        hierarchy.newItem(messageLog, "Received text msgs.", 0.0056);
        hierarchy.newItem(messageLog, "Received pic msgs.", 0.0028);
        hierarchy.newItem(messageLog, "Clear all counters", 0.0023);
        return data;
    }
}
