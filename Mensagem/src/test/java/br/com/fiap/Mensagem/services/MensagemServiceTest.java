package br.com.fiap.Mensagem.services;

import br.com.fiap.Mensagem.model.Mensagem;
import br.com.fiap.Mensagem.repositories.MensagemRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static br.com.fiap.Mensagem.helper.MensagemHelper.gerarMensagem;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MensagemServiceTest {

    @Mock
    private MensagemRepository mensagemRepository;

    private MensagemService mensagemService;
    AutoCloseable mock;

    @BeforeEach
    void setup(){
        mock = MockitoAnnotations.openMocks(this);
        mensagemService = new MensagemServiceImp(mensagemRepository);
    }

    @AfterEach
    void teardown() throws Exception{
        mock.close();
    }


    @Test
    void devePermitirRegistrarMensagem(){
        //arrange
        var mensagem = gerarMensagem();
        when(mensagemRepository.save(any(Mensagem.class))).thenAnswer(i -> i.getArguments()[0]);

        //act
        var mensagemRegistrada = mensagemService.registrarMensagem(mensagem);

        //assert
        assertThat(mensagemRegistrada)
                .isNotNull()
                .isInstanceOf(Mensagem.class);

        assertThat(mensagemRegistrada.getId())
                .isNotNull()
                .isEqualTo(mensagem.getId());

        assertThat(mensagemRegistrada.getUsuario())
                .isNotNull()
                .isEqualTo(mensagem.getUsuario());

        assertThat(mensagemRegistrada.getConteudo())
                .isNotNull()
                .isEqualTo(mensagem.getConteudo());

    }

    @Test
    void devePermitirObterMensagemPorId(){
        //arrange
        var id = UUID.randomUUID();
        var mensagem = gerarMensagem();
        mensagem.setId(id);

        when(mensagemRepository.findById(any(UUID.class))).thenReturn(Optional.of(mensagem));

        //act
        var mensagemObtida = mensagemService.obterMensagemPorId(id);

        //assert
        verify(mensagemRepository, times(1)).findById(id);
        assertThat(mensagemObtida)
                .isEqualTo(mensagem);
    }

    @Test
    void devePermitirObterMensagens(){
        Pageable pageable = PageRequest.of(0, 10);
        List<Mensagem> mensagens = List.of(
                gerarMensagem(),
                gerarMensagem()
        );
        Page<Mensagem> paginaMensagens = new PageImpl<>(mensagens, pageable, mensagens.size());


        when(mensagemRepository.obterMensagens(any(Pageable.class))).thenReturn(paginaMensagens);

        mensagemService.obterMensagens(pageable);

        assertThat(paginaMensagens)
                .isNotNull();
        assertThat(paginaMensagens.getContent())
                .hasSize(mensagens.size());
        assertThat(paginaMensagens.getTotalElements()).isEqualTo(mensagens.size());

        verify(mensagemRepository, times(1)).obterMensagens(any(Pageable.class));
    }

    @Test
    void devePermitirModificarMensagem(){
       var id = UUID.randomUUID();
       var mensagemAntiga = gerarMensagem();
       mensagemAntiga.setId(id);
       var mensagemNova = mensagemAntiga;
       mensagemNova.setConteudo("Abcd");

        when(mensagemRepository.findById(any(UUID.class))).thenReturn(Optional.of(mensagemAntiga));
        when(mensagemRepository.save(any(Mensagem.class))).thenAnswer(i -> i.getArguments()[0]);

        var mensagemObtida = mensagemService.atualizarMensagem(id, mensagemNova);

        assertThat(mensagemObtida).
                isInstanceOf(Mensagem.class)
                .isNotNull();
        assertThat(mensagemObtida.getId())
                .isEqualTo(mensagemAntiga.getId());
        assertThat(mensagemObtida.getUsuario())
                .isEqualTo(mensagemAntiga.getUsuario());
        assertThat(mensagemObtida.getConteudo())
                .isEqualTo(mensagemAntiga.getConteudo());
        verify(mensagemRepository, times(1)).findById(id);
    }

    @Test
    void devePermitirRemoverMensagem(){
        var id = UUID.randomUUID();
        var mensagem = gerarMensagem();
        mensagem.setId(id);


        when(mensagemRepository.findById(any(UUID.class))).thenReturn(Optional.of(mensagem));
        doNothing().when(mensagemRepository).delete(any(Mensagem.class));

        var mensagemRemovida = mensagemService.removerMensagem(id);

        assertThat(mensagemRemovida)
                .isTrue();

        verify(mensagemRepository, times(1)).delete(mensagem);
    }

}
