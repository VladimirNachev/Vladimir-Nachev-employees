package com.example.employees.dtos;

public class EmployeesPairDTO {
    private long firstEmployeeId;
    private long secondEmployeeId;
    private int commonWorkingDaysCount;

    public EmployeesPairDTO(long firstEmployeeId, long secondEmployeeId, int commonWorkingDaysCount) {
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

    public void setFirstEmployeeId(long firstEmployeeId) {
        this.firstEmployeeId = firstEmployeeId;
    }

    public void setSecondEmployeeId(long secondEmployeeId) {
        this.secondEmployeeId = secondEmployeeId;
    }

    public void setCommonWorkingDaysCount(int commonWorkingDaysCount) {
        this.commonWorkingDaysCount = commonWorkingDaysCount;
    }
}
