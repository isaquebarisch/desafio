package com.desafio.tecnico.dto;

import com.desafio.tecnico.model.Device.DeviceState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Objects;

/**
 * DTO (Data Transfer Object) para receber dados de requisições relacionadas a dispositivos.
 * Contém validações para garantir que dados obrigatórios sejam fornecidos.
 *
 * Possíveis melhorias:
 * - Adicionar validações mais específicas (ex: tamanho mínimo e máximo de strings)
 * - Implementar validações personalizadas (ex: validação de marcas permitidas)
 * - Adicionar grupos de validação para diferenciar validações entre criação e atualização
 */
public class DeviceRequestDTO {

    @NotBlank(message = "Nome do dispositivo não pode ser vazio")
    private String name;

    @NotBlank(message = "Marca do dispositivo não pode ser vazia")
    private String brand;

    @NotNull(message = "Estado do dispositivo é obrigatório")
    private DeviceState state;

    // Construtores
    public DeviceRequestDTO() {
    }

    public DeviceRequestDTO(String name, String brand, DeviceState state) {
        this.name = name;
        this.brand = brand;
        this.state = state;
    }

    // Getters e Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public DeviceState getState() {
        return state;
    }

    public void setState(DeviceState state) {
        this.state = state;
    }

    // equals, hashCode e toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceRequestDTO that = (DeviceRequestDTO) o;
        return Objects.equals(name, that.name) &&
               Objects.equals(brand, that.brand) &&
               state == that.state;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, brand, state);
    }

    @Override
    public String toString() {
        return "DeviceRequestDTO{" +
                "name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", state=" + state +
                '}';
    }
}
