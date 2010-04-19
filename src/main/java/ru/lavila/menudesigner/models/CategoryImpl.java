package ru.lavila.menudesigner.models;

import java.util.*;

public class CategoryImpl implements Category
{
    private final String name;
    private final List<Choice> choices;

    public CategoryImpl(String name)
    {
        this.name = name;
        choices = new ArrayList<Choice>();
    }

    public List<Choice> getChoices()
    {
        return Collections.unmodifiableList(choices);
    }

    public void add(Choice... newChoices)
    {
        choices.addAll(Arrays.asList(newChoices));
    }

    public String getName()
    {
        return name;
    }

    public double getPopularity()
    {
        double popularity = 0;
        for (Choice choice : choices)
        {
            popularity += choice.getPopularity();
        }
        return popularity;
    }
}
