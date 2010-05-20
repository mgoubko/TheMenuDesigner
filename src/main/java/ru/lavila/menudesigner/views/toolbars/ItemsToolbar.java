package ru.lavila.menudesigner.views.toolbars;

import ru.lavila.menudesigner.controllers.ItemsController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ItemsToolbar extends JToolBar
{
    private final ItemsController controller;

    public ItemsToolbar(ItemsController controller)
    {
        super("Designer Toolbar");
        this.controller = controller;
        setupButtons();
    }

    private void setupButtons()
    {
        JButton normalize = new JButton("Normalize");
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
