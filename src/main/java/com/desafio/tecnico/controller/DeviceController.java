package com.desafio.tecnico.controller;

import com.desafio.tecnico.dto.DeviceRequestDTO;
import com.desafio.tecnico.dto.DeviceResponseDTO;
import com.desafio.tecnico.model.Device.DeviceState;
import com.desafio.tecnico.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
@Tag(name = "Device Management")
public class DeviceController {

    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping
    @Operation(summary = "Criar um novo dispositivo")
    public ResponseEntity<DeviceResponseDTO> createDevice(@Valid @RequestBody DeviceRequestDTO requestDTO) {
        DeviceResponseDTO responseDTO = deviceService.createDevice(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar um dispositivo por ID")
    public ResponseEntity<DeviceResponseDTO> getDeviceById(@PathVariable Long id) {
        DeviceResponseDTO responseDTO = deviceService.getDeviceById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    @Operation(summary = "Listar todos os dispositivos")
    public ResponseEntity<List<DeviceResponseDTO>> getAllDevices() {
        List<DeviceResponseDTO> devices = deviceService.getAllDevices();
        return ResponseEntity.ok(devices);
    }

    @GetMapping("/brand/{brand}")
    @Operation(summary = "Buscar dispositivos por marca")
    public ResponseEntity<List<DeviceResponseDTO>> getDevicesByBrand(@PathVariable String brand) {
        List<DeviceResponseDTO> devices = deviceService.getDevicesByBrand(brand);
        return ResponseEntity.ok(devices);
    }

    @GetMapping("/state/{state}")
    @Operation(summary = "Buscar dispositivos por estado")
    public ResponseEntity<List<DeviceResponseDTO>> getDevicesByState(@PathVariable DeviceState state) {
        List<DeviceResponseDTO> devices = deviceService.getDevicesByState(state);
        return ResponseEntity.ok(devices);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um dispositivo completamente")
    public ResponseEntity<DeviceResponseDTO> updateDevice(
            @PathVariable Long id,
            @Valid @RequestBody DeviceRequestDTO requestDTO) {
        DeviceResponseDTO responseDTO = deviceService.updateDevice(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualizar um dispositivo parcialmente")
    public ResponseEntity<DeviceResponseDTO> partialUpdateDevice(
            @PathVariable Long id,
            @RequestBody DeviceRequestDTO requestDTO) {
        DeviceResponseDTO responseDTO = deviceService.partialUpdateDevice(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um dispositivo")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }
}
