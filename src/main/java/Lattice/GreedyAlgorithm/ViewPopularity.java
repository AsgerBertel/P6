package Lattice.GreedyAlgorithm;

import java.util.Objects;

public class ViewPopularity {
    private String name;
    private int amountOfTimesUsed = 0;
    private int overAllValue;
    private int currentDailyValue;
    private int globalAccumulatedValue;
    private int dailyAccumulatedValue;

    public ViewPopularity(String name, int dailyAccumulatedValue, int globalAccumulatedValue) {
        this.dailyAccumulatedValue = dailyAccumulatedValue;
        this.globalAccumulatedValue = globalAccumulatedValue;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getCurrentDailyValue() {
        return currentDailyValue;
    }

    public void incrementAmountOfTimesUsed() {
        this.amountOfTimesUsed++;
    }

    public void setOverAllValue(int overAllValue) {
        this.overAllValue += overAllValue;
    }

    public int getGlobalAverageValue() {
        if (overAllValue != 0 || amountOfTimesUsed != 0)
            return overAllValue / amountOfTimesUsed;

        return 1;
    }

    public void setCurrentDailyValue(int currentDailyValue) {
        this.currentDailyValue = currentDailyValue;
    }

    public void setGlobalAccumulatedValue(int globalAccumulatedValue) {
        this.globalAccumulatedValue = globalAccumulatedValue;
    }

    public void setDailyAccumulatedValue(int dailyAccumulatedValue) {
        this.dailyAccumulatedValue = dailyAccumulatedValue;
    }


    public int calculatePopularityValue() {
        return ((currentDailyValue/dailyAccumulatedValue) + (getGlobalAverageValue()/globalAccumulatedValue))/2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ViewPopularity that = (ViewPopularity) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
