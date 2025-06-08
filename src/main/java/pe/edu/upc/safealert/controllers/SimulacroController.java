package pe.edu.upc.safealert.controllers;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.safealert.dtos.SimulacroDTO;
import pe.edu.upc.safealert.entities.Simulacro;
import pe.edu.upc.safealert.servicesinterfaces.ISimulacroService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/simulacro")
@Slf4j
public class SimulacroController {

    @Autowired
    private ISimulacroService sS;

    @GetMapping("/list")
    public List<SimulacroDTO> listarSimulacros() {
        log.info("Solicitud GET para listar todos los simulacros");
        return sS.list().stream().map(x -> {
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper.map(x, SimulacroDTO.class);
        }).collect(Collectors.toList());
    }

    @PostMapping("/insert")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void insertar(@RequestBody SimulacroDTO sDTO) {
        log.info("Solicitud POST para insertar simulacro: {}", sDTO);
        ModelMapper modelMapper = new ModelMapper();
        Simulacro s = modelMapper.map(sDTO, Simulacro.class);
        sS.insert(s);
        log.debug("Simulacro insertado correctamente");
    }

    @GetMapping("/list/{idSimulacro}")
    public SimulacroDTO listarId(@PathVariable("idSimulacro") int idSimulacro) {
        log.info("Solicitud GET para obtener simulacro con ID: {}", idSimulacro);
        ModelMapper m = new ModelMapper();
        return m.map(sS.listarId(idSimulacro), SimulacroDTO.class);
    }

    @DeleteMapping("/delete/{idSimulacro}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void eliminar(@PathVariable("idSimulacro") int idSimulacro) {
        log.warn("Solicitud DELETE para eliminar simulacro con ID: {}", idSimulacro);
        sS.delete(idSimulacro);
    }

    @PutMapping("/put")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void modificar(@RequestBody SimulacroDTO sDTO) {
        log.info("Solicitud PUT para modificar simulacro: {}", sDTO);
        ModelMapper m = new ModelMapper();
        Simulacro s = m.map(sDTO, Simulacro.class);
        sS.update(s);
        log.debug("Simulacro modificado correctamente");
    }
}
