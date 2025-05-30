package com.desafio.tecnico.dto;

import com.desafio.tecnico.model.Device.DeviceState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeviceRequestDTO {

    @NotBlank(message = "Nome do dispositivo não pode ser vazio")
    private String name;

    @NotBlank(message = "Marca do dispositivo não pode ser vazia")
    private String brand;

    @NotNull(message = "Estado do dispositivo é obrigatório")
    private DeviceState state;
}
