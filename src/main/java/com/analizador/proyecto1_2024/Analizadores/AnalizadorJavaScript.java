/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.analizador.proyecto1_2024.Analizadores;

import com.analizador.proyecto1_2024.Modelos.TokenModel;
import java.util.ArrayList;

/**
 *
 * @author David
 */
public class AnalizadorJavaScript {
    private ArrayList<TokenModel> model;
    private int lineaFinal;
    private int posicionFinal;
    private final String[] reservadas={"function", "true", "false","const","let", "document", "event", 
                                        "alert", "for", "while", "if", "else", "return", "console.log", "null"};
    private String clasificacion;

    public AnalizadorJavaScript() {
        model= new ArrayList<>();
        lineaFinal=0;
        clasificacion="";
    }

    public int getPosicionFinal() {
        return posicionFinal;
    }

    public ArrayList<TokenModel> getModel() {
        return model;
    }

    public int getLineaFinal() {
        return lineaFinal;
    }
    //Aca se ingrese todo el texto a analizar
    public ArrayList<TokenModel> AnalizarJavaScript(int inicial, char[] texto, int posicion) {
        lineaFinal = inicial;
        int columna = 1;
        String lexema = "";
        int estado = 0;
        for (int i = posicion; i < texto.length; i++) {
            if (estado == 0) {
                if (texto[i]==32) {
                    columna++;
                    lexema="";
                }else if (texto[i]==10){
                    lineaFinal++;
                    columna=1;
                }
                if ((texto[i] > 98 && texto[i] < 103) || texto[i] == 97 || texto[i] == 105 || texto[i] == 108
                        || texto[i] == 110 || texto[i] == 114 || texto[i] == 116 || texto[i] == 119) {//reservados
                    i--;
                    estado = 2;
                } else if ((texto[i] > 64 && texto[i] < 91) || (texto[i] > 96 && texto[i] < 123)) {//identificadores
                    i--;
                    estado = 1;
                } else if (texto[i] > 47 && texto[i] < 58) {//numeros enteros y decimales
                    estado = 3;
                    i--;
                } else if (texto[i] == 42 || texto[i] == 47) {//multiplicacion y division
                    lexema += texto[i];
                    try {
                        if (texto[i] == 47 && texto[i+1]==47) {
                            estado=9;
                        } else {
                            clasificacion = (texto[i] == 42) ? "Multiplicacion" : "Division";
                            model.add(new TokenModel("Javascript", clasificacion, lexema, lineaFinal, columna, "Operador", "*|/"));
                            columna++;
                            lexema = "";
                        }
                    } catch (Exception e) {
                    }
                } else if (texto[i] == 43 || texto[i]==45) { //Incrementales ||suma y resta
                    lexema+=texto[i];
                    estado= 5;
                }else if (texto[i] == 34 || texto[i]== 39 || texto[i]==96) {//cadenas
                    lexema+=texto[i];
                    estado=6;
                }else if ((texto[i] > 59 && texto[i]< 63) || texto[i]==33) {//Relacionales y Asignacion
                    lexema+=texto[i];
                    estado=7;
                }else if (texto[i]==59 || texto[i]==44 || texto[i]==46 || texto[i]==58 || (texto[i]<42 && texto[i]>39) || 
                        texto[i]==91 || texto[i]==93 || texto[i]==123 || texto[i]==125) {//estado de otros
                    AgregarOtros(texto[i], lineaFinal, columna);
                    columna++;
                    lexema="";
                }else if (texto[i]==124 || texto[i]==38) {//estado para logicos
                    lexema+=texto[i];
                    estado=8;
                }
            } else if (estado == 1) { //estado de identificadores  
                if ((texto[i] > 64 && texto[i] < 91) || (texto[i] > 96 && texto[i] < 123) || (texto[i] > 47 && texto[i] < 58) || texto[i] == 95) {
                    lexema += texto[i];
                } else {
                    //agregar simbolos que pueden venir pegados ejemplo =)
                    if (texto[i] == 32 || texto[i] == 10 || ComprobarSimbolosAdjuntos(texto[i])) {
                        model.add(new TokenModel("Javascript", "Identificador", lexema, lineaFinal, columna, "Identificador", "[a-zA-Z]([a-zA-Z] | [0-9] | [ _ ])*"));
                        columna += (lexema.length() + 1);
                        estado = 0;
                        lexema = "";
                        if (texto[i] == 10) {
                            lineaFinal++;
                            columna=1;
                        }
                        if (ComprobarSimbolosAdjuntos(texto[i])) i--;
                    }else{
                        System.out.println("esto es un error");
                    }
                }
            } else if (estado == 2) {//estado de palabra reservadas 
                lexema += texto[i];
                String temp1 = "";
                for (int j = 0; j < reservadas.length; j++) {
                    temp1 = "";
                    char[] temp2 = reservadas[j].toCharArray();
                    for (int k = 0; k < lexema.trim().length() ; k++) {
                        if (k<temp2.length) {
                            temp1 += temp2[k];
                        }
                    }
                    if (temp1.equals(lexema.trim())) {
                        j = reservadas.length;
                    }
                }
                if (temp1.equals(lexema.trim())) {
                    if (texto[i]==32 || texto[i]==10) {
                        if (lexema.trim().equals("true") || lexema.trim().equals("true")) {
                            clasificacion = (lexema.trim().equals("true")) ? "True" : "False";
                            model.add(new TokenModel("Javascript", clasificacion, lexema, lineaFinal, columna, "Booleano", "true | false"));
                        }else{
                            model.add(new TokenModel("Javascript", "Palabra Reservada", lexema, lineaFinal, columna, "Palabra Reservada", lexema));
                        }
                        columna+=lexema.length();
                        lexema="";
                        estado=0;
                        if (texto[i]==10) i--;
                    }
                }else{
                    lexema= lexema.substring(0, lexema.length()-1);
                    i--;
                    estado=1;
                }
            } else if (estado == 3) {//estado de numeros Enteros
                if (texto[i] > 47 && texto[i] < 58) {
                    lexema += texto[i];
                } else if (texto[i] == '.') {
                    lexema += texto[i];
                    estado = 4;
                } else if (texto[i] == 32 || texto[i] == 10 || ComprobarSimbolosAdjuntosNumeros(texto[i])) {
                    model.add(new TokenModel("Javascript", "Entero", lexema, lineaFinal, columna, "Tipo de Datos", "(0-9)+"));
                    columna += (lexema.length() + 1);
                    estado = 0;
                    lexema = "";
                    if (texto[i] == 10) {
                        lineaFinal++;
                        columna=1;
                    }
                    if (ComprobarSimbolosAdjuntosNumeros(texto[i])) i--;
                } else {
                    System.out.println("Error en los numeros el lexema actual es "+lexema);//empezar a interar apartir de aca hasta encontrar el espacio o salto de linea
                }
            } else if (estado == 4) {//Estado Decimales
                if (texto[i] > 47 && texto[i] < 58) {
                    lexema += texto[i];
                } else if (texto[i] == 32 || texto[i] == 10 || ComprobarSimbolosAdjuntosNumeros(texto[i])) {
                    model.add(new TokenModel("Javascript", "Decimal", lexema, lineaFinal, columna, "Tipo de Datos","(0-9)+.(0-9)+"));
                    columna += (lexema.length() + 1);
                    estado = 0;
                    lexema = "";
                    if (texto[i] == 10) {
                        lineaFinal++;
                        columna=1;
                    }
                    if (ComprobarSimbolosAdjuntosNumeros(texto[i])) i--;
                } else {
                    System.out.println("Error");//empezar a interar apartir de aca hasta encontrar el espacio o salto de linea
                }
                //estado de incrementales || suma y resta
            } else if (estado==5) {
                if (lexema.length()==1) {//comprueba que solo exista un caracter antes +, -
                    if ((texto[i]==45 && texto[i-1]==45) || (texto[i]==43 && texto[i-1]==43)) {
                        lexema+=texto[i];
                        clasificacion = (lexema.trim().equals("++")) ? "Incremento" : "Decremento";
                        model.add(new TokenModel("Javascript", clasificacion , lexema, lineaFinal, columna, "Incrementales","++ | --"));
                        estado=0;
                        columna+=lexema.length()+1;
                        lexema="";
                    }else if ((texto[i] > 64 && texto[i] < 91) || (texto[i] > 96 && texto[i] < 123) 
                            || (texto[i] > 47 && texto[i] < 58) || texto[i] == 32 || texto[i] == 10) {
                        clasificacion =(lexema.trim().equals("+")) ? "Suma" : "Resta";
                        model.add(new TokenModel("Javascript", clasificacion, lexema, lineaFinal, columna, "Operacion", "+ | -"));
                        estado=0;
                        columna+=lexema.length()+1;
                        lexema="";
                        i--;
                    }
                }
                //estado de cadenas
            } else if (estado==6) {
                lexema+=texto[i];
                if (texto[i]==lexema.charAt(0)) {
                    model.add(new TokenModel("Javascript", "Cadena", lexema, lineaFinal, columna, "Tipo de Datos",'"'+".*"+'"'));
                    estado=0;
                    lexema="";
                    columna+=lexema.length()+1;
                }else if (texto[i]==10) {
                    System.out.println("Error no puede haber saltos de linea en las cedemnas");
                    columna+=lexema.length()+1;
                    estado=0;
                }
                //estado de relacionales
            } else if (estado==7) {
                if ((lexema+texto[i]).equals("==") || (lexema+texto[i]).equals("<=") || (lexema+texto[i]).equals(">=")
                        || (lexema+texto[i]).equals("!=")) {
                    if ((lexema+texto[i]).equals("==")) {
                        clasificacion="Igual";
                    }else if ((lexema+texto[i]).equals("<=")) {
                        clasificacion="Menor o Igual";
                    }else if ((lexema+texto[i]).equals(">=")) {
                        clasificacion="Mayor o Igual";
                    }else if ((lexema+texto[i]).equals("!=")) {
                        clasificacion="Diferente";
                    }
                    model.add(new TokenModel("Javascript", clasificacion, lexema+texto[i], lineaFinal, columna, "Relacional", lexema));
                    columna++;
                    estado=0;
                    lexema="";
                }else {
                    if (lexema.equals("<") || lexema.equals(">")) {
                        if (lexema.equals(">") && texto[i]==62) {
                            if (texto[i+1]==91) {
                                posicionFinal=i-1;
                                i=texto.length;
                            }
                        }else{
                            clasificacion = (lexema.equals(">")) ? "Mayor que" : "Menor que";
                            model.add(new TokenModel("Javascript", clasificacion, lexema, lineaFinal, columna, "Relacional", lexema));
                            columna++;
                            i--;
                            estado=0;
                            lexema="";
                        }
                    }else if (lexema.equals("=")) {
                        model.add(new TokenModel("Javascript", "Asignacion", lexema, lineaFinal, columna, "Asignacion", lexema));
                        columna++;
                        estado=0;
                        lexema="";
                        i--;
                    }else if (lexema.equals("!")) {
                        model.add(new TokenModel("Javascript", "Not", lexema, lineaFinal, columna, "Logicos", lexema));
                        columna++;
                        estado=0;
                        lexema="";
                        i--;
                    }  else {
                        System.out.println("Error ya no es parte las relacionales y asignacion el estado actual es "+estado);
                    }
                }
            }else if (estado==8) {
                 if ((lexema+texto[i]).equals("||") || (lexema+texto[i]).equals("&&")) {
                    clasificacion = ((lexema+texto[i]).equals("||")) ? "Or" : "Not";
                    model.add(new TokenModel("Javascript", clasificacion, lexema+texto[i], lineaFinal, columna, "Logico", lexema));
                    columna++;
                    estado=0;
                    lexema="";
                }
            }else if (estado==9) {
                lexema+=texto[i];
                if (texto[i]==10) {
                    model.add(new TokenModel("Javascript", "Comentario", lexema, lineaFinal, columna, "Comentario", "//.*"));
                    lineaFinal++;
                    lexema="";
                    columna=1;
                    estado=0;
                }
            } else{
                System.out.println("aca todo es error hasta salir");
            }
            
            if (i==texto.length-1) {
                posicionFinal=i;
            }
        }
        return model;
    }
    
