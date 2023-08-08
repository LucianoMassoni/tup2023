package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MateriaDaoMemoryImpl implements MateriaDao {

    private static final Map<Integer, Materia> repositorioMateria = new HashMap<>();
    @Override
    public Materia save(Materia materia) {
        Random random = new Random();
        materia.setMateriaId(random.nextInt());
        repositorioMateria.put(materia.getMateriaId(), materia);
        return materia;
    }

    @Override
    public void delete(Materia m) {
        repositorioMateria.remove(m.getMateriaId());
    }

    @Override
    public void modificar(Materia materia){
        repositorioMateria.replace(materia.getMateriaId(), materia);
    }

    @Override
    public Materia findById(int idMateria) throws MateriaNotFoundException {
        for (Materia m:
             repositorioMateria.values()) {
            if (idMateria == m.getMateriaId()) {
                return m;
            }
        }
        throw new MateriaNotFoundException("No se encontró la materia con id " + idMateria);
    }

    public Map<Integer, Materia> getAllMaterias(){
        return repositorioMateria;
    }

    public Materia findByName(String materiaNomrbe) throws MateriaNotFoundException {
        for (Materia m: repositorioMateria.values()){
            if (m.getNombre().contains(materiaNomrbe)){
                return m;
            }
        }
        throw new MateriaNotFoundException("La materia " + materiaNomrbe +" no se existe");
    }

}
