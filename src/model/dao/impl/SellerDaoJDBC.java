package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import Db.DataBase;
import Db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao{
    private Connection conn; 

    public SellerDaoJDBC(Connection conn){
        this.conn = conn;
    }

    @Override
    public void insert(Seller seller) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement(
                "INSERT INTO seller " +
                "(Name, Email,BirthDate, BaseSalary, DepartmentId) " +
                "VALUES (?,?,?,?,?)", java.sql.Statement.RETURN_GENERATED_KEYS
            );
            st.setString(1, seller.getName());
            st.setString(2, seller.getEmail());
            st.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
            st.setDouble(4, seller.getBaseSalary());
            st.setInt(5, seller.getDepartment().getId());
            int rowsAffected = st.executeUpdate();
            if(rowsAffected > 0){ 
                rs = st.getGeneratedKeys();
                if(rs.next()){
                    int id = rs.getInt(1);
                    seller.setId(id);   
                }
            }else{
                throw new DbException("Nenhuma Linha Afetada");
            }
        }catch(SQLException e){
            throw new DbException(e.getMessage());
        }finally{
            DataBase.closeResultSet(rs);
            DataBase.closeStatement(st);
        }
        
    }

    @Override
    public void update(Seller seller) {
        PreparedStatement st = null;
        //ResultSet rs = null;
        try{
            st = conn.prepareStatement(
                "UPDATE seller " +
                "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " +
                "WHERE Id = ?"
            );
            st.setString(1, seller.getName());
            st.setString(2, seller.getEmail());
            st.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
            st.setDouble(4, seller.getBaseSalary());
            st.setInt(5, seller.getDepartment().getId());
            st.setInt(6, seller.getId());
            st.executeUpdate(); 
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
                "DELETE FROM seller " +
                "WHERE Id = ?"
            );
            st.setInt(1, id);
            st.executeUpdate();
        } catch(SQLException e){
            throw new DbException(e.getMessage());
        }finally{
            DataBase.closeStatement(st);
        }
    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement st = null;
        ResultSet rs = null;
        try{
            st = conn.prepareStatement(
                "SELECT seller.*, department.Name as DepName " +
                "FROM seller INNER JOIN department " +
                "ON seller.DepartmentId = department.Id "+
                "WHERE seller.Id = ?"
            );
            st.setInt(1, id);
            rs = st.executeQuery();
            if(rs.next()){
                Department dep = instantiateDepartment(rs);
                Seller s = instantiateSeller(rs, dep);
                return s;
            }
            return null;
        }catch(SQLException e){
            throw new DbException(e.getMessage());
        }finally{
            DataBase.closeStatement(st);
            DataBase.closeResultSet(rs);
        }
    }

    private Seller instantiateSeller(ResultSet rs, Department dep) throws SQLException{
        Seller s = new Seller();
            s.setId(rs.getInt("Id"));
            s.setName(rs.getString("Name"));
            s.setEmail(rs.getString("Email"));
            s.setBirthDate(new java.util.Date(rs.getTimestamp("BirthDate").getTime()));
            s.setBaseSalary(rs.getDouble("BaseSalary"));
            s.setDepartment(dep);
        return s;
    }

    private Department instantiateDepartment(ResultSet rs) throws SQLException{
        Department dep = new Department();
        dep.setId(rs.getInt("DepartmentId"));
        dep.setName(rs.getString("DepName"));
        return dep;
    }

    @Override
    public List<Seller> findAll() {
        PreparedStatement st = null;
        ResultSet rs = null;

        try{
            st = conn.prepareStatement(
                "SELECT seller.*, department.Name as DepName " +
                "FROM seller INNER JOIN department " +
                "ON seller.DepartmentId = department.Id "+
                "ORDER BY Name"
            );
            rs = st.executeQuery();
            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();
            while(rs.next()){
                Department dep = map.get(rs.getInt("departmentId"));
                if(dep == null) {
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("departmentId"), dep);
                }
                Seller s = instantiateSeller(rs, dep);
                list.add(s);
            }
            return list;
        }catch(SQLException e){
            throw new DbException(e.getMessage());
        }finally{
            DataBase.closeStatement(st);
            DataBase.closeResultSet(rs);
        }        
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement st = null;
        ResultSet rs = null;

        try{
            st = conn.prepareStatement(
                "SELECT seller.*, department.Name as DepName " +
                "FROM seller INNER JOIN department " +
                "ON seller.DepartmentId = department.Id "+
                "WHERE department.Id = ? " +
                "ORDER BY Name"
            );
            st.setInt(1, department.getId());
            rs = st.executeQuery();
            List<Seller> list = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();
            while(rs.next()){
                Department dep = map.get(rs.getInt("departmentId"));
                if(dep == null) {
                    dep = instantiateDepartment(rs);
                    map.put(rs.getInt("departmentId"), dep);
                }
                Seller s = instantiateSeller(rs, dep);
                list.add(s);
            }
            return list;
        }catch(SQLException e){
            throw new DbException(e.getMessage());
        }finally{
            DataBase.closeStatement(st);
            DataBase.closeResultSet(rs);
        }
    }
    
}
