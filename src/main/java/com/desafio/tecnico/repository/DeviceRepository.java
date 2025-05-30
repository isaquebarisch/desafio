package com.desafio.tecnico.repository;

import com.desafio.tecnico.model.Device;
import com.desafio.tecnico.model.Device.DeviceState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DeviceRepository extends JpaRepository<Device, Long> {

    List<Device> findByBrand(String brand);

    List<Device> findByState(DeviceState state);
}
