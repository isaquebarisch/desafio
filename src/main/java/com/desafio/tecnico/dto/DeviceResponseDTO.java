package com.desafio.tecnico.dto;

import com.desafio.tecnico.model.Device.DeviceState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceResponseDTO {
    private Long id;
    private String name;
    private String brand;
    private DeviceState state;
    private LocalDateTime creationTime;
}
