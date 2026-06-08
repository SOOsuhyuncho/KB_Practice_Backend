package edu.employee.run;

import edu.employee.service.EmployeeService;

public class EmployeeRun {
    public static void main(String[] args) {
        EmployeeService service = new EmployeeService();

        service.displayMenu();
    }
}
