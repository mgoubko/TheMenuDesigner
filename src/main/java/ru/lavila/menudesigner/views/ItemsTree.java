package ru.lavila.menudesigner.views;

import ru.lavila.menudesigner.models.Choice;
import ru.lavila.menudesigner.presenters.PopularityFormatter;

import javax.swing.*;
import javax.swing.tree.TreeModel;

public class ItemsTree extends JTree
{
    public ItemsTree(TreeModel treeModel)
    {
        super(treeModel);
    }

    @Override
    public String convertValueToText(Object value, boolean selected, boolean expanded, boolean leaf, int row, boolean hasFocus)
    {
        return value instanceof Choice ? ((Choice) value).getName() + " (" + PopularityFormatter.format(((Choice) value).getPopularity()) + ")" : "";
    }
}
