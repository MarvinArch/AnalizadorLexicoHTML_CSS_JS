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
public class AnalizadorCss {
    private final String[] etiqueta={"body","header", "main", "nav", "aside", "div", "ul", "ol", "li", "a", "h1",
                                        "h2", "h3", "h4", "h5", "h6", "p", "span", "label", "textarea", "button", "section",
                                        "article", "footer"};
    private final String[] reglas={"color", "background-color", "background", "font-size", "font-weight", "font-family", 
                                "font-align", "width", "height", "min-width", "min-height", "max-width", "max-height", 
                                "display", "inline", "block", "inline-block", "flex", "grid", "none", "margin", "border", 
                                "padding", "content", "border-color", "border-style", "border-width", "border-top", 
                                "border-bottom", "border-left", "border-right", "box-sizing", "border-box", "position", 
                                "static", "relative", "absolute", "sticky", "fixed", "top", "bottom", "left", "right", 
                                "z-index", "justify-content", "align-items", "border-radius", "auto", "float", 
                                "list-style", "text-align", "box-shadow"};
    private final String[] otros ={"px", "rem", "em", "vw", "vh", ":hover", ":active", ":not()", ":nth-child()","odd", 
                            "even", "::before", "::after"};
    private String lexema;
    private ArrayList<TokenModel> model;
    private int linea;
    private int columna;
    private int posicionFinal;
    
