package ru.lavila.menudesigner.views.toolbars;

import ru.lavila.menudesigner.MenuDesigner;
import ru.lavila.menudesigner.controllers.ItemsController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ItemsToolBar extends JToolBar
{
    private final ItemsController controller;

    public ItemsToolBar(ItemsController controller)
    {
        super("Items");
        this.controller = controller;
        setupButtons();
    }

    private void setupButtons()
    {
        JButton normalize = new JButton(MenuDesigner.getIcon("refresh"));
        normalize.setToolTipText("Normalize items' popularities");
        normalize.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.normalizePopularities();
            }
        });
        add(normalize);
    }
}
