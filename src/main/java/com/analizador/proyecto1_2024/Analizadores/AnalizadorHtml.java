/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.analizador.proyecto1_2024.Analizadores;

import com.analizador.proyecto1_2024.Modelos.TokenModel;
import java.util.ArrayList;
import lombok.Data;

/**
 *
 * @author David
 */
@Data
public class AnalizadorHtml {
    private final String[] etiquetas={"principal", "encabezado", "navegacion", "apartado", "listaordenada", "listadesordenada", "itemlista", "anclaje", 
                    "contenedor", "seccion", "articulo", "parrafo", "span", "formulario", "label", "boton", "piepagina"};
    private final String[] etiquetas1Linea={"entrada", "area"};
    private final String[] traduccionEtiqueta ={"main", "header", "nav", "aside", "ul", "ol", "li", "a",
                    "div", "section", "article", "p", "span", "form", "label", "button", "footer"};
    private final String[] reservadas={"class", "href", "onClick", "id", "style", "type", "placeholder", "required", "name"};
    private String lexema;
    private ArrayList<TokenModel> model;
    private int linea;
    private int columna;
    private int posicionFinal;
    private ArrayList<String> errores;

    public ArrayList<TokenModel> AnalizarHtml(int inicial, char[] texto, int posicion) {
        errores = new ArrayList<>();
        model = new ArrayList<>();
        this.linea = inicial;
        this.columna = 1;
        int estado = 0;
        lexema = "";
        boolean linea1 = false;
        for (int i = posicion; i < texto.length; i++) {
            if (estado == 0) {
                if (texto[i] == 32) {
                    columna++;
                } else if (texto[i] == 10) {
                    linea++;
                    columna = 1;
                } else if (texto[i] == 60) {
                    model.add(new TokenModel("HTML", "Apertura Etiqueta", "" + texto[i], linea, columna, "<", "<"));
                    columna++;
                    estado = 1;// se envia a estado 1 para buscar el nombre de la etiqueta
                } else if (texto[i] == 47 && texto[i + 1] == 47) {
                    i++;
                    estado = 7;
                } else if (texto[i] == 62 && texto[i + 1] == 62) {
                    posicionFinal = i - 2;
                    linea -= 2;
                    return model;
                } else {
                    lexema += texto[i];
                    estado = 8;
                }
            } else if (estado == 1) {//buscar la etiqueta si viene algo distinto es error
                if (isEtiqueta(lexema + texto[i])) {//si contiene caracteres que lo clasifique como etiqueta sige concatenando 
                    lexema += texto[i];
                } else if (isReservadaLinea(lexema + texto[i])) {//si contiene caracteres que lo clasifique como etiqueta de 1 linea sige concatenando 
                    lexema += texto[i];
                } else if (texto[i] == 32 && (isEtiqueta(lexema) || isReservadaLinea(lexema))) { //si se encuentra un espacio se deduce que existe una palabra reservada
                    if (isReservadaLinea(lexema)) {//si la palabra reservada es de 1 sola linea manda a el estado en el que se cierra con />
                        String trad = "";
                        if (lexema.equals("entrada")) {
                            trad = "input";
                        } else if (lexema.equals("area")) {
                            trad = "textarea";
                        }
                        model.add(new TokenModel("HTML", "Etiqueta", lexema, linea, columna, trad, lexema));
                        columna += lexema.length();
                        lexema = "";
                        estado = 2;
                        linea1 = true;
                    } else {// si no es de una sola linea lo envia al estado en donde termina con >
                        model.add(new TokenModel("HTML", "Etiqueta", lexema, linea, columna, devolverTraduccion(lexema), lexema));
                        columna += lexema.length();
                        lexema = "";
                        estado = 2;
                    }

                } else if ("titulo".contains(lexema + texto[i])) {//si la etiqueta es un titulo comprueba que el numero sea entre 1-6
                    lexema += texto[i];
                    if (lexema.equals("titulo")) {
                        estado = 6;
                    }
                } else if (texto[i] == 47 && isEtiqueta(lexema)) {//permite cerra si la etiqueta la etiqueta e multi linea
                    model.add(new TokenModel("HTML", "Etiqueta Cierre", "/" + lexema, linea, columna, "/" + devolverTraduccion(lexema), lexema));
                    columna += lexema.length();
                    lexema = "";
                    estado = 5;
                } else if (texto[i] == 62 && isEtiqueta(lexema)) {
                    model.add(new TokenModel("HTML", "Etiqueta", lexema, linea, columna, devolverTraduccion(lexema), lexema));
                    columna += lexema.length();
                    model.add(new TokenModel("HTML", "Cierre Etiqueta", ">", linea, columna, ">", ">"));
                    lexema = "";
                    estado = 0;
                } else {
                    lexema += texto[i];
                    estado = 100;
                }
            } else if (estado == 2) {//busca una palabra reservada y = 
                if (isReservada(lexema + texto[i])) {
                    lexema += texto[i];
                } else if (texto[i] == 61 && isReservada(lexema)) {
                    model.add(new TokenModel("HTML", "Reservada", lexema, linea, columna, lexema, lexema));
                    columna += lexema.length();
                    model.add(new TokenModel("HTML", "Reservada", "=", linea, columna, "=", "="));
                    columna++;
                    lexema = "";
                    estado = 3;
                } else if (lexema.equals("") && texto[i] == 62) {
                    model.add(new TokenModel("HTML", "Cierre Etiqueta", lexema, linea, columna, ">", ">"));
                    columna++;
                }
            } else if (estado == 3) {
                if (texto[i] == 34) {
                    lexema += texto[i];
                    estado = 4;
                } else if (texto[i] == 32) {
                    columna++;
                }
            } else if (estado == 4) {//estado de cadenas
                if (texto[i] != 34) {
                    lexema += texto[i];
                } else if (texto[i] == 34) {
                    lexema += texto[i];
                    model.add(new TokenModel("HTML", "Cadena", lexema, linea, columna, lexema, '"' + ".*" + '"'));
                    columna += lexema.length();
                    lexema = "";
                    estado = 5;
                }
            } else if (estado == 5) {// luego de haber identificado un cierre de cadena
                if (texto[i] == 32) {
                    columna++;
                } else if ((texto[i] > 96 && texto[i] < 123)) {
                    lexema += texto[i];
                    estado = 2;
                } else if (texto[i] == 62) {
                    model.add(new TokenModel("HTML", "Cierre Etiqueta", ">", linea, columna, ">", ">"));
                    columna++;
                    estado = 0;
                } else if (texto[i] == 47) {
                    if (linea1) {
                        model.add(new TokenModel("HTML", "Cierre Etiqueta", "/>", linea, columna, "/>", "/>"));
                        columna += 2;
                        i++;
                        estado = 0;
                        linea1 = false;
                    }
                }
            } else if (estado == 6) {
                if (texto[i] > 48 && texto[i] < 55) {
                    model.add(new TokenModel("HTML", "Apertura Etiqueta", lexema + texto[i], linea, columna, "h" + texto[i], "h[1-6]"));
                    columna += lexema.length() + 1;
                    estado = 2;
                    lexema = "";
                    if (texto[i + 1] == 62) {
                        model.add(new TokenModel("HTML", "Cierre Etiqueta", ">", linea, columna, ">", ">"));
                        columna += 1;
                        i++;
                        estado = 0;
                        linea1 = false;
                    }
                } else {
                    lexema += texto[i];
                    estado = 100;
                }
            } /*else if (estado == 7) {
                if (texto[i] == 10) {
                    model.add(new TokenModel("HTML", "cometario", lexema, linea, columna, lexema, '"' + ".*" + '"'));
                    i--;
                    lexema = "";
                } else {
                    lexema += texto[i];
                }
            } else if (estado == 8) {
                if (texto[i] == 10) {
                    model.add(new TokenModel("HTML", "texto", lexema, linea, columna, lexema, ".*"));
                    i--;
                    lexema = "";
                } else {
                    lexema += texto[i];
                }
            } */else if (estado == 100) {//estado de error
                if (texto[i] == 10) {
                    estado = 0;
                    lexema = "";
                    i--;
                } else {
                    lexema += texto[i];
                }
            }
        }
        posicionFinal = texto.length;
        return model;
    }
    
