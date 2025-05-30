package com.desafio.tecnico.controller;

import com.desafio.tecnico.dto.DeviceRequestDTO;
import com.desafio.tecnico.dto.DeviceResponseDTO;
import com.desafio.tecnico.model.Device.DeviceState;
import com.desafio.tecnico.service.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/devices")
@Tag(name = "Device Management", description = "Operações para gerenciamento de dispositivos")
public class DeviceController {

    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    @PostMapping
    @Operation(summary = "Criar um novo dispositivo", description = "Cria um novo dispositivo com os dados fornecidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Dispositivo criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = DeviceResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados de requisição inválidos")
    })
    public ResponseEntity<DeviceResponseDTO> createDevice(@Valid @RequestBody DeviceRequestDTO requestDTO) {
        DeviceResponseDTO responseDTO = deviceService.createDevice(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar um dispositivo por ID", description = "Retorna um dispositivo específico com base no ID fornecido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dispositivo encontrado"),
            @ApiResponse(responseCode = "404", description = "Dispositivo não encontrado")
    })
    public ResponseEntity<DeviceResponseDTO> getDeviceById(@PathVariable Long id) {
        DeviceResponseDTO responseDTO = deviceService.getDeviceById(id);
        return ResponseEntity.ok(responseDTO);
    }

    @GetMapping
    @Operation(summary = "Listar todos os dispositivos", description = "Retorna uma lista com todos os dispositivos cadastrados")
    public ResponseEntity<List<DeviceResponseDTO>> getAllDevices() {
        List<DeviceResponseDTO> devices = deviceService.getAllDevices();
        return ResponseEntity.ok(devices);
    }

    @GetMapping("/brand/{brand}")
    @Operation(summary = "Buscar dispositivos por marca", description = "Retorna uma lista de dispositivos filtrados pela marca especificada")
    public ResponseEntity<List<DeviceResponseDTO>> getDevicesByBrand(@PathVariable String brand) {
        List<DeviceResponseDTO> devices = deviceService.getDevicesByBrand(brand);
        return ResponseEntity.ok(devices);
    }

    @GetMapping("/state/{state}")
    @Operation(summary = "Buscar dispositivos por estado", description = "Retorna uma lista de dispositivos filtrados pelo estado especificado (AVAILABLE, IN_USE, INACTIVE)")
    public ResponseEntity<List<DeviceResponseDTO>> getDevicesByState(@PathVariable DeviceState state) {
        List<DeviceResponseDTO> devices = deviceService.getDevicesByState(state);
        return ResponseEntity.ok(devices);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um dispositivo completamente", description = "Atualiza completamente um dispositivo existente com os dados fornecidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dispositivo atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou operação não permitida"),
            @ApiResponse(responseCode = "404", description = "Dispositivo não encontrado")
    })
    public ResponseEntity<DeviceResponseDTO> updateDevice(
            @PathVariable Long id,
            @Valid @RequestBody DeviceRequestDTO requestDTO) {
        DeviceResponseDTO responseDTO = deviceService.updateDevice(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Atualizar um dispositivo parcialmente", description = "Atualiza parcialmente um dispositivo existente com os dados fornecidos")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dispositivo atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou operação não permitida"),
            @ApiResponse(responseCode = "404", description = "Dispositivo não encontrado")
    })
    public ResponseEntity<DeviceResponseDTO> partialUpdateDevice(
            @PathVariable Long id,
            @RequestBody DeviceRequestDTO requestDTO) {
        DeviceResponseDTO responseDTO = deviceService.partialUpdateDevice(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um dispositivo", description = "Remove um dispositivo existente com base no ID fornecido")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Dispositivo removido com sucesso"),
            @ApiResponse(responseCode = "400", description = "Operação não permitida"),
            @ApiResponse(responseCode = "404", description = "Dispositivo não encontrado")
    })
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }
}
