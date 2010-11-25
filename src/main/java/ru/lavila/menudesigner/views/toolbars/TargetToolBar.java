package ru.lavila.menudesigner.views.toolbars;

import ru.lavila.menudesigner.MenuDesigner;
import ru.lavila.menudesigner.controllers.TargetTreeController;
import ru.lavila.menudesigner.io.ExcelWorkbookFilter;
import ru.lavila.menudesigner.views.MainContentPane;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TargetToolBar extends JToolBar
{
    private final MainContentPane contentPane;
    private final TargetTreeController controller;
    private final JFileChooser fileChooser;

    public TargetToolBar(MainContentPane contentPane, TargetTreeController controller)
    {
        super("Target");
        this.contentPane = contentPane;
        this.controller = controller;
        fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.addChoosableFileFilter(new ExcelWorkbookFilter());
        setupButtons();
    }

    private void setupButtons()
    {
        JButton save = new JButton(MenuDesigner.getIcon("save"));
        save.setToolTipText("Save menu structure...");
        save.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                int result = fileChooser.showSaveDialog(contentPane);
                if (result == JFileChooser.APPROVE_OPTION)
                {
                    controller.save(fileChooser.getSelectedFile().getPath());
                }
            }
        });
        add(save);
        JButton optimize = new JButton(MenuDesigner.getIcon("normalize"));
        optimize.setToolTipText("Optimize Menu");
        optimize.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                controller.optimizeSubTree();
            }
        });
        add(optimize);
    }
}
