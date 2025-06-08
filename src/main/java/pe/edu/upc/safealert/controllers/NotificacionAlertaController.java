package pe.edu.upc.safealert.controllers;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.safealert.dtos.CantidadNotificacionxUserDTO;
import pe.edu.upc.safealert.dtos.NotificacionAlertaDTO;
import pe.edu.upc.safealert.entities.NotificacionAlerta;
import pe.edu.upc.safealert.servicesinterfaces.INotificacionAlertaService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/notificacionalerta")
@Slf4j
public class NotificacionAlertaController {

    @Autowired
    private INotificacionAlertaService naS;

    @GetMapping("/list")
    public List<NotificacionAlertaDTO> listarSimulacros() {
        log.info("Solicitud GET para listar notificaciones de alerta");
        return naS.list().stream().map(x -> {
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper.map(x, NotificacionAlertaDTO.class);
        }).collect(Collectors.toList());
    }

    @PostMapping("insert")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void insertar(@RequestBody NotificacionAlertaDTO naDTO) {
        log.info("Solicitud POST para insertar notificación de alerta: {}", naDTO);
        ModelMapper modelMapper = new ModelMapper();
        NotificacionAlerta na = modelMapper.map(naDTO, NotificacionAlerta.class);
        naS.insert(na);
        log.debug("Notificación de alerta insertada correctamente");
    }

    @GetMapping("/list/{idNotificacionAlerta}")
    public NotificacionAlertaDTO listarPorId(@PathVariable("idNotificacionAlerta") int idNotificacionAlerta) {
        log.info("Solicitud GET para obtener notificación con ID: {}", idNotificacionAlerta);
        ModelMapper modelMapper = new ModelMapper();
        NotificacionAlertaDTO naDTO = modelMapper.map(naS.listarId(idNotificacionAlerta), NotificacionAlertaDTO.class);
        return naDTO;
    }

    @DeleteMapping("/delete/{idNotificacionAlerta}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void eliminar(@PathVariable("idNotificacionAlerta") int idNotificacionAlerta) {
        log.warn("Solicitud DELETE para eliminar notificación con ID: {}", idNotificacionAlerta);
        naS.delete(idNotificacionAlerta);
    }

    @PutMapping("/put")
    public void modificar(@RequestBody NotificacionAlertaDTO naDTO) {
        log.info("Solicitud PUT para modificar notificación de alerta: {}", naDTO);
        ModelMapper modelMapper = new ModelMapper();
        NotificacionAlerta na = modelMapper.map(naDTO, NotificacionAlerta.class);
        naS.update(na);
        log.debug("Notificación de alerta modificada correctamente");
    }

    @GetMapping("/list/CantidadNotificacionesRevisadasPorUsuario")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<CantidadNotificacionxUserDTO> cantidadNotificaciones() {
        log.info("Solicitud GET para obtener cantidad de notificaciones revisadas por usuario");
        List<CantidadNotificacionxUserDTO> dtoLista = new ArrayList<>();
        List<String[]> filaLista = naS.quantityNotificacionPorUser();
        for (String[] columna : filaLista) {
            CantidadNotificacionxUserDTO dto = new CantidadNotificacionxUserDTO();
            dto.setUsername(columna[0]);
            dto.setCantidad(Integer.parseInt(columna[1]));
            dtoLista.add(dto);
        }
        log.debug("Cantidad de notificaciones revisadas por usuario procesada correctamente");
        return dtoLista;
    }
}
