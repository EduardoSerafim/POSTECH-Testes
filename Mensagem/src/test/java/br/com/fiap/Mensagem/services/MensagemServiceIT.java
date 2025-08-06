package br.com.fiap.Mensagem.services;

import br.com.fiap.Mensagem.helper.MensagemHelper;
import br.com.fiap.Mensagem.model.Mensagem;
import br.com.fiap.Mensagem.repositories.MensagemRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureTestDatabase
@Transactional
        @Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
        @Sql(scripts = {"/db_load.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class MensagemServiceIT {

    @Autowired
    private MensagemRepository mensagemRepository;
    @Autowired
    private MensagemService mensagemService;

    @Test
    void devePermitirRegistrarMensagem(){
        //Arrange
        var mensagem = MensagemHelper.gerarMensagem();

        //Act
        var mensagemRegistrada = mensagemService.registrarMensagem(mensagem);

        //Assert
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
    void devePermitirObterMensagem(){
        UUID id = UUID.fromString("170c04d3-d0e5-4b03-915c-7b914a45f8f8");

        var mensagemObtida = mensagemService.obterMensagemPorId(id);


        //Assert
        assertThat(mensagemObtida)
                .isNotNull()
                .isInstanceOf(Mensagem.class);

        assertThat(mensagemObtida.getId())
                .isNotNull();

        assertThat(mensagemObtida.getUsuario())
                .isNotNull();

        assertThat(mensagemObtida.getConteudo())
                .isNotNull();
    }


    @Test
    void devePermitirRemoverMensagem(){
        UUID id = UUID.fromString("170c04d3-d0e5-4b03-915c-7b914a45f8f8");

        var mensagemRemvoida = mensagemService.removerMensagem(id);


        //Assert
        assertThat(mensagemRemvoida).isTrue();
    }
}
