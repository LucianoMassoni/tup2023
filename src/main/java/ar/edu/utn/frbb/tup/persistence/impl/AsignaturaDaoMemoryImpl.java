package ar.edu.utn.frbb.tup.persistence.impl;


import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.persistence.AsignaturaDao;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AsignaturaDaoMemoryImpl implements AsignaturaDao {
    private static final Map<Integer, Asignatura> repositorioMateria = new HashMap<>();

    @Override
    public Asignatura save(Asignatura a) {
        Random r = new Random();
        int random;
        do {
            random = r.nextInt();
            if (random < 0) random *= -1;
        }while (repositorioMateria.containsKey(random));
        a.setId(random);
        repositorioMateria.put(a.getId(),a);

        return a;
    }

    @Override
    public void update(Asignatura asignatura) throws AsignaturaNotFoundException {
        if (!repositorioMateria.containsKey(asignatura.getId()))
            throw new AsignaturaNotFoundException("No se encontró una asignatura.");
        repositorioMateria.replace(asignatura.getId(), asignatura);
    }

    @Override
    public Asignatura findById(int id) throws AsignaturaNotFoundException {
        if (!repositorioMateria.containsKey(id))
            throw new AsignaturaNotFoundException("No se encontró una asignatura con id: " + id);
        return repositorioMateria.get(id);
    }

    @Override
    public void delete(int id) throws AsignaturaNotFoundException {
        if (!repositorioMateria.containsKey(id))
            throw new AsignaturaNotFoundException("No se encontró una asignatura con id: " + id);
        repositorioMateria.remove(id);
    }

    @Override
    public Map<Integer, Asignatura> getAllAsignaturas(){
        return repositorioMateria;
    }
}