package edu.employee.dao;

public interface EmployeeDao {
    void getDepartmentEmployees(String deptTitle);
    void getDepartmentAvgSalary();
    void getWorkingEmployees();
    void increaseSalary(String deptCode);
    void getEmployeesWithoutPhone();
}