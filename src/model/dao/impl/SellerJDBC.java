package model.dao.impl;

import db.DB;
import db.DBException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.*;
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
        PreparedStatement preparedStatement = null;

        try{
            preparedStatement = connection.prepareStatement(
                    """
                    INSERT INTO seller
                    (Name, Email, BirthDate, BaseSalary, DepartmentId)
                    VALUES 
                    (?, ?, ?, ?, ?)
                    """,
                    PreparedStatement.RETURN_GENERATED_KEYS
            );

            preparedStatement.setString(1, seller.getName());
            preparedStatement.setString(2, seller.getEmail());
            preparedStatement.setDate(3, new Date(seller.getBirthDate().getTime()));
            preparedStatement.setDouble(4, seller.getBaseSalary());
            preparedStatement.setInt(5, seller.getDepartment().getId());

            int rowsAffected = preparedStatement.executeUpdate();

            if(rowsAffected > 0){
                ResultSet resultSet = preparedStatement.getGeneratedKeys();

                if(resultSet.next()){
                    seller.setId(resultSet.getInt(1));
                }
            } else {
                throw new DBException("Nenhuma linha foi afetada");
            }
        }catch (SQLException exception){
            throw new DBException(exception.getMessage());
        } finally {
            DB.closeStatement(preparedStatement);
        }
    }

    @Override
    public void update(Seller seller) {
        PreparedStatement preparedStatement = null;

        try{
            preparedStatement = connection.prepareStatement(
                    """
                    UPDATE seller
                    SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ?
                    WHERE Id = ?
                    """,
                    PreparedStatement.RETURN_GENERATED_KEYS
            );

            preparedStatement.setString(1, seller.getName());
            preparedStatement.setString(2, seller.getEmail());
            preparedStatement.setDate(3, new Date(seller.getBirthDate().getTime()));
            preparedStatement.setDouble(4, seller.getBaseSalary());
            preparedStatement.setInt(5, seller.getDepartment().getId());
            preparedStatement.setInt(6, seller.getId());

            preparedStatement.executeUpdate();

        }catch (SQLException exception){
            throw new DBException(exception.getMessage());
        } finally {
            DB.closeStatement(preparedStatement);
        }
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
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try{
            preparedStatement = connection.prepareStatement(
                    """
                    SELECT seller.*, department.Name as DepName
                    FROM seller INNER JOIN department
                    ON seller.DepartmentId = department.Id
                    ORDER BY Name
                    """
            );

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
