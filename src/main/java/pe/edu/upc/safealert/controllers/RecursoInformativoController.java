package pe.edu.upc.safealert.controllers;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.safealert.dtos.CantidadRecursoxUsuarioDTO;
import pe.edu.upc.safealert.dtos.RecursoInformativoDTO;
import pe.edu.upc.safealert.entities.RecursoInformativo;
import pe.edu.upc.safealert.servicesinterfaces.IRecursoInformativoService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/recursoinformativo")
@Slf4j
public class RecursoInformativoController {

    @Autowired
    private IRecursoInformativoService riS;

    @GetMapping("/list")
    public List<RecursoInformativoDTO> listarRecursosInformativos() {
        log.info("Solicitud GET para listar todos los recursos informativos");
        return riS.list().stream().map(x -> {
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper.map(x, RecursoInformativoDTO.class);
        }).collect(Collectors.toList());
    }

    @PostMapping("/insert")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void insertar(@RequestBody RecursoInformativoDTO riDTO) {
        log.info("Solicitud POST para insertar recurso informativo: {}", riDTO);
        ModelMapper modelMapper = new ModelMapper();
        RecursoInformativo ri = modelMapper.map(riDTO, RecursoInformativo.class);
        riS.insert(ri);
        log.debug("Recurso informativo insertado correctamente");
    }

    @GetMapping("/list/{idRecursoInformativo}")
    public RecursoInformativoDTO listarPorId(@PathVariable("idRecursoInformativo") int idRecursoInformativo) {
        log.info("Solicitud GET para obtener recurso informativo con ID: {}", idRecursoInformativo);
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(riS.listarId(idRecursoInformativo), RecursoInformativoDTO.class);
    }

    @DeleteMapping("/delete/{idRecursoInformativo}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void eliminarPorId(@PathVariable("idRecursoInformativo") int idRecursoInformativo) {
        log.warn("Solicitud DELETE para eliminar recurso informativo con ID: {}", idRecursoInformativo);
        riS.delete(idRecursoInformativo);
    }

    @PutMapping("/put")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void modificar(@RequestBody RecursoInformativoDTO riDTO) {
        log.info("Solicitud PUT para modificar recurso informativo: {}", riDTO);
        ModelMapper modelMapper = new ModelMapper();
        RecursoInformativo ri = modelMapper.map(riDTO, RecursoInformativo.class);
        riS.update(ri);
        log.debug("Recurso informativo modificado correctamente");
    }

    @GetMapping("/list/CantidadRecursosPorUsuario")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<CantidadRecursoxUsuarioDTO> cantidadRecursos() {
        log.info("Solicitud GET para obtener cantidad de recursos informativos por usuario");
        List<CantidadRecursoxUsuarioDTO> dtoLista = new ArrayList<>();
        List<String[]> filaLista = riS.cantidadRecursosPorUsuario();
        for (String[] columna : filaLista) {
            CantidadRecursoxUsuarioDTO dto = new CantidadRecursoxUsuarioDTO();
            dto.setUsername(columna[0]);
            dto.setCantidad(Integer.parseInt(columna[1]));
            dtoLista.add(dto);
        }
        return dtoLista;
    }
}
