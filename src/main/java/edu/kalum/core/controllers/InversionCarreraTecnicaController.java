package edu.kalum.core.controllers;

import edu.kalum.core.model.dao.services.ICarreraTecnicaService;
import edu.kalum.core.model.dao.services.InversionCarreraTecnicaService;
import edu.kalum.core.model.entities.InversionCarreraTecnica;
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
public class InversionCarreraTecnicaController {

    @Value("${edu.kalum.core.configuration.page.size}")
    private Integer size;

    private Logger logger = LoggerFactory.getLogger(InversionCarreraTecnicaController.class);

    @Autowired
    private InversionCarreraTecnicaService inversionCarreraTecnicaService;

    @Autowired
    private ICarreraTecnicaService carreraTecnicaService;

    @GetMapping("/inversion-carreras-tecnicas/page/{page}")
    public ResponseEntity<?> index(@PathVariable Integer page){
        Map<String,Object> response= new HashMap<>();
        try{
            Pageable pageable = PageRequest.of(page,size);
            Page<InversionCarreraTecnica> inversionCarreraTecnicaPage = inversionCarreraTecnicaService.findAll(pageable);
            if(inversionCarreraTecnicaPage == null && inversionCarreraTecnicaPage.getSize() == 0){
                logger.warn("No existen registros de inversion de Carreras Tecnicas");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }else{
                logger.info("Se proceso la consulta de las inversiones de Carreras Tecnicas por pagina correctamente");
                return new ResponseEntity<Page<InversionCarreraTecnica>>(inversionCarreraTecnicaPage,HttpStatus.OK);
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

    @GetMapping("/inversion-carreras-tecnicas")
    public ResponseEntity<?> listarInversionCarrerasTecnicas() {
        Map<String, Object> response = new HashMap<>();
        logger.debug("Iniciando proceso de consulta de las inversiones de carreras tecnicas");
        try {
            List<InversionCarreraTecnica> inversionCarreraTecnica = inversionCarreraTecnicaService.findAll();
            if (inversionCarreraTecnica == null && inversionCarreraTecnica.isEmpty()) {
                logger.warn("No existen registros de las inversiones de carreras tecnicas");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                logger.info("Se consulto la informacion de las inveriones de carreras tecnicas de forma exitosa");
                return new ResponseEntity<List<InversionCarreraTecnica>>(inversionCarreraTecnica, HttpStatus.OK);
            }
        } catch (CannotCreateTransactionException e) {
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

    @GetMapping("/inversion-carreras-tecnicas/{inversionId}")
    public ResponseEntity<?> showInversionCarreraTecnicaId(@PathVariable String inversionId) {
        Map<String, Object> response = new HashMap<>();
        logger.debug("Iniciando proceso de busqueda de las inversiones de carrera tecnica con id ".concat(inversionId));
        try {
            InversionCarreraTecnica inversionCarreraTecnica = inversionCarreraTecnicaService.findById(inversionId);
            if (inversionCarreraTecnica == null) {
                logger.warn(("No existe invesison de carrera tecnica con el id ".concat(inversionId)));
                response.put("mensaje", "No existe invesison de carrera tecnica con el id ".concat(inversionId));
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            } else {
                logger.info("se proceso la busqueda de la inversion de carrera tecnica de forma exitosa");
                return new ResponseEntity<InversionCarreraTecnica>(inversionCarreraTecnica, HttpStatus.OK);
            }
        } catch (CannotCreateTransactionException e) {
            logger.error("Error al momento de conectarse a la base de datos");
            response.put("Mensaje", "Error al momento de conectarse a la base de datos");
            response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);
        } catch (DataAccessException e) {
            logger.error("Error al momento de ejecutar la consulta a la base de datos");
            response.put("Mensaje", "Error al momento de conectarse a la base de datos");
            response.put("Error", e.getMessage().concat(": ").concat(e.getMostSpecificCause().getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    @PostMapping("/inversion-carreras-tecnicas")
    public ResponseEntity<?> createInversion(@Valid @RequestBody InversionCarreraTecnica value, BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        if (result.hasErrors() == true) {
            List<String> errores = result.getFieldErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.toList());
            response.put("Errores:", errores);
            logger.info("Se encontraron errores de validaciones en la peticion");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            value.setInversionId(UUID.randomUUID().toString());
            value.setCarreraId(value.getCarreraId());
            InversionCarreraTecnica inversionCarreraTecnica = inversionCarreraTecnicaService.save(value);
            response.put("Mensaje: ", "La Inversion de carrera Tecnica a sido creada Exitosamente");
            response.put("Inversion Carrera Tecnica ", inversionCarreraTecnica);
            logger.info("La Inversion de carrera Tecnica a sido creada Exitosamente");
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

    @PutMapping("/inversion-carreras-tecnicas/{id}")
    public ResponseEntity<?> updateInversion(@Valid @RequestBody InversionCarreraTecnica value, BindingResult result, @PathVariable String id){
        Map<String, Object> response = new HashMap<>();
        if (result.hasErrors() == true) {
            List<String> errores = result.getFieldErrors().stream().map(error -> error.getDefaultMessage()).collect(Collectors.toList());
            response.put("Errores:", errores);
            logger.info("Se encontraron errores de validacion en la peticion");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }try {
            InversionCarreraTecnica inversionCarreraTecnica = inversionCarreraTecnicaService.findById(id);
            if(inversionCarreraTecnica == null){
                response.put("Mensaje:" , "La Inversion de carrera tecnica con el id ".concat(id).concat(" no existe"));
                logger.warn("La Inversion de carrera tecnica con el id ".concat(id).concat(" no existe"));
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            }else{
                inversionCarreraTecnica.setMontoInscripcion(value.getMontoInscripcion());
                inversionCarreraTecnica.setNumeroPagos(value.getNumeroPagos());
                inversionCarreraTecnica.setMontoPagos(value.montoPagos);
                inversionCarreraTecnicaService.save(inversionCarreraTecnica);
                response.put("Mensaje", "La Inversion de la carrera Tecnica fue actualizada con exito");
                response.put("Inversion Carrera Tecnica",inversionCarreraTecnica);
                logger.info("La Inversion de la carrera Tecnica fue actualizada con exito");
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
    @DeleteMapping("/inversion-carreras-tecnicas/{id}")
    public ResponseEntity<?> delete(@PathVariable String id){
        Map<String, Object> response = new HashMap<>();
        try{

            InversionCarreraTecnica inversionCarreraTecnica = inversionCarreraTecnicaService.findById(id);
            if(inversionCarreraTecnica == null){
                response.put("Mensaje:" , "La Inversion de carrera tecnica con el id ".concat(id).concat(" no existe"));
                logger.warn("La Inversion de carrera tecnica con el id ".concat(id).concat(" no existe"));
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            }else{
                inversionCarreraTecnicaService.delete(inversionCarreraTecnica);
                response.put("Mensaje: ", "La Inversion de la carrera tecnica fue eliminada exitosamente");
                response.put("Inversion Carrera Tecnica", inversionCarreraTecnica);
                logger.info("Se elimino la inversion de la carrera tecnica correctamente");
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

