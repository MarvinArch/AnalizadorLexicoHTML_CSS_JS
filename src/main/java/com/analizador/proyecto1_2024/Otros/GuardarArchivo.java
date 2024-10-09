/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.analizador.proyecto1_2024.Otros;

import java.awt.Component;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 *
 * @author David
 */
public class GuardarArchivo extends JFileChooser{
    String ubicacion = null;
    FileNameExtensionFilter imgFilter = new FileNameExtensionFilter("Archivos", "txt", "py");
    
    public void guardarNuevoArchivo(Component parent, String contenido){
        this.setFileFilter(imgFilter);
        int eleccion=this.showSaveDialog(parent);
        if (eleccion == 0) {
            ubicacion = this.getSelectedFile().getAbsolutePath();
            try {
                if (ubicacion.contains(".py") || ubicacion.contains(".txt")) {
                    ubicacion=ubicacion;
                }else{
                    ubicacion=ubicacion+".py";
                }
                File file = new File(ubicacion);
                // Si el archivo no existe es creado
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileWriter fw = new FileWriter(file);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(contenido);
                bw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    public void guardarArchivoExistente(Component parent, String contenido, String ubicacion) {

        this.ubicacion = ubicacion;
        try {
            
            File file = new File(ubicacion);
            // Si el archivo no existe es creado
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(contenido);
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getUbicacion() {
        return ubicacion;
    }
    
    public String devolverTitulo(){
        char[] title = ubicacion.toCharArray();
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
}
