package com.nit.controller;

import java.util.List;

import com.nit.entity.Employee;
import com.nit.repository.EmployeeRepository;

import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;

    public EmployeeController(
            EmployeeRepository employeeRepository) {

        this.employeeRepository = employeeRepository;
    }

 

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public List<Employee> getAllEmployees() {

        return employeeRepository.findAll();
    }

   
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Employee addEmployee(
            @Valid @RequestBody Employee employee) {

        return employeeRepository.save(employee);
    }

   
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteEmployee(
            @PathVariable Integer id) {

        if (!employeeRepository.existsById(id)) {

            return ResponseEntity.notFound().build();
        }

        employeeRepository.deleteById(id);

        return ResponseEntity.ok(
                "Employee deleted successfully"
        );
    }
}