package edu.kalum.core.controllers;

import edu.kalum.core.model.dao.services.ICargoService;
import edu.kalum.core.model.entities.Alumno;
import edu.kalum.core.model.entities.Cargo;
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
public class CargoController {
    private Logger logger = LoggerFactory.getLogger(CargoController.class);

    @Value("${edu.kalum.core.configuration.page.size}")
    private Integer size;

    @Autowired
    private ICargoService cargoService;

    @GetMapping("/cargo")
    public ResponseEntity<?> listarCargos(){
        Map<String, Object> response = new HashMap<>();
        logger.info("Iniciando el proceso de listar cargos");
        try {
            List<Cargo> cargos = cargoService.findAll();
            if(cargos == null && cargos.isEmpty()){
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

    @GetMapping("/cargo/page/{page}")
    public ResponseEntity<?> index(@PathVariable Integer page){
        Map<String,Object> response= new HashMap<>();
        try{
            Pageable pageable = PageRequest.of(page,size);
            Page<Cargo> cargoPage = cargoService.findAll(pageable);
            if(cargoPage == null && cargoPage.getSize() == 0){
                logger.warn("No existen registos de Cargos");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }else{
                logger.info("Se proceso la consulta de Cargos por pagina");
                return new ResponseEntity<Page<Cargo>>(cargoPage,HttpStatus.OK);
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

    @PostMapping("/cargo")
    public ResponseEntity<?> create(@Valid @RequestBody Cargo value, BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        if (result.hasErrors() == true) {
            List<String> errores = result.getFieldErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.toList());
            response.put("Errores:", errores);
            logger.info("Se encontraron errores de validaciones en la peticion");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            value.setCargoId(UUID.randomUUID().toString());
            Cargo cargo = cargoService.save(value);
            response.put("Mensaje: ", "El Cargo a sido creado Exitosamente");
            response.put("Cargo ", cargo);
            logger.info("El Cargo a sido creado Exitosamente");
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

    @PutMapping("/cargo/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Cargo value, BindingResult result, @PathVariable String id){
        Map<String, Object> response = new HashMap<>();
        if (result.hasErrors() == true) {
            List<String> errores = result.getFieldErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.toList());
            response.put("Errores:", errores);
            logger.info("Se encontraron errores de validacion en la peticion");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }try {
            Cargo cargo = cargoService.findById(id);
            if(cargo == null){
                response.put("Mensaje:" , "No Existe el Cargo con el Id ".concat(id));
                logger.warn("No Existe el Cargo con el Id ".concat(id));
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            }else{
                cargo.setDescripcion(value.getDescripcion());
                cargo.setPrefijo(value.getPrefijo());
                cargo.setMonto(value.getMonto());
                cargoService.save(cargo);
                response.put("Mensaje", "El cargo fue actualizado con exito");
                response.put("Cargo",cargo);
                logger.info("Se actualizo la informacion del cargo de forma exitosa");
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
    @DeleteMapping("/cargo/{id}")
    public ResponseEntity<?> delete(@PathVariable String id){
        Map<String, Object> response = new HashMap<>();
        try{

            Cargo cargo = cargoService.findById(id);
            if(cargo == null){
                response.put("Mensaje:" , "No Existe el Cargo con el Id ".concat(id));
                logger.warn("No Existe el Cargo con el Id ".concat(id));
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            }else{
                cargoService.delete(cargo);
                response.put("Mensaje: ", "El cargo fue eliminado exitosamente");
                response.put("Cargo", cargo);
                logger.info("Se elimino el cargo correctamente");
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
