package ru.lavila.menudesigner.presenters;

import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Hierarchy;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class ItemsTreePresenter implements TreeModel
{
    private final Hierarchy hierarchy;

    public ItemsTreePresenter(Hierarchy hierarchy)
    {
        this.hierarchy = hierarchy;
    }

    public Object getRoot()
    {
        return hierarchy.root;
    }

    public Object getChild(Object parent, int index)
    {
        return parent instanceof Category ? ((Category) parent).getChoices().get(index) : null;
    }

    public int getChildCount(Object parent)
    {
        return parent instanceof Category ? ((Category) parent).getChoices().size() : 0;
    }

    public boolean isLeaf(Object node)
    {
        return !(node instanceof Category);
    }

    public void valueForPathChanged(TreePath path, Object newValue)
    {
    }

    public int getIndexOfChild(Object parent, Object child)
    {
        return parent instanceof Category ? ((Category) parent).getChoices().indexOf(child) : 0;
    }

    public void addTreeModelListener(TreeModelListener l)
    {
    }

    public void removeTreeModelListener(TreeModelListener l)
    {
    }
}
