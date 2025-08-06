package br.com.fiap.Mensagem.services;

import br.com.fiap.Mensagem.exceptions.MensagemNotFoundException;
import br.com.fiap.Mensagem.model.Mensagem;
import br.com.fiap.Mensagem.repositories.MensagemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MensagemServiceImp implements MensagemService {

    private final MensagemRepository mensagemRepository;

    @Override
    public Mensagem registrarMensagem(Mensagem mensagem) {
        mensagem.setId(UUID.randomUUID());
        return mensagemRepository.save(mensagem);
    }

    @Override
    public Mensagem obterMensagemPorId(UUID id) {
        return mensagemRepository.findById(id).
                orElseThrow(() -> new MensagemNotFoundException("mensagem não encontrada"));
    }

    @Override
    public Mensagem atualizarMensagem(UUID id, Mensagem mensagemNova) {
        var mensagem = obterMensagemPorId(id);
        if(!mensagem.getId().equals(mensagemNova.getId())){
            throw new MensagemNotFoundException("mensagem não apresenta o ID correto");
        }
        mensagem.setDataAlteracao(LocalDateTime.now());
        mensagem.setConteudo(mensagemNova.getConteudo());
        return mensagemRepository.save(mensagem);

    }

    @Override
    public boolean removerMensagem(UUID id) {
        var mensagem = obterMensagemPorId(id);
        mensagemRepository.delete(mensagem);
        return true;
    }


    @Override
    public Page<Mensagem> obterMensagens(Pageable pageable) {
        return mensagemRepository.obterMensagens(pageable);
    }


}
