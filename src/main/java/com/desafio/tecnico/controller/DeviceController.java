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

/**
 * Controlador REST responsável por expor os endpoints da API de Dispositivos.
 * Implementa operações CRUD e buscas especializadas seguindo o padrão RESTful.
 *
 * Possíveis melhorias:
 * - Implementar paginação para endpoints que retornam listas
 * - Adicionar filtros adicionais (por data de criação, nome, etc.)
 * - Implementar ordenação dos resultados
 * - Adicionar cache para operações de leitura frequentes
 *
 */
@RestController
@RequestMapping("/api/v1/devices")
@Tag(name = "Device Management")
public class DeviceController {

    private final DeviceService deviceService;

    @Autowired
    public DeviceController(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    /**
     * Cria um novo dispositivo no sistema.
     *
     * @param requestDTO DTO contendo os dados do dispositivo a ser criado
     * @return DTO com os dados do dispositivo criado, incluindo ID e data de criação
     */
    @PostMapping
    @Operation(summary = "Criar um novo dispositivo")
    public ResponseEntity<DeviceResponseDTO> createDevice(@Valid @RequestBody DeviceRequestDTO requestDTO) {
        DeviceResponseDTO responseDTO = deviceService.createDevice(requestDTO);
        return new ResponseEntity<>(responseDTO, HttpStatus.CREATED);
    }

    /**
     * Busca um dispositivo específico pelo seu ID.
     *
     * @param id ID do dispositivo a ser buscado
     * @return DTO com os dados do dispositivo encontrado
     * @throws DeviceNotFoundException se o dispositivo não for encontrado
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar um dispositivo por ID")
    public ResponseEntity<DeviceResponseDTO> getDeviceById(@PathVariable Long id) {
        DeviceResponseDTO responseDTO = deviceService.getDeviceById(id);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Lista todos os dispositivos cadastrados no sistema.
     *
     * Possível melhoria: Implementar paginação para evitar sobrecarga com grandes volumes de dados
     *
     * @return Lista de DTOs com os dados de todos os dispositivos
     */
    @GetMapping
    @Operation(summary = "Listar todos os dispositivos")
    public ResponseEntity<List<DeviceResponseDTO>> getAllDevices() {
        List<DeviceResponseDTO> devices = deviceService.getAllDevices();
        return ResponseEntity.ok(devices);
    }

    /**
     * Busca dispositivos por marca.
     *
     * @param brand Nome da marca para filtrar os dispositivos
     * @return Lista de DTOs com os dispositivos da marca especificada
     */
    @GetMapping("/brand/{brand}")
    @Operation(summary = "Buscar dispositivos por marca")
    public ResponseEntity<List<DeviceResponseDTO>> getDevicesByBrand(@PathVariable String brand) {
        List<DeviceResponseDTO> devices = deviceService.getDevicesByBrand(brand);
        return ResponseEntity.ok(devices);
    }

    /**
     * Busca dispositivos por estado (disponível, em uso, inativo).
     *
     * @param state Estado para filtrar os dispositivos
     * @return Lista de DTOs com os dispositivos no estado especificado
     */
    @GetMapping("/state/{state}")
    @Operation(summary = "Buscar dispositivos por estado")
    public ResponseEntity<List<DeviceResponseDTO>> getDevicesByState(@PathVariable DeviceState state) {
        List<DeviceResponseDTO> devices = deviceService.getDevicesByState(state);
        return ResponseEntity.ok(devices);
    }

    /**
     * Atualiza completamente um dispositivo existente.
     * Todas as propriedades são atualizadas com os novos valores fornecidos.
     *
     * @param id ID do dispositivo a ser atualizado
     * @param requestDTO DTO contendo os novos dados do dispositivo
     * @return DTO com os dados do dispositivo atualizado
     * @throws DeviceNotFoundException se o dispositivo não for encontrado
     * @throws InvalidOperationException se o dispositivo estiver em uso e houver tentativa de atualizar nome ou marca
     */
    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um dispositivo completamente")
    public ResponseEntity<DeviceResponseDTO> updateDevice(
            @PathVariable Long id,
            @Valid @RequestBody DeviceRequestDTO requestDTO) {
        DeviceResponseDTO responseDTO = deviceService.updateDevice(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Atualiza parcialmente um dispositivo existente.
     * Apenas as propriedades não nulas são atualizadas.
     *
     * @param id ID do dispositivo a ser atualizado
     * @param requestDTO DTO contendo os campos a serem atualizados
     * @return DTO com os dados do dispositivo atualizado
     * @throws DeviceNotFoundException se o dispositivo não for encontrado
     * @throws InvalidOperationException se o dispositivo estiver em uso e houver tentativa de atualizar nome ou marca
     */
    @PatchMapping("/{id}")
    @Operation(summary = "Atualizar um dispositivo parcialmente")
    public ResponseEntity<DeviceResponseDTO> partialUpdateDevice(
            @PathVariable Long id,
            @RequestBody DeviceRequestDTO requestDTO) {
        DeviceResponseDTO responseDTO = deviceService.partialUpdateDevice(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    /**
     * Remove um dispositivo do sistema.
     *
     * @param id ID do dispositivo a ser removido
     * @throws DeviceNotFoundException se o dispositivo não for encontrado
     * @throws InvalidOperationException se o dispositivo estiver em uso
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um dispositivo")
    public ResponseEntity<Void> deleteDevice(@PathVariable Long id) {
        deviceService.deleteDevice(id);
        return ResponseEntity.noContent().build();
    }
}
