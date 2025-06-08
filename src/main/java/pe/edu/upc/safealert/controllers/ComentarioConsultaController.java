package pe.edu.upc.safealert.controllers;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.safealert.dtos.ComentarioConsultaDTO;
import pe.edu.upc.safealert.dtos.ContarComentarioDTO;
import pe.edu.upc.safealert.entities.ComentarioConsulta;
import pe.edu.upc.safealert.servicesinterfaces.IComentarioConsultaService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/comentario")
@Slf4j // Lombok
public class ComentarioConsultaController {

    @Autowired
    private IComentarioConsultaService coS;

    @GetMapping("/list")
    public List<ComentarioConsultaDTO> listarComentario() {
        log.info("Solicitud GET para listar todos los comentarios");
        return coS.list().stream().map(x -> {
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper.map(x, ComentarioConsultaDTO.class);
        }).collect(Collectors.toList());
    }

    @PostMapping("/insert")
    public void insertar(@RequestBody ComentarioConsultaDTO coDto) {
        log.info("Solicitud POST para insertar un nuevo comentario: {}", coDto);
        ModelMapper modelMapper = new ModelMapper();
        ComentarioConsulta co = modelMapper.map(coDto, ComentarioConsulta.class);
        coS.insert(co);
        log.debug("Comentario insertado con éxito");
    }

    // GET: obtener comentario por ID
    @GetMapping("/list/{idComentario}")
    public ComentarioConsultaDTO listarId(@PathVariable("idComentario") int idComentario) {
        log.info("Solicitud GET para obtener comentario con ID: {}", idComentario);
        ModelMapper m = new ModelMapper();
        ComentarioConsultaDTO coDTO = m.map(coS.listarId(idComentario), ComentarioConsultaDTO.class);
        return coDTO;
    }

    @DeleteMapping("/delete/{idComentario}")
    public void eliminar(@PathVariable("idComentario") int idComentario) {
        log.warn("Solicitud DELETE para eliminar comentario con ID: {}", idComentario);
        coS.delete(idComentario);
    }

    @PutMapping("/put")
    public void modificar(@RequestBody ComentarioConsultaDTO coDTO) {
        log.info("Solicitud PUT para modificar comentario: {}", coDTO);
        ModelMapper m = new ModelMapper();
        ComentarioConsulta co = m.map(coDTO, ComentarioConsulta.class);
        coS.update(co);
        log.debug("Comentario modificado con éxito");
    }

    // GET: cantidad de comentarios por usuario
    @GetMapping("/list/CantidadesComentariosPorUsuario")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<ContarComentarioDTO> cantidadPorComentario() {
        log.info("Solicitud GET para obtener cantidad de comentarios por usuario");
        List<ContarComentarioDTO> dtoLista = new ArrayList<>();
        List<String[]> filaLista = coS.contarcomentariousuario();
        for (String[] columna : filaLista) {
            ContarComentarioDTO dto = new ContarComentarioDTO();
            dto.setNombre(columna[0]);
            dto.setContarcomentario(Integer.parseInt(columna[1]));
            dtoLista.add(dto);
        }
        log.debug("Cantidad de comentarios calculada correctamente");
        return dtoLista;
    }
}
