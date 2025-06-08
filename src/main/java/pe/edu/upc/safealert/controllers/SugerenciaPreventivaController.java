package pe.edu.upc.safealert.controllers;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.safealert.dtos.SugerenciaPreventivaDTO;
import pe.edu.upc.safealert.entities.SugerenciaPreventiva;
import pe.edu.upc.safealert.servicesinterfaces.ISugerenciaPreventivaService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/sugerenciapreventiva")
@Slf4j
public class SugerenciaPreventivaController {

    @Autowired
    private ISugerenciaPreventivaService spS;

    @GetMapping("/list")
    public List<SugerenciaPreventivaDTO> listarSugerenciapreventiva() {
        log.info("Solicitud GET para listar todas las sugerencias preventivas");
        return spS.list().stream().map(x -> {
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper.map(x, SugerenciaPreventivaDTO.class);
        }).collect(Collectors.toList());
    }

    @PostMapping("/insert")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void insertarSugerenciaPreventiva(@RequestBody SugerenciaPreventivaDTO spDto) {
        log.info("Solicitud POST para insertar sugerencia preventiva: {}", spDto);
        ModelMapper modelMapper = new ModelMapper();
        SugerenciaPreventiva sp = modelMapper.map(spDto, SugerenciaPreventiva.class);
        spS.insert(sp);
        log.debug("Sugerencia preventiva insertada correctamente");
    }

    @GetMapping("/list/{idSugerenciaPreventiva}")
    public SugerenciaPreventivaDTO listarId(@PathVariable("idSugerenciaPreventiva") int idSugerenciaPreventiva) {
        log.info("Solicitud GET para obtener sugerencia preventiva con ID: {}", idSugerenciaPreventiva);
        ModelMapper m = new ModelMapper();
        return m.map(spS.listarId(idSugerenciaPreventiva), SugerenciaPreventivaDTO.class);
    }

    @DeleteMapping("/delete/{idSugerenciaPreventiva}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void eliminarSugerenciaPreventiva(@PathVariable("idSugerenciaPreventiva") int idSugerenciaPreventiva) {
        log.warn("Solicitud DELETE para eliminar sugerencia preventiva con ID: {}", idSugerenciaPreventiva);
        spS.delete(idSugerenciaPreventiva);
    }

    @PutMapping("/put")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void modificarSugerenciaPreventiva(@RequestBody SugerenciaPreventivaDTO spDTO) {
        log.info("Solicitud PUT para modificar sugerencia preventiva: {}", spDTO);
        ModelMapper m = new ModelMapper();
        SugerenciaPreventiva sp = m.map(spDTO, SugerenciaPreventiva.class);
        spS.update(sp);
        log.debug("Sugerencia preventiva modificada correctamente");
    }
}
