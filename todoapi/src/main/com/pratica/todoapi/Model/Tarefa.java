import java.time.LocalDateTime;
package main.com.pratica.todoapi.Modelcom.pratica.todoapi.Model;
public class Tarefa  {
    private int id;
    public String titulo;
    public String Descricao;
    public boolean conclusão = false;
    //aqui vou usar o enum nivel da classe prioridade que esta no arquivo prioridade tambem model. Preciso descobrir como implementar
    static LocalDateTime datacriacao = LocalDateTime.of(0, null, 0, 0, 0) // não sei se funciona, estou testando o datetime, preciso ver depois se é assim que usa, aqui prenche sozin
    LocalDateTime dataconclusao = LocalDateTime.of(null, null); // aqui precisa ficar nulo até a tarefa ser marcada como concluida


}
