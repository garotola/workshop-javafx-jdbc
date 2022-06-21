package model.services;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

public class DepartmentService {
    private DepartmentDao dao = DaoFactory.createDepartmentDao();

    public List<Department> findAll(){
        // MOCK - retornar os dados de mentira
        return dao.findAll();
    }
}
