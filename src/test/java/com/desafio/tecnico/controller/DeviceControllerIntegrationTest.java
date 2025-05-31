package com.desafio.tecnico.controller;

import com.desafio.tecnico.dto.DeviceRequestDTO;
import com.desafio.tecnico.model.Device.DeviceState;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class DeviceControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testCreateDevice() throws Exception {
        DeviceRequestDTO requestDTO = new DeviceRequestDTO();
        requestDTO.setName("Laptop");
        requestDTO.setBrand("Dell");
        requestDTO.setState(DeviceState.AVAILABLE);

        mockMvc.perform(post("/api/devices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.name", is("Laptop")))
                .andExpect(jsonPath("$.brand", is("Dell")))
                .andExpect(jsonPath("$.state", is("AVAILABLE")))
                .andExpect(jsonPath("$.creationTime", notNullValue()));
    }

    @Test
    public void testGetAllDevices() throws Exception {
        // Primeiro, crie um dispositivo para garantir que há algo para buscar
        DeviceRequestDTO requestDTO = new DeviceRequestDTO();
        requestDTO.setName("Tablet");
        requestDTO.setBrand("Apple");
        requestDTO.setState(DeviceState.AVAILABLE);

        mockMvc.perform(post("/api/devices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated());

        // Agora busque todos os dispositivos
        mockMvc.perform(get("/api/devices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(emptyArray())))
                .andExpect(jsonPath("$[*].name", hasItem("Tablet")));
    }

    @Test
    public void testGetDeviceById() throws Exception {
        // Primeiro crie um dispositivo
        DeviceRequestDTO requestDTO = new DeviceRequestDTO();
        requestDTO.setName("Monitor");
        requestDTO.setBrand("Samsung");
        requestDTO.setState(DeviceState.AVAILABLE);

        String response = mockMvc.perform(post("/api/devices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        // Extraia o ID do dispositivo criado
        Integer deviceId = objectMapper.readTree(response).get("id").asInt();

        // Busque o dispositivo pelo ID
        mockMvc.perform(get("/api/devices/{id}", deviceId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(deviceId)))
                .andExpect(jsonPath("$.name", is("Monitor")));
    }

    @Test
    public void testUpdateDevice() throws Exception {
        // Crie um dispositivo
        DeviceRequestDTO createDTO = new DeviceRequestDTO();
        createDTO.setName("Keyboard");
        createDTO.setBrand("Logitech");
        createDTO.setState(DeviceState.AVAILABLE);

        String response = mockMvc.perform(post("/api/devices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Integer deviceId = objectMapper.readTree(response).get("id").asInt();

        // Atualize o dispositivo
        DeviceRequestDTO updateDTO = new DeviceRequestDTO();
        updateDTO.setName("Updated Keyboard");
        updateDTO.setBrand("Microsoft");
        updateDTO.setState(DeviceState.IN_USE);

        mockMvc.perform(put("/api/devices/{id}", deviceId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Updated Keyboard")))
                .andExpect(jsonPath("$.brand", is("Microsoft")))
                .andExpect(jsonPath("$.state", is("IN_USE")));
    }

    @Test
    public void testPartialUpdateDevice() throws Exception {
        // Crie um dispositivo
        DeviceRequestDTO createDTO = new DeviceRequestDTO();
        createDTO.setName("Mouse");
        createDTO.setBrand("Logitech");
        createDTO.setState(DeviceState.AVAILABLE);

        String response = mockMvc.perform(post("/api/devices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Integer deviceId = objectMapper.readTree(response).get("id").asInt();

        // Faça uma atualização parcial do dispositivo
        DeviceRequestDTO partialUpdateDTO = new DeviceRequestDTO();
        partialUpdateDTO.setState(DeviceState.INACTIVE);
        // Nome e marca não são alterados

        mockMvc.perform(patch("/api/devices/{id}", deviceId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(partialUpdateDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Mouse"))) // Nome deve permanecer o mesmo
                .andExpect(jsonPath("$.brand", is("Logitech"))) // Marca deve permanecer a mesma
                .andExpect(jsonPath("$.state", is("INACTIVE"))); // Estado deve ser atualizado
    }

    @Test
    public void testDeleteDevice() throws Exception {
        // Crie um dispositivo
        DeviceRequestDTO createDTO = new DeviceRequestDTO();
        createDTO.setName("Webcam");
        createDTO.setBrand("Logitech");
        createDTO.setState(DeviceState.AVAILABLE);

        String response = mockMvc.perform(post("/api/devices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Integer deviceId = objectMapper.readTree(response).get("id").asInt();

        // Exclua o dispositivo
        mockMvc.perform(delete("/api/devices/{id}", deviceId))
                .andExpect(status().isNoContent());

        // Verifique se o dispositivo foi realmente excluído
        mockMvc.perform(get("/api/devices/{id}", deviceId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCannotDeleteDeviceInUse() throws Exception {
        // Crie um dispositivo
        DeviceRequestDTO createDTO = new DeviceRequestDTO();
        createDTO.setName("Printer");
        createDTO.setBrand("HP");
        createDTO.setState(DeviceState.AVAILABLE);

        String response = mockMvc.perform(post("/api/devices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDTO)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Integer deviceId = objectMapper.readTree(response).get("id").asInt();

        // Atualize o estado para IN_USE
        DeviceRequestDTO updateDTO = new DeviceRequestDTO();
        updateDTO.setName("Printer");
        updateDTO.setBrand("HP");
        updateDTO.setState(DeviceState.IN_USE);

        mockMvc.perform(put("/api/devices/{id}", deviceId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDTO)))
                .andExpect(status().isOk());

        // Tente excluir o dispositivo em uso (deve falhar)
        mockMvc.perform(delete("/api/devices/{id}", deviceId))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testGetDevicesByBrand() throws Exception {
        // Crie um dispositivo com marca específica
        DeviceRequestDTO requestDTO = new DeviceRequestDTO();
        requestDTO.setName("Smartphone");
        requestDTO.setBrand("Samsung");
        requestDTO.setState(DeviceState.AVAILABLE);

        mockMvc.perform(post("/api/devices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated());

        // Busque dispositivos pela marca
        mockMvc.perform(get("/api/devices/brand/{brand}", "Samsung"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(emptyArray())))
                .andExpect(jsonPath("$[*].brand", everyItem(is("Samsung"))));
    }

    @Test
    public void testGetDevicesByState() throws Exception {
        // Crie um dispositivo com estado específico
        DeviceRequestDTO requestDTO = new DeviceRequestDTO();
        requestDTO.setName("Camera");
        requestDTO.setBrand("Canon");
        requestDTO.setState(DeviceState.INACTIVE);

        mockMvc.perform(post("/api/devices")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated());

        // Busque dispositivos pelo estado
        mockMvc.perform(get("/api/devices/state/{state}", "INACTIVE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(emptyArray())))
                .andExpect(jsonPath("$[*].state", everyItem(is("INACTIVE"))));
    }
}
