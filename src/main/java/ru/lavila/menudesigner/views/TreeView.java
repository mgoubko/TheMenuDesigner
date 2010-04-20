package ru.lavila.menudesigner.views;

import javax.swing.*;
import javax.swing.tree.TreeModel;

public class TreeView extends JTree
{
    public TreeView(TreeModel treeModel)
    {
        super(treeModel);
        setEditable(true);
    }
}
