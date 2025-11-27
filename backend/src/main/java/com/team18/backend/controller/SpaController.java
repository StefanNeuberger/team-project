package com.team18.backend.controller;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SpaController {

    /**
     * Catch-all controller for SPA routing.
     * Serves index.html for all routes that don't match API endpoints or static files.
     * This allows React Router to handle client-side routing.
     */
    @RequestMapping(value = {"/", "/{path:[^\\.]*}", "/{path:[^\\.]*}/**"})
    public ResponseEntity<Resource> index() {
        Resource resource = new ClassPathResource("static/index.html");
        if (!resource.exists()) {
            return ResponseEntity.notFound().build();
        }
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.TEXT_HTML);
        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}

