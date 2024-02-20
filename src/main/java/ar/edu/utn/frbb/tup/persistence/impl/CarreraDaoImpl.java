package ar.edu.utn.frbb.tup.persistence.impl;

import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.persistence.CarreraDao;
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
        Random r = new Random();
        int random;

        do {
            random = r.nextInt();
            if (random < 0) random *= -1;
        } while (repositorioCarrera.containsKey(random));

        carrera.setCarreraId(random);
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
        throw new CarreraNotFoundException("No se encontró una carrera con el id: " + carreraId);
    }

    @Override
    public void actualizar(int id, Carrera carrera){
        repositorioCarrera.replace(carrera.getCarreraId(), carrera);
    }

    @Override
    public void delete(int carreraId) throws CarreraNotFoundException {
        if (!repositorioCarrera.containsKey(carreraId))
            throw new CarreraNotFoundException("No se encontró una carrera con el id: " + carreraId);
        repositorioCarrera.remove(carreraId);
    }

    @Override
    public Map<Integer, Carrera> getAll(){
        return repositorioCarrera;
    }

}
