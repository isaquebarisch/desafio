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
 * Testes existentes:
 * - createDevice_ShouldReturnDeviceResponseDTO: Verifica se um dispositivo é criado corretamente
 * - getDeviceById_ShouldReturnDeviceResponseDTO: Testa a busca de um dispositivo por ID
 * - getDeviceById_ShouldThrowDeviceNotFoundException: Verifica se a exceção é lançada quando o dispositivo não existe
 * - getAllDevices_ShouldReturnListOfDevices: Testa a listagem de todos os dispositivos
 * - getDevicesByBrand_ShouldReturnListOfDevices: Verifica a busca de dispositivos por marca
 * - getDevicesByState_ShouldReturnListOfDevices: Testa a busca de dispositivos por estado
 * - updateDevice_ShouldUpdateAndReturnDevice: Verifica a atualização completa de um dispositivo
 * - updateDevice_ShouldThrowInvalidOperationException_WhenDeviceInUse: Verifica se a exceção é lançada ao tentar atualizar nome/marca de dispositivo em uso
 * - partialUpdateDevice_ShouldUpdateOnlySomeFields: Testa a atualização parcial de um dispositivo
 * - deleteDevice_ShouldDeleteDevice: Verifica a exclusão de um dispositivo
 * - deleteDevice_ShouldThrowInvalidOperationException_WhenDeviceInUse: Testa se a exceção é lançada ao tentar excluir dispositivo em uso
 *
 * Possíveis melhorias nos testes:
 * - Adicionar testes para cenários de borda, como tentativas de atualizar o campo creationTime
 * - Implementar testes para verificar o comportamento quando são passados valores nulos
 * - Adicionar testes de performance para verificar o comportamento com grandes volumes de dados
 * - Considerar testes parametrizados para validar diferentes combinações de estados e operações
 * - Incorporar testes com boundary values para os campos de texto (nome e marca)
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
        Device device = deviceRepository.findById(id)
            .orElseThrow(() -> new DeviceNotFoundException("Dispositivo não encontrado com ID: " + id));
        return mapToResponseDTO(device);
    }

    /**
     * Lista todos os dispositivos cadastrados.
     *
     * @return Lista de DTOs com os dados de todos os dispositivos
     */
    @Transactional(readOnly = true)
    public List<DeviceResponseDTO> getAllDevices() {
        return deviceRepository.findAll().stream()
            .map(this::mapToResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Busca dispositivos pela marca.
     *
     * @param brand Marca dos dispositivos a serem buscados
     * @return Lista de DTOs com os dispositivos da marca especificada
     */
    @Transactional(readOnly = true)
    public List<DeviceResponseDTO> getDevicesByBrand(String brand) {
        return deviceRepository.findByBrandIgnoreCase(brand).stream()
            .map(this::mapToResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Busca dispositivos pelo estado.
     *
     * @param state Estado dos dispositivos a serem buscados
     * @return Lista de DTOs com os dispositivos no estado especificado
     */
    @Transactional(readOnly = true)
    public List<DeviceResponseDTO> getDevicesByState(DeviceState state) {
        return deviceRepository.findByState(state).stream()
            .map(this::mapToResponseDTO)
            .collect(Collectors.toList());
    }

    /**
     * Atualiza todos os campos de um dispositivo.
     *
     * Valida se o dispositivo está em uso antes de permitir a atualização de nome ou marca.
     *
     * @param id ID do dispositivo a ser atualizado
     * @param requestDTO DTO com os novos dados do dispositivo
     * @return DTO com os dados atualizados do dispositivo
     * @throws DeviceNotFoundException se o dispositivo não for encontrado
     * @throws InvalidOperationException se tentar modificar nome ou marca de um dispositivo em uso
     */
    @Transactional
    public DeviceResponseDTO updateDevice(Long id, DeviceRequestDTO requestDTO) {
        Device device = deviceRepository.findById(id)
            .orElseThrow(() -> new DeviceNotFoundException("Dispositivo não encontrado com ID: " + id));

        // Verifica se o dispositivo está em uso antes de atualizar nome ou marca
        if (device.getState() == DeviceState.IN_USE &&
            (!device.getName().equals(requestDTO.getName()) || !device.getBrand().equals(requestDTO.getBrand()))) {
            throw new InvalidOperationException("Não é permitido alterar nome ou marca de um dispositivo em uso");
        }

        device.setName(requestDTO.getName());
        device.setBrand(requestDTO.getBrand());
        device.setState(requestDTO.getState());

        Device updatedDevice = deviceRepository.save(device);
        return mapToResponseDTO(updatedDevice);
    }

    /**
     * Realiza uma atualização parcial de um dispositivo.
     *
     * Os campos nulos no DTO não serão atualizados.
     * Valida se o dispositivo está em uso antes de permitir a atualização de nome ou marca.
     *
     * @param id ID do dispositivo a ser atualizado
     * @param requestDTO DTO com os campos a serem atualizados
     * @return DTO com os dados atualizados do dispositivo
     * @throws DeviceNotFoundException se o dispositivo não for encontrado
     * @throws InvalidOperationException se tentar modificar nome ou marca de um dispositivo em uso
     */
    @Transactional
    public DeviceResponseDTO partialUpdateDevice(Long id, DeviceRequestDTO requestDTO) {
        Device device = deviceRepository.findById(id)
            .orElseThrow(() -> new DeviceNotFoundException("Dispositivo não encontrado com ID: " + id));

        // Verifica se o dispositivo está em uso e se está tentando atualizar nome ou marca
        if (device.getState() == DeviceState.IN_USE) {
            if (requestDTO.getName() != null && !device.getName().equals(requestDTO.getName())) {
                throw new InvalidOperationException("Não é permitido alterar o nome de um dispositivo em uso");
            }
            if (requestDTO.getBrand() != null && !device.getBrand().equals(requestDTO.getBrand())) {
                throw new InvalidOperationException("Não é permitido alterar a marca de um dispositivo em uso");
            }
        }

        // Atualiza apenas os campos não nulos
        if (requestDTO.getName() != null) {
            device.setName(requestDTO.getName());
        }
        if (requestDTO.getBrand() != null) {
            device.setBrand(requestDTO.getBrand());
        }
        if (requestDTO.getState() != null) {
            device.setState(requestDTO.getState());
        }

        Device updatedDevice = deviceRepository.save(device);
        return mapToResponseDTO(updatedDevice);
    }

    /**
     * Remove um dispositivo do sistema.
     *
     * Valida se o dispositivo não está em uso antes de permitir a exclusão.
     *
     * @param id ID do dispositivo a ser removido
     * @throws DeviceNotFoundException se o dispositivo não for encontrado
     * @throws InvalidOperationException se tentar excluir um dispositivo em uso
     */
    @Transactional
    public void deleteDevice(Long id) {
        Device device = deviceRepository.findById(id)
            .orElseThrow(() -> new DeviceNotFoundException("Dispositivo não encontrado com ID: " + id));

        // Verifica se o dispositivo está em uso antes de permitir a exclusão
        if (device.getState() == DeviceState.IN_USE) {
            throw new InvalidOperationException("Não é permitido excluir um dispositivo em uso");
        }

        deviceRepository.delete(device);
    }

    /**
     * Converte uma entidade Device para um DTO DeviceResponseDTO.
     *
     * @param device Entidade a ser convertida
     * @return DTO com os dados da entidade
     */
    private DeviceResponseDTO mapToResponseDTO(Device device) {
        return new DeviceResponseDTO(
            device.getId(),
            device.getName(),
            device.getBrand(),
            device.getState(),
            device.getCreationTime()
        );
    }
}
