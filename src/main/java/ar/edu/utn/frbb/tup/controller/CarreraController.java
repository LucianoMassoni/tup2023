package ar.edu.utn.frbb.tup.controller;

import ar.edu.utn.frbb.tup.business.CarreraService;
import ar.edu.utn.frbb.tup.model.Carrera;
import ar.edu.utn.frbb.tup.model.dto.CarreraDto;
import ar.edu.utn.frbb.tup.persistence.exception.CarreraNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("carrera")
public class CarreraController {
    @Autowired
    private CarreraService carreraService;

    @PostMapping()
    public Carrera crearCarrera(@RequestBody CarreraDto carreraDto){
        return carreraService.crearCarrera(carreraDto);
    }

    @GetMapping("/getAll")
    public Map<Integer, Carrera> getAll(){
        return carreraService.getAll();
    }

    @GetMapping("/{idCarrera}")
    public Carrera buscarCarreraPorId(@PathVariable int idCarrera) throws CarreraNotFoundException {
        return carreraService.buscarCarrera(idCarrera);
    }

    @PutMapping("/{idCarrera}")
    public Carrera actualizarCarrera(@PathVariable int idCarrera, @RequestBody CarreraDto carreraDto) throws CarreraNotFoundException {
        return carreraService.actualizarCarrera(idCarrera, carreraDto);
    }

    @DeleteMapping("/{idCarrera}")
    public void borrarCarrera(@PathVariable int idCarrera) throws CarreraNotFoundException {
        carreraService.eliminarCarrera(idCarrera);
    }
}
