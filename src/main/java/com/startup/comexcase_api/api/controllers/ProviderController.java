package com.startup.comexcase_api.api.controllers;

import com.startup.comexcase_api.api.utils.CurrentUser;
import com.startup.comexcase_api.domain.dtos.ProviderDTO;
import com.startup.comexcase_api.domain.entities.ProviderEntity;
import com.startup.comexcase_api.domain.services.IProviderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(value = "/api/providers")
public class ProviderController {
    private final IProviderService providerService;

    public ProviderController(IProviderService providerService) {
        this.providerService = providerService;
    }

    @PreAuthorize("hasRole('DEALER')")
    @GetMapping
    public ResponseEntity<Page<ProviderEntity>> findAll(@PageableDefault Pageable pageable) {
        return ResponseEntity
                .ok(providerService.findAll(pageable));
    }

    @PreAuthorize("hasRole('DEALER')")
    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid ProviderDTO providerDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(providerService.save(providerDTO, CurrentUser.getUsername()));
    }

    @PreAuthorize("hasRole('PROVIDER')")
    @PutMapping
    public ResponseEntity<?> update(@RequestBody @Valid ProviderDTO providerDTO) {
        return ResponseEntity
                .ok(providerService.update(providerDTO, CurrentUser.getUsername()));
    }
}
