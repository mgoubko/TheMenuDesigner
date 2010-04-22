package ru.lavila.menudesigner.views;

import ru.lavila.menudesigner.controllers.TreeController;
import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.models.Element;
import ru.lavila.menudesigner.presenters.TreePresenter;

import javax.swing.*;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.List;

public class TreeView extends JTree
{
    private final TreeController controller;

    public TreeView(TreePresenter treePresenter, TreeController controller)
    {
        super(treePresenter);
        this.controller = controller;
        setEditable(true);
    }

    @Override
    public TreePresenter getModel()
    {
        return (TreePresenter) super.getModel();
    }

    public void addCategory()
    {
        TreePath path = getSelectionPath();
        expandPath(path);
        controller.addCategory(getModel().getSelectedCategory(path));
    }

    public void removeSelection()
    {
        controller.removeNodes(getSelectionPaths());
    }

    public List<Element> getSelectedElements()
    {
        return getModel().getSelectedElements(getSelectionPaths());
    }

    public Category getSelectedCategory()
    {
        return getModel().getSelectedCategory(getSelectionPath());
    }
}
