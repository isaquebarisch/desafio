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

@Service
public class DeviceService {

    private final DeviceRepository deviceRepository;

    @Autowired
    public DeviceService(DeviceRepository deviceRepository) {
        this.deviceRepository = deviceRepository;
    }

    @Transactional
    public DeviceResponseDTO createDevice(DeviceRequestDTO requestDTO) {
        Device device = new Device();
        device.setName(requestDTO.getName());
        device.setBrand(requestDTO.getBrand());
        device.setState(requestDTO.getState());

        Device savedDevice = deviceRepository.save(device);
        return mapToResponseDTO(savedDevice);
    }

    @Transactional(readOnly = true)
    public DeviceResponseDTO getDeviceById(Long id) {
        Device device = findDeviceOrThrow(id);
        return mapToResponseDTO(device);
    }

    @Transactional(readOnly = true)
    public List<DeviceResponseDTO> getAllDevices() {
        return deviceRepository.findAll().stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DeviceResponseDTO> getDevicesByBrand(String brand) {
        return deviceRepository.findByBrand(brand).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<DeviceResponseDTO> getDevicesByState(DeviceState state) {
        return deviceRepository.findByState(state).stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public DeviceResponseDTO updateDevice(Long id, DeviceRequestDTO requestDTO) {
        Device device = findDeviceOrThrow(id);

        // Validação: Devices em uso não podem ter nome e marca atualizados
        if (device.getState() == DeviceState.IN_USE &&
            (!device.getName().equals(requestDTO.getName()) ||
             !device.getBrand().equals(requestDTO.getBrand()))) {
            throw new InvalidOperationException("Nome e marca não podem ser atualizados quando o dispositivo está em uso");
        }

        // Atualiza os campos
        device.setName(requestDTO.getName());
        device.setBrand(requestDTO.getBrand());
        device.setState(requestDTO.getState());

        Device updatedDevice = deviceRepository.save(device);
        return mapToResponseDTO(updatedDevice);
    }

    @Transactional
    public DeviceResponseDTO partialUpdateDevice(Long id, DeviceRequestDTO requestDTO) {
        Device device = findDeviceOrThrow(id);

        // Atualiza os campos fornecidos, mantendo os valores existentes para campos nulos
        if (requestDTO.getName() != null) {
            // Validação: Devices em uso não podem ter nome atualizado
            if (device.getState() == DeviceState.IN_USE && !device.getName().equals(requestDTO.getName())) {
                throw new InvalidOperationException("Nome não pode ser atualizado quando o dispositivo está em uso");
            }
            device.setName(requestDTO.getName());
        }

        if (requestDTO.getBrand() != null) {
            // Validação: Devices em uso não podem ter marca atualizada
            if (device.getState() == DeviceState.IN_USE && !device.getBrand().equals(requestDTO.getBrand())) {
                throw new InvalidOperationException("Marca não pode ser atualizada quando o dispositivo está em uso");
            }
            device.setBrand(requestDTO.getBrand());
        }

        if (requestDTO.getState() != null) {
            device.setState(requestDTO.getState());
        }

        Device updatedDevice = deviceRepository.save(device);
        return mapToResponseDTO(updatedDevice);
    }

    @Transactional
    public void deleteDevice(Long id) {
        Device device = findDeviceOrThrow(id);

        // Validação: Devices em uso não podem ser deletados
        if (device.getState() == DeviceState.IN_USE) {
            throw new InvalidOperationException("Dispositivos em uso não podem ser removidos");
        }

        deviceRepository.delete(device);
    }

    private Device findDeviceOrThrow(Long id) {
        return deviceRepository.findById(id)
                .orElseThrow(() -> new DeviceNotFoundException("Dispositivo não encontrado com ID: " + id));
    }

    private DeviceResponseDTO mapToResponseDTO(Device device) {
        DeviceResponseDTO responseDTO = new DeviceResponseDTO();
        responseDTO.setId(device.getId());
        responseDTO.setName(device.getName());
        responseDTO.setBrand(device.getBrand());
        responseDTO.setState(device.getState());
        responseDTO.setCreationTime(device.getCreationTime());
        return responseDTO;
    }
}
