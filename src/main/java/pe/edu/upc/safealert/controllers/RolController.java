package pe.edu.upc.safealert.controllers;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.safealert.dtos.RolDTO;
import pe.edu.upc.safealert.entities.Rol;
import pe.edu.upc.safealert.servicesinterfaces.IRolService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rol")
@Slf4j
public class RolController {

    @Autowired
    private IRolService rS;

    @GetMapping("/list")
    public List<RolDTO> listarRol() {
        log.info("Solicitud GET para listar todos los roles");
        return rS.list().stream().map(x -> {
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper.map(x, RolDTO.class);
        }).collect(Collectors.toList());
    }

    @PostMapping("/insert")
    public void insertarRol(@RequestBody RolDTO RDto) {
        log.info("Solicitud POST para insertar un nuevo rol: {}", RDto);
        ModelMapper modelMapper = new ModelMapper();
        Rol r = modelMapper.map(RDto, Rol.class);
        rS.insert(r);
        log.debug("Rol insertado correctamente");
    }

    @GetMapping("/list/{idRol}")
    public RolDTO listarId(@PathVariable("idRol") int idRol) {
        log.info("Solicitud GET para obtener rol por ID: {}", idRol);
        ModelMapper m = new ModelMapper();
        return m.map(rS.listarId(idRol), RolDTO.class);
    }

    @PutMapping("/put")
    public void modificarRol(@RequestBody RolDTO RDto) {
        log.info("Solicitud PUT para modificar un rol: {}", RDto);
        ModelMapper m = new ModelMapper();
        Rol r = m.map(RDto, Rol.class);
        rS.update(r);
        log.debug("Rol modificado correctamente");
    }

    @DeleteMapping("/delete/{idRol}")
    public void eliminarRol(@PathVariable("idRol") int idRol) {
        log.info("Solicitud DELETE para eliminar un rol: {}", idRol);
        rS.delete(idRol);
    }


}
