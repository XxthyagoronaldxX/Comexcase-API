package com.startup.comexcase_api.api.controllers;

import com.startup.comexcase_api.api.events.OnRegistrationCompleteEvent;
import com.startup.comexcase_api.api.utils.CurrentUser;
import com.startup.comexcase_api.domain.dtos.dealer.CreateDealerDTO;
import com.startup.comexcase_api.domain.dtos.dealer.UpdateDealerDTO;
import com.startup.comexcase_api.domain.entities.DealerEntity;
import com.startup.comexcase_api.domain.services.IDealerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.context.ApplicationEventPublisher;
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
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping(value = "/api/dealers")
public class DealerController {
    final IDealerService dealerService;
    final ApplicationEventPublisher applicationEventPublisher;

    public DealerController(IDealerService dealerService, ApplicationEventPublisher applicationEventPublisher) {
        this.dealerService = dealerService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PreAuthorize("permitAll()")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DealerEntity> save(
            @RequestBody @Valid CreateDealerDTO createDealerDTO,
            HttpServletRequest request
    ) {
        DealerEntity dealerEntity = dealerService.save(createDealerDTO);

        String appUrl = request.getContextPath();
        applicationEventPublisher.publishEvent(new OnRegistrationCompleteEvent(
                dealerEntity,
                request.getLocale(),
                appUrl
        ));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(dealerEntity);
    }

    @PreAuthorize("hasRole('DEALER')")
    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DealerEntity> update(@RequestBody @Valid UpdateDealerDTO updateDealerDTO) {
        return ResponseEntity
                .ok(dealerService.update(updateDealerDTO, CurrentUser.getUsername()));
    }

    @PreAuthorize("hasRole('DEALER')")
    @GetMapping("/{dealerId}")
    public ResponseEntity<DealerEntity> findByDealerId(@PathVariable UUID dealerId) {
        return ResponseEntity
                .ok(dealerService.findByDealerId(dealerId));
    }

    @PreAuthorize("hasRole('DEALER')")
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete() {
        dealerService
                .remove(CurrentUser.getUsername());
    }

    @PreAuthorize("hasRole('DEALER')")
    @GetMapping
    public ResponseEntity<Page<DealerEntity>> findAll(@PageableDefault Pageable pageable) {
        return ResponseEntity
                .ok(dealerService.findAll(pageable));
    }
}
