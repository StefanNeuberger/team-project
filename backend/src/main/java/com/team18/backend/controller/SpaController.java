package com.team18.backend.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller // Note: Use @Controller, not @RestController for view forwarding
public class SpaController {

    // Regex explanation:
    // Match everything that does NOT start with /api (so we don't break backend endpoints)
    // AND does not look like a static file (has a dot extension)
    @RequestMapping(value = {
        "/{path:[^\\.]*}",       // Matches /dashboard
        "/**/{path:[^\\.]*}"     // Matches /users/123/edit
    })
    public String forward() {
        // Forward to the static index.html
        // Spring Boot usually maps "static/index.html" to root "/" internally
        return "forward:/index.html";
    }
}