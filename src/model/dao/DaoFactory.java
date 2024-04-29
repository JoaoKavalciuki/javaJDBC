package model.dao;

import model.dao.impl.DepartmentJDBC;
import model.dao.impl.SellerJDBC;

public class DaoFactory {

    public static SellerDao createSellerDao(){
        return new SellerJDBC();
    }

    public static DepartmentDao createDepartmentDao(){
        return new DepartmentJDBC();
    }
}
