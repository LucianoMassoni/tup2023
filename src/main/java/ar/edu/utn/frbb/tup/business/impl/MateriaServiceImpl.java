package ar.edu.utn.frbb.tup.business.impl;

import ar.edu.utn.frbb.tup.business.CarreraService;
import ar.edu.utn.frbb.tup.business.MateriaService;
import ar.edu.utn.frbb.tup.business.ProfesorService;
import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.Materia;
import ar.edu.utn.frbb.tup.model.dto.MateriaDto;
import ar.edu.utn.frbb.tup.model.dto.MateriaInfoDto;
import ar.edu.utn.frbb.tup.persistence.MateriaDao;
import ar.edu.utn.frbb.tup.persistence.exception.CarreraNotFoundException;
import ar.edu.utn.frbb.tup.persistence.exception.MateriaNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class MateriaServiceImpl implements MateriaService {

    @Autowired
    private MateriaDao dao;
    @Autowired
    private ProfesorService profesorService;
    @Autowired
    private CarreraService carreraService;


    private MateriaInfoDto crearMateriaInfoDto(Materia materia){
        MateriaInfoDto materiaInfoDto = new MateriaInfoDto();

        materiaInfoDto.setId(materia.getMateriaId());
        materiaInfoDto.setNombre(materia.getNombre());
        materiaInfoDto.setAnio(materia.getAnio());
        materiaInfoDto.setCuatrimestre(materia.getCuatrimestre());
        return materiaInfoDto;
    }

    /*
     * Para la creación de la materia entiendo que el año y los cuatrimestres son independientes, es decir,
     * el año en el que se cursa con respecto a la carrera y el cuatrimestre en que se cursa con respecto a la carrera.
     */

    @Override
    public Materia crearMateria(MateriaDto materiaDto) throws IllegalArgumentException, CarreraNotFoundException, MateriaNotFoundException {

        validarNombreUnico(materiaDto);
        validarExistenciaDeCarrera(materiaDto);
        validarCoincidenciaCuatrimestresYAnosDeCarrera(materiaDto);
        validarAnioCorrelativasMenorActual(materiaDto);
        validarCuatrimestreCorrelativaMenorActual(materiaDto);

        Materia m = new Materia();
        m.setNombre(materiaDto.getNombre());
        m.setAnio(materiaDto.getAnio());
        m.setCuatrimestre(materiaDto.getCuatrimestre());
        m.setProfesor(profesorService.buscarProfesor(materiaDto.getProfesorId()));
        m.setCarreraIds(materiaDto.getCarreraIds());
        if (!materiaDto.getCorrelativasIds().isEmpty()){
            for (int idCorrelativa:materiaDto.getCorrelativasIds()){
                m.agregarCorrelatividad(crearMateriaInfoDto(dao.findById(idCorrelativa)));
            }
        }

        dao.save(m);
        if (!materiaDto.getCarreraIds().isEmpty()){
            for (int carreraId : materiaDto.getCarreraIds()){
                carreraService.agregarMateriaEnCarrera(carreraId, m.getMateriaId());
            }
        }
        return m;
    }


    @Override
    public void eliminarMateria(int idMateria) throws MateriaNotFoundException, CarreraNotFoundException {
        Materia materia = dao.findById(idMateria);
        dao.delete(idMateria);
        if (!materia.getCarreraIds().isEmpty()){
            for (int carreraId : materia.getCarreraIds()){
                carreraService.eliminarMateriaEnCarrera(carreraId, materia.getMateriaId());
            }
        }
    }

    public void modificarMateria(int materiaId, MateriaDto materiaDto) throws MateriaNotFoundException, CarreraNotFoundException {
        Materia materia = dao.findById(materiaId);

        //verificaciones de la materiaDto
        if (esCorrelativaDeOtraMateria(materia)) {
            validarCambiosNoConflictivosConCorrelativas(materia, materiaDto);
        }
        validarExistenciaDeCarrera(materiaDto);
        validarCoincidenciaCuatrimestresYAnosDeCarrera(materiaDto);
        validarAnioCorrelativasMenorActual(materiaDto);
        validarCuatrimestreCorrelativaMenorActual(materiaDto);

        // le asigna los valores entrantes de la  materiaDto a la materia
        materia.setNombre(materiaDto.getNombre());
        materia.setAnio(materiaDto.getAnio());
        materia.setCuatrimestre(materiaDto.getCuatrimestre());
        materia.setProfesor(profesorService.buscarProfesor(materiaDto.getProfesorId()));
        materia.setCarreraIds(materiaDto.getCarreraIds());
        setCorrelativasModificarMateria(materiaDto, materia);

        //Verificaciones de la materia
        validarNombreUnicoEnModificarMateria(materia);

        //Guardar lo modificado
        dao.modificar(materia);
        carreraService.actualizarMateriaEnCarrera(materia.getCarreraIds(), materiaId);
    }

    //El getMateriaById es para poder acceder a las materias desde el service de otra clase si que accedan al dao de materia
    @Override
    public Materia getMateriaById(int idMateria) throws MateriaNotFoundException {
        return dao.findById(idMateria);
    }

    @Override
    public List<Materia> getMateriaByName(String materiaNombre) throws MateriaNotFoundException {
        List<Materia> lista = new ArrayList<>();
        for(Materia materia : dao.getAllMaterias().values()){
            if (materia.getNombre().toLowerCase().contains(materiaNombre.toLowerCase())){
                lista.add(materia);
            }
        }
        if (lista.isEmpty()) throw new MateriaNotFoundException("La materia " + materiaNombre + " no existe");
        return lista;
    }

    @Override
    public List<Materia> getAllMateriasOrdenadas(String orden){
        return switch (orden) {
            case "nombre_asc" -> ordenarPorNombreAsc();
            case "nombre_desc" -> ordenarPorNombreDesc();
            case "codigo_asc" -> ordenarPorCodigoAsc();
            case "codigo_desc" -> ordenarPorCodigoDesc();
            default -> throw new IllegalArgumentException("Orden no válido");
        };
    }

    /*
        --------------------------- funciones de validacion para crear y modificar materia ----------------------------
    */
    private boolean esCorrelativaDeOtraMateria(Materia materia){
        for (Materia m :dao.getAllMaterias().values()){
            for (MateriaInfoDto correlativa : m.getCorrelatividades()){
                if (correlativa.getId() == materia.getMateriaId())
                    return true;
            }
        }
        return false;
    }

    private List<Materia> obtenerMateriasConEstaCorrelativa(Materia materia) {
        List<Materia> materiasConEstaCorrelativa = new ArrayList<>();
        for (Materia m : dao.getAllMaterias().values()) {
            for (MateriaInfoDto correlativa : m.getCorrelatividades()) {
                if (correlativa.getId() == materia.getMateriaId()) {
                    materiasConEstaCorrelativa.add(m);
                }
            }
        }
        return materiasConEstaCorrelativa;
    }

    private void validarCambiosNoConflictivosConCorrelativas(Materia materia, MateriaDto materiaDto) {
        List<Materia> materiasConEstaCorrelativa = obtenerMateriasConEstaCorrelativa(materia);

        // Ejemplo: Verificar que el nuevo cuatrimestre no sea menor al cuatrimestre de las correlativas
        for (Materia correlativa : materiasConEstaCorrelativa) {
            if (materiaDto.getCuatrimestre() >= correlativa.getCuatrimestre()) {
                throw new IllegalArgumentException("Esta materia es correlativa de otra la cual se cursa en un cuatrimestre igual o anterior");
            }
        }

        for (Materia correlativa : materiasConEstaCorrelativa) {
            if (materiaDto.getAnio() > correlativa.getAnio()) {
                throw new IllegalArgumentException("Esta materia es correlativa de otra la cual se cursa en un anio anterior");
            }
        }
    }

    private void validarNombreUnicoEnModificarMateria(Materia materia){
        for (Materia m:dao.getAllMaterias().values()){
            if (Objects.equals(m.getNombre().toLowerCase(), materia.getNombre().toLowerCase()) && materia.getMateriaId() != m.getMateriaId()){
                throw new IllegalArgumentException("Ya existe una materia con el nombre " + materia.getNombre());
            }
        }
    }

    private void setCorrelativasModificarMateria(MateriaDto materiaDto, Materia materia) throws MateriaNotFoundException {
        List<MateriaInfoDto> nuevasCorrelativas = new ArrayList<>();

        for (int correlativaId : materiaDto.getCorrelativasIds()) {
            MateriaInfoDto nuevaCorrelativa = crearMateriaInfoDto(dao.findById(correlativaId));
            nuevasCorrelativas.add(nuevaCorrelativa);
        }

        materia.getCorrelatividades().clear();
        materia.getCorrelatividades().addAll(nuevasCorrelativas);
    }

    private void validarNombreUnico(MateriaDto materiaDto){
        for (Materia m:dao.getAllMaterias().values()){
            if (Objects.equals(m.getNombre().toLowerCase(), materiaDto.getNombre().toLowerCase())){
                throw new IllegalArgumentException("Ya existe una materia con el nombre " + materiaDto.getNombre());
            }
        }
    }

    private void validarExistenciaDeCarrera(MateriaDto materiaDto) throws CarreraNotFoundException {
        if (!materiaDto.getCarreraIds().isEmpty()){
            for (int carreraId : materiaDto.getCarreraIds()){
                carreraService.buscarCarrera(carreraId);
            }
        }
    }

    private void validarCoincidenciaCuatrimestresYAnosDeCarrera(MateriaDto materiaDto) throws CarreraNotFoundException {
        if (!materiaDto.getCarreraIds().isEmpty()){
            for (int carreraId : materiaDto.getCarreraIds()){
                Carrera carrera = carreraService.buscarCarrera(carreraId);

                int annosDeCarrera = carrera.getCantidadCuatrimestres()/2;
                if (materiaDto.getAnio() < 1 || materiaDto.getAnio() > annosDeCarrera){
                    throw new IllegalArgumentException("La carrera " + carrera.getNombre() +" tiene " + annosDeCarrera + " años.");
                }
            }
        }
    }

    private void validarAnioCorrelativasMenorActual(MateriaDto materiaDto) throws MateriaNotFoundException {
        //Controla si el año de la correlativa es menor a la materia actual
        if (!materiaDto.getCorrelativasIds().isEmpty()) {
            for (int correlativaId : materiaDto.getCorrelativasIds()) {
                Materia correlativa = dao.findById(correlativaId);
                if (correlativa != null && correlativa.getAnio() > materiaDto.getAnio()) {
                    throw new IllegalArgumentException("La materia actual tiene un año menor al de las correlativas");
                }
            }
        }
    }

    private void validarCuatrimestreCorrelativaMenorActual(MateriaDto materiaDto) throws MateriaNotFoundException {
        //chequea que las materias correlativas tengan un cuatrimestre menor a la materia actual
        if (!materiaDto.getCorrelativasIds().isEmpty()) {
            for (int correlativaId : materiaDto.getCorrelativasIds()) {
                Materia correlativa = dao.findById(correlativaId);
                if (correlativa != null && correlativa.getCuatrimestre() >= materiaDto.getCuatrimestre()) {
                    throw new IllegalArgumentException("La materia actual tiene un cuatrimestre menor o igual al de las correlativas");
                }
            }
        }
    }

    // ----------------------------------------------------------------------------------------------------------------
    /*
        ------------------------------------------ funciones de ordenamiento ------------------------------------------
     */

    private List<Materia> ordenarPorNombreAsc() {
        List<Materia> materias = new ArrayList<>(dao.getAllMaterias().values().stream().toList());
        materias.sort((m1, m2) -> m1.getNombre().toLowerCase().compareTo(m2.getNombre().toLowerCase()));
        return materias;
    }

    private List<Materia> ordenarPorNombreDesc() {
        List<Materia> materias = new ArrayList<>(dao.getAllMaterias().values().stream().toList());
        materias.sort((m1, m2) -> m2.getNombre().toLowerCase().compareTo(m1.getNombre().toLowerCase()));
        return materias;
    }

    private List<Materia> ordenarPorCodigoAsc() {
        List<Materia> materias = new ArrayList<>(dao.getAllMaterias().values().stream().toList());
        materias.sort((m1, m2) -> Integer.compare(m1.getMateriaId(), m2.getMateriaId()));
        return materias;
    }

    private List<Materia> ordenarPorCodigoDesc() {
        List<Materia> materias = new ArrayList<>(dao.getAllMaterias().values().stream().toList());
        materias.sort((m1, m2) -> Integer.compare(m2.getMateriaId(), m1.getMateriaId()));
        return materias;
    }
    // ---------------------------------------------------------------------------------------------------------------
}
