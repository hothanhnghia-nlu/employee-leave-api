package vn.edu.hcmuaf.fit.backend.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.edu.hcmuaf.fit.backend.dto.BossDTO;
import vn.edu.hcmuaf.fit.backend.dto.EmployeeDTO;
import vn.edu.hcmuaf.fit.backend.exception.ResourceNotFoundException;
import vn.edu.hcmuaf.fit.backend.model.Employee;
import vn.edu.hcmuaf.fit.backend.repository.EmployeeRepository;
import vn.edu.hcmuaf.fit.backend.service.EmployeeService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public List<EmployeeDTO> getAllEmployee() {
        List<Employee> employees = employeeRepository.findAll();

        return employees.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private EmployeeDTO convertToDTO(Employee employee) {
        BossDTO bossDTO = null;
        if (employee.getBossId() != null) {
            bossDTO = new BossDTO(
                    employee.getBossId().getId(),
                    employee.getBossId().getEmail(),
                    employee.getBossId().getFullName(),
                    employee.getBossId().getPosition()
            );
        }

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setUserId(employee.getId());
        employeeDTO.setUsername(employee.getUsername());
        employeeDTO.setEmail(employee.getEmail());
        employeeDTO.setFullName(employee.getFullName());
        employeeDTO.setPosition(employee.getPosition());
        employeeDTO.setDayOffRemaining(employee.getDayOffRemaining());
        employeeDTO.setFirstDayOfWork(employee.getFirstDayOfWork());
        employeeDTO.setCreatedAt(employee.getCreatedAt());
        employeeDTO.setUpdatedAt(employee.getUpdatedAt());
        employeeDTO.setBoss(bossDTO);

        return employeeDTO;
    }

    @Override
    public Employee getEmployeeByID(int id) {
        return employeeRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Employee", "Id", id));
    }

    @Override
    public Employee updateEmployeeByID(int id, EmployeeDTO employeeDTO) {
        Employee existingEmployee = employeeRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Employee", "Id", id));

        existingEmployee.setFullName(employeeDTO.getFullName() != null ? employeeDTO.getFullName() : existingEmployee.getFullName());
        existingEmployee.setEmail(employeeDTO.getEmail() != null ? employeeDTO.getEmail() : existingEmployee.getEmail());
        existingEmployee.setPosition(employeeDTO.getPosition() != null ? employeeDTO.getPosition() : existingEmployee.getPosition());
        existingEmployee.setFirstDayOfWork(employeeDTO.getFirstDayOfWork() != null ? employeeDTO.getFirstDayOfWork() : existingEmployee.getFirstDayOfWork());
        existingEmployee.setUpdatedAt(LocalDateTime.now());

        return employeeRepository.save(existingEmployee);
    }

    @Override
    public Employee updatePassword(String email, EmployeeDTO employeeDTO) {
        Employee existingEmployee = employeeRepository.findByEmail(email);

        existingEmployee.setPassword(employeeDTO.getPassword());
        existingEmployee.setUpdatedAt(LocalDateTime.now());

        return employeeRepository.save(existingEmployee);
    }

    @Override
    public void deleteEmployeeByID(int id) {
        employeeRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException("Employee", "Id", id));

        employeeRepository.deleteById(id);
    }

    @Override
    public String login(String username, String pass) {
        Employee e = employeeRepository.findByUsernameAndPassword(username, pass);
        if (e != null) return e.getId() + "";
        return "username hoặc pass không đúng!";
    }
}
