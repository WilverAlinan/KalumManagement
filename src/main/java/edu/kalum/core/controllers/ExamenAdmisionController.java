package edu.kalum.core.controllers;

import edu.kalum.core.model.dao.services.IExamenAdmisionService;
import edu.kalum.core.model.entities.CarreraTecnica;
import edu.kalum.core.model.entities.ExamenAdmision;
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
public class ExamenAdmisionController {
    private Logger logger = LoggerFactory.getLogger(ExamenAdmisionController.class);

    @Value("${edu.kalum.core.configuration.page.size}")
    private Integer index;

    @Autowired
    private IExamenAdmisionService examenAdmisionService;

    @GetMapping("/examen-admision")
    public ResponseEntity<?> listarExamenAdmision(){
        Map<String,Object> response = new HashMap<>();
        logger.info("Iniciando el proceso de listar los Examenes de Admision");
        try{
            List<ExamenAdmision> examenAdmision = examenAdmisionService.findAll();
            if(examenAdmision == null && examenAdmision.isEmpty()){
                return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
            }else{
                logger.info("Iniciando el proceso de listar los Examenes de Admision");
                return new ResponseEntity<List<ExamenAdmision>>(examenAdmision,HttpStatus.OK);
            }
        }catch (CannotCreateTransactionException e){
            logger.error("Error al momento de conectarse a la base de datos");
            response.put("Mensaje: ", "Error al momento de conectarse a la base de datos");
            response.put("Error: ",e.getMessage().concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response,HttpStatus.SERVICE_UNAVAILABLE);
        }catch (DataAccessException e){
            logger.error("Error al momento de ejecutar la consulta en la base de datos");
            response.put("Mensaje: ", "Error al momento de ejecutar la consulta en la base de datos");
            response.put("Error: ",e.getMessage().concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
    @GetMapping("/examen-admision/{examenId}")
    public ResponseEntity<?> mostrarExamenAdmison(@PathVariable String examenId){
        Map<String,Object> response = new HashMap<>();
        logger.info("Iniciando el proceso de busqueda de Examen de Admision por Id ".concat(examenId));
        try{
            ExamenAdmision examenAdmision = examenAdmisionService.findById(examenId);
            if (examenAdmision == null){
                logger.warn("No existe el Examen de Admison con el Id ".concat(examenId));
                response.put("Mensaje: ","No existe el Examen de Admison con el Id ".concat(examenId));
                return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
            }else {
                logger.info("Se realizo la busqueda del Examen de Admision con el Id ".concat(examenId).concat(" de forma correcta"));
                response.put("Mensaje: ", "Se realizo la busqueda del Examen de Admision con el Id ".concat(examenId).concat(" de forma correcta"));
                return new ResponseEntity<ExamenAdmision>(examenAdmision,HttpStatus.OK);
            }
        }catch (CannotCreateTransactionException e){
            logger.error("Error al momento de conectarse a la base de datos");
            response.put("Mensaje: ", "Error al momento de conectarse a la base de datos");
            response.put("Error: ", e.getMessage().concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String , Object>>(response,HttpStatus.SERVICE_UNAVAILABLE);
        }catch (DataAccessException e){
            logger.error("Error al momento de ejecutar la consulta en la base de datos");
            response.put("Mensaje: ", "Error al momento de ejecutar la consulta en la base de datos");
            response.put("Error: ", e.getMessage().concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @GetMapping("/examen-admision/page/{page}")
    public ResponseEntity<?> index(@PathVariable Integer page){
        Map<String,Object> response= new HashMap<>();
        try{
            Pageable pageable = PageRequest.of(page,index);
            Page<ExamenAdmision> examenAdmisionPage = examenAdmisionService.findAll(pageable);
            if(examenAdmisionPage == null && examenAdmisionPage.getSize() == 0){
                logger.warn("No existen registros de Examen de Admision");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }else{
                logger.info("Se proceso correctamente la consulta Examenes de Admision");
                return new ResponseEntity<Page<ExamenAdmision>>(examenAdmisionPage,HttpStatus.OK);
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

    @PostMapping("/examen-admision")
    public ResponseEntity<?> create(@Valid @RequestBody ExamenAdmision value, BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        if (result.hasErrors() == true) {
            List<String> errores = result.getFieldErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.toList());
            response.put("Errores:", errores);
            logger.info("Se encontraron errores de validaciones en la peticion");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            value.setExamenId(UUID.randomUUID().toString());
            ExamenAdmision examenAdmision = examenAdmisionService.save(value);
            response.put("Mensaje: ", "El Nuevo Examen de Admison a sido creado exitosamente");
            response.put("Examen de Admision ", examenAdmision);
            logger.info("El Nuevo Examen de Admison a sido creado exitosamente");
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

    @PutMapping("/examen-admision/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody ExamenAdmision value, BindingResult result, @PathVariable String id){
        Map<String, Object> response = new HashMap<>();
        if (result.hasErrors() == true) {
            List<String> errores = result.getFieldErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.toList());
            response.put("Errores:", errores);
            logger.info("Se encontraron errores de validacion en la peticion");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }try {
            ExamenAdmision examenAdmision = examenAdmisionService.findById(id);
            if(examenAdmision == null){
                response.put("Mensaje:" , "El Examen de Adimision con el id ".concat(id).concat(" no existe"));
                logger.warn("El Examen de Adimision con el id ".concat(id).concat(" no existe"));
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            }else{
                //examenAdmision.setExamenId(value.getExamenId());
                examenAdmision.setFechaExamen(value.getFechaExamen());
                examenAdmisionService.save(examenAdmision);
                response.put("Mensaje", "El Examen de Admision fue Actualizado Existosamente");
                response.put("Examen de Admision",examenAdmision);
                logger.info("El Examen de Admision fue Actualizado Existosamente");
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
    @DeleteMapping("/examen-admision/{id}")
    public ResponseEntity<?> delete(@PathVariable String id){
        Map<String, Object> response = new HashMap<>();
        try{

            ExamenAdmision examenAdmision = examenAdmisionService.findById(id);
            if(examenAdmision == null){
                response.put("Mensaje:" , "El Examen de Adimision con el id ".concat(id).concat(" no existe"));
                logger.warn("El Examen de Adimision con el id ".concat(id).concat(" no existe"));
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            }else{
                examenAdmisionService.delete(examenAdmision);
                response.put("Mensaje: ", "El Examen de Admison fue eliminado exitosamente");
                response.put("Examen de Admision", examenAdmision);
                logger.info("El Examen de Admison fue eliminado exitosamente");
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
