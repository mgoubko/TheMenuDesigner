package ru.lavila.menudesigner.views;

import ru.lavila.menudesigner.controllers.TreeController;
import ru.lavila.menudesigner.presenters.TreePresenter;

import javax.swing.*;
import javax.swing.tree.TreePath;

public class TreeView extends JTree
{
    private final TreeController treeController;

    public TreeView(TreePresenter treePresenter, TreeController treeController)
    {
        super(treePresenter);
        this.treeController = treeController;
        setEditable(true);
    }

    public void addCategory()
    {
        TreePath path = getSelectionPath();
        expandPath(path);
        treeController.addCategory(path);
    }

    public void removeSelection()
    {
        treeController.removeNodes(getSelectionPaths());
    }
}
