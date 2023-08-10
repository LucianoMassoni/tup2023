package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.CarreraService;
import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.CarreraDto;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.persistence.CarreraDao;
import ar.edu.utn.frbb.tup.persistence.exception.CarreraNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CarreraServiceImpl implements CarreraService {
    @Autowired
    private CarreraDao carreraDao;

    //Todo ver que tirar si los casos son errones, tirar errores etc.
    @Override
    public Carrera crearCarrera(CarreraDto carrera) {
        Carrera c = new Carrera();
        c.setNombre(carrera.getNombre());
        c.setDepartamento(carrera.getDepartamento());
        c.setCantidadCuatrimestres(carrera.getCantidadCuatrimestres());
        carreraDao.save(c);
        return c;
    }
    public Carrera buscarCarrera(int carreraId) throws CarreraNotFoundException {
        if (carreraDao.load(carreraId) != null){
            return carreraDao.load(carreraId);
        }
        return null;
    }

    @Override
    public Carrera actualizarCarrera(int id, CarreraDto carreraDto) throws CarreraNotFoundException {
        Carrera c = carreraDao.load(id);
        if (c != null){
            c.setNombre(carreraDto.getNombre());
            c.setDepartamento(carreraDto.getDepartamento());
            c.setCantidadCuatrimestres(carreraDto.getCantidadCuatrimestres());
            carreraDao.actualizar(id, c);
            return c;
        }
        return null;
    }

    @Override
    public void eliminarCarrera(int id) throws CarreraNotFoundException {
        Carrera c = carreraDao.load(id);
        if (c != null){
            carreraDao.delete(id);
        }
    }

    @Override
    public Map<Integer, Carrera> getAll(){
        return carreraDao.getAll();
    }

    @Override
    public void agregarMateria(MateriaDto materia){
       carreraDao.agregarMateria(materia);
    }

    @Override
    public void eliminarMateria(String materiaNombre){
        carreraDao.eliminarMateria(materiaNombre);
    }

    @Override
    public void actualizarMateria(MateriaDto materia){
        carreraDao.actualizarMateria(materia);
    }
}
