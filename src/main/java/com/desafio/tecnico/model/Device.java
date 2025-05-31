package com.desafio.tecnico.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidade Device representa um dispositivo no sistema.
 * Esta classe mapeia para a tabela "devices" no banco de dados.
 *
 * Testes existentes:
 * - Não há testes específicos para esta classe de modelo
 * - A funcionalidade de prePersist que define o creationTime não é testada diretamente
 * - O comportamento da entidade é testado indiretamente através dos testes de serviço e controlador
 *
 * Possíveis melhorias nos testes:
 * - Adicionar testes unitários para validar os métodos equals, hashCode e toString
 * - Testar o comportamento do método prePersist em um ambiente JPA simulado
 * - Verificar o comportamento da entidade com valores extremos ou nulos
 * - Adicionar testes para validar as constraints JPA (como @Column(nullable = false))
 *
 * Possíveis melhorias:
 * - Adicionar histórico de mudanças de estado
 * - Incluir campos adicionais como descrição, número de série, modelo, etc.
 * - Implementar auditoria com @CreatedBy, @LastModifiedBy, @CreatedDate, @LastModifiedDate
 * - Adicionar suporte a categorias/tipos de dispositivo
 */
@Entity
@Table(name = "devices")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String brand;

    /**
     * Estado atual do dispositivo (AVAILABLE, IN_USE, INACTIVE)
     * Esta propriedade é fundamental para as regras de negócio
     * relacionadas a atualizações e exclusões.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeviceState state;

    /**
     * Data e hora de criação do dispositivo.
     * Este campo é definido automaticamente pelo método prePersist
     * e não pode ser atualizado posteriormente.
     */
    @Column(name = "creation_time", nullable = false, updatable = false)
    private LocalDateTime creationTime;

    /**
     * Método invocado automaticamente antes da persistência inicial da entidade.
     * Define a data/hora de criação do dispositivo como o momento atual.
     */
    @PrePersist
    public void prePersist() {
        this.creationTime = LocalDateTime.now();
    }

    // Construtores
    public Device() {
    }

    public Device(Long id, String name, String brand, DeviceState state, LocalDateTime creationTime) {
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
        Device device = (Device) o;
        return Objects.equals(id, device.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Device{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", state=" + state +
                ", creationTime=" + creationTime +
                '}';
    }

    /**
     * Enum que representa os possíveis estados de um dispositivo.
     *
     * AVAILABLE - Dispositivo disponível para uso
     * IN_USE - Dispositivo atualmente em uso (possui restrições para atualização e exclusão)
     * INACTIVE - Dispositivo inativo/fora de funcionamento
     */
    public enum DeviceState {
        AVAILABLE,
        IN_USE,
        INACTIVE
    }
}
