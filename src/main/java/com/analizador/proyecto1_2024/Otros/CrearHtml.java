/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.analizador.proyecto1_2024.Otros;

import com.analizador.proyecto1_2024.Modelos.TokenModel;
import java.util.ArrayList;

/**
 *
 * @author David
 */
public class CrearHtml {
    private ArrayList<TokenModel> modeloTemporal;
    
    public String CrearHtml(ArrayList<TokenModel> modelo){
        modeloTemporal = new ArrayList<>();
        int linea=0;
        String retorno="<!DOCTYPE html>\n" + "<html>\n" +"<head>\n" + "\t<meta charset=\"utf-8\">\n" +"\t<meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                    "\t<title>Document</title>\n" +"\t<style >";
        devolverCss(modelo);
        for (int i = 1; i < modeloTemporal.size(); i++) {
            if (linea!=modeloTemporal.get(i).getLinea()) {
                retorno+="\n\t\t";
                linea=modeloTemporal.get(i).getLinea();
                int agregartabulaciones=(modeloTemporal.get(i).getColumna()-1)/4;
                for (int j = 0; j < agregartabulaciones; j++) {
                    retorno+="\t";
                }
                retorno+=modeloTemporal.get(i).getLexema().trim()+" ";
            }else{
                retorno+=modeloTemporal.get(i).getLexema().trim()+" ";
            }
        }
        retorno+="\n\t</style>"+"\n\t<script >\n";
        modeloTemporal.clear();
        devolverJs(modelo);
        for (int i = 1; i < modeloTemporal.size(); i++) {
            if (linea!=modeloTemporal.get(i).getLinea()) {
                retorno+="\n\t\t";
                linea=modeloTemporal.get(i).getLinea();
                int agregartabulaciones=(modeloTemporal.get(i).getColumna()-1)/4;
                for (int j = 0; j < agregartabulaciones; j++) {
                    retorno+="\t";
                }
                retorno+=modeloTemporal.get(i).getLexema().trim()+" ";
            }else{
                if (!modeloTemporal.get(i).getTipo().equalsIgnoreCase("Estado") && !modeloTemporal.get(i).getTipo().equalsIgnoreCase("comentario")) {
                    retorno+=modeloTemporal.get(i).getLexema().trim()+" ";
                }
            }
        }
        retorno+="\n\t</script>"+"\n</head>"+"\n<body>\n";
        modeloTemporal.clear();
        devolverHtml(modelo);
        for (int i = 1; i < modeloTemporal.size(); i++) {
            if (linea!=modeloTemporal.get(i).getLinea()) {
                retorno+="\n\t\t";
                linea=modeloTemporal.get(i).getLinea();
                int agregartabulaciones=(modeloTemporal.get(i).getColumna()-1)/4;
                for (int j = 0; j < agregartabulaciones; j++) {
                    retorno+="\t";
                }
                retorno+=modeloTemporal.get(i).getLexema().trim()+" ";
            }else{
                if (!modeloTemporal.get(i).getTipo().equalsIgnoreCase("Estado") && !modeloTemporal.get(i).getTipo().equalsIgnoreCase("comentario")) {
                    retorno+=modeloTemporal.get(i).getLexema().trim()+" ";
                }
            }
        }
        retorno+="\n</body>\n" +"</html>";
        return retorno;
    }
    
    public ArrayList<TokenModel> devolverCss(ArrayList<TokenModel> modelo){
        for (int i = 0; i < modelo.size(); i++) {
            if (modelo.get(i).getLenguaje().equalsIgnoreCase("css")) {
                modeloTemporal.add(modelo.get(i));
            }
        }
        return modeloTemporal;
    }
    public ArrayList<TokenModel> devolverJs(ArrayList<TokenModel> modelo){
        for (int i = 0; i < modelo.size(); i++) {
            if (modelo.get(i).getLenguaje().equalsIgnoreCase("javascript")) {
                modeloTemporal.add(modelo.get(i));
            }
        }
        return modeloTemporal;
    }
    public ArrayList<TokenModel> devolverHtml(ArrayList<TokenModel> modelo){
        for (int i = 0; i < modelo.size(); i++) {
            if (modelo.get(i).getLenguaje().equalsIgnoreCase("html")) {
                modeloTemporal.add(modelo.get(i));
            }
        }
        return modeloTemporal;
    }
}
