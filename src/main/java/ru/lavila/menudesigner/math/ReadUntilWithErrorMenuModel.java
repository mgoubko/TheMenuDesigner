package ru.lavila.menudesigner.math;

import org.apache.commons.math.FunctionEvaluationException;
import org.apache.commons.math.analysis.UnivariateRealFunction;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolver;
import org.apache.commons.math.analysis.solvers.UnivariateRealSolverFactory;

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
    public double[] getOptimalProportion(int itemsSize)
    {
        if (itemsSize < 2) return new double[]{1};
        try
        {
            UnivariateRealSolver solver = UnivariateRealSolverFactory.newInstance().newDefaultSolver();
            ProportionFinderFunction bestFunction = null;
            double bestBase = 0;
            double minResult = 0;
            for (int size = 2; size <= itemsSize; size++)
            {
                ProportionFinderFunction function = new ProportionFinderFunction(size);
                double base = solver.solve(function, 0, 1, 0.1);
                double result = -1 / Math.log(base);
                if (bestFunction == null || result < minResult)
                {
                    bestFunction = function;
                    bestBase = base;
                    minResult = result;
                }
            }
            return bestFunction.proportion(bestBase);
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error solving equation", e);
        }
    }

    private class ProportionFinderFunction implements UnivariateRealFunction
    {
        private final int size;
        private final double modelFactor;

        public ProportionFinderFunction(int size)
        {
            this.size = size;
            modelFactor = ((1 + 2 * errorProbability) * (tResp + tLoad * size + tClick) + errorProbability * tRead * size) / ((1 + errorProbability) * tRead);
        }

        private double element(double base, int index)
        {
            return Math.pow(base, modelFactor + index + 1);
        }

        private double[] proportion(double base)
        {
            double[] proportion = new double[size];
            for (int index = 0; index < size; index++)
            {
                proportion[index] = element(base, index);
            }
            return proportion;
        }

        public double value(double base) throws FunctionEvaluationException
        {
            double value = -1;
            for (int index = 0; index < size; index++)
            {
                value += element(base, index);
            }
            return value;
        }
    }
}
