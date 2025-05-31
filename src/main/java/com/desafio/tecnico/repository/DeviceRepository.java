package com.desafio.tecnico.repository;

import com.desafio.tecnico.model.Device;
import com.desafio.tecnico.model.Device.DeviceState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório para acessar e manipular dados de dispositivos no banco de dados.
 * Estende JpaRepository para herdar métodos CRUD básicos e de paginação.
 *
 * Possíveis melhorias:
 * - Adicionar consultas personalizadas com @Query para buscas mais complexas
 * - Implementar consultas com critérios dinâmicos usando Specification
 * - Adicionar métodos para busca por múltiplos parâmetros
 * - Implementar métodos com suporte a paginação e ordenação
 */
@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    /**
     * Busca dispositivos por marca.
     * Spring Data JPA gera a implementação automaticamente com base no nome do método.
     *
     * @param brand Marca para filtrar dispositivos
     * @return Lista de dispositivos da marca especificada
     */
    List<Device> findByBrand(String brand);

    /**
     * Busca dispositivos por estado.
     * Spring Data JPA gera a implementação automaticamente com base no nome do método.
     *
     * @param state Estado para filtrar dispositivos (AVAILABLE, IN_USE, INACTIVE)
     * @return Lista de dispositivos no estado especificado
     */
    List<Device> findByState(DeviceState state);
}
