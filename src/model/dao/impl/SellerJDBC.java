package model.dao.impl;

import db.DB;
import db.DBException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerJDBC implements SellerDao {
    private Connection connection;

    public SellerJDBC(Connection connection){
        this.connection = connection;
    }

    @Override
    public void insert(Seller seller) {

    }

    @Override
    public void update(Seller seller) {

    }

    @Override
    public void deleteById(Integer sellerId) {

    }

    @Override
    public Seller findById(Integer sellerId) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = connection.prepareStatement(
                    """
                    SELECT seller.*, department.Name as DepName
                    FROM seller INNER JOIN department
                    ON seller.DepartmentId = department.Id
                    WHERE seller.Id = ? 
                    """
            );

            preparedStatement.setInt(1, sellerId);

            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                Department department = new Department(
                        resultSet.getInt("DepartmentId"),
                        resultSet.getString("DepName")
                );

                Seller seller = new Seller(
                        resultSet.getString("Name"),
                        resultSet.getString("Email"),
                        resultSet.getDate("BirthDate"),
                        resultSet.getDouble("BaseSalary"),
                        department

                );
                return seller;
            }
            return null;
        } catch(SQLException exception){
            throw   new DBException(exception.getMessage());
        } finally {
            DB.closeStatement(preparedStatement);
            DB.closeResultSet(resultSet);
        }

    }

    @Override
    public List<Seller> findAll() {
        return null;
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = connection.prepareStatement(
                    """
                    SELECT seller.*, department.Name as DepName
                    FROM seller INNER JOIN department
                    ON seller.DepartmentId = department.Id
                    WHERE department.id = ?
                    ORDER BY Name
                    """
            );

            preparedStatement.setInt(1, department.getId());

            resultSet = preparedStatement.executeQuery();

            List<Seller> sellers = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();
            while(resultSet.next()){
                Department newDepartment = map.get(resultSet.getInt("DepartmentId"));

                if(newDepartment == null){
                    newDepartment = new Department(
                            resultSet.getInt("DepartmentId"),
                            resultSet.getString("DepName")
                    );
                    map.put(resultSet.getInt("DepartmentId"), newDepartment);

                }

                Seller seller = new Seller(
                        resultSet.getString("Name"),
                        resultSet.getString("Email"),
                        resultSet.getDate("BirthDate"),
                        resultSet.getDouble("BaseSalary"),
                        newDepartment

                );
                sellers.add(seller);

            }
            return null;
        } catch(SQLException exception){
            throw   new DBException(exception.getMessage());
        } finally {
            DB.closeStatement(preparedStatement);
            DB.closeResultSet(resultSet);
        }

    }
}
