package com.example.restapi.controller;

import com.example.restapi.entity.Employee;
import com.example.restapi.repository.EmployeeRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeRepository repository;

    public EmployeeController(EmployeeRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Employee> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Employee getById(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow();
    }

    @PostMapping
    public Employee create(@RequestBody Employee employee) {
        return repository.save(employee);
    }
/*
    @PutMapping("/{id}")
    public Employee update(@PathVariable Long id,
                           @RequestBody Employee employee) {

        Employee existing = repository.findById(id)
                .orElseThrow();

        existing.setEmpname(employee.getEmpname());
        existing.setSalary(employee.getSalary());

        return repository.save(existing);
    }
*/

@PutMapping("/{id}")
public Employee update(@PathVariable Long id,
                       @RequestBody Employee employee) {

    Employee existing = repository.findById(id)
            .orElseThrow();

    if (employee.getEmpname() != null) {
        existing.setEmpname(employee.getEmpname());
    }

    if (employee.getSalary() != null) {
        existing.setSalary(employee.getSalary());
    }

    return repository.save(existing);
}

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repository.deleteById(id);
    }
}