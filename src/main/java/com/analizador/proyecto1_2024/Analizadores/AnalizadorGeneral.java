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
public class AnalizadorGeneral {
    private int linea;
    private ArrayList<TokenModel> reporte; 

    public AnalizadorGeneral() {
        reporte = new ArrayList<>();
        linea = 1;
    }
    
    public ArrayList<TokenModel>ClasificarTexto(String texto) {
        char[] analisis = texto.toCharArray();
        String tipo = "";
        int estado = -1;
        int columna = 1;
        if (texto.length() > 0) {//verifica si existe texto
            for (int i = 0; i < analisis.length; i++) {
                if (analisis[i] == 62 && estado == -1) {
                    tipo += analisis[i];
                    estado = 0;
                } else if (estado == 0) {
                    tipo += analisis[i];
                    if ((analisis[i] == 10 || analisis[i] == 32) && estado == 0) {
                        if (tipo.trim().equals(">>[html]")) {
                            tipo="";
                            i=EstadoHtml(analisis, i);
                        } else if (tipo.trim().equals(">>[css]")) {
                            System.out.println("empezo el analisis de css");
                            if (analisis[i]==10) linea++;
                            reporte.add(new TokenModel("Css", "Estado", ">>[css]", linea, columna, "Estado", tipo.trim()));
                            i=EstadoCss(analisis, i+1);
                            estado=-1;
                            i--;
                            tipo="";
                        } else if (tipo.trim().equals(">>[js]")) {
                            if (analisis[i]==10) linea++;
                            reporte.add(new TokenModel("JavaScript", "Estado", ">>[js]", linea, columna, "Estado", tipo.trim()));
                            i=EstadoJavaScript(analisis, i+1);
                            estado=-1;
                            i--;
                            tipo="";
                        }
                    }
                } else if (estado==-1 && analisis[i]==10) {
                    linea++;
                } else if (estado==-1 && analisis[i]==32) {
                    columna++;
                }

            }
        } else {
            System.out.println("no contiene nada");
        }
        return reporte;
    }
    
    public int EstadoHtml(char[] analizar, int posicion){
        System.out.println("reconocio Html");
        return posicion;
    }
    public int EstadoCss(char[] analizar, int posicion){
        AnalizadorCss temp = new AnalizadorCss();
        ArrayList<TokenModel>resTemp=temp.AnalizarCss(linea, analizar, posicion);
        AgregarALista(resTemp);
        linea=temp.getLinea();
        posicion=temp.getPosicionFinal();
        return posicion;
    }
    public int EstadoJavaScript(char[] analizar, int posicion){
        /*se envia todo el texto a analizar si encuentra un cambio de estado js a html o css regresa  
           la linea en la que termino de analizar y la posicion */
        AnalizadorJavaScript temp = new AnalizadorJavaScript();
        ArrayList<TokenModel>resTemp=temp.AnalizarJavaScript(linea, analizar, posicion);
        AgregarALista(resTemp);
        linea=temp.getLineaFinal();
        posicion=temp.getPosicionFinal();
        return posicion;
    }
    
    public void imprimir(){
        for (int i = 0; i < reporte.size(); i++) {
            System.out.println(reporte.get(i).toString());
        }
    }
    
    private void AgregarALista(ArrayList<TokenModel> tokens){
        for (int i = 0; i < tokens.size(); i++) {
            reporte.add(tokens.get(i));
        }
    }
}
