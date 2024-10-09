/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.analizador.proyecto1_2024.Modelos;

import java.util.ArrayList;

/**
 *
 * @author David
 */
public class ModeloGeneral {
    ArrayList<TokenModel> listaJavaScript;

    public ModeloGeneral() {
        listaJavaScript = new ArrayList<>();
    }
    
    public void AgregarModeloJavaScript(TokenModel model){
        System.out.println("se agrego a la lista");
        listaJavaScript.add(model);
    }

    public ArrayList<TokenModel> getListaJavaScript() {
        return listaJavaScript;
    }
    
}
