package com.startup.comexcase_api.api.controllers;

import com.startup.comexcase_api.api.utils.CurrentUser;
import com.startup.comexcase_api.domain.dtos.AddressDTO;
import com.startup.comexcase_api.domain.entities.AddressEntity;
import com.startup.comexcase_api.domain.services.IAddressService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping(value = "/api/addresses")
public class AddressController {
    private final IAddressService addressService;

    public AddressController(IAddressService addressService) {
        this.addressService = addressService;
    }

    @PreAuthorize("hasRole('DEALER')")
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<AddressEntity> save(@RequestBody @Valid AddressDTO addressDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(addressService.save(addressDTO, CurrentUser.getUsername()));
    }

    @PreAuthorize("hasRole('DEALER')")
    @GetMapping
    public ResponseEntity<Iterable<AddressEntity>> findAll(@PageableDefault Pageable pageable) {
        return ResponseEntity
                .ok(addressService.findAll(pageable));
    }

    @PreAuthorize("hasRole('DEALER')")
    @GetMapping("/{addressId}")
    public ResponseEntity<Optional<AddressEntity>> findById(@PathVariable UUID addressId) {
        return ResponseEntity
                .ok(addressService.findById(addressId));
    }

    @PreAuthorize("hasRole('DEALER')")
    @GetMapping("/dealer")
    public ResponseEntity<Iterable<AddressEntity>> findAllByDealerPhoneNumber() {
        return ResponseEntity
                .ok(addressService.findAllByDealerEmail(CurrentUser.getUsername()));
    }

    @PreAuthorize("hasRole('DEALER')")
    @GetMapping("/dealer/{dealerId}")
    public ResponseEntity<Iterable<AddressEntity>> findAllByDealerId(@PathVariable UUID dealerId) {
        return ResponseEntity
                .ok(addressService.findAllByDealerId(dealerId));
    }

    @PreAuthorize("hasRole('DEALER')")
    @PutMapping(value = "/{addressId}", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<AddressEntity> update(@RequestBody @Valid AddressDTO addressDTO, @PathVariable UUID addressId) {
        return ResponseEntity
                .ok(addressService.update(addressDTO, addressId, CurrentUser.getUsername()));
    }

    @PreAuthorize("hasRole('DEALER')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID id) {
        addressService
                .remove(id, CurrentUser.getUsername());
    }
}
