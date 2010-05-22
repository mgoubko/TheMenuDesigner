package ru.lavila.menudesigner.views.toolbars;

import ru.lavila.menudesigner.MenuDesigner;
import ru.lavila.menudesigner.views.TreeView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TreeToolBar extends JToolBar
{
    private final TreeView treeView;

    public TreeToolBar(TreeView treeView)
    {
        super("Tree");
        this.treeView = treeView;
        setupButtons();
    }

    private void setupButtons()
    {
        JButton newCategory = new JButton(MenuDesigner.getIcon("create"));
        newCategory.setToolTipText("Create new category");
        newCategory.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                treeView.addCategory();
            }
        });
        add(newCategory);

        JButton remove = new JButton(MenuDesigner.getIcon("delete"));
        remove.setToolTipText("Remove selected elements");
        remove.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                treeView.removeSelection();
            }
        });
        add(remove);
    }
}