    public ArrayList<TokenModel> AnalizarCss(int inicial, char[] texto, int posicion) {
        linea =inicial;
        columna=1;
        model = new ArrayList<>();
        lexema = "";
        int estado = 0;

        for (int i = posicion; i < texto.length; i++) {
            if (estado == 0) {
                if (texto[i]==10) {
                    linea++;
                    columna=1;
                }else if (texto[i]==32) {
                    columna++;
                }
                //Si empieza con letras minuscula es etiqueta  
                if ((texto[i] > 96 && texto[i] < 123)) {
                    lexema += texto[i];
                    estado = 1;
                }else if (texto[i]==37 || texto[i]==58 || texto[i]==59 || texto[i]==44 || texto[i]==40
                        || texto[i]==41 || texto[i]==123 || texto[i]==125 ) {
                    lexema+=texto[i];
                    estado=3;
                }else if (texto[i]==39) {
                    lexema+=texto[i];
                    estado=4;
                } else if (texto[i]==46) {
                    lexema+=texto[i];
                    estado=5;
                } else if (texto[i]==35) {
                    lexema+=texto[i];
                    estado=6;
                } else if ((texto[i] > 47 && texto[i] < 58)) {//enteros
                    lexema+=texto[i];
                    estado=8;
                }else if (texto[i]==62) {
                    lexema+=texto[i];
                    if (lexema.length()==2 && lexema.equals(">>")) {
                        posicionFinal=i-2;
                        return model;
                    }
                }else if (texto[i]==42) {
                     model.add(new TokenModel("Css", "Universal", lexema, linea, columna, "universal","*"));
                    lexema="";
                    columna++;
                    estado=0;
                }else if ( (texto[i] > 64 && texto[i] < 91)) {
                    lexema+=texto[i];
                    estado=9;
                }else if (texto[i]==47) {
                    lexema+=texto[i];
                    estado =10;
                }
            }else if (estado==1) {// estado de etiqueta 
                if ((texto[i] > 96 && texto[i] < 123)) {
                    lexema+= texto[i];
                    if(isEtiqueta(lexema)==false) {//si no coincide con una etiqueta se busca dentro de las reglas
                        estado=2;
                    }
                }else if (texto[i]==32 || texto[i]==10 || texto[i]==58 || texto[i]==59) {
                    model.add(new TokenModel("Css", "Etiqueta", lexema, linea, columna, "Etiqueta", lexema));
                    columna+= lexema.length();
                    lexema="";
                    estado=0;
                    i--;
                }else if (texto[i]==45) {
                    lexema+=texto[i];
                    estado=2;
                }else if ((texto[i] > 47 && texto[i] < 58)) {
                    lexema+=texto[i];
                    estado=9;
                }
                // estado de Reglas
            }else if (estado==2) {
                if ((texto[i] > 96 && texto[i] < 123) || texto[i]==45) {
                    lexema+= texto[i];
                    if (isRegla(lexema)==false) {
                        if (lexema.equals("rgb")) {
                            estado=7;
                        }else{
                            estado=3;
                        }
                    }
                }else if (texto[i]==32 || texto[i]==10 || texto[i]==58 || texto[i]==59) {
                    model.add(new TokenModel("Css", "Regla", lexema, linea, columna, "Regla", lexema));
                    columna+= lexema.length();
                    lexema="";
                    estado=0;
                    i--;
                }else if ((texto[i] > 47 && texto[i] < 58)) {
                    lexema+=texto[i];
                    estado=9;
                }else{
                    lexema+=texto[i];
                    estado=3;
                }
            }else if (estado==3) {//Estado perteneciente e Otros
                if (texto[i]==32 || texto[i]==10) {
                    if (lexema.equals("%") || lexema.equals(":") || lexema.equals(";") || lexema.equals(",")
                            || lexema.equals("(") || lexema.equals(")") || lexema.equals("{") || lexema.equals("}")) {
                        model.add(new TokenModel("Css", "Otros", lexema, linea, columna, "Otros", lexema));
                        columna++;
                        i--;
                        lexema="";
                        estado=0;
                    } else {
                        if (isOtro(lexema)) {
                            model.add(new TokenModel("Css", "Otros", lexema, linea, columna, "Otros", lexema));
                            columna+=lexema.length();
                            i--;
                            lexema="";
                            estado=0;
                        }
                    }
                }else {//comprobar si viene letras
                    if (isOtro(lexema+texto[i])) {
                        lexema+=texto[i];
                    }else{
                        estado=9;
                        i--;
                    }
                }
            } else if (estado==4) {//Cadena
                lexema+=texto[i];
                if (texto[i]==lexema.charAt(0)) {
                    model.add(new TokenModel("Css", "Cadena", lexema, linea, columna, "Cadena","'.*'"));
                    columna+=lexema.length();
                    lexema="";
                    estado=0;
                }
            } else if (estado==5) {//Estado de Clase
                if ((texto[i] > 96 && texto[i] < 123) || texto[i]=='-' || (texto[i] > 47 && texto[i] < 58)) {
                    lexema+=texto[i];
                }else if (texto[i]==32 || texto[i]==10) {
                    model.add(new TokenModel("Css", "De Clase", lexema, linea, columna, "De Clase", ".[a-z]+ [0-9]* (- ([a-z] | [0-9])+)*"));
                    columna+=lexema.length();
                    i--;
                    lexema="";
                    estado=0;
                }
            } else if (estado==6) {//Estado de ID
                if (texto[i]==32 || texto[i]==10 || texto[i]==58 || texto[i]==59) {
                    if (isColor(lexema)) {
                        model.add(new TokenModel("Css", "Color", lexema.trim(), linea, columna, "Color", "#^([a-f] | [0-9){6}$"));
                        columna+=lexema.length();
                        lexema="";
                        i--;
                        estado=0;
                    }else{
                        model.add(new TokenModel("Css", "De ID", lexema.trim(), linea, columna, "De ID", "#[a-z]+ [0-9]* (- ([a-z] | [0-9])+)*"));
                        columna+=lexema.length();
                        lexema="";
                        i--;
                        estado=0;
                    }
                }else{
                    lexema+=texto[i];
                }
            } else if (estado==7) {//colores
                if (texto[i]==41 && isRGBA(lexema+texto[i], true)) {
                    model.add(new TokenModel("Css", "Color", lexema+texto[i], linea, columna, "Color","rgba\\([0-9]){3}$,[0-9){3}$,[0-9){3}$,(0.[0-9])?\\)"));
                    columna=lexema.length();
                    lexema="";
                    estado=0;
                } else if (isRGBA(lexema+texto[i], false)) {
                    lexema+=texto[i];
                }else{
                    System.out.println("error no es rgba");
                }
            } else if (estado==8) {//Enteros
                if ((texto[i] > 47 && texto[i] < 58)) {
                    lexema+=texto[i];
                }else if (texto[i]==32 || texto[i]==10 ) {
                    model.add(new TokenModel("Css", "Entero", lexema, linea, columna, "Entero", "[0-9]+"));
                    columna+=lexema.length();
                    lexema="";
                    estado=0;
                    i--;
                }
            } else if (estado==9) {//Estado de identificador
                if ((texto[i] > 47 && texto[i] < 58) || (texto[i] > 96 && texto[i] < 123) || texto[i]==45) {
                    lexema+=texto[i];
                }else if (texto[i]==32 || texto[i]==10) {
                    model.add(new TokenModel("Css", "Identificador", lexema, linea, columna, "Identificador","[a-z]([a-z] | [0-9] | [ - ])*"));
                    columna+=lexema.length();
                    lexema="";
                    estado=0;
                    i--;
                }
            } else if (estado==10) {
                lexema+=texto[i];
                if (lexema.charAt(0)==47 &&lexema.charAt(1)==47) {
                    if (texto[i]==10) {
                        model.add(new TokenModel("Css", "Comentario", lexema, linea, columna, "Comentario", "//.*"));
                        linea++;
                        columna=1;
                        lexema="";
                        estado=0;
                    }
                }else{
                    System.out.println("erro en la linea de comentario en Css");
                }
            }  else {
                return model;
            }
            posicionFinal=i;
        } 
        
        return model;
    }
    
