package ar.edu.utn.frbb.tup.persistence.impl;

import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.persistence.MateriaDao;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MateriaDaoMemoryImpl implements MateriaDao {

    private static final Map<Integer, Materia> repositorioMateria = new HashMap<>();

    @Override
    public Materia save(Materia materia) {
        Random r = new Random();
        int random;

        do {
            random = r.nextInt();
            if (random < 0) random *= -1;
        } while (repositorioMateria.containsKey(random));

        materia.setMateriaId(random);
        repositorioMateria.put(materia.getMateriaId(), materia);
        return materia;
    }

    @Override
    public void delete(int materiaId) throws MateriaNotFoundException {
        if (!repositorioMateria.containsKey(materiaId))
            throw new MateriaNotFoundException("No se encontro la materia");
        repositorioMateria.remove(materiaId);
    }

    @Override
    public void modificar(Materia materia) throws MateriaNotFoundException {
        if (!repositorioMateria.containsValue(materia))
            throw new MateriaNotFoundException("No se encontro la materia");
        repositorioMateria.replace(materia.getMateriaId(), materia);
    }

    @Override
    public Materia findById(int idMateria) throws MateriaNotFoundException {
        for (Materia m: repositorioMateria.values()) {
            if (idMateria == m.getMateriaId()) {
                return m;
            }
        }
        throw new MateriaNotFoundException("No se encontró la materia con id: " + idMateria);
    }

    public Map<Integer, Materia> getAllMaterias(){
        return repositorioMateria;
    }


}
