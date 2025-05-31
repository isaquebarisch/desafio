package com.desafio.tecnico.config;

import com.desafio.tecnico.model.Device;
import com.desafio.tecnico.repository.DeviceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner initData(DeviceRepository deviceRepository) {
        return args -> {
            // Cria alguns dispositivos apenas se o banco estiver vazio
            if (deviceRepository.count() == 0) {
                Device device1 = new Device();
                device1.setName("Smartphone Samsung");
                device1.setBrand("Samsung");
                device1.setState(Device.DeviceState.AVAILABLE);
                deviceRepository.save(device1);

                Device device2 = new Device();
                device2.setName("Laptop Dell");
                device2.setBrand("Dell");
                device2.setState(Device.DeviceState.IN_USE);
                deviceRepository.save(device2);

                Device device3 = new Device();
                device3.setName("Tablet iPad");
                device3.setBrand("Apple");
                device3.setState(Device.DeviceState.INACTIVE);
                deviceRepository.save(device3);

                System.out.println("Dados iniciais criados com sucesso!");
            }
        };
    }
}