    private boolean ComprobarSimbolosAdjuntos(char a){
        if (a==33 || (a>39 && a<48) || a==58 || a==59
            || (a>59 && a<63) || a==91 || a==93 || a==123 || a==125 ) {
            return true;
        }
        return false;
    }
    
    private boolean ComprobarSimbolosAdjuntosNumeros(char a){
        if (a==33 || a==44 || a==42 || a==47 || a==60 || a==62
            || a==45 || a==43 || a==41 ) {
            return true;
        }
        return false;
    }
    
    private void AgregarOtros(char a, int linea, int columna){
        if (a==40 || a==41) {
            model.add(new TokenModel("Javascript", "Parentesis", ""+a, linea, columna,"Otros", ""+a));
        }else if (a==91 || a==93) {
            model.add(new TokenModel("Javascript", "Corchetes", ""+a, linea, columna,"Otros", ""+a));
        }else if (a==123 || a==125) {
            model.add(new TokenModel("Javascript", "Llaves", ""+a, linea, columna,"Otros", ""+a));
        }else if (a==59) {
            model.add(new TokenModel("Javascript", "Punto y Coma", ""+a, linea, columna,"Otros", ""+a));
        }else if (a==44) {
            model.add(new TokenModel("Javascript", "Coma", ""+a, linea, columna,"Otros", ""+a));
        }else if (a==46) {
            model.add(new TokenModel("Javascript", "Punto", ""+a, linea, columna,"Otros", ""+a));
        }else if (a==58) {
            model.add(new TokenModel("Javascript", "Dos Puntos", ""+a, linea, columna,"Otros", ""+a));
        }
    }
    
}
