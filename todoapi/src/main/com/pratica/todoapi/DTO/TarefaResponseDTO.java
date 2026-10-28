package com.pratica.todoapi.DTO;
import java.time.LocalDateTime;
public class TarefaResponseDTO {
    long id;
    String titulo;
    String descricao;
    boolean conclusao;
    enum Prioridade = prioridade;
    static LocalDateTime datacriacao = LocalDateTime.of(0, null, 0, 0, 0) // não sei se funciona, estou testando o datetime, preciso ver depois se é assim que usa, aqui prenche sozin
    LocalDateTime dataconclusao = LocalDateTime.of(null, null); // aqui precisa ficar nulo até a tarefa ser marcada como concluida
}
