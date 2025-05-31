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
 * Testes existentes:
 * - Não há testes diretos para o repositório, apenas testes indiretos via camada de serviço
 * - Os métodos findByBrand e findByState são testados através de DeviceServiceTest
 *
 * Possíveis melhorias nos testes:
 * - Implementar testes diretos com @DataJpaTest para validar consultas personalizadas
 * - Adicionar testes para verificar o comportamento com múltiplos registros no banco
 * - Testar a paginação e ordenação em conjuntos de dados grandes
 * - Verificar o comportamento de pesquisa com caracteres especiais ou case sensitivity
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
