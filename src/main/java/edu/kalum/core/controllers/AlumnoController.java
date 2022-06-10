package edu.kalum.core.controllers;

import edu.kalum.core.model.dao.services.IAlumnoService;
import edu.kalum.core.model.entities.Alumno;
import edu.kalum.core.model.entities.CarreraTecnica;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/kalum-management/v1")
public class AlumnoController {
    private Logger logger = LoggerFactory.getLogger(AlumnoController.class);

    @Autowired
    private IAlumnoService alumnoService;

    @GetMapping("/alumno")
    public ResponseEntity<?> listarAlumnos() {
        Map<String, Object> response = new HashMap<>();
        logger.info("Iniciando el proceso de listar Alumnos");
        try {
            List<Alumno> alumnos = alumnoService.findAll();
            if (alumnos != null && alumnos.isEmpty()) {
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
}