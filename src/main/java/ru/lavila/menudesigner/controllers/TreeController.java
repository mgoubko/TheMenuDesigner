package ru.lavila.menudesigner.controllers;

import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.CategoryImpl;
import ru.lavila.menudesigner.models.Element;
import ru.lavila.menudesigner.models.Hierarchy;
import ru.lavila.menudesigner.presenters.TreePresenter;

import javax.swing.tree.TreePath;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class TreeController
{
    private final Hierarchy hierarchy;

    public TreeController(Hierarchy hierarchy)
    {
        this.hierarchy = hierarchy;
    }

    public void addCategory(TreePath parentPath)
    {
        TreePresenter.ElementTreeNode parentNode = null;
        if (parentPath != null)
        {
            parentNode = (TreePresenter.ElementTreeNode) (parentPath.getLastPathComponent());
            if (parentNode != null && !(parentNode.element instanceof Category))
            {
                parentNode = (TreePresenter.ElementTreeNode) parentNode.getParent();
            }
        }
        Category parent = parentNode != null ? (Category) parentNode.element : hierarchy.root;
        parent.add(new CategoryImpl("New category"));
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
            category.remove(elements.toArray(new Element[elements.size()]));
        }
    }
}
