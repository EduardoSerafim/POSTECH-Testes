package br.com.fiap;


import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class CalculadoraTest{
    public Calculadora calculadora;
    private int valorA;
    private int valorB;


    @BeforeEach
    void setup(){
        calculadora = new Calculadora();
        valorA = 5;
        valorB = 2;
    }


    @Test
    void devePermitirSomar(){
        int resultado = calculadora.somar(valorA, valorB);
        assertEquals(7, resultado);
    }

    @Test
    void devePermitirSubtrair(){
        int resultado = calculadora.subtrair(valorA, valorB);
        assertEquals(3, resultado);
    }

    @Test
    void devePermitirMultiplicar(){
        int resultado = calculadora.multiplicar(valorA, valorB);
        assertEquals(10, resultado);
    }

    @Test
    void devePermitirDividir(){
        valorA = 6;
        double resultado = calculadora.dividir(valorA, valorB);
        assertEquals(3, resultado);
    }


    @Test
    void deveGerarErroAoDividirPorZero(){
        valorA = 0;

       Exception erro = Assertions.assertThrows(ArithmeticException.class, () -> {
          calculadora.dividir(valorA, valorB);
       });

       String mensagemErroEsperada = "Não é possível dividir por zero";
       String mensagemErroRecebida = erro.getMessage();

       assertEquals(mensagemErroEsperada, mensagemErroRecebida);
    }


//    @BeforeAll //executado antes de todos os testes
//    static void setupClass(){
//        System.out.println("Iniciando os testes");
//    }
//
//    @AfterAll //executado depois de todos os testes
//    static void setupTearDown(){
//        System.out.println("Finalizando os testes");
//    }
//
//
//
//    @BeforeEach //executado antes de cada teste
//    void setUp(){
//        System.out.println("Antes do teste");
//    }
//
//    @AfterEach //executado depois de cada teste
//    void tearDown(){
//        System.out.println("depois do teste");
//    }
//
//
//    @Nested  //é possível agrupar testes usando classes e a anotação Nested
//    class GroupA{
//        @Test
//        void testeA(){
//            System.out.println("TESTE A");
//
//        }
//
//        @Test
//        void testeB(){
//            System.out.println("TESTE B");
//        }
//    }
//
//    @Nested
//    class GroupB{
//        @Test
//        void testeA(){
//            System.out.println("TESTE A");
//
//        }
//
//        @Test
//        void testeB(){
//            System.out.println("TESTE B");
//        }
//    }


}

