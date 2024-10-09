/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.analizador.proyecto1_2024.Otros;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author David
 */
public class CargarArchivos extends JFileChooser {
    FileNameExtensionFilter imgFilter = new FileNameExtensionFilter("Archivos", "txt"); 
    String titulo = "sin titulo";
    String direccion="";
    
    public ArrayList<String> cargarMiArchivo(Component parent) {
        BufferedReader br = null;
        ArrayList<String> texto = new ArrayList<>();
        this.setFileFilter(imgFilter);
        int result = this.showOpenDialog(parent);
        if (result != JFileChooser.CANCEL_OPTION) {
            texto.add("aceptado");
            try{
                String linea;
                File fileName = this.getSelectedFile();
                titulo=fileName.getAbsolutePath();
                direccion=fileName.getAbsolutePath();
                FileReader fr = new FileReader (fileName);
                br = new BufferedReader(fr);
                while((linea=br.readLine())!=null) texto.add(linea);

            }catch(Exception e){
                System.out.println(e);
            }
            
        }else{
            texto.add("Cancelado");
        }
        
        return texto;
    }
    
    public String devolverTitulo(){
        char[] title = titulo.toCharArray();
        String newTitle="";
        ArrayList<Character> finalTitle= new ArrayList<>();
        for (char c : title) {
            finalTitle.add(new Character(c));
            if (c=='/') {
                finalTitle.clear();
            }
        }
        for (Character character : finalTitle) {
            newTitle+=character;
        }
        
        return newTitle;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getTitulo() {
        return titulo;
    }
    
}
