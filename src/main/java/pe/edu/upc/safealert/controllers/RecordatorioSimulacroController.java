package pe.edu.upc.safealert.controllers;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.safealert.dtos.RecordatorioSimulacroDTO;
import pe.edu.upc.safealert.entities.RecordatorioSimulacro;
import pe.edu.upc.safealert.servicesinterfaces.IRecordatorioSimulacroService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recordatoriosimulacro")
@Slf4j
public class RecordatorioSimulacroController {

    @Autowired
    private IRecordatorioSimulacroService rsS;

    @GetMapping("/list")
    public List<RecordatorioSimulacroDTO> listarRecordatorioSimulacro() {
        log.info("Solicitud GET para listar recordatorios de simulacro");
        return rsS.list().stream().map(x -> {
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper.map(x, RecordatorioSimulacroDTO.class);
        }).collect(Collectors.toList());
    }

    @PostMapping("/insert")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void insertarRecordatorioSimulacro(@RequestBody RecordatorioSimulacroDTO rsDto) {
        log.info("Solicitud POST para insertar recordatorio de simulacro: {}", rsDto);
        ModelMapper modelMapper = new ModelMapper();
        RecordatorioSimulacro rs = modelMapper.map(rsDto, RecordatorioSimulacro.class);
        rsS.insert(rs);
        log.debug("Recordatorio de simulacro insertado correctamente");
    }

    @GetMapping("/list/{idRecordatorioSimulacro}")
    public RecordatorioSimulacroDTO listarId(@PathVariable("idRecordatorioSimulacro") int idRecordatorioSimulacro) {
        log.info("Solicitud GET para obtener recordatorio con ID: {}", idRecordatorioSimulacro);
        ModelMapper m = new ModelMapper();
        return m.map(rsS.listarId(idRecordatorioSimulacro), RecordatorioSimulacroDTO.class);
    }

    @DeleteMapping("/delete/{idRecordatorioSimulacro}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void eliminarRecordatorioSimulacro(@PathVariable("idRecordatorioSimulacro") int idRecordatorioSimulacro) {
        log.warn("Solicitud DELETE para eliminar recordatorio con ID: {}", idRecordatorioSimulacro);
        rsS.delete(idRecordatorioSimulacro);
    }

    @PutMapping("/put")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void modificarRecordatorioSimulacro(@RequestBody RecordatorioSimulacroDTO fnDTO) {
        log.info("Solicitud PUT para modificar recordatorio de simulacro: {}", fnDTO);
        ModelMapper m = new ModelMapper();
        RecordatorioSimulacro rs = m.map(fnDTO, RecordatorioSimulacro.class);
        rsS.update(rs);
        log.debug("Recordatorio de simulacro modificado correctamente");
    }

}
