package com.example.employees.models;

import java.util.Objects;

public class EmployeesProjectTripple {
    private long firstEmployeeId;
    private long secondEmployeeId;
    private long projectId;

    public EmployeesProjectTripple() {
    }

    public EmployeesProjectTripple(long firstEmployeeId, long secondEmployeeId, long projectId) {
        this.firstEmployeeId = firstEmployeeId;
        this.secondEmployeeId = secondEmployeeId;
        this.projectId = projectId;
    }

    public long getFirstEmployeeId() {
        return firstEmployeeId;
    }

    public long getSecondEmployeeId() {
        return secondEmployeeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeesProjectTripple that = (EmployeesProjectTripple) o;
        return firstEmployeeId == that.firstEmployeeId && secondEmployeeId == that.secondEmployeeId && projectId == that.projectId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstEmployeeId, secondEmployeeId, projectId);
    }
}
