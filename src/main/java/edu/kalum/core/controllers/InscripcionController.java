package edu.kalum.core.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/kalum-management/v1")
public class InscripcionController {

    @GetMapping("/inscripcion")
    public ResponseEntity<String> test(){
        return new ResponseEntity<String>("Testing", HttpStatus.OK);
    }


}
