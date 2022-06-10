package edu.kalum.core.controllers;

import edu.kalum.core.model.dao.services.ICargoService;
import edu.kalum.core.model.entities.Alumno;
import edu.kalum.core.model.entities.Cargo;
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
public class CargoController {
    private Logger logger = LoggerFactory.getLogger(CargoController.class);

    @Autowired
    private ICargoService cargoService;

    @GetMapping("/cargo")
    public ResponseEntity<?> listarCargos(){
        Map<String, Object> response = new HashMap<>();
        logger.info("Iniciando el proceso de listar cargos");
        try {
            List<Cargo> cargos = cargoService.findAll();
            if(cargos != null && cargos.isEmpty()){
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }else{
                logger.info("Iniciando el proceso de listar cargos");
                return new ResponseEntity<List<Cargo>>(cargos,HttpStatus.OK);
            }
        }catch (CannotCreateTransactionException e){
            logger.error("Error al momento de conectarse a la base de datos");
            response.put("Mensaje: ","Error al momento de conectarse a la base de datos");
            response.put("Error: ",e.getMessage().concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);
        }catch (DataAccessException e){
            logger.error("Error al momento de ejecutar la consulta en la base de datos");
            response.put("Mensje: ", "Error al momento de ejecutar la consulta en la base de datos");
            response.put("Error: ",e.getMessage().concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
    @GetMapping("/cargo/{cargoId}")
    public ResponseEntity<?> mostrarCargo(@PathVariable String cargoId){
        Map<String,Object> response = new HashMap<>();
        logger.info("Iniciando el proceso de busqueda de cargo por Id".concat(cargoId));
        try {
            Cargo cargo = cargoService.findById(cargoId);
            if(cargo == null){
                logger.warn("No existe el cargo con el Id ".concat(cargoId));
                response.put("Mensaje: ","No existe el cargo con el Id ".concat(cargoId));
                return new ResponseEntity<Map<String,Object>>(response,HttpStatus.NOT_FOUND);
            }else{
                logger.info("Se realizo la busqueda del cargo con el Id ".concat(cargoId).concat(" de forma exitosa"));
                response.put("Mensaje: ", "Se realizo la busqueda del cargo con el Id ".concat(cargoId).concat(" de forma exitosa"));
                return new ResponseEntity<Cargo>(cargo,HttpStatus.OK);
            }

        }catch (CannotCreateTransactionException e){
            logger.error("Error al momento de conectarse a la base de datos");
            response.put("Mensaje: ", "Error al momento de conectarse a la base de datos");
            response.put("Error: ", e.getMessage().concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.SERVICE_UNAVAILABLE);
        }catch (DataAccessException e){
            logger.error("Error al momento de ejecutar la consulta en la base de datos");
            response.put("Mensaje: ", "Error al momento de ejecutar la consulta en la base de datos");
            response.put("Error: ", e.getMessage().concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String,Object>>(response,HttpStatus.SERVICE_UNAVAILABLE);
        }

    }

}
