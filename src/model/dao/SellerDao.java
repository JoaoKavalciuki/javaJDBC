package model.dao;


import model.entities.Department;
import model.entities.Seller;

import java.util.List;

public interface SellerDao {

    void insert(Seller seller);
    void update(Seller seller);
    void deleteById(Integer sellerId);
    Seller findById(Integer sellerId);
    List<Seller> findAll();
    List<Seller> findByDepartment(Department department);
}
