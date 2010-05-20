package ru.lavila.menudesigner.controllers;

import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Element;
import ru.lavila.menudesigner.models.Hierarchy;
import ru.lavila.menudesigner.presenters.TreePresenter;

import javax.swing.tree.TreePath;

public class TreeController
{
    private final Hierarchy hierarchy;

    public TreeController(Hierarchy hierarchy)
    {
        this.hierarchy = hierarchy;
    }

    public void addCategory(Category parentCategory)
    {
        hierarchy.newCategory(parentCategory, "New category");
    }

    public void addElements(Category parentCategory, int index, Element[] elements)
    {
        hierarchy.add(parentCategory, index, elements);
    }

    public void removeNodes(TreePath[] paths)
    {
        if (paths == null) return;
        Element[] toRemove = new Element[paths.length];
        for (int index = 0; index < paths.length; index++)
        {
            toRemove[index] = ((TreePresenter.ElementTreeNode) paths[index].getLastPathComponent()).element;
        }
        hierarchy.remove(toRemove);
    }
}
