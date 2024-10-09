/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.analizador.proyecto1_2024.visual;

import com.analizador.proyecto1_2024.Modelos.TokenModel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

/**
 *
 * @author David
 */
public class VentanaReporte extends JFrame{
    
    private JScrollPane panelFondo;
    private JPanel contenedorPrincipal;
    private JPanel panelMatriz;
    
    public VentanaReporte() {
        this.setBounds(10,10,900,700);
        contenedorPrincipal = new JPanel();
        contenedorPrincipal.setLayout(new BoxLayout(contenedorPrincipal, BoxLayout.Y_AXIS));
        panelMatriz = new JPanel();
    }
    
    public void tipoReporte(ArrayList<TokenModel> reporte, boolean optimizado,ArrayList<TokenModel> eliminados){
        int filas = reporte.size()+1;
        if (optimizado) {
            filas+=eliminados.size()+1;
            panelMatriz.setLayout(new GridLayout(0, 6));
            crearReporteOptimizado();
        }else{
            panelMatriz.setLayout(new GridLayout(0, 6));
            reporteGeneral(reporte);
        }
        
    }
    
    public void crearReporteOptimizado(){
        for (int i = 0; i < 5; i++) {
            
            JLabel label = new JLabel("");
            label.setPreferredSize(new Dimension(125, 50));
            label.setHorizontalAlignment(SwingConstants.CENTER); 
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK)); 
            panelMatriz.add(label);
           // label.setFont(fuente);
        }
    }
    
    public void reporteGeneral(ArrayList<TokenModel> reporteSinOptimizar){
        int filas = reporteSinOptimizar.size()+1; 
        
        JLabel[] encabezados= new JLabel[5];
        String[] tituloEncabezado={"Token", "Expresion", "Lenguaje", "Tipo", "Fila", "Columna"};
        Font fuente = new Font("Courier", Font. BOLD,12);
        for (int i = 0; i < 6; i++) {
            JLabel label = new JLabel(tituloEncabezado[i]);
            label.setPreferredSize(new Dimension(116, 30));
            label.setBackground(Color.red);
            label.setHorizontalAlignment(SwingConstants.CENTER); 
            label.setBorder(BorderFactory.createLineBorder(Color.BLACK)); 
            panelMatriz.add(label);
            label.setFont(fuente);
        }
        
        for (int i = 0; i < filas-1; i++) {
            for (int j = 0; j < 6; j++) {
                JLabel label;
                switch (j) {
                    case 0:
                        label = new JLabel(reporteSinOptimizar.get(i).getLexema());
                        break;
                    case 1:
                        label = new JLabel(reporteSinOptimizar.get(i).getExpresionRegular());
                        break;
                    case 2:
                        label = new JLabel(reporteSinOptimizar.get(i).getLenguaje());
                        break;
                    case 3:
                        label = new JLabel(reporteSinOptimizar.get(i).getTipo());
                        break;
                    case 4:
                        label = new JLabel(reporteSinOptimizar.get(i).getLinea()+"");
                        break;
                    case 5:
                        label = new JLabel(reporteSinOptimizar.get(i).getColumna()+"");
                        break;
                    default:
                        throw new AssertionError();
                }
                label.setPreferredSize(new Dimension(116, 30));
                label.setHorizontalAlignment(SwingConstants.CENTER); 
                label.setBorder(BorderFactory.createLineBorder(Color.BLACK)); 
                panelMatriz.add(label);
            }
            
            
        }
        contenedorPrincipal.add(panelMatriz);
        panelFondo = new JScrollPane(contenedorPrincipal);
        panelFondo.setLocation(0, 100);
        this.add(panelFondo);
    }
}
