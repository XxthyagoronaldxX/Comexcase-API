package com.startup.comexcase_api.api.controllers;

import com.startup.comexcase_api.api.utils.CurrentUser;
import com.startup.comexcase_api.domain.dtos.ItemPedidoDTO;
import com.startup.comexcase_api.domain.entities.ItemPedidoEntity;
import com.startup.comexcase_api.domain.services.IItemPedidoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/items-pedido")
public class ItemPedidoController {
    private final IItemPedidoService itemPedidoService;

    public ItemPedidoController(IItemPedidoService itemPedidoService) {
        this.itemPedidoService = itemPedidoService;
    }

    @PreAuthorize("hasRole('DEALER')")
    @GetMapping("/pedido/{pedidoId}")
    public ResponseEntity<?> findAllByPedidoId(@PathVariable UUID pedidoId, @PageableDefault Pageable pageable) {
        return ResponseEntity
                .ok(itemPedidoService.findAllByPedidoId(pedidoId, CurrentUser.getUsername(), pageable));
    }

    @PreAuthorize("hasRole('DEALER')")
    @PostMapping
    public ResponseEntity<?> save(@RequestBody @Valid ItemPedidoDTO itemPedidoDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(itemPedidoService.save(itemPedidoDTO, CurrentUser.getUsername()));
    }
}
