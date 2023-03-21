package com.startup.comexcase_api.api.controllers;

import com.startup.comexcase_api.api.utils.CurrentUser;
import com.startup.comexcase_api.domain.dtos.ProductDTO;
import com.startup.comexcase_api.domain.entities.ProductEntity;
import com.startup.comexcase_api.domain.services.IProductService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final IProductService productService;

    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    @PreAuthorize("hasRole('DEALER')")
    @GetMapping
    public ResponseEntity<Page<ProductEntity>> findAll(@PageableDefault Pageable pageable) {
        return ResponseEntity
                .ok(productService.findAll(pageable));
    }

    @PreAuthorize("hasRole('DEALER')")
    @GetMapping("/provider/{providerId}")
    public ResponseEntity<Page<ProductEntity>> findAllByProviderId(@PathVariable UUID providerId, @PageableDefault Pageable pageable) {
        return ResponseEntity
                .ok(productService.findAllByProviderId(providerId, pageable));
    }

    @PreAuthorize("hasRole('PROVIDER')")
    @GetMapping("/provider")
    public ResponseEntity<Page<ProductEntity>> findAllByCurrentProvider(@PageableDefault Pageable pageable) {
        return ResponseEntity
                .ok(productService.findAllByProviderEmail(CurrentUser.getUsername(), pageable));
    }

    @PreAuthorize("hasRole('PROVIDER')")
    @PostMapping
    public ResponseEntity<ProductEntity> save(@RequestBody @Valid ProductDTO productDTO) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(productService.save(productDTO, CurrentUser.getUsername()));
    }

    @PreAuthorize("hasRole('PROVIDER')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductEntity> update(@RequestBody @Valid ProductDTO productDTO, @PathVariable UUID id) {
        return ResponseEntity
                .ok(productService.update(productDTO, id, CurrentUser.getUsername()));
    }

    @PreAuthorize("hasRole('PROVIDER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        productService
                .remove(id, CurrentUser.getUsername());

        return ResponseEntity
                .noContent()
                .build();
    }
}
