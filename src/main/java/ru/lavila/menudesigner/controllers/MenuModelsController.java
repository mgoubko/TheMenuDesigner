package ru.lavila.menudesigner.controllers;

import ru.lavila.menudesigner.math.ItemsListCalculator;
import ru.lavila.menudesigner.math.MenuModel;

public class MenuModelsController
{
    private final ItemsListCalculator calculator;

    public MenuModelsController(ItemsListCalculator calculator)
    {
        this.calculator = calculator;
    }

    public void setMenuModel(MenuModel model)
    {
        calculator.setMenuModel(model);
    }
}
