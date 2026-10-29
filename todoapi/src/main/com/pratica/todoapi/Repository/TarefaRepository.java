package com.pratica.todoapi.repository;

public interface TarefaRepository extends JpaRepository<Tarefa, Long> {
    List<Tarefa> findByConcluida(boolean concluida);
}
