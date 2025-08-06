package br.com.fiap.Mensagem.controllers;

import br.com.fiap.Mensagem.exceptions.MensagemNotFoundException;
import br.com.fiap.Mensagem.model.Mensagem;
import br.com.fiap.Mensagem.services.MensagemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/mensagens")
@RequiredArgsConstructor
public class MensagemController {

    private final MensagemService mensagemService;


    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Mensagem> registrarMensagem(@Valid @RequestBody Mensagem mensagem){
        var mensagemRegistrada = mensagemService.registrarMensagem(mensagem);
        return new ResponseEntity<>(mensagemRegistrada, HttpStatus.CREATED);
    }


    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> buscarMensagemPorId(@PathVariable String id){
        try{
            var uuid = UUID.fromString(id);
            var mensagemEncontrada = mensagemService.obterMensagemPorId(uuid);
            return new ResponseEntity<>(mensagemEncontrada, HttpStatus.OK);

        }catch (MensagemNotFoundException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID Invalido");
        }
    }

}
