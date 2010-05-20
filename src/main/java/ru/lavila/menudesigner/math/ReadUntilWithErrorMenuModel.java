package ru.lavila.menudesigner.math;

public class ReadUntilWithErrorMenuModel extends ReadUntilMenuModel
{
    protected final double errorProbability;

    public ReadUntilWithErrorMenuModel(double tResp, double tLoad, double tRead, double tClick, double errorProbability)
    {
        super(tResp, tLoad, tRead, tClick);
        this.errorProbability = errorProbability;
    }

    @Override
    public double getTimeToSelect(int target, int total)
    {
        return (1 + 2 * errorProbability) * (tResp + tClick + tLoad * total) + errorProbability * tRead * total + (1 + errorProbability) * tRead * target;
    }

    @Override
    public double[] getOptimalProportion()
    {
        return new double[]{0.329586, 0.226308, 0.155393, 0.1067, 0.0732647, 0.0503068, 0.0345428, 0.0237186};
    }
}
