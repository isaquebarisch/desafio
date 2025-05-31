package com.desafio.tecnico.service;

import com.desafio.tecnico.dto.DeviceRequestDTO;
import com.desafio.tecnico.dto.DeviceResponseDTO;
import com.desafio.tecnico.exception.DeviceNotFoundException;
import com.desafio.tecnico.exception.InvalidOperationException;
import com.desafio.tecnico.model.Device;
import com.desafio.tecnico.model.Device.DeviceState;
import com.desafio.tecnico.repository.DeviceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço responsável pela lógica de negócios relacionada a dispositivos.
 * Implementa as regras de validação e operações CRUD.
 *
 * Possíveis melhorias:
 * - Implementar cache para operações de leitura frequentes
 * - Adicionar eventos para notificar alterações importantes (ex: quando um dispositivo fica indisponível)
 * - Adicionar logging detalhado para auditoria
 * - Adicionar suporte a operações em lote para melhor performance
 * - Implementar soft delete para manter histórico de dispositivos
 */
@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    /**
     * Cria um novo dispositivo no sistema.
     * A data de criação é definida automaticamente pelo método prePersist da entidade.
     *
     * @param requestDTO DTO contendo os dados do novo dispositivo
     * @return DTO com os dados do dispositivo criado, incluindo ID e data de criação
     */
    @Transactional
    public DeviceResponseDTO createDevice(DeviceRequestDTO requestDTO) {
        Device device = new Device();
        device.setName(requestDTO.getName());
        device.setBrand(requestDTO.getBrand());
        device.setState(requestDTO.getState());

        Device savedDevice = deviceRepository.save(device);
        return mapToResponseDTO(savedDevice);
    }

    /**
     * Busca um dispositivo pelo ID.
     *
     * @param id ID do dispositivo a ser buscado
     * @return DTO com os dados do dispositivo encontrado
     * @throws DeviceNotFoundException se o dispositivo não for encontrado
     */
    @Transactional(readOnly = true)
    public DeviceResponseDTO getDeviceById(Long id) {
