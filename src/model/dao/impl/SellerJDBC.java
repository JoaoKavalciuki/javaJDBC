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
import java.util.List;

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
                        resultSet.getString("DepartmentName")
                );

                Seller seller = new Seller(
                        resultSet.getString("Name"),
                        resultSet.getString("Email"),
                        resultSet.getDate("BirthDate"),
                        resultSet.getDouble("BaseSalary"),
                        department

                );
            }
            return null;
        } catch(SQLException exception){
            throw new DBException(exception.getMessage());
        } finally {
            DB.closeStatement(preparedStatement);
            DB.closeResultSet(resultSet);
        }

    }

    @Override
    public List<Seller> findAll() {
        return null;
    }
}
