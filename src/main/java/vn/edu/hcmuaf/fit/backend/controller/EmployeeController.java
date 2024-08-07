package vn.edu.hcmuaf.fit.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmuaf.fit.backend.dto.EmployeeDTO;
import vn.edu.hcmuaf.fit.backend.model.Employee;
import vn.edu.hcmuaf.fit.backend.service.EmployeeService;

import java.util.List;

@RestController
@RequestMapping("api/employees")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Create a new Employee
    @PostMapping()
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        return new ResponseEntity<>(employeeService.saveEmployee(employee), HttpStatus.CREATED);
    }

    // Get all Employee
    @GetMapping
    public List<EmployeeDTO> getAllEmployees() {
        return employeeService.getAllEmployee();
    }

    // Get Employee by id
    @GetMapping("{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable ("id") int id) {
        return new ResponseEntity<>(employeeService.getEmployeeByID(id), HttpStatus.OK);
    }

    // Update Employee by id
    @PutMapping("{id}")
    public ResponseEntity<Employee> updateEmployeeById(@PathVariable ("id") int id,
                                                   @RequestBody EmployeeDTO employeeDTO) {
        return new ResponseEntity<>(employeeService.updateEmployeeByID(id, employeeDTO), HttpStatus.OK);
    }

    // Change password
    @PutMapping("/change-password/{email}")
    public ResponseEntity<String> changePassword(@PathVariable ("email") String email,
                                                   @RequestBody EmployeeDTO employeeDTO) {
        employeeService.updatePassword(email, employeeDTO);
        return new ResponseEntity<>("Employee email: " + email + " is changed password successfully", HttpStatus.OK);
    }

    // Delete Employee by id
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteEmployeeById(@PathVariable ("id") int id) {
        employeeService.deleteEmployeeByID(id);
        return new ResponseEntity<>("Employee " + id + " is deleted successfully!", HttpStatus.OK);
    }

}
