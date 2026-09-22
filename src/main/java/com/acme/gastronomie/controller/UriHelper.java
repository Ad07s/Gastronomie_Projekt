package com.acme.gastronomie.controller;

import jakarta.servlet.http.HttpServletRequest;
import java.net.URI;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


@Component
public class UriHelper {
    UriHelper() {

    }

    public URI getBaseUri(final HttpServletRequest request) {
        return ServletUriComponentsBuilder.fromServletMapping(request)
                .build()
                .toUri();
    }
}
