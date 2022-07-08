package edu.kalum.core.controllers;
import edu.kalum.core.model.dao.IExamenAdmisionDao;
import edu.kalum.core.model.dao.services.IAspiranteService;
import edu.kalum.core.model.dao.services.ICarreraTecnicaService;
import edu.kalum.core.model.dao.services.IExamenAdmisionService;
import edu.kalum.core.model.dao.services.IJornadaService;
import edu.kalum.core.model.entities.Aspirante;
import edu.kalum.core.model.entities.CarreraTecnica;
import edu.kalum.core.model.entities.ExamenAdmision;
import edu.kalum.core.model.entities.Jornada;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
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
public class AspiranteController {
    @Value("${edu.kalum.core.configuration.page.size}")
    private Integer size;
    private Logger logger = LoggerFactory.getLogger(AspiranteController.class);
    @Autowired
    private IAspiranteService aspiranteService;
    @Autowired
    private ICarreraTecnicaService carreraTecnicaService;
    @Autowired
    private IJornadaService jornadaService;
    @Autowired
    private IExamenAdmisionService examenAdmisionService;

    @GetMapping("/aspirante")
    public ResponseEntity<?> listarAspirantes() {
        Map<String, Object> response = new HashMap<>();
        logger.debug("Iniciando consulta de Aspirantes");
        try {
            List<Aspirante> aspirantes = aspiranteService.findAll();
            if (aspirantes == null && aspirantes.size() == 0) {
                logger.warn("No existen Registros de Aspirantes");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                logger.info("Se realizo con exito la consulta de Aspirantes");
                return new ResponseEntity<List<Aspirante>>(aspirantes, HttpStatus.OK);
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

    @GetMapping("/aspirante/page/{page}")
    public ResponseEntity<?> index(@PathVariable Integer page) {
        Map<String, Object> response = new HashMap<>();
        Pageable pageable = PageRequest.of(page, size);
        try {
            Page<Aspirante> aspirantePage = aspiranteService.findAll(pageable);
            if (aspirantePage == null && aspirantePage.getSize() == 0) {
                logger.warn("No existen registros de alumnos");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NO_CONTENT);
            } else {
                logger.info("Se proceso la consulta de aspirante por pagina correctamente");
                return new ResponseEntity<Page<Aspirante>>(aspirantePage, HttpStatus.OK);
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

    @GetMapping("/aspirante/{noExpediente}")
    public ResponseEntity<?> showAspirante (@PathVariable String noExpediente){
        Map<String,Object> response = new HashMap<>();
        logger.debug("Iniciando proceso de busqueda de aspirante por Id");
        try {
            Aspirante aspirante = aspiranteService.findById(noExpediente);
            if(aspirante == null) {
                logger.warn("No existe Aspirante con el id ".concat(noExpediente));
                response.put("Error: " ,"No existe aspirante con el id ".concat(noExpediente));
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }else {
                logger.info("Se realizo exitosamente la consulta del aspirante con Id ".concat(noExpediente));
                response.put("Mensaje: ", "Se realizo exitosamente la consulta del aspirante con Id".concat(noExpediente));
                return new ResponseEntity<Aspirante>(aspirante,HttpStatus.OK);
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

    @PostMapping("/aspirante")
    public ResponseEntity<?> create(@Valid @RequestBody Aspirante value, BindingResult result){
        Map<String, Object> response = new HashMap<>();
        if (result.hasErrors()){
            List<String> errores = result.getFieldErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("Errores: ", errores);
            logger.info("Se encontraron errores de validacion en la peticion 1235456");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            Aspirante aspirante1 = aspiranteService.findById(value.getNoExpediente());
            if(aspirante1 != null){
                response.put("Mensaje: ", "Ya existe un registro con el numero de Expediente ".concat(value.getNoExpediente()));
              return new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST);
            }
            CarreraTecnica carreraTecnica = carreraTecnicaService.findById(value.getCarreraTecnica().getCarreraId());
            if(carreraTecnica == null){
                response.put("Mensaje: ","No existe una carrera tecnica con el Codigo ".concat(value.getCarreraTecnica().getCarreraId()));
                return new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST);
            }else{
                value.setCarreraTecnica(carreraTecnica);
            }
            Jornada jornada = jornadaService.findById(value.getJornada().getJornadaId());
            if(jornada == null){
                response.put("Mensaje: ","No existe una jornada con el codigo".concat(value.getJornada().getJornadaId()));
                return new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST);
            }else{
                value.setJornada(jornada);
            }
            ExamenAdmision examenAdmision = examenAdmisionService.findById(value.getExamenAdmision().getExamenId());
            if(examenAdmision == null){
                response.put("Mensaje ", "No existe un esamen de admision con el codigo ".concat(value.getExamenAdmision().getExamenId()));
                return new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST);
            }else {
                value.setExamenAdmision(examenAdmision);
            }
            aspiranteService.save(value);
            response.put("Mensaje: ", "El nuevo aspirante fue creado con exito");
            response.put("Aspirante ", value);
            logger.info("Se agrego al aspirante de forma exitosa");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

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

    @PutMapping("/aspirante/{noExpediente}")
    public ResponseEntity<?> update (@Valid @RequestBody Aspirante value, BindingResult result, @PathVariable String noExpediente){
        Map<String, Object> response = new HashMap<>();
        if (result.hasErrors()){
            List<String> errores = result.getFieldErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("Errores: ", errores);
            logger.info("Se encontraron errores de validacion en la peticion 1235456");
            return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            Aspirante aspirante1 = aspiranteService.findById(noExpediente);
            if(aspirante1 == null){
                response.put("Mensaje: ", "No Existe un Aspirente con el expediente ".concat(value.getNoExpediente()));
                return new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST);
            }
            CarreraTecnica carreraTecnica = carreraTecnicaService.findById(value.getCarreraTecnica().getCarreraId());
            if(carreraTecnica == null){
                response.put("Mensaje: ","No existe una carrera tecnica con el Codigo ".concat(value.getCarreraTecnica().getCarreraId()));
                return new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST);
            }else{
                value.setCarreraTecnica(carreraTecnica);
            }
            Jornada jornada = jornadaService.findById(value.getJornada().getJornadaId());
            if(jornada == null){
                response.put("Mensaje: ","No existe una jornada con el codigo".concat(value.getJornada().getJornadaId()));
                return new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST);
            }else{
                value.setJornada(jornada);
            }
            ExamenAdmision examenAdmision = examenAdmisionService.findById(value.getExamenAdmision().getExamenId());
            if(examenAdmision == null){
                response.put("Mensaje ", "No existe un esamen de admision con el codigo ".concat(value.getExamenAdmision().getExamenId()));
                return new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST);
            }else {
                value.setExamenAdmision(examenAdmision);
            }
            aspiranteService.save(value);
            response.put("Mensaje: ", "El aspirante fue actualizado con exito");
            response.put("Aspirante ", value);
            logger.info("El aspirante fue actualizado con exito");
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

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


    @DeleteMapping("/aspirante/{id}")
    public ResponseEntity<?> delete(@PathVariable String id){
        Map<String, Object> response = new HashMap<>();
        try{
            Aspirante aspirante=  aspiranteService.findById(id);
            if(aspirante == null){
                response.put("Mensaje:" , "El aspirante con el id ".concat(id).concat(" no existe"));
                logger.warn("No existen registros del aspirante");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            }else{
                aspiranteService.delete(aspirante);
                response.put("Mensaje: ", "Se elimino correctamente al aspirante");
                response.put("Aspirante ", aspirante);
                logger.info("Se elimino correctamente al aspirante");
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






