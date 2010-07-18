package ru.lavila.menudesigner.views.toolbars;

import ru.lavila.menudesigner.MenuDesigner;
import ru.lavila.menudesigner.controllers.TargetTreeController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TargetToolBar extends JToolBar
{
    private final TargetTreeController controller;

    public TargetToolBar(TargetTreeController controller)
    {
        super("Target");
        this.controller = controller;
        setupButtons();
    }

    private void setupButtons()
    {
        JButton optimize = new JButton(MenuDesigner.getIcon("normalize"));
        optimize.setToolTipText("Optimize Menu");
        optimize.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.optimize();
            }
        });
        add(optimize);
    }
}
