package com.desafio.tecnico.service;

import com.desafio.tecnico.dto.DeviceRequestDTO;
import com.desafio.tecnico.dto.DeviceResponseDTO;
import com.desafio.tecnico.exception.DeviceNotFoundException;
import com.desafio.tecnico.exception.InvalidOperationException;
import com.desafio.tecnico.model.Device;
import com.desafio.tecnico.model.Device.DeviceState;
import com.desafio.tecnico.repository.DeviceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeviceServiceTest {

    @Mock
    private DeviceRepository deviceRepository;

    @InjectMocks
    private DeviceService deviceService;

    private Device device;
    private DeviceRequestDTO requestDTO;
    private LocalDateTime creationTime;

    @BeforeEach
    void setUp() {
        creationTime = LocalDateTime.now();

        device = new Device();
        device.setId(1L);
        device.setName("Smartphone");
        device.setBrand("Samsung");
        device.setState(DeviceState.AVAILABLE);
        device.setCreationTime(creationTime);

        requestDTO = new DeviceRequestDTO();
        requestDTO.setName("Smartphone");
        requestDTO.setBrand("Samsung");
        requestDTO.setState(DeviceState.AVAILABLE);
    }

    @Test
    void createDevice_ShouldReturnDeviceResponseDTO() {
        when(deviceRepository.save(any(Device.class))).thenReturn(device);

        DeviceResponseDTO responseDTO = deviceService.createDevice(requestDTO);

        assertNotNull(responseDTO);
        assertEquals(device.getId(), responseDTO.getId());
        assertEquals(device.getName(), responseDTO.getName());
        assertEquals(device.getBrand(), responseDTO.getBrand());
        assertEquals(device.getState(), responseDTO.getState());
        assertEquals(device.getCreationTime(), responseDTO.getCreationTime());

        verify(deviceRepository, times(1)).save(any(Device.class));
    }

    @Test
    void getDeviceById_ShouldReturnDeviceResponseDTO() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));

        DeviceResponseDTO responseDTO = deviceService.getDeviceById(1L);

        assertNotNull(responseDTO);
        assertEquals(device.getId(), responseDTO.getId());
        assertEquals(device.getName(), responseDTO.getName());

        verify(deviceRepository, times(1)).findById(1L);
    }

    @Test
    void getDeviceById_ShouldThrowDeviceNotFoundException() {
        when(deviceRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(DeviceNotFoundException.class, () -> {
            deviceService.getDeviceById(999L);
        });

        verify(deviceRepository, times(1)).findById(999L);
    }

    @Test
    void getAllDevices_ShouldReturnListOfDevices() {
        List<Device> devices = Arrays.asList(device);
        when(deviceRepository.findAll()).thenReturn(devices);

        List<DeviceResponseDTO> responseDTOs = deviceService.getAllDevices();

        assertNotNull(responseDTOs);
        assertEquals(1, responseDTOs.size());
        assertEquals(device.getId(), responseDTOs.get(0).getId());

        verify(deviceRepository, times(1)).findAll();
    }

    @Test
    void getDevicesByBrand_ShouldReturnListOfDevices() {
        List<Device> devices = Arrays.asList(device);
        when(deviceRepository.findByBrand("Samsung")).thenReturn(devices);

        List<DeviceResponseDTO> responseDTOs = deviceService.getDevicesByBrand("Samsung");

        assertNotNull(responseDTOs);
        assertEquals(1, responseDTOs.size());
        assertEquals("Samsung", responseDTOs.get(0).getBrand());

        verify(deviceRepository, times(1)).findByBrand("Samsung");
    }

    @Test
    void getDevicesByState_ShouldReturnListOfDevices() {
        List<Device> devices = Arrays.asList(device);
        when(deviceRepository.findByState(DeviceState.AVAILABLE)).thenReturn(devices);

        List<DeviceResponseDTO> responseDTOs = deviceService.getDevicesByState(DeviceState.AVAILABLE);

        assertNotNull(responseDTOs);
        assertEquals(1, responseDTOs.size());
        assertEquals(DeviceState.AVAILABLE, responseDTOs.get(0).getState());

        verify(deviceRepository, times(1)).findByState(DeviceState.AVAILABLE);
    }

    @Test
    void updateDevice_ShouldUpdateAndReturnDevice() {
        DeviceRequestDTO updateDTO = new DeviceRequestDTO();
        updateDTO.setName("Updated Smartphone");
        updateDTO.setBrand("Apple");
        updateDTO.setState(DeviceState.AVAILABLE);

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));
        when(deviceRepository.save(any(Device.class))).thenReturn(device);

        DeviceResponseDTO responseDTO = deviceService.updateDevice(1L, updateDTO);

        assertNotNull(responseDTO);
        verify(deviceRepository, times(1)).findById(1L);
        verify(deviceRepository, times(1)).save(any(Device.class));
    }

    @Test
    void updateDevice_ShouldThrowInvalidOperationException_WhenDeviceInUse() {
        device.setState(DeviceState.IN_USE);

        DeviceRequestDTO updateDTO = new DeviceRequestDTO();
        updateDTO.setName("Updated Name");
        updateDTO.setBrand("Updated Brand");
        updateDTO.setState(DeviceState.IN_USE);

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));

        assertThrows(InvalidOperationException.class, () -> {
            deviceService.updateDevice(1L, updateDTO);
        });

        verify(deviceRepository, times(1)).findById(1L);
        verify(deviceRepository, times(0)).save(any(Device.class));
    }

    @Test
    void partialUpdateDevice_ShouldUpdateOnlySomeFields() {
        DeviceRequestDTO partialUpdateDTO = new DeviceRequestDTO();
        partialUpdateDTO.setState(DeviceState.INACTIVE);
        // Nome e marca permanecem null

        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));
        when(deviceRepository.save(any(Device.class))).thenReturn(device);

        DeviceResponseDTO responseDTO = deviceService.partialUpdateDevice(1L, partialUpdateDTO);

        assertNotNull(responseDTO);
        // Estado deve ser atualizado, mas nome e marca devem permanecer os mesmos
        assertEquals(DeviceState.INACTIVE, responseDTO.getState());
        assertEquals("Smartphone", responseDTO.getName());
        assertEquals("Samsung", responseDTO.getBrand());

        verify(deviceRepository, times(1)).findById(1L);
        verify(deviceRepository, times(1)).save(any(Device.class));
    }

    @Test
    void deleteDevice_ShouldDeleteDevice() {
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));
        doNothing().when(deviceRepository).delete(device);

        deviceService.deleteDevice(1L);

        verify(deviceRepository, times(1)).findById(1L);
        verify(deviceRepository, times(1)).delete(device);
    }

    @Test
    void deleteDevice_ShouldThrowInvalidOperationException_WhenDeviceInUse() {
        device.setState(DeviceState.IN_USE);
        when(deviceRepository.findById(1L)).thenReturn(Optional.of(device));

        assertThrows(InvalidOperationException.class, () -> {
            deviceService.deleteDevice(1L);
        });

        verify(deviceRepository, times(1)).findById(1L);
        verify(deviceRepository, times(0)).delete(any());
    }
}
