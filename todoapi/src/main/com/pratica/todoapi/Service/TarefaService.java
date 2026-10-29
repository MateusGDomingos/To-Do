package main.com.pratica.todoapi.Service;

public interface TarefaService {

    TarefaResponseDTO criar(TarefaRequestDTO dto);
    List<TarefaResponseDTO> listarTodas(Boolean concluida); // concluida pode ser null (sem filtro)
    TarefaResponseDTO buscarPorId(Long id);
    TarefaResponseDTO atualizar(Long id, TarefaRequestDTO dto);
    TarefaResponseDTO marcarComoConcluida(Long id);
    void deletar(Long id);

}