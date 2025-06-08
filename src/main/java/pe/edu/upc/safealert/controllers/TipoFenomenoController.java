package pe.edu.upc.safealert.controllers;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.safealert.dtos.TipoFenomenoDTO;
import pe.edu.upc.safealert.entities.TipoFenomeno;
import pe.edu.upc.safealert.servicesimplements.TipoFenomenoServiceImplement;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tipofenomeno")
@Slf4j
public class TipoFenomenoController {

    @Autowired
    private TipoFenomenoServiceImplement tS;

    @GetMapping("/list")
    public List<TipoFenomenoDTO> listartipofenomeno() {
        log.info("Solicitud GET para listar todos los tipos de fenómeno");
        return tS.listar().stream().map(x -> {
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper.map(x, TipoFenomenoDTO.class);
        }).collect(Collectors.toList());
    }

    @PostMapping("/insert")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void insertar(@RequestBody TipoFenomenoDTO tfDTO) {
        log.info("Solicitud POST para insertar tipo de fenómeno: {}", tfDTO);
        ModelMapper modelMapper = new ModelMapper();
        TipoFenomeno tf = modelMapper.map(tfDTO, TipoFenomeno.class);
        tS.insert(tf);
        log.debug("Tipo de fenómeno insertado correctamente");
    }

    @GetMapping("/list/{idTipoFenomeno}")
    public TipoFenomenoDTO listarId(@PathVariable("idTipoFenomeno") int idTipoFenomeno) {
        log.info("Solicitud GET para obtener tipo de fenómeno con ID: {}", idTipoFenomeno);
        ModelMapper m = new ModelMapper();
        return m.map(tS.listarId(idTipoFenomeno), TipoFenomenoDTO.class);
    }

    @DeleteMapping("/delete/{idTipoFenomeno}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void eliminar(@PathVariable("idTipoFenomeno") int idTipoFenomeno) {
        log.warn("Solicitud DELETE para eliminar tipo de fenómeno con ID: {}", idTipoFenomeno);
        tS.delete(idTipoFenomeno);
    }

    @PutMapping("/put")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void modificar(@RequestBody TipoFenomenoDTO tfDTO) {
        log.info("Solicitud PUT para modificar tipo de fenómeno: {}", tfDTO);
        ModelMapper m = new ModelMapper();
        TipoFenomeno tf = m.map(tfDTO, TipoFenomeno.class);
        tS.update(tf);
        log.debug("Tipo de fenómeno modificado correctamente");
    }
}
