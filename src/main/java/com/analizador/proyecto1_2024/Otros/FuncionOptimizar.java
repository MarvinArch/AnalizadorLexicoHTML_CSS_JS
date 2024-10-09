/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.analizador.proyecto1_2024.Otros;

import com.analizador.proyecto1_2024.Modelos.TokenModel;
import java.util.ArrayList;
import lombok.Data;

/**
 *
 * @author David
 */
@Data
public class FuncionOptimizar {
    
    public ArrayList<TokenModel> eliminados;
    
    public String respuesta(String texto){
        String devuelvo="";
        eliminados = new ArrayList<>();
        char[] quitarEspacios=texto.toCharArray();
        ArrayList<String> separacionLineas= new ArrayList<>();
        String linea="";
        for (int i = 0; i < quitarEspacios.length; i++) {
            linea+=quitarEspacios[i];
            if (quitarEspacios[i]==10) {
                separacionLineas.add(linea);
                linea="";
            }
            if (i==quitarEspacios.length-1) {
                separacionLineas.add(linea);
                linea="";
            }
        }
        ArrayList<String> espaciosBorrados= new ArrayList<>();
        separacionLineas.stream().forEach(token->{
            if (!token.trim().equals("")) {
                espaciosBorrados.add(token);
            }
        });
        
        ArrayList<String> sinComentarios=buscandoCOmentarios(espaciosBorrados);
        for (int i = 0; i < sinComentarios.size(); i++) {
            devuelvo+=sinComentarios.get(i);
        }
        return devuelvo;
    }
    
    private ArrayList<String> buscandoCOmentarios(ArrayList<String> texto){
        String lenguaje="";
        int estado=-1;
        int columna=0;
        for (int i = 0; i < texto.size(); i++) {
            if (texto.get(i).trim().equals(">>[html]")) {
                lenguaje="HTML";
            }else if (texto.get(i).trim().equals(">>[css]")) {
                lenguaje="CSS";
            }else if (texto.get(i).trim().equals(">>[js]")) {
                lenguaje="Javascript";
            }else{
                char [] buscaComentario = texto.get(i).toCharArray();
                String acumulado="";
                estado=-1;
                for (int j = 0; j < buscaComentario.length; j++) {
                    
                    if (estado==-1 && buscaComentario[j]==47) {
                        estado=j;
                    }else if (estado !=-1 && estado==j-1 && buscaComentario[j]==47) {
                        if (!acumulado.trim().equals("")) {
                            eliminados.add(new TokenModel(lenguaje,"indefinido",acumulado,i,0,"Ni idea que es","por definir su expresion regular"));
                        }
                        acumulado="//";
                        columna=j-1;
                    }else{
                       acumulado+=buscaComentario[j]; 
                    }
                    
                    if (estado!=-1 && j==buscaComentario.length-1) {
                        eliminados.add(new TokenModel(lenguaje,"comentario", acumulado,i+1,columna, "Comentario","//.*"));
                    }
                }
            }
        }
        ArrayList<String> elimandoCOmentarios= new ArrayList<>();
        texto.stream().forEach(comentario->{
            if (!comentario.contains("//")) {
                elimandoCOmentarios.add(comentario);
            }
        });
        
        return elimandoCOmentarios;
    }
}
