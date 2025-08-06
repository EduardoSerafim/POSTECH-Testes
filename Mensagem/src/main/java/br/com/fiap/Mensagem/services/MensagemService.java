package br.com.fiap.Mensagem.services;

import br.com.fiap.Mensagem.model.Mensagem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface MensagemService {
    Mensagem registrarMensagem(Mensagem mensagem);

    Mensagem obterMensagemPorId(UUID id);

    Mensagem atualizarMensagem(UUID id, Mensagem mensagemNova);

    boolean removerMensagem(UUID id);

    Page<Mensagem> obterMensagens(Pageable pageable);
}
