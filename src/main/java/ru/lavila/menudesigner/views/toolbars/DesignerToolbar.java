package ru.lavila.menudesigner.views.toolbars;

import ru.lavila.menudesigner.controllers.DesignerController;
import ru.lavila.menudesigner.views.ItemsView;
import ru.lavila.menudesigner.views.TreeView;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DesignerToolbar extends JToolBar
{
    private final ItemsView sourceView;
    private final TreeView targetView;
    private final DesignerController controller;

    public DesignerToolbar(ItemsView sourceView, TreeView targetView, DesignerController controller)
    {
        super("Designer Toolbar");
        this.sourceView = sourceView;
        this.targetView = targetView;
        this.controller = controller;
        setupButtons();
    }

    private void setupButtons()
    {
        JButton use = new JButton(">>");
        use.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.aliasElements(targetView.getSelectedCategory(), sourceView.getSelectedElements());
            }
        });
        add(use);
    }
}
