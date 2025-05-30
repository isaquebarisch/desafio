package com.desafio.tecnico;

import org.springframework.boot.SpringApplication;

public class TestTecnicoApplication {

	public static void main(String[] args) {
		SpringApplication.from(Main::main).with(TestcontainersConfiguration.class).run(args);
	}

}
