package com.desafio.tecnico.dto;

import com.desafio.tecnico.model.Device.DeviceState;

import java.time.LocalDateTime;
import java.util.Objects;

public class DeviceResponseDTO {
    private Long id;
    private String name;
    private String brand;
    private DeviceState state;
    private LocalDateTime creationTime;

    // Construtores
    public DeviceResponseDTO() {
    }

    public DeviceResponseDTO(Long id, String name, String brand, DeviceState state, LocalDateTime creationTime) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.state = state;
        this.creationTime = creationTime;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(LocalDateTime creationTime) {
        this.creationTime = creationTime;
    }

    // equals, hashCode e toString
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DeviceResponseDTO that = (DeviceResponseDTO) o;
        return Objects.equals(id, that.id) &&
               Objects.equals(name, that.name) &&
               Objects.equals(brand, that.brand) &&
               state == that.state &&
               Objects.equals(creationTime, that.creationTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, brand, state, creationTime);
    }

    @Override
    public String toString() {
        return "DeviceResponseDTO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", state=" + state +
                ", creationTime=" + creationTime +
                '}';
    }
}
