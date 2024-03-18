package ar.edu.utn.frbb.tup.model.dto;

import ar.edu.utn.frbb.tup.model.EstadoAsignatura;

public class AsignaturaDto {
    private int materiaId;
    private EstadoAsignatura estadoAsignatura;
    private int nota;

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

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }
}
