package com.steelroyal.ecommercebackend.security.api.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1", produces = "application/json")
@RequiredArgsConstructor
public class DemoController {

    @GetMapping(value = "/demo")
    public String getSuccessfully()
    {
        return "successfully GET";
    }
    @PostMapping(value = "/demo")
    public String postSuccessfully()
    {
        return "successfully POST";
    }
}

