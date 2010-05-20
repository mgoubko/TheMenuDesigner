package ru.lavila.menudesigner.math;

public class ReadUntilMenuModel implements MenuModel
{
    protected final double tResp;
    protected final double tLoad;
    protected final double tRead;
    protected final double tClick;

    public ReadUntilMenuModel(double tResp, double tLoad, double tRead, double tClick)
    {
        this.tResp = tResp;
        this.tLoad = tLoad;
        this.tRead = tRead;
        this.tClick = tClick;
    }

    public double getTimeToSelect(int target, int total)
    {
        return tResp + tLoad * total + tRead * target + tClick;
    }
}
