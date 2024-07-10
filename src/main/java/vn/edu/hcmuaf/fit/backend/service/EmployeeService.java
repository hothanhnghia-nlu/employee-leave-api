package vn.edu.hcmuaf.fit.backend.service;

import vn.edu.hcmuaf.fit.backend.dto.EmployeeDTO;
import vn.edu.hcmuaf.fit.backend.model.Employee;

import java.util.List;

public interface EmployeeService {
    Employee saveEmployee(Employee employee);
    List<EmployeeDTO> getAllEmployee();
    Employee getEmployeeByID(int id);
    Employee updateEmployeeByID(int id, EmployeeDTO employeeDTO);
    Employee updatePassword(String email, EmployeeDTO employeeDTO);
    void deleteEmployeeByID(int id);
    String login(String username, String pass);
}
