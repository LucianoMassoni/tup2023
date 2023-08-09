package ar.edu.utn.frbb.tup.persistence;

import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.dto.CarreraDto;
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
    public Carrera load(int carreraId){
        for (Carrera c:repositorioCarrera.values()){
            if (Objects.equals(c.getCarreraId(), carreraId)){
                return c;
            }
        }
        //todo tirar una exception? || Dejar los Daos simples para el testeo
        //throw new CarreraNotFoundException("No se encontró una carrera con el id: " + carreraId);
        return null;
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
}