    private boolean isEtiqueta(String lexematemp){
        for (int i = 0; i < etiqueta.length; i++) {
            String tempo="";
            for (int j = 0; j < lexematemp.length(); j++) {
                if (j<etiqueta[i].length()) {
                    tempo+=etiqueta[i].charAt(j);
                }
            }
            if (lexematemp.equals(tempo)) {
                i=etiqueta.length;
                return true;
            }
        }
        return false;
    }
    
    private boolean isRegla(String lexematemp){
        for (int i = 0; i < reglas.length; i++) {
            String tempo="";
            for (int j = 0; j < lexematemp.length(); j++) {
                if (j<reglas[i].length()) {
                    tempo+= reglas[i].charAt(j);
                }
            }
            if (lexematemp.equals(tempo)) {
                i=reglas.length;
                return true;
            }
        }
        return false;
    }
    
    private boolean isOtro(String lexemaTemp){
        for (int i = 0; i < otros.length; i++) {
            String tempo="";
            for (int j = 0; j < lexemaTemp.length(); j++) {
                if (j<otros[i].length()) {
                    tempo+=otros[i].charAt(j);
                }
            }
            if (lexemaTemp.equals(tempo)) {
                return true;
            }
        }
        return false;
    }
    
    private boolean isColor(String lexemaTemp){
        if (lexemaTemp.trim().length()==7) {//primero comprobar si el lexema cumple con la longitud de ser un color hexagesimal
            if (lexemaTemp.charAt(0)=='#') {
                for (int i = 1; i < lexemaTemp.trim().length(); i++) {
                    if ((lexemaTemp.charAt(i)>47 && lexemaTemp.charAt(i)<58) || 
                            (lexemaTemp.charAt(i)>64 && lexemaTemp.charAt(i)<71) || (lexemaTemp.charAt(i)>96 && lexemaTemp.charAt(i)<103)) {
                        
                    } else {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }
   
    private boolean isRGBA(String lexemaTemp, boolean finalizado) {
        String rgba = "rgba(";
        String temp = "";
        String temp2 = "";
        int posiciontemp=0;
        boolean numerosValidos = false;
        for (int i = 0; i < lexemaTemp.length(); i++) {
            if (i < 5) {
                temp += lexemaTemp.charAt(i);
                temp2 += rgba.charAt(i);
            }
            if (lexemaTemp.charAt(i)==40) {
                posiciontemp=i+1;
            }
        }
        if (temp.equals(temp2) && !finalizado) {
            return true;
        }
        if (finalizado && temp.equals(temp2)) {
            temp = "";
            temp2 = "rojo";
            for (int i = posiciontemp; i < lexemaTemp.length(); i++) {
                if (lexemaTemp.charAt(i) == 44 || lexemaTemp.charAt(i) == 41) {
                    if (temp2.equals("rojo") || temp2.equals("verde") || temp2.equals("azul")) {
                        int menor = Integer.parseInt(temp.trim());
                        if (menor < 256) {
                            temp = "";
                            if (temp2.equals("rojo")) {
                                temp2 = "verde";
                            } else if (temp2.equals("verde")) {
                                temp2 = "azul";
                            } else if (temp2.equals("azul")) {
                                temp2 = "alfa";
                                numerosValidos = true;
                            }
                        } else {//se retorna false si algun numero es mayor a 255 
                            return false;
                        }
                    } else if (temp2.equals("alfa")) {
                        double menor2 = Double.parseDouble(temp.trim());
                        if (menor2<1.0) {
                            return true;
                        }
                    } else {
                        return false;
                    }
                } else {
                    temp += lexemaTemp.charAt(i);
                }

            }
            if (numerosValidos) {
                return true;
            }
        }
        return false;
    }
}
