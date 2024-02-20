package ar.edu.utn.frbb.tup.persistence.impl;

import ar.edu.utn.frbb.tup.model.Profesor;
import ar.edu.utn.frbb.tup.persistence.ProfesorDao;
import org.springframework.stereotype.Service;

@Service
public class ProfesorDaoMemoryImpl implements ProfesorDao {

    @Override
    public Profesor get(int id) {
            return new Profesor("Luciano", "Salotto", "Lic. Ciencias Computación");
    }
}
