package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.CarreraService;
import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.dto.CarreraDto;
import ar.edu.utn.frbb.tup.persistence.CarreraDao;
import ar.edu.utn.frbb.tup.persistence.exception.CarreraNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class CarreraServiceImpl implements CarreraService {
    @Autowired
    private CarreraDao carreraDao;

    @Override
    public Carrera crearCarrera(CarreraDto carreraDto) {
        validarNombreUnico(carreraDto);
        validarDepartamento(carreraDto);
        validarCuatrimestres(carreraDto);

        Carrera c = new Carrera();
        c.setNombre(carreraDto.getNombre());
        c.setDepartamento(carreraDto.getDepartamento());
        c.setCantidadCuatrimestres(carreraDto.getCantidadCuatrimestres());
        carreraDao.save(c);
        return c;
    }

    @Override
    public Carrera buscarCarrera(int carreraId) throws CarreraNotFoundException {
        return carreraDao.load(carreraId);
    }

    @Override
    public void actualizarCarrera(int id, CarreraDto carreraDto) throws CarreraNotFoundException {
        Carrera c = carreraDao.load(id);

        validarDepartamento(carreraDto);
        validarCuatrimestres(carreraDto);

        c.setNombre(carreraDto.getNombre());
        c.setDepartamento(carreraDto.getDepartamento());
        c.setCantidadCuatrimestres(carreraDto.getCantidadCuatrimestres());

        validarNombreUnicoEnCarreras(c);

        carreraDao.actualizar(id, c);
    }

    @Override
    public void eliminarCarrera(int id) throws CarreraNotFoundException {
        carreraDao.delete(id);
    }

    @Override
    public Map<Integer, Carrera> getAll(){
        return carreraDao.getAll();
    }

    /*
            Validaciones para crear y modificar carrera
     */

    private void validarNombreUnico(CarreraDto carreraDto) throws IllegalArgumentException {
        for (Carrera carrera: carreraDao.getAll().values()){
            if (carreraDto.getNombre().equalsIgnoreCase(carrera.getNombre())){
                throw new IllegalArgumentException("Ya existe una carrera con el nombre " + carrera.getNombre());
            }
        }
    }

    private void validarNombreUnicoEnCarreras(Carrera carrera){
        for (Carrera c : carreraDao.getAll().values()){
            if (Objects.equals(c.getNombre().toLowerCase(), carrera.getNombre().toLowerCase()) && (c.getCarreraId() != carrera.getCarreraId())){
                throw new IllegalArgumentException("Ya existe una carrera con el nombre " + carrera.getNombre());
            }

        }
    }

    private void validarDepartamento(CarreraDto carreraDto){
        if (carreraDto.getDepartamento() < 0)
            throw new IllegalArgumentException("El departanto no puede tener un id negativo");
    }

    private void validarCuatrimestres(CarreraDto carreraDto){
        if (carreraDto.getCantidadCuatrimestres() < 1)
            throw new IllegalArgumentException("El cuatrimestre no puede ser un numero negativo o 0");
    }

    // --------------------------------- Funciones de las materias en al carrera ---------------------------------
    @Override
    public void agregarMateriaEnCarrera(int carreraId, int materiaId) throws CarreraNotFoundException {
        Carrera carrera = carreraDao.load(carreraId);

        validarExistenciaMateriaParaAgregar(carrera, materiaId);

        carrera.agregarMateria(materiaId);
        carreraDao.actualizar(carreraId, carrera);
    }

    @Override
    public void eliminarMateriaEnCarrera(int carreraId, int materiaId) throws CarreraNotFoundException{
        Carrera carrera = carreraDao.load(carreraId);

        validarExistenciaMateriaParaEliminar(carrera, materiaId);

        carrera.getMateriasIds().remove(Integer.valueOf(materiaId));
        carreraDao.actualizar(carreraId, carrera);
    }

    @Override
    public void actualizarMateriaEnCarrera(List<Integer> listaCarreraIds, int materiaId) {
        List<Carrera> carreras = new ArrayList<>(carreraDao.getAll().values());

        if (listaCarreraIds.isEmpty()){
            for (Carrera carrera : carreras){
                if (!carrera.getMateriasIds().isEmpty()){
                    carrera.getMateriasIds().clear();
                    carreraDao.actualizar(carrera.getCarreraId(), carrera);
                }
            }
        } else {
            for (Carrera carrera : carreras){
                if (listaCarreraIds.contains(carrera.getCarreraId()) && !carrera.getMateriasIds().contains(materiaId)){
                    carrera.getMateriasIds().add(materiaId);
                    carreraDao.actualizar(carrera.getCarreraId(), carrera);
                } else if (!listaCarreraIds.contains(carrera.getCarreraId()) && carrera.getMateriasIds().contains(materiaId)) {
                    carrera.getMateriasIds().remove(Integer.valueOf(materiaId));
                    carreraDao.actualizar(carrera.getCarreraId(), carrera);
                }
            }
        }
    }
    /*
            Validaciones de las funciones de materias en carrera
     */
    private void validarExistenciaMateriaParaAgregar(Carrera carrera, int materiaId) {
        if (carrera.getMateriasIds().contains(materiaId))
            throw new IllegalArgumentException("La carrera " + carrera.getNombre() + " ya tiene la materia con id: " + materiaId);
    }

    private void validarExistenciaMateriaParaEliminar(Carrera carrera, int materiaId) {
        if (!(carrera.getMateriasIds().contains(materiaId)))
            throw new IllegalArgumentException("La carrera " + carrera.getNombre() + " no tiene una materia con id: " + materiaId);
    }
}
