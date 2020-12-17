/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

/**
 *
 * @author Ivo
 */
public class VersaoInvalidaException extends RuntimeException {
  public String objeto;
  public int versaoEsperada;
  public int versaoEncontrada;

  public VersaoInvalidaException(String objeto, int versaoEsperada, int versaoEncontrada) {
    super("Versão invalida no " + objeto + ", (o Codegen está na v"+versaoEsperada+", porém o projeto é da v"+versaoEncontrada+")");
    this.versaoEsperada = versaoEsperada;
    this.versaoEncontrada = versaoEncontrada;
    this.objeto = objeto;
  }
}
