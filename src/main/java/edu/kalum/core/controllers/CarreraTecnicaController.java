package edu.kalum.core.controllers;

import edu.kalum.core.model.dao.services.IAlumnoService;
import edu.kalum.core.model.dao.services.ICarreraTecnicaService;
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
public class CarreraTecnicaController {
    private Logger logger = LoggerFactory.getLogger(CarreraTecnicaController.class);

    @Autowired
    private ICarreraTecnicaService carreraTecnicaService;

    @GetMapping("/carreras-tecnicas")
    public ResponseEntity<?> listarCarrerasTecnicas(){
        Map<String,Object> response = new HashMap<>();
        logger.debug("Iniciando proceso de consulta de carreras tecnicas");
        try {
            List<CarreraTecnica> carrerasTecnicas = carreraTecnicaService.findAll();
            if (carrerasTecnicas != null && carrerasTecnicas.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                logger.info("Se consulto la informacion de las carreras tecnicas de forma exitosa");
                return new ResponseEntity<List<CarreraTecnica>>(carrerasTecnicas, HttpStatus.OK);
            }
        }catch(CannotCreateTransactionException e){
            logger.error("Error al momento de conectarse a la base de datos");
            response.put("Mensaje","Error al momento de conectarse a la base de datos");
            response.put("Error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.SERVICE_UNAVAILABLE);
        }catch (DataAccessException e){
            logger.error("Error al momento de ejecutar la consulta a la base de datos" );
            response.put("Mensaje","Error al momento de ejecutar la consulta a la base de datos");
            response.put("Error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @GetMapping("/carreras-tecnicas/{carreraId}")
    public ResponseEntity<?> showCarreraTecnica(@PathVariable String carreraId){
        Map<String, Object> response = new HashMap<>();
        logger.debug("Iniciando proceso de busqueda de la carrera con id ".concat(carreraId));
        try {
            CarreraTecnica carreraTecnica = carreraTecnicaService.findById(carreraId);
            if (carreraTecnica == null) {
                logger.warn(("No existe la carrera tecnica con el id".concat(carreraId)));
                response.put("mensaje", "no existe la carrera tecnica con el id".concat(carreraId));
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            } else {
                logger.info("se proceso la busqueda de la carrera tecnica de forma exitosa");
                return new ResponseEntity<CarreraTecnica>(carreraTecnica, HttpStatus.OK);
            }
        }catch (CannotCreateTransactionException e){
            logger.error("Error al momento de conectarse a la base de datos");
            response.put("Mensaje","Error al momento de conectarse a la base de datos");
            response.put("Error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.SERVICE_UNAVAILABLE);
        }catch (DataAccessException e){
            logger.error("Error al momento de ejecutar la consulta a la base de datos" );
            response.put("Mensaje","Error al momento de conectarse a la base de datos");
            response.put("Error",e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);
        }

    }
}
