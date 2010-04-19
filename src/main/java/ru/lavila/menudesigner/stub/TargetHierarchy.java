package ru.lavila.menudesigner.stub;

import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.CategoryImpl;
import ru.lavila.menudesigner.models.Hierarchy;
import ru.lavila.menudesigner.models.ItemImpl;

public class TargetHierarchy extends Hierarchy
{
    public TargetHierarchy()
    {
        populateStub();
    }

    private void populateStub()
    {
        Category folders = new CategoryImpl("Folders");
        folders.add(new ItemImpl("Sent Items", 0.08), new ItemImpl("Drafts", 0.04), new ItemImpl("Outbox", 0.03));
        Category settings = new CategoryImpl("Settings & maintenance");
        Category voiceMail = new CategoryImpl("Voice mail");
        voiceMail.add(new ItemImpl("Listen to voice mails", 0.045), new ItemImpl("Voice mailbox no.", 0.0005));
        root.add(new ItemImpl("Create Message", 0.28), new ItemImpl("Inbox", 0.28), folders, settings, voiceMail);
    }
}
