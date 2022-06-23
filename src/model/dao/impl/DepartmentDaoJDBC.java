package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import Db.DataBase;
import Db.DataBaseIntegrityException;
import Db.DbException;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentDaoJDBC implements DepartmentDao{

    Connection conn = null;

    public DepartmentDaoJDBC(Connection conn){
        this.conn = conn;
    }

    @Override
    public void insert(Department department) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try{
            st = conn.prepareStatement(
                "INSERT INTO department (Name) VALUES (?)", Statement.RETURN_GENERATED_KEYS
            );
            st.setString(1,department.getName());
            int rowsAffected = st.executeUpdate();
            if(rowsAffected > 1){
                rs = st.getGeneratedKeys();
                if(rs.next()){
                    //System.out.println(rs);
                    int id = rs.getInt(1);
                    department.setId(id);
                }
            }
        }catch(SQLException e){
            throw new DbException(e.getMessage());
        }finally{
            DataBase.closeResultSet(rs);
            DataBase.closeStatement(st);
        }
        
    }

    @Override
    public void update(Department department) {
        PreparedStatement st = null;
        //ResultSet rs = null;
        
        try{
            st = conn.prepareStatement(
                "UPDATE department SET Name = ? WHERE Id = ?"
            );
            st.setString(1, department.getName());
            st.setInt(2, department.getId());
            int rowsAffected = st.executeUpdate();
            if(rowsAffected == 0) throw new DbException("Id não encontrado.");
        }catch(SQLException e){
            throw new DbException(e.getMessage());
        }finally{
            //DataBase.closeResultSet(rs);
            DataBase.closeStatement(st);
        }
        
    }

    @Override
    public void deleteById(Integer id) {
        PreparedStatement st = null;
        try{
            st = conn.prepareStatement(
                "DELETE FROM department WHERE Id = ?"
            );
            st.setInt(1, id);
            int rowsAffected = st.executeUpdate();
            if(rowsAffected == 0) throw new DbException("Id não Encontrado.");
        }catch(SQLException e){
            throw new DataBaseIntegrityException(e.getMessage());
        }finally{
            DataBase.closeStatement(st);
        }
    }

    @Override
    public Department findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement(
                "SELECT * FROM department WHERE Id = ?"
            );
            st.setInt(1, id);
            rs = st.executeQuery();
            if(rs.next()){
                return new Department(rs.getInt("Id"), rs.getString("Name"));
            }
            return null;
        }catch(SQLException e){
            throw new DbException(e.getMessage());
        }
    }

    @Override
    public List<Department> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement(
                "SELECT * FROM department ORDER BY NAME"
            );
            rs = st.executeQuery();
            List<Department> list = new ArrayList<>();
            while(rs.next()){
                list.add(new Department(rs.getInt("Id"), rs.getString("Name")));
            }
            return list;
        }catch(SQLException e){
            throw new DbException(e.getMessage());
        }
    }
    
}
