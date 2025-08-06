package br.com.fiap.Mensagem.controller;

import br.com.fiap.Mensagem.controllers.MensagemController;
import br.com.fiap.Mensagem.exceptions.MensagemNotFoundException;
import br.com.fiap.Mensagem.handler.GlobalExceptionHandler;
import br.com.fiap.Mensagem.helper.MensagemHelper;
import br.com.fiap.Mensagem.model.Mensagem;
import br.com.fiap.Mensagem.services.MensagemService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MensagemControllerTest {

    private MockMvc mockMvc;

    @Mock
    private MensagemService mensagemService;
    AutoCloseable mock;

    @BeforeEach
    void setup(){
        mock = MockitoAnnotations.openMocks(this);
        MensagemController mensagemController = new MensagemController(mensagemService);
        mockMvc = MockMvcBuilders.standaloneSetup(mensagemController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .addFilter((request, response, chain) -> {
                    response.setCharacterEncoding("UTF-8");
                    chain.doFilter(request, response);
                }, "/*")
                .build();
    }

    @AfterEach
    void teardown() throws Exception{
        mock.close();
    }


    @Test
    void devePertimirRegistrarMensagem() throws Exception {
        //Arrange
        var mensagemRequest = MensagemHelper.gerarMensagem();

        when(mensagemService.registrarMensagem(any(Mensagem.class))).thenAnswer(i -> i.getArguments()[0]);

        //Act + assert
        mockMvc.perform(post("/mensagens")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(mensagemRequest))
                )
                .andDo(print())
                .andExpect(status().isCreated());
        verify(mensagemService, times(1)).registrarMensagem(any(Mensagem.class));

    }


    @Test
    void devePertimirObterMensagem() throws Exception {
        //Arrange
        var id = UUID.fromString("e19dac7d-d713-4ede-9f94-a409c0a601c1");
        var mensagemResponse = MensagemHelper.gerarMensagem();
        mensagemResponse.setId(id);
        mensagemResponse.setDataCriacao(LocalDateTime.now());
        mensagemResponse.setDataAlteracao(LocalDateTime.now());

        when(mensagemService.obterMensagemPorId(any(UUID.class))).thenReturn(mensagemResponse);


        //act + response
        mockMvc.perform(get("/mensagens/{id}", id)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        verify(mensagemService, times(1)).obterMensagemPorId(any(UUID.class));
    }

    @Test
    void deveGerarExcecaoAoObterMensagemComIdNaoExistente() throws Exception {
        //Arrange
        var id = UUID.fromString("e19dac7d-d713-4ede-9f94-a409c0a601c1");

        when(mensagemService.obterMensagemPorId(any(UUID.class))).thenThrow(new MensagemNotFoundException("mensagem não encontrada"));


        //act + response
        mockMvc.perform(get("/mensagens/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
        verify(mensagemService, times(1)).obterMensagemPorId(any(UUID.class));
    }

    @Test
    void deveGerarExcecaoAoObterMensagemComIdInvalido() throws Exception {
        //Arrange
        var id = "123";

        //act + response
        mockMvc.perform(get("/mensagens/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        verify(mensagemService, times(0)).obterMensagemPorId(any(UUID.class));
    }

//    @Test
//    void devePertimirRemoverMensagem(){
//        fail("não implementado");
//    }




    private String asJsonString(final Object obj) {
        try{
            return new ObjectMapper().writeValueAsString(obj);
        }catch (JsonProcessingException e){
            throw new RuntimeException(e);
        }
    }


}
