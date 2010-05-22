package ru.lavila.menudesigner.views;

import ru.lavila.menudesigner.math.HierarchyCalculator;
import ru.lavila.menudesigner.models.Category;
import ru.lavila.menudesigner.presenters.CalculationsListener;

import javax.swing.*;
import java.awt.*;

public class CalculationsPanel extends JPanel implements CalculationsListener
{
    private final HierarchyCalculator calculator;
    private final JLabel currentSearchTime;
    private final JLabel optimalSearchTime;
    private final JLabel searchTimeExcess;
    private final JPanel panelCalculations;

    public CalculationsPanel(HierarchyCalculator calculator)
    {
        super(new BorderLayout());

        this.calculator = calculator;

        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel hierarchyCalculations = new JPanel(new GridLayout(3, 2, 20, 5));
        hierarchyCalculations.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Menu Search Time"), BorderFactory.createEmptyBorder(5, 10, 5, 0)));
        hierarchyCalculations.add(new JLabel("Current"));
        currentSearchTime = new JLabel();
        hierarchyCalculations.add(currentSearchTime);
        hierarchyCalculations.add(new JLabel("Optimal"));
        optimalSearchTime = new JLabel();
        hierarchyCalculations.add(optimalSearchTime);
        hierarchyCalculations.add(new JLabel("Excess"));
        searchTimeExcess = new JLabel();
        hierarchyCalculations.add(searchTimeExcess);
        add(hierarchyCalculations, BorderLayout.WEST);

        panelCalculations = new JPanel(new GridLayout(3, 1, 20, 5));
        panelCalculations.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Selected Category"), BorderFactory.createEmptyBorder(5, 20, 5, 0)));
        panelCalculations.add(new JLabel());
        panelCalculations.add(new JLabel());
        panelCalculations.add(new JLabel());

        add(panelCalculations, BorderLayout.CENTER);

        valuesChanged();
    }

    public void valuesChanged()
    {
        double current = calculator.getHierarchySearchTime();
        currentSearchTime.setText(String.format("%.2f", current));
        double optimal = calculator.getOptimalSearchTime();
        optimalSearchTime.setText(String.format("%.2f", optimal));
        searchTimeExcess.setText(String.format("%d%%", Math.round(100 * (current - optimal) / optimal)));
    }

    public void showFor(Category category)
    {
        String[] texts = new String[3];
        if (category != null)
        {
            texts[0] = category.getName();
            texts[1] = "Time loss";
            texts[2] = String.format("%.4f", calculator.getCategoryTimeLoss(category));
        }
        for (int index = 0; index < 3; index++)
        {
            ((JLabel) panelCalculations.getComponent(index)).setText(texts[index]);
        }
    }
}
