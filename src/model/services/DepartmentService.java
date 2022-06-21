package model.services;
import java.util.ArrayList;
import java.util.List;

import model.entities.Department;

public class DepartmentService {
    public List<Department> findAll(){
        // MOCK - retornar os dados de mentira
        List<Department> list = new ArrayList<>();
        list.add(new Department(1, "Livros"));
        list.add(new Department(2, "Eletronicos"));
        list.add(new Department(3, "Computadores"));
        return list;
    }
}
