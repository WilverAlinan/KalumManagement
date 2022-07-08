package edu.kalum.core.controllers;


import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import edu.kalum.core.model.dao.services.IAspiranteService;
import edu.kalum.core.model.dao.services.ICarreraTecnicaService;
import edu.kalum.core.model.dao.services.InscripcionService;
import edu.kalum.core.model.dtos.EnrollmentRequestDTO;
import edu.kalum.core.model.entities.Alumno;
import edu.kalum.core.model.entities.Aspirante;
import edu.kalum.core.model.entities.CarreraTecnica;
import edu.kalum.core.model.entities.Inscripcion;
import org.bouncycastle.jcajce.provider.asymmetric.ec.KeyFactorySpi;
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
import org.springframework.security.crypto.codec.Utf8;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/kalum-management/v1")
public class InscripcionController {

    private Logger logger = LoggerFactory.getLogger(InscripcionController.class);

    @Autowired
    private ICarreraTecnicaService carreraTecnicaService;

    @Autowired
    private InscripcionService inscripcionService;

    @Autowired
    private IAspiranteService aspiranteService;

    @Value("${edu.kalum.core.configuration.page.size}")
    private Integer index;

    @GetMapping("/inscripcion")
    public ResponseEntity<String> test() {
        return new ResponseEntity<String>("Testing", HttpStatus.OK);
    }

    @PostMapping("/inscripciones/enrollment")
    public ResponseEntity<?> enrollmentProcess(@Valid @RequestBody EnrollmentRequestDTO request, BindingResult result) {
        Map<String, Object> response = new HashMap<>();
        if (result.hasErrors()) {
            List<String> errores = result.getFieldErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.toList());
            response.put("Errores: ", errores);
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
        }
        try {
            Aspirante aspirante = aspiranteService.findById(request.getNoExpediente());
            if (aspirante == null) {
                logger.warn("No Existe el aspirante con el No. de Expediente ".concat(request.getNoExpediente()));
                response.put("Mensaje: ", "No Existe el aspirante con el No. de Expediente ".concat(request.getNoExpediente()));
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            }
            CarreraTecnica carreraTecnica = carreraTecnicaService.findById(request.getCarreraId());
            if (carreraTecnica == null) {
                logger.warn("No Existe la Carrera Tecnica con el Id: ".concat(request.getCarreraId()));
                response.put("Mensaje: ", "No Existe la Carrera Tecnica con el Id: ".concat(request.getCarreraId()));
            }

            boolean respuesta = crearSolicitudInscipcion(request);
            if (respuesta == true) {
                response.put("message:","La solicitud de inscripcion fue generada con exito");
                return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CREATED);
            } else {
                response.put("Mensaje: ", "Error al momento de crear la solicitud de inscripcion con el expediente".concat(request.getNoExpediente()));
                response.put("Error: ", "Error al momento de escribir a la cola");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);
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
        } catch (Exception e) {
            logger.error(e.getMessage());
            response.put("Mensaje", "Error interno del servicio inscripcion");
            response.put("Error", e.getMessage().concat(": ").concat(e.getMessage()));
            return new ResponseEntity<Map<String, Object>>(response, HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    private boolean crearSolicitudInscipcion(EnrollmentRequestDTO request) throws Exception {
        boolean respuesta = false;
        Connection conexion = null;
        Channel channel = null;
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        factory.setPort(5672);
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setVirtualHost("/");
        try {
            conexion = factory.newConnection();
            channel = conexion.createChannel();
            String message = new Gson().toJson(request);
            channel.basicPublish("edu.kalum.broker.exchange",
                    "",
                    null,
                    message.getBytes(StandardCharsets.UTF_8));
            respuesta = true;

        } catch (Exception e) {
            logger.error("Error al escribir en la cola " + e.getMessage());

        } finally {
            channel.close();
            conexion.close();
        }
        return respuesta;
    }

   @GetMapping("/inscripciones/{inscripcionId}")
    public ResponseEntity<?> showInscripcion (@PathVariable String inscripcionId){
        Map<String,Object> response = new HashMap<>();
        logger.debug("Iniciando proceso de busqueda de aspirante por Id");
        try {
            Inscripcion inscripcion = inscripcionService.findById(inscripcionId);
            if(inscripcion == null) {
                logger.warn("No existe Inscripcion con el id ".concat(inscripcionId));
                response.put("Error: " ,"No existe Inscripcion con el id ".concat(inscripcionId));
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }else {
                logger.info("Se realizo exitosamente la consulta de la inscripcion con el id ".concat(inscripcionId));
                response.put("Mensaje: ", "Se realizo exitosamente la consulta de la inscripcion con el id ".concat(inscripcionId));
                return new ResponseEntity<Inscripcion>(inscripcion,HttpStatus.OK);
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

    @GetMapping("/inscripciones")
    public ResponseEntity<?> listarInscripciones() {
        Map<String, Object> response = new HashMap<>();
        logger.info("Iniciando el proceso de listar Inscripciones");
        try {
            List<Inscripcion> inscripcion = inscripcionService.findAll();
            if (inscripcion == null && inscripcion.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                logger.info("Iniciando el proceso de listar Inscripciones");
                return new ResponseEntity<List<Inscripcion>>(inscripcion, HttpStatus.OK);
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

    @GetMapping("/inscripciones/page/{page}")
    public ResponseEntity<?> paginarInscripciones(@PathVariable Integer page){
        Map<String,Object> response= new HashMap<>();
        try{
            Pageable pageable = PageRequest.of(page,index);
            Page<Inscripcion> inscripcionPage = inscripcionService.findAll(pageable);
            if(inscripcionPage == null && inscripcionPage.getSize() == 0){
                logger.warn("No existen registros de carreras tecnicas");
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }else{
                logger.info("Se proceso la consulta de Carreras Tecnicas por pagina");
                return new ResponseEntity<Page<Inscripcion>>(inscripcionPage,HttpStatus.OK);
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

    @DeleteMapping("/inscripciones/{id}")
    public ResponseEntity<?> delete(@PathVariable String id){
        Map<String, Object> response = new HashMap<>();
        try{

             Inscripcion inscripcion = inscripcionService.findById(id);
            if(inscripcion == null){
                response.put("Mensaje:" , "La Inscripcion con el id ".concat(id).concat(" no existe"));
                logger.warn("No existen registros de la carrera");
                return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
            }else{
                inscripcionService.delete(inscripcion);
                response.put("Mensaje: ", "La Inscripcion fue eliminada exitosamente");
                response.put("Inscripcion Eliminada ", inscripcion);
                logger.info("La Inscripcion fue eliminada exitosamente");
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






