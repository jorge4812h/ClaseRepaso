package dh.backend.model;

import java.time.LocalDate;

public class Paciente {

    private Integer id;
    private String apellido;
    private String nombre;
    private String DNI;
    private LocalDate fechaIngreso;
    private Domicilio domicilio;

    public Paciente(Integer id, String apellido, String nombre, String DNI, LocalDate fechaIngreso, Domicilio domicilio) {
        this.id = id;
        this.apellido = apellido;
        this.nombre = nombre;
        this.DNI = DNI;
        this.fechaIngreso = fechaIngreso;
        this.domicilio = domicilio;
    }

    public Paciente(String apellido, String nombre, String DNI, LocalDate fechaIngreso, Domicilio domicilio) {
        this.apellido = apellido;
        this.nombre = nombre;
        this.DNI = DNI;
        this.fechaIngreso = fechaIngreso;
        this.domicilio = domicilio;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDNI() {
        return DNI;
    }

    public void setDNI(String DNI) {
        this.DNI = DNI;
    }

    public LocalDate getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(LocalDate fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Domicilio getDomicilio() {
        return domicilio;
    }

    public void setDomicilio(Domicilio domicilio) {
        this.domicilio = domicilio;
    }
}
