package br.com.fiap.Mensagem.repositories;

import br.com.fiap.Mensagem.model.Mensagem;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class MensagemRepositoryTest {

    @Mock
    private MensagemRepository mensagemRepository;

    AutoCloseable mock;

    @BeforeEach
    void setup(){
        mock = MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void teardown() throws Exception{
        mock.close();
    }

    @Test
    void devePermitirRegistrarMensagem() {
        //Arrange - preparar
        var mensagem = gerarMensagem();
        when(mensagemRepository.save(any(Mensagem.class))).thenReturn(mensagem);

        //Act - atuar
        var mensagemArmazenada = mensagemRepository.save(mensagem);

        //Assert - Validar
        verify(mensagemRepository, times(1)).save(mensagem);
    }


    @Test
    void devePermitirConsultarMensagem() {
        var id = UUID.randomUUID();
        var mensagem = gerarMensagem();
        mensagem.setId(id);

        when(mensagemRepository.findById(any(UUID.class))).thenReturn((Optional.of(mensagem)));

        //act
        var mensagemEncontrada = mensagemRepository.findById(id);

        assertThat(mensagemEncontrada)
                .isNotNull()
                .containsSame(mensagem);
    }

    @Test
    void devePermitirApagarMensagem() {
        var id = UUID.randomUUID();
        var mensagem = gerarMensagem();
        mensagem.setId(id);

        doNothing().when(mensagemRepository).deleteById(any(UUID.class));


        //act
        mensagemRepository.deleteById(id);

        verify(mensagemRepository, times(1)).deleteById(id);

    }

    @Test
    void devePermitirConsultarMensagns() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Mensagem> mensagens = List.of(
                gerarMensagem(),
                gerarMensagem()
        );
        Page<Mensagem> paginaMensagens = new PageImpl<>(mensagens, pageable, mensagens.size());


        when(mensagemRepository.obterMensagens(any(Pageable.class))).thenReturn(paginaMensagens);

        mensagemRepository.obterMensagens(pageable);

        assertThat(paginaMensagens)
                .isNotNull();
        assertThat(paginaMensagens.getContent())
                .hasSize(mensagens.size());
        assertThat(paginaMensagens.getTotalElements()).isEqualTo(mensagens.size());

        verify(mensagemRepository, times(1)).obterMensagens(any(Pageable.class));

    }






}
