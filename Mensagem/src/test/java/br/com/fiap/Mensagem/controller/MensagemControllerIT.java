package br.com.fiap.Mensagem.controller;

import br.com.fiap.Mensagem.helper.MensagemHelper;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = {"/db_clean.sql"}, executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
@Sql(scripts = {"/db_load.sql"}, executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class MensagemControllerIT {

    @LocalServerPort
    private int port;

    @BeforeEach
    void setup(){
        RestAssured.port = port;
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void devePertimirRegistrarMensagem() throws Exception {

        var mensagemRequest = MensagemHelper.gerarMensagem();

        given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(mensagemRequest)
                .when().post("/mensagens")
        .then().statusCode(HttpStatus.CREATED.value())
                .body("$", hasKey("id"))
                .body("$", hasKey("usuario"))
                .body("$", hasKey("gostei"))
                .body("$", hasKey("dataCriacao"))
                .body("usuario", equalTo(mensagemRequest.getUsuario()))
                .body("conteudo", equalTo(mensagemRequest.getConteudo()));
    }

    @Test
    void devePertimirObterMensagem() throws Exception {
        var id = "170c04d3-d0e5-4b03-915c-7b914a45f8f8";

        when()
                .get("/mensagens/{id}", id)
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("$", hasKey("id"))
                .body("$", hasKey("usuario"))
                .body("$", hasKey("gostei"))
                .body("$", hasKey("dataCriacao"))
                .body("usuario", equalTo("José"))
                .body("conteudo", equalTo("Mensagem de José"));


    }

}
