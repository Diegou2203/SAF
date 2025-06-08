package pe.edu.upc.safealert.controllers;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import pe.edu.upc.safealert.dtos.CantidadUbicacionxFDTO;
import pe.edu.upc.safealert.dtos.FenomenoNaturalDTO;
import pe.edu.upc.safealert.dtos.HistoricoFenomenosDTO;
import pe.edu.upc.safealert.entities.FenomenoNatural;
import pe.edu.upc.safealert.servicesinterfaces.IFenomenoNaturalService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/fenomeno")
@Slf4j // Lombok
public class FenomenoNaturalController {

    @Autowired
    private IFenomenoNaturalService fnS;

    @GetMapping("/list")
    public List<FenomenoNaturalDTO> listarFenomenos() {
        log.info("Solicitud GET para listar todos los fenómenos naturales");
        return fnS.list().stream().map(x -> {
            ModelMapper modelMapper = new ModelMapper();
            return modelMapper.map(x, FenomenoNaturalDTO.class);
        }).collect(Collectors.toList());
    }

    @PostMapping("/insert")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void insertar(@RequestBody FenomenoNaturalDTO fNDto) {
        log.info("Solicitud POST para insertar fenómeno natural: {}", fNDto);
        ModelMapper modelMapper = new ModelMapper();
        FenomenoNatural fn = modelMapper.map(fNDto, FenomenoNatural.class);
        fnS.insert(fn);
        log.debug("Fenómeno natural insertado correctamente");
    }

    // GET: obtener fenómeno natural por ID
    @GetMapping("/list/{idFenomenoNatural}")
    public FenomenoNaturalDTO listarId(@PathVariable("idFenomenoNatural") int idFenomenoNatural) {
        log.info("Solicitud GET para obtener fenómeno natural con ID: {}", idFenomenoNatural);
        ModelMapper m = new ModelMapper();
        return m.map(fnS.listarId(idFenomenoNatural), FenomenoNaturalDTO.class);
    }

    @DeleteMapping("/delete/{idFenomenoNatural}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void eliminar(@PathVariable("idFenomenoNatural") int idFenomenoNatural) {
        log.warn("Solicitud DELETE para eliminar fenómeno natural con ID: {}", idFenomenoNatural);
        fnS.delete(idFenomenoNatural);
    }

    @PutMapping("/put")
    @PreAuthorize("hasAuthority('ADMIN')")
    public void modificar(@RequestBody FenomenoNaturalDTO fnDTO) {
        log.info("Solicitud PUT para modificar fenómeno natural: {}", fnDTO);
        ModelMapper m = new ModelMapper();
        FenomenoNatural fn = m.map(fnDTO, FenomenoNatural.class);
        fnS.update(fn);
        log.debug("Fenómeno natural modificado con éxito");
    }

    // GET: obtener cantidad de fenómenos por ubicación
    @GetMapping("/list/CantidadFenomenosNaturalesPorUbicacion")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<CantidadUbicacionxFDTO> cantidadFenomeno() {
        log.info("Solicitud GET para obtener cantidad de fenómenos por ubicación");
        List<CantidadUbicacionxFDTO> dtoLista = new ArrayList<>();
        List<String[]> filaLista = fnS.quantityFenomenoPorUbicacion();
        for (String[] columna : filaLista) {
            CantidadUbicacionxFDTO dto = new CantidadUbicacionxFDTO();
            dto.setCiudad(columna[0]);
            dto.setCantidad(Integer.parseInt(columna[1]));
            dtoLista.add(dto);
        }
        log.debug("Cantidad de fenómenos por ubicación calculada correctamente");
        return dtoLista;
    }

    // GET: obtener histórico de fenómenos por intensidad
    @GetMapping("/list/HistoricoPorIntensidad")
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<HistoricoFenomenosDTO> obtenerHistoricoPorIntensidad() {
        log.info("Solicitud GET para obtener histórico de fenómenos por intensidad");
        List<String[]> data = fnS.findHistoricoFenomenosPorIntensidad();
        List<HistoricoFenomenosDTO> dtos = new ArrayList<>();

        for (String[] row : data) {
            HistoricoFenomenosDTO dto = new HistoricoFenomenosDTO();
            dto.setIntensidad(row[0]);
            dto.setamio(Integer.parseInt(row[1]));
            dto.setCantidad(Integer.parseInt(row[2]));
            dtos.add(dto);
        }

        log.debug("Histórico por intensidad procesado correctamente");
        return dtos;
    }
}
