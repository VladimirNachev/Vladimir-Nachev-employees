package com.example.employees.models;

public class EmployeesPair {
    private final long firstEmployeeId;
    private final long secondEmployeeId;
    private final int commonWorkingDaysCount;

    public EmployeesPair(long firstEmployeeId, long secondEmployeeId, int commonWorkingDaysCount) {
        this.firstEmployeeId = firstEmployeeId;
        this.secondEmployeeId = secondEmployeeId;
        this.commonWorkingDaysCount = commonWorkingDaysCount;
    }

    public long getFirstEmployeeId() {
        return firstEmployeeId;
    }

    public long getSecondEmployeeId() {
        return secondEmployeeId;
    }

    public int getCommonWorkingDaysCount() {
        return commonWorkingDaysCount;
    }
}
