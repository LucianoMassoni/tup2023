package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.CarreraDto;
import ar.edu.utn.frbb.tup.persistence.exception.CarreraNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

@Service
public class CarreraDaoImpl implements CarreraDao {

    private static final Map<Integer, Carrera> repositorioCarrera = new HashMap<>();
    @Override
    public Carrera save(Carrera carrera){
        Random random = new Random();
        carrera.setCarreraId(random.nextInt());
        repositorioCarrera.put(carrera.getCarreraId(), carrera);
        return carrera;
    }
    @Override
    public Carrera load(int carreraId) throws CarreraNotFoundException {
        for (Carrera c:repositorioCarrera.values()){
            if (Objects.equals(c.getCarreraId(), carreraId)){
                return c;
            }
        }
        //todo tirar una exception? || Dejar los Daos simples para el testeo
        throw new CarreraNotFoundException("No se encontró una carrera con el id: " + carreraId);
    }
    @Override
    public void actualizar(int id, Carrera carrera){
        repositorioCarrera.replace(carrera.getCarreraId(), carrera);
    }
    @Override
    public void delete(int carreraId){
        repositorioCarrera.remove(carreraId);
    }

    @Override
    public Map<Integer, Carrera> getAll(){
        return repositorioCarrera;
    }

    //todo
    // Cuando agrega la materia a la lista de materias ahora la lista tiene una materia que tiene una carrera con una lista con materias...
    // no se si la lista tendria que ser de ids de materias. O un map con {nombre : materiaNombre, id: materiaId}
    @Override
    public void agregarMateria(Materia materia){
        for (Carrera c:repositorioCarrera.values()){
            if (Objects.equals(c.getCarreraId(),materia.getCarrera().getCarreraId())){
                c.agregarMateria(materia);
            }
        }
    }

    @Override
    public void eliminarMateria(int materiaId){
        for (Carrera c:repositorioCarrera.values()){
            for (Materia m:c.getMateriasList()){
                if (Objects.equals(m.getMateriaId(),materiaId)){
                    c.getMateriasList().remove(m);
                }
            }
        }
    }

    @Override
    public void actualizarMateria(Materia materia){
        for (Carrera c:repositorioCarrera.values()){
            for (Materia m: c.getMateriasList()){
                if (Objects.equals(m.getMateriaId(),materia.getMateriaId())){
                    c.getMateriasList().set(c.getMateriasList().indexOf(m), materia);
                }
            }
        }
    }

}
