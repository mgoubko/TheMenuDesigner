package ru.lavila.menudesigner.views;

import ru.lavila.menudesigner.presenters.TreePresenter;

import javax.swing.*;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class TreeView extends JTree
{
    public TreeView(TreeModel treeModel)
    {
        super(treeModel);
        setEditable(true);
    }

    public void addCategory()
    {
        TreePath path = getSelectionPath();
        expandPath(path);
        ((TreePresenter) getModel()).addCategory(path == null ? null : path.getLastPathComponent());
    }
}
