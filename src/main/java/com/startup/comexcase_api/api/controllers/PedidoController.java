package com.startup.comexcase_api.api.controllers;

import com.startup.comexcase_api.api.utils.CurrentUser;
import com.startup.comexcase_api.domain.dtos.PedidoDTO;
import com.startup.comexcase_api.domain.services.IPedidoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/pedidos")
public class PedidoController {
    private final IPedidoService pedidoService;

    public PedidoController(IPedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<?> findAll(@PageableDefault Pageable pageable) {
        return ResponseEntity
                .ok(pedidoService.findAll(pageable));
    }

    @PreAuthorize("hasRole('DEALER')")
    @GetMapping("/dealer")
    public ResponseEntity<?> findAllByDealer(@PageableDefault Pageable pageable) {
        return ResponseEntity
                .ok(pedidoService.findAllByDealer(CurrentUser.getUsername(), pageable));
    }

    @PreAuthorize("hasRole('PROVIDER')")
    @GetMapping("/provider")
    public ResponseEntity<?> findAllByProvider(@PageableDefault Pageable pageable) {
        return ResponseEntity
                .ok(pedidoService.findAllByProvider(CurrentUser.getUsername(), pageable));
    }

    @PreAuthorize("hasRole('DEALER')")
    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid PedidoDTO pedidoDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(pedidoService.save(pedidoDTO, CurrentUser.getUsername()));
    }

    @PreAuthorize("hasRole('PROVIDER')")
    @PatchMapping("/accept/{pedidoId}")
    public ResponseEntity<?> accept(@PathVariable UUID pedidoId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pedidoService.accept(pedidoId, CurrentUser.getUsername()));
    }

    @PreAuthorize("hasRole('PROVIDER')")
    @PatchMapping("/unaccept/{pedidoId}")
    public ResponseEntity<?> unaccept(@PathVariable UUID pedidoId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(pedidoService.unaccept(pedidoId, CurrentUser.getUsername()));
    }
}
