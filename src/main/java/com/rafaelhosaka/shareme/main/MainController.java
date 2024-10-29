package com.rafaelhosaka.shareme.main;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class MainController {
    @Value("${build.version}")
    private String buildVersion;

    @GetMapping
    public String getVersion(){
        return "Rafael Hideki Hosaka © 2024 ShareMe "+buildVersion;
    }
}
