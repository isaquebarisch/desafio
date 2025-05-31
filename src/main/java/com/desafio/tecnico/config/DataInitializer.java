package com.desafio.tecnico.config;

import com.desafio.tecnico.model.Device;
import com.desafio.tecnico.repository.DeviceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Arrays;
import java.util.List;

/**
 * Classe responsável por inicializar dados no banco de dados.
 * Utiliza o padrão de design Factory para criar instâncias de Device com dados pré-definidos.
 * Executa apenas quando a aplicação é iniciada e o banco de dados está vazio.
 *
 * Possíveis melhorias:
 * - Extrair os dados iniciais para um arquivo de configuração (JSON, YAML, etc.)
 * - Implementar mecanismo de versionamento dos dados (Flyway, Liquibase)
 * - Adicionar mais variedade de dados para testes mais abrangentes
 * - Criar seeds específicos para diferentes ambientes (dev, test, staging)
 */
@Configuration
public class DataInitializer {

    /**
     * Bean que inicializa o banco de dados com alguns dispositivos de exemplo.
     * Utiliza a classe Factory interna para criar instâncias de dispositivos.
     * A anotação @Profile("!test") garante que os dados não serão criados em ambiente de teste.
     *
     * @param deviceRepository Repositório para acesso aos dispositivos
     * @return CommandLineRunner que executa a inicialização
     */
    @Bean
    @Profile("!test") // Não executa no perfil de teste
    public CommandLineRunner initData(DeviceRepository deviceRepository) {
        return args -> {
            // Cria alguns dispositivos apenas se o banco estiver vazio
            if (deviceRepository.count() == 0) {
                List<Device> devices = Arrays.asList(
                    DeviceFactory.createSamsungSmartphone(),
                    DeviceFactory.createDellLaptop(),
                    DeviceFactory.createAppleTablet(),
                    DeviceFactory.createXiaomiSmartphone(),
                    DeviceFactory.createLenovoLaptop(),
                    DeviceFactory.createHPPrinter(),
                    DeviceFactory.createSonyCamera(),
                    DeviceFactory.createLogitechMouse()
                );

                deviceRepository.saveAll(devices);
                System.out.println("Banco de dados populado com " + devices.size() + " dispositivos!");
            } else {
                System.out.println("O banco de dados já contém dispositivos. Não foi necessário criar dados iniciais.");
            }
        };
    }

    /**
     * Factory para criação de dispositivos com dados pré-definidos.
     * Implementa o padrão de design Factory Method para encapsular a criação de objetos.
     *
     * Possíveis melhorias:
     * - Adicionar mais variedade de dispositivos
     * - Implementar mecanismo para gerar dados aleatórios
     * - Criar métodos para criar conjuntos específicos de dispositivos (ex: todos da mesma marca)
     */
    public static class DeviceFactory {

        /**
         * Método base para criação de dispositivos.
         * Centraliza a lógica de criação, facilitando mudanças futuras.
         *
         * @param name Nome do dispositivo
         * @param brand Marca do dispositivo
         * @param state Estado inicial do dispositivo
         * @return Objeto Device configurado, mas não persistido
         */
        public static Device createDevice(String name, String brand, Device.DeviceState state) {
            Device device = new Device();
            device.setName(name);
            device.setBrand(brand);
            device.setState(state);
            // O creationTime será definido automaticamente pelo método prePersist
            return device;
        }

        // Métodos específicos para criar dispositivos pré-definidos

        public static Device createSamsungSmartphone() {
            return createDevice("Galaxy S23", "Samsung", Device.DeviceState.AVAILABLE);
        }

        public static Device createDellLaptop() {
            return createDevice("XPS 13", "Dell", Device.DeviceState.IN_USE);
        }

        public static Device createAppleTablet() {
            return createDevice("iPad Pro", "Apple", Device.DeviceState.INACTIVE);
        }

        public static Device createXiaomiSmartphone() {
            return createDevice("Mi 12", "Xiaomi", Device.DeviceState.AVAILABLE);
        }

        public static Device createLenovoLaptop() {
            return createDevice("ThinkPad X1", "Lenovo", Device.DeviceState.IN_USE);
        }

        public static Device createHPPrinter() {
            return createDevice("LaserJet Pro", "HP", Device.DeviceState.AVAILABLE);
        }

        public static Device createSonyCamera() {
            return createDevice("Alpha A7 III", "Sony", Device.DeviceState.INACTIVE);
        }

        public static Device createLogitechMouse() {
            return createDevice("MX Master 3", "Logitech", Device.DeviceState.AVAILABLE);
        }
    }
}
