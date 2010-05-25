package ru.lavila.menudesigner.views;

import ru.lavila.menudesigner.controllers.MenuModelsController;
import ru.lavila.menudesigner.presenters.CalculationsListener;
import ru.lavila.menudesigner.presenters.MenuModelsPresenter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MenuModelsPanel extends JPanel implements CalculationsListener
{
    private final MenuModelsPresenter presenter;
    private final MenuModelsController controller;
    private final JLabel timeLabel;
    private final JLabel proportionLabel;
    private final JLabel branchingLabel;

    public MenuModelsPanel(MenuModelsPresenter presenter, MenuModelsController controller)
    {
        super(new GridLayout(4, 1));

        this.presenter = presenter;
        this.controller = controller;
        presenter.addCalculationListener(this);

        setPreferredSize(new Dimension(500, 120));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        timeLabel = new JLabel();
        branchingLabel = new JLabel();
        proportionLabel = new JLabel();

        JPanel menuTypePanel = new JPanel(new BorderLayout(10, 0));
        menuTypePanel.add(new JLabel("Menu type"), BorderLayout.WEST);

        final JComboBox menuModels = new JComboBox(this.presenter.getModelNames());
        menuModels.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                MenuModelsPanel.this.controller.setMenuModel(MenuModelsPanel.this.presenter.getModel(menuModels.getSelectedIndex()));
            }
        });
        menuModels.setSelectedIndex(this.presenter.getDefaultIndex());
        menuTypePanel.add(menuModels, BorderLayout.CENTER);
        add(menuTypePanel);

        add(timeLabel);
        add(branchingLabel);
        add(proportionLabel);
    }

    public void valuesChanged()
    {
        timeLabel.setText("Optimal menu search time: " + presenter.getOptimalSearchTime());
        branchingLabel.setText("Menu panels should have " + presenter.getOptimalBranchingFactor() + " choices with relative popularities");
        proportionLabel.setText(presenter.getOptimalProportion());
    }
}
