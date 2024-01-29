package ar.edu.utn.frbb.tup.persistence;


import ar.edu.utn.frbb.tup.model.Asignatura;
import ar.edu.utn.frbb.tup.persistence.exception.AsignaturaNotFoundException;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class AsignaturaDaoMemoryImpl implements AsignaturaDao {
    private static final Map<Integer, Asignatura> repositorioMateria = new HashMap<>();


    @Override
    public Asignatura save(Asignatura a) {
        Random random = new Random();
        a.setId(random.nextInt());
        repositorioMateria.put(a.getId(),a);

        return a;
    }

    @Override
    public Asignatura actualizar(Asignatura asignatura) throws AsignaturaNotFoundException {
        for (Asignatura a: repositorioMateria.values()){
            if (Objects.equals(a.getId(), asignatura.getId())){
                repositorioMateria.replace(asignatura.getId(), asignatura);
                return asignatura;
            }
        }
        throw new AsignaturaNotFoundException("No se encontro una asignatura.");
    }


    @Override
    public void delete(int id) throws AsignaturaNotFoundException {
        Iterator<Map.Entry<Integer, Asignatura>> iterator = repositorioMateria.entrySet().iterator();
        boolean deleteRelese = false;

        while (iterator.hasNext()) {
            Map.Entry<Integer, Asignatura> entry = iterator.next();
            if (Objects.equals(entry.getValue().getId(), id)) {
                iterator.remove();
                deleteRelese = true;
            }
        }

        if (!deleteRelese) {
            throw new AsignaturaNotFoundException("No se encontro una asignatura con el id: " + id);
        }
    }

    public List<Asignatura> getAllAsignaturas(){
        return repositorioMateria.values().stream().toList();
    }
}
