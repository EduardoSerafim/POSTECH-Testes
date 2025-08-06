package br.com.fiap;

public class Calculadora {

    public int somar(int a, int b){
        return a + b;
    }

    public int subtrair(int a, int b){
        return a - b;
    }

    public int multiplicar(int a, int b){
        return a * b;
    }

    public int dividir(int a, int b){
        if (a == 0 || b == 0){
            throw new ArithmeticException("Não é possível dividir por zero");
        }
        return  a / b;
    }
}