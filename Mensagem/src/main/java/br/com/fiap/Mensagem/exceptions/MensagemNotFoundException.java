package br.com.fiap.Mensagem.exceptions;

public class MensagemNotFoundException extends RuntimeException {
    public MensagemNotFoundException(String mensagem) {
        super(mensagem);
    }
}