    private boolean isEtiqueta(String lexematemp){
        for (int i = 0; i < etiquetas.length; i++) {
            String tempo="";
            for (int j = 0; j < lexematemp.length(); j++) {
                if (j<etiquetas[i].length()) {
                    tempo+= etiquetas[i].charAt(j);
                }
            }
            if (lexematemp.equals(tempo)) {
                i=etiquetas.length;
                return true;
            }
        }
        return false;
    }
    private boolean isReservada(String lexematemp){
        for (int i = 0; i < reservadas.length; i++) {
            String tempo="";
            for (int j = 0; j < lexematemp.trim().length(); j++) {
                if (j<reservadas[i].length()) {
                    tempo+= reservadas[i].charAt(j);
                }
            }
            if (lexematemp.trim().equals(tempo)) {
                i=reservadas.length;
                return true;
            }
        }
        return false;
    }
    
    private String devolverTraduccion(String etiquetaTemp){
        for (int i = 0; i < etiquetas.length; i++) {
            if (etiquetaTemp.equals(etiquetas[i])) {
                return etiquetas[i];
            }
        }
        return ""; 
    }
    
    private boolean isReservadaLinea(String lexematemp){
        for (int i = 0; i < etiquetas1Linea.length; i++) {
            String tempo="";
            for (int j = 0; j < lexematemp.length(); j++) {
                if (j<etiquetas1Linea[i].length()) {
                    tempo+= etiquetas1Linea[i].charAt(j);
                }
            }
            if (lexematemp.equals(tempo)) {
                i=reservadas.length;
                return true;
            }
        }
        return false;
    }
}
