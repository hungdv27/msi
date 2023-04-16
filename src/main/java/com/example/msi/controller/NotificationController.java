package com.example.msi.controller;

import com.example.msi.models.notification.NotificationDTO;
import com.example.msi.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.NoSuchElementException;

import static com.example.msi.service.impl.CompanyServiceImpl.getPageable;

@RequiredArgsConstructor
@RequestMapping("api/notification")
@RestController
public class NotificationController {
  private final NotificationService service;

  @GetMapping
  public ResponseEntity<Page<NotificationDTO>> getAll(
      Principal principal,
      @RequestParam(defaultValue = "0") Integer page,
      @RequestParam(defaultValue = "5") Integer size
  ) {
    Pageable pageable = getPageable(page, size);
    Page<NotificationDTO> responseData = service.getAllNotifications(pageable, principal.getName()).map(NotificationDTO::getInstance);
    return ResponseEntity.ok(responseData);
  }

  @GetMapping("/{id}")
  public ResponseEntity<NotificationDTO> notificationById(@PathVariable Long id) throws NoSuchElementException {
    var response = NotificationDTO.getInstance(service.getById(id));
    return new ResponseEntity<>(response, HttpStatus.OK);
  }
}
