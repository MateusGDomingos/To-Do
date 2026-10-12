package com.pratica.todoapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TodoapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodoapiApplication.class, args);


	}
	
Public class Tarefas {
		public String titulo;
		private int id;
		public String descricao;
		public boolean concluido = false;
		private enum prioridade {ALTA, MEDIA, BAIXA};
		public LocalDateTime dataCriacao =LocalDateTime.now();
		public LocalDateTime dataConclusao = null;
	}

}
