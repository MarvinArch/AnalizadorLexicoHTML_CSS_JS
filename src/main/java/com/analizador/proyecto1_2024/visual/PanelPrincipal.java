/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.analizador.proyecto1_2024.visual;

import com.analizador.proyecto1_2024.Otros.NumerarLineas;
import java.util.ArrayList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author David
 */
public class PanelPrincipal extends JScrollPane{
    JTextArea areaEditable;
    NumerarLineas numeros;
    
    public PanelPrincipal() {
        areaEditable = new JTextArea();
        this.add(areaEditable);
        numeros = new NumerarLineas(areaEditable);
        this.setViewportView(areaEditable);
        this.setRowHeaderView(numeros);
        
    }
    
    public int[] contadorLinea(String texto, int posicion){
        char espacio[]= texto.toCharArray();
        int posiciones[]=new int[2];
        posiciones[0]=0;
        posiciones[1]=0;
        int temp=0;
        for (int i = 0; i <posicion; i++) {
            if (espacio[i]=='\n') {
                posiciones[0]++;
                temp=i;
            }
        }
        posiciones[1]=posicion-temp;
        if(posiciones[0]==0) posiciones[1]++;
        return posiciones;
    }

    public JTextArea getAreaEditable() {
        return areaEditable;
    }
    
    public void editarTextArea(ArrayList<String> textoRecib){
        String textoIngresado="";
        for (int i = 0; i < textoRecib.size(); i++) {
            textoIngresado+=textoRecib.get(i)+"\n";
        }
        areaEditable.setText(textoIngresado);
    }
    
    public void setAreaEditable (ArrayList<String>texto){
        String agregar="";
        for (int i = 0; i < texto.size(); i++) {
            agregar+=texto.get(i)+"\n";
        }
        areaEditable.setText(agregar);
    }
}
