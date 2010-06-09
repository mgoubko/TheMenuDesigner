package ru.lavila.menudesigner.math;

import ru.lavila.menudesigner.models.Item;
import ru.lavila.menudesigner.models.ItemsList;
import ru.lavila.menudesigner.models.menumodels.MenuModel;

import java.util.Arrays;
import java.util.List;

public class ItemsListCalculator extends MenuModelClient
{
    private final ItemsList itemsList;
    private MenuModel menuModel;

    public ItemsListCalculator(ItemsList itemsList, MenuModel menuModel)
    {
        this.itemsList = itemsList;
        this.menuModel = menuModel;
    }

    public MenuModel getMenuModel()
    {
        return menuModel;
    }

    public void setMenuModel(MenuModel menuModel)
    {
        this.menuModel = menuModel;
        fireModelChanged();
    }

    public double getOptimalSearchTime()
    {
        return getOptimalSearchTime(Arrays.asList(itemsList.toArray()));
    }

    public double[] getOptimalProportion()
    {
        return menuModel.getOptimalProportion();
    }

    double getOptimalSearchTime(List<Item> items)
    {
        if (items.isEmpty()) return 0;
        double[] proportion = menuModel.getOptimalProportion();
        double result = getSearchTime(proportion);
        double sum = 0;
        for (double popularity : proportion)
        {
            sum += popularity * Math.log(popularity);
        }
        result /= sum;
        sum = 0;
        double totalPopularity = 0;
        for (Item item : items)
        {
            double popularity = item.getPopularity();
            totalPopularity += popularity;
            sum += popularity * Math.log(popularity);
        }
        return result * (sum - totalPopularity * Math.log(totalPopularity));
    }

    double getSearchTime(double[] proportion)
    {
        double result = 0;
        for (int index = 0; index < proportion.length; index++)
        {
            result += menuModel.getTimeToSelect(index + 1, proportion.length) * proportion[index];
        }
        return result;
    }
}
