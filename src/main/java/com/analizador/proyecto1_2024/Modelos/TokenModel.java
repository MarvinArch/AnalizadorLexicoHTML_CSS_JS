/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.analizador.proyecto1_2024.Modelos;

import lombok.Data;


/**
 *
 * @author David
 */
@Data
public class TokenModel {
    private String lenguaje;
    private String tipo;
    private String lexema;
    private int linea;
    private int columna;
    private boolean eliminado;
    private String grupo;
    private String expresionRegular;

    public TokenModel(String lenguaje, String tipo, String lexema, int linea, int columna, String grupo, String expresionRegular) {
        this.lenguaje = lenguaje;
        this.tipo = tipo;
        this.lexema = lexema;
        this.linea = linea;
        this.columna = columna;
        this.eliminado=false;
        this.grupo=grupo;
        this.expresionRegular=expresionRegular;
    }

    @Override
    public String toString() {
        return "TokenModelJavaScript{" + "lenguaje=" + lenguaje + ", tipo=" + tipo + ", lexema=" + lexema + ", linea=" + linea + ", columna=" + columna + '}';
    }
}
