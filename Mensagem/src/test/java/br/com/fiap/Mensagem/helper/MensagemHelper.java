package br.com.fiap.Mensagem.helper;

import br.com.fiap.Mensagem.model.Mensagem;

public abstract class MensagemHelper {

    public static Mensagem gerarMensagem(){
        return Mensagem.builder()
                .usuario("João")
                .conteudo("Conteudo 1")
                .build();
    }

}
