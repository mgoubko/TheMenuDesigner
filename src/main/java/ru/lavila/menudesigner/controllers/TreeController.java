package ru.lavila.menudesigner.controllers;

import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Element;
import ru.lavila.menudesigner.models.Hierarchy;
import ru.lavila.menudesigner.presenters.TreePresenter;

import javax.swing.tree.TreePath;
import java.util.*;

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

    public void removeNodes(TreePath[] paths)
    {
        if (paths == null) return;
        Map<Category, Collection<Element>> toRemove = new HashMap<Category, Collection<Element>>();
        for (TreePath path : paths)
        {
            TreePresenter.ElementTreeNode childNode = (TreePresenter.ElementTreeNode) path.getLastPathComponent();
            if (childNode.getParent() != null)
            {
                Category parent = (Category) ((TreePresenter.ElementTreeNode) childNode.getParent()).element;
                Collection<Element> parentElements = toRemove.get(parent);
                if (parentElements == null)
                {
                    parentElements = new HashSet<Element>();
                    toRemove.put(parent, parentElements);
                }
                parentElements.add(childNode.element);
            }
        }
        for (Category category : toRemove.keySet())
        {
            Collection<Element> elements = toRemove.get(category);
            hierarchy.remove(category, elements.toArray(new Element[elements.size()]));
        }
    }
}
