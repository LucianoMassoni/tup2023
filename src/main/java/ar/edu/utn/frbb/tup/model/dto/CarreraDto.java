package ar.edu.utn.frbb.tup.model.dto;


public class CarreraDto {
    String nombre;
    int departamento;
    int cantidadCuatrimestres;


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getDepartamento() {
        return departamento;
    }

    public void setDepartamento(int departamento) {
        this.departamento = departamento;
    }

    public int getCantidadCuatrimestres() {
        return cantidadCuatrimestres;
    }

    public void setCantidadCuatrimestres(int cantidadCuatrimestres) {
        this.cantidadCuatrimestres = cantidadCuatrimestres;
    }


}
