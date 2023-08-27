package ar.edu.utn.frbb.tup.model.dto;

import ar.edu.utn.frbb.tup.model.EstadoAsignatura;

public class AsignaturaDto {
    private int materiaId;
    private EstadoAsignatura estadoAsignatura;
    private Integer nota;

    public int getMateriaId() {
        return materiaId;
    }

    public void setMateriaId(int materiaId) {
        this.materiaId = materiaId;
    }

    public EstadoAsignatura getEstadoAsignatura() {
        return estadoAsignatura;
    }

    public void setEstadoAsignatura(EstadoAsignatura estadoAsignatura) {
        this.estadoAsignatura = estadoAsignatura;
    }

    public Integer getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }
}
