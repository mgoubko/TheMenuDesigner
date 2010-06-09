package ru.lavila.menudesigner;

import ru.lavila.menudesigner.views.MainContentPane;

import javax.swing.*;

public class MenuDesigner
{
    private static void createAndShowGUI()
    {
        JFrame frame = new JFrame("Menu Designer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(new MainContentPane());
        frame.setSize(1000, 600);
        frame.setVisible(true);
    }

    public static Icon getIcon(String name)
    {
        return new ImageIcon(MenuDesigner.class.getResource("icons/" + name + ".png"));
    }

    public static void main(String[] args)
    {
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Menu Designer");
        javax.swing.SwingUtilities.invokeLater(new Runnable()
        {
            public void run()
            {
                createAndShowGUI();
            }
        });
    }
}
