package model.dao;

import model.entities.Department;

import java.util.List;

public interface DepartmentDao {

    void insert(Department department);
    void update(Department department);
    void deleteById(Integer departmentId);
    Department findById(Integer departmentId);
    List<Department> findAll();
}
