package ru.lavila.menudesigner.controllers;

import ru.lavila.menudesigner.io.MenuModelsLoader;
import ru.lavila.menudesigner.math.ItemsListCalculator;
import ru.lavila.menudesigner.models.menumodels.MenuModel;
import ru.lavila.menudesigner.models.menumodels.MenuModelsLibrary;

import java.util.List;

public class MenuModelsController
{
    private final ItemsListCalculator calculator;
    private final MenuModelsLibrary library;

    public MenuModelsController(ItemsListCalculator calculator, MenuModelsLibrary library)
    {
        this.calculator = calculator;
        this.library = library;
    }

    public void setMenuModel(MenuModel model)
    {
        if (calculator.getMenuModel() != model)
        {
            calculator.setMenuModel(model);
        }
    }

    public void loadMenuModels(String filename)
    {
        List<MenuModel> models = new MenuModelsLoader().loadMenuModels(filename);
        if (models == null || models.isEmpty()) return;
        library.addMenuModels(models);
    }
}
