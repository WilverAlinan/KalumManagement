package edu.kalum.core.controllers;

import edu.kalum.core.model.dao.services.IExamenAdmisionService;
import edu.kalum.core.model.entities.ExamenAdmision;
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
public class ExamenAdmisionController {

    private Logger logger = LoggerFactory.getLogger(ExamenAdmisionController.class);
    @Autowired
    private IExamenAdmisionService examenAdmisionService;
    @GetMapping("/listar-examen-admision")
    public ResponseEntity<?> listarExamenAdmision(){
        Map<String,Object> response = new HashMap<>();
        logger.info("Iniciando el proceso de listar los Examenes de Admision");
        try{
            List<ExamenAdmision> examenAdmision = examenAdmisionService.findAll();
            if(examenAdmision != null && examenAdmision.isEmpty()){
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
    @GetMapping("/listar-examen-admision/{examenId}")
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
}
