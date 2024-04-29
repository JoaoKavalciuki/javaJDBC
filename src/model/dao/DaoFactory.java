package model.dao;

import db.DB;
import model.dao.impl.DepartmentJDBC;
import model.dao.impl.SellerJDBC;

public class DaoFactory {

    public static SellerDao createSellerDao(){
        return new SellerJDBC(DB.getConnection());
    }

    public static DepartmentDao createDepartmentDao(){
        return new DepartmentJDBC();
    }
}
