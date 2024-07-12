package vn.edu.hcmuaf.fit.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import vn.edu.hcmuaf.fit.backend.dto.LoginBodyDTO;
import vn.edu.hcmuaf.fit.backend.service.EmployeeService;

@RestController
@RequestMapping("api/login")
public class LoginController {
    @Autowired
    private EmployeeService employeeService;

    public LoginController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping()
    public String login(@RequestBody LoginBodyDTO loginBodydto){
        return employeeService.login(loginBodydto.getUsername(),loginBodydto.getPass());
    }
}
