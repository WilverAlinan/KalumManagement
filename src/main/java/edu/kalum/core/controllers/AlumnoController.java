package edu.kalum.core.controllers;

import edu.kalum.core.model.dao.services.IAlumnoService;
import edu.kalum.core.model.entities.Alumno;
import edu.kalum.core.model.entities.CarreraTecnica;
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
public class AlumnoController {
    private Logger logger = LoggerFactory.getLogger(AlumnoController.class);

    @Value("${edu.kalum.core.configuration.page.size}")
    private Integer index;

    @Autowired
    private IAlumnoService alumnoService;

    @GetMapping("/alumno")
    public ResponseEntity<?> listarAlumnos() {
        Map<String, Object> response = new HashMap<>();
        logger.info("Iniciando el proceso de listar Alumnos");
        try {
            List<Alumno> alumnos = alumnoService.findAll();
            if (alumnos == null && alumnos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                logger.info("Iniciando el proceso de listar Alumnos");
                return new ResponseEntity<List<Alumno>>(alumnos, HttpStatus.OK);
            }
        } catch (CannotCreateTransactionException e) {
            logger.error("Error al momento de conectarse a la base de datos");
            response.put("Mensaje", "Error al momento de conectarse a la base de datos");
            response.put("Error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.SERVICE_UNAVAILABLE);
        }catch (DataAccessException e){
            logger.error("Error al momento de ejecutar la consulta en la base de datos");
            response.put("Mensaje","Error al momento de ejecutar la consulta en la base de datos");
            response.put("Error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @GetMapping("/alumno/{alumnoId}")
    public ResponseEntity<?> showAlumno(@PathVariable String alumnoId){
        Map<String,Object> response = new HashMap<>();
        logger.info("Iniciando el proceso de busqueda de alumno por Id".concat(alumnoId));
        try{
            Alumno alumno = alumnoService.findById(alumnoId);
            if(alumno == null){
                logger.warn("No existe el alumno con el id: ".concat(alumnoId));
                response.put("Mensaje","No existe el alumno con el id: ".concat(alumnoId));
                return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
            }else{
                logger.info("Se realizo la busqueda del alumno con el id de forma exitosa!!");
                return new ResponseEntity<Alumno>(alumno,HttpStatus.OK);
            }
        }catch (CannotCreateTransactionException e){
            logger.error("Error al momento de conectarse a la base de datos");
            response.put("Mensaje", "Error al momento de conectarse a la base de datos");
            response.put("Error",e.getMessage().concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.SERVICE_UNAVAILABLE);
        }catch (DataAccessException e){
            logger.error("Error al momento de ejecutar la consulta en la base de datos");
            response.put("Mensaje","Error al momento de ejecutar la consulta en la base de datos");
            response.put("Error",e.getMessage().concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @GetMapping("/alumno/page/{page}")
    public ResponseEntity<?> index(@PathVariable Integer page){
        Map<String,Object> response= new HashMap<>();
        try{
            Pageable pageable = PageRequest.of(page,index);
            Page<Alumno> alumnoPage = alumnoService.findAll(pageable);
            if(alumnoPage == null && alumnoPage.getSize() == 0){
                logger.warn("No Existen registro de Alumnos");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }else{
                logger.info("Se proceso correctamente la consulta de Alumnos por pagina");
                return new ResponseEntity<Page<Alumno>>(alumnoPage,HttpStatus.OK);
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

    @PutMapping("/alumno/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Alumno value, BindingResult result, @PathVariable String id){
        Map<String, Object> response = new HashMap<>();
        if (result.hasErrors() == true) {
            List<String> errores = result.getFieldErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.toList());
            response.put("Errores:", errores);
            logger.info("Se encontraron errores de validacion en la peticion");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }try {
            Alumno alumno = alumnoService.findById(id);
            if(alumno == null){
                response.put("Mensaje:" , "El Alumno con el Carne ".concat(id).concat(" no existe"));
                logger.warn("El Alumno con el Carne ".concat(id).concat(" no existe"));
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            }else{
                alumno.setApellidos(value.getApellidos());
                alumno.setNombres(value.getNombres());
                alumno.setDireccion(value.getDireccion());
                alumno.setTelefono(value.getTelefono());
                alumno.setEmail(value.getEmail());
                alumnoService.save(alumno);
                response.put("Mensaje", "La Informacion de Alumno fue actualizad con exito");
                response.put("Alumno",alumno);
                logger.info("La Informacion de Alumno fue actualizad con exito");
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
    @DeleteMapping("/alumno/{id}")
    public ResponseEntity<?> delete(@PathVariable String id){
        Map<String, Object> response = new HashMap<>();
        try{
            Alumno alumno = alumnoService.findById(id);
            if(alumno == null){
                response.put("Mensaje:" , "El Alumno con el Carne ".concat(id).concat(" no existe"));
                logger.warn("El Alumno con el Carne ".concat(id).concat(" no existe"));
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            }else{
                alumnoService.delete(alumno);
                response.put("Mensaje: ", "El Alumno fue eliminado con Exito");
                response.put("Alumno", alumno);
                logger.info("Se elimino el Alumno correctamente");
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