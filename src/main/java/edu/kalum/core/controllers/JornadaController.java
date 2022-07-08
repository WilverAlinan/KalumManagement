package edu.kalum.core.controllers;

import edu.kalum.core.model.dao.services.IJornadaService;
import edu.kalum.core.model.entities.CarreraTecnica;
import edu.kalum.core.model.entities.Jornada;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/kalum-management/v1")
public class JornadaController {
    private Logger logger = LoggerFactory.getLogger(JornadaController.class);

    @Value("${edu.kalum.core.configuration.page.size}")
    private Integer index;

    @Autowired
    private IJornadaService jornadaService;

    @GetMapping("/jornada/page/{page}")
    public ResponseEntity<?> index(@PathVariable Integer page){
        Map<String,Object> response= new HashMap<>();
        try{
            Pageable pageable = PageRequest.of(page,index);
            Page<Jornada> jornadaPage = jornadaService.findAll(pageable);
            if(jornadaPage == null && jornadaPage.getSize() == 0){
                logger.warn("No existen registros de Jornadas");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }else{
                logger.info("Se proceso la consulta de Jornadas por pagina");
                return new ResponseEntity<Page<Jornada>>(jornadaPage,HttpStatus.OK);
            }

        }catch (CannotCreateTransactionException e) {
            logger.error("Error al momento de conectarse a la base de datos");
            response.put("Mensaje", "Error al momento de conectarse a la base de datos");
            response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);
        } catch (DataAccessException e) {
            logger.error("Error al momento de ejecutar la consulta a la base de datos");
            response.put("Mensaje", "Error al momento de ejecutar la consulta a la base de datos");
            response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @GetMapping("/jornada")
    public ResponseEntity<?> listarJornada() {
        Map<String, Object> response = new HashMap<>();
        logger.info("Iniciando el proceso de listar Jornadas");
        try {
            List<Jornada> jornada = jornadaService.findAll();
            if (jornada == null && jornada.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                logger.info("Se consulto la informacion de las Jorndas de forma exitosa");
                return new ResponseEntity<List<Jornada>>(jornada, HttpStatus.OK);
            }
        } catch (CannotCreateTransactionException e) {
            logger.error("Error al momento de conectarse a la base de datos");
            response.put("Mensaje: ", "Error al momento de conectarse a la base de datos");
            response.put("Error: ", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);
        } catch (DataAccessException e) {
            logger.error("Error al momento de ejecutar la consulta a la base de datos");
            response.put("Mensaje: ", "Error al momento de ejecutar la consulta a la base de datos");
            response.put("Error: ", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @GetMapping("/jornada/{jornadaId}")
    public ResponseEntity<?> mostrarJornada(@PathVariable String jornadaId){
        Map<String,Object> response = new HashMap<>();
        logger.info("Iniciando el proceso de busqueda de jornada por el Id ".concat(jornadaId));
        try{
            Jornada jornada = jornadaService.findById(jornadaId);
            if (jornada == null){
                logger.info("No existe la jornada con el Id ".concat(jornadaId));
                response.put("Mensaje: ", "No existe la jornada con el Id ".concat(jornadaId));
                return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
            }else{
                logger.info("Se proceso la busqueda de la jornada con el Id ".concat(jornadaId).concat(" de forma exitosa"));
                return new ResponseEntity<Jornada>(jornada,HttpStatus.OK);
            }
        }catch (CannotCreateTransactionException e){
            logger.error("Error al momento de conectarse a la base de datos");
            response.put("Mensaje: ","Error al momento de conectarse a la base de datos");
            response.put("Error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.SERVICE_UNAVAILABLE);
        }catch (DataAccessException e){
            logger.error("Error al momento de ejecutar la consulta a la base de datos" );
            response.put("Mensaje: ","Error al momento de conectarse a la base de datos");
            response.put("Error: ",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @PostMapping("/jornada")
    public ResponseEntity<?> create(@Valid @RequestBody Jornada value, BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        if (result.hasErrors() == true) {
            List<String> errores = result.getFieldErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.toList());
            response.put("Errores:", errores);
            logger.info("Se encontraron errores de validaciones en la peticion");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            value.setJornadaId(UUID.randomUUID().toString());
            Jornada jornada = jornadaService.save(value);
            response.put("Mensaje: ", "La Jornada a sido creada Exitosamente");
            response.put("Jornada ", jornada);
            logger.info("Se creo una carrera tecnica de forma exitosa");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
        } catch (CannotCreateTransactionException e) {
            logger.error("Error al momento de conectarse a la base de datos");
            response.put("Mensaje", "Error al momento de conectarse a la base de datos");
            response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);
        } catch (DataAccessException e) {
            logger.error("Error al momento de crear el nuevo registro");
            response.put("Mensaje", "Error al momento de crear el nuevo registro");
            response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @PutMapping("/jornada/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Jornada value, BindingResult result, @PathVariable String id){
        Map<String, Object> response = new HashMap<>();
        if (result.hasErrors() == true) {
            List<String> errores = result.getFieldErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.toList());
            response.put("Errores:", errores);
            logger.info("Se encontraron errores de validacion en la peticion");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }try {
            Jornada jornada = jornadaService.findById(id);
            if(jornada == null){
                response.put("Mensaje:" , "La Jornada con id ".concat(id).concat(" no existe"));
                logger.warn("La Jornada con id ".concat(id).concat(" no existe"));
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            }else{
                jornada.setJornada(value.getJornada());
                jornada.setDescripcion(value.getDescripcion());
                jornadaService.save(jornada);
                response.put("Mensaje", "La Jornada fue actualizada con exito");
                response.put("Jornada ",jornada);
                logger.info("Se actualizo la informacion de la Jornda de forma existosa");
                return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);
            }
        }catch (CannotCreateTransactionException e) {
            logger.error("Error al momento de conectarse a la base de datos");
            response.put("Mensaje", "Error al momento de conectarse a la base de datos");
            response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);
        } catch (DataAccessException e) {
            logger.error("Error al momento de actualizar la informacion");
            response.put("Mensaje", "Error al momento de actualizar la informacion");
            response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @DeleteMapping("/jornada/{id}")
    public ResponseEntity<?> delete(@PathVariable String id){
        Map<String, Object> response = new HashMap<>();
        try{

            Jornada jornada = jornadaService.findById(id);
            if(jornada == null){
                response.put("Mensaje:" , "La Jornada con id ".concat(id).concat(" no existe"));
                logger.warn("No existen registros de la Jornada");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            }else{
                jornadaService.delete(jornada);
                response.put("Mensaje: ", "La Jornada fue eliminada exitosamente");
                response.put("Jornada", jornada);
                logger.info("Se elimino la Jornada correctamente");
                return new ResponseEntity<Map<String,Object>>(response,HttpStatus.OK);
            }
        }catch (CannotCreateTransactionException e) {
            logger.error("Error al momento de conectarse a la base de datos");
            response.put("Mensaje", "Error al momento de conectarse a la base de datos");
            response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);
        } catch (DataAccessException e) {
            logger.error("Error al momento de eliminar el registro");
            response.put("Mensaje", "Error al momento de eliminar el registro");
            response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);
        }

    }

}
