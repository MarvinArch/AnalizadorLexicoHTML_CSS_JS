/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.analizador.proyecto1_2024.visual;

import com.analizador.proyecto1_2024.Analizadores.AnalizadorGeneral;
import com.analizador.proyecto1_2024.Modelos.TokenModel;
import com.analizador.proyecto1_2024.Otros.CargarArchivos;
import com.analizador.proyecto1_2024.Otros.FuncionOptimizar;
import com.analizador.proyecto1_2024.Otros.GuardarArchivo;
import java.awt.Color;
import java.util.ArrayList;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

/**
 *
 * @author David
 */
public class VentanaPrincipal extends JFrame{
    private JMenuBar barra;
    private PanelPrincipal panelEscritura;
    private JMenu archivo;
    private JMenu analisis;
    private JMenuItem cargar;
    private JMenuItem guardar;
    private JMenuItem nuevoArchivo;
    private JMenuItem optimizar;
    private JMenuItem analizar;
    private JMenu reporte;
    private JMenuItem reporteGeneral;
    private JMenuItem reporteOptimizado;
    private String ubicacion;
    private ArrayList<TokenModel> eliminados; // al optimizar se genera un arreglo que contiene componentes de la linea que haya sido eliminada en el caso de los comentarios
    private ArrayList<TokenModel> reporteFinal;//contiene el arreglo de tonkens que se reconocen en la escritura;

    public VentanaPrincipal() {
        DimensionesVentana();
        CrearBarraMenu();
    }
    
    private void DimensionesVentana(){
        eliminados = new ArrayList<>();
        this.setLayout(null);
        this.setBounds(10, 5, 1300, 800);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setTitle("Nuevo Archivo");
        
        //inicializar panel
        panelEscritura = new PanelPrincipal();
        panelEscritura.setBounds(0, 30, 1270, 720);
        this.add(panelEscritura);
        panelEscritura.setBackground(Color.red);
    }
    private void CrearBarraMenu(){
        barra = new JMenuBar();
        barra.setBounds(0, 0, this.getSize().width, 25);
        this.add(barra);
        archivo= new JMenu("Archivo");
        analisis = new JMenu("Analisis");
        cargar= new JMenuItem("Cargar Archivo");
        guardar = new JMenuItem("Guardar Archivo");
        nuevoArchivo = new JMenuItem("Nuevo Archivo");
        optimizar= new JMenuItem("Optimizar");
        analizar= new JMenuItem("Analizar");
        reporte=new JMenu("Reportes");
        reporteGeneral = new JMenuItem("Reporte General");
        reporteOptimizado= new JMenuItem("Reporte Optimizado");
        barra.add(archivo);
        barra.add(analisis);
        barra.add(reporte);
        reporte.add(reporteGeneral);
        reporte.add(reporteOptimizado);
        archivo.add(cargar);
        archivo.add(guardar);
        archivo.add(nuevoArchivo);
        analisis.add(optimizar);
        analisis.add(analizar);
        MenuCargar();
        MenuGuardar();
        NuevoArchivo();
        OptimizarCodigo();
        ReporteGeneral();
        AnalizarTexto();
    }
    
    private void MenuCargar(){
        cargar.addActionListener(cargarArchivo -> {
            if (this.getTitle().equals("Nuevo Archivo")) {
                CargarArchivos nuevo = new CargarArchivos();
                panelEscritura.setAreaEditable(nuevo.cargarMiArchivo(this));
                ubicacion=nuevo.getDireccion();
                this.setTitle(nuevo.devolverTitulo());
            }else{
                int opcion = JOptionPane.showConfirmDialog(this, "desea guardar los cambios");
                if (opcion==0) {
                    GuardarArchivo guardaexistente = new GuardarArchivo();
                    guardaexistente.guardarArchivoExistente(this, panelEscritura.getAreaEditable().getText(), ubicacion);
                }else if(opcion==1){
                    CargarArchivos nuevo = new CargarArchivos();
                    panelEscritura.setAreaEditable(nuevo.cargarMiArchivo(this));
                    ubicacion=nuevo.getDireccion();
                    this.setTitle(nuevo.devolverTitulo());
                }
            }
        });
    }
    
    private void MenuGuardar(){
        guardar.addActionListener(guardarArchivo->{
            if (this.getTitle().equals("Nuevo Archivo")) {
                GuardarArchivo guardaNuevo = new GuardarArchivo();
                guardaNuevo.guardarNuevoArchivo(this, panelEscritura.getAreaEditable().getText());
            }else{
                int opcion = JOptionPane.showConfirmDialog(this, "desea guardar los cambios");
                if (opcion==0) {
                    GuardarArchivo guardaexistente = new GuardarArchivo();
                    guardaexistente.guardarArchivoExistente(this, panelEscritura.getAreaEditable().getText(), ubicacion);
                }else if(opcion==1){
                    this.setTitle("Nuevo Archivo");
                    panelEscritura.getAreaEditable().setText("");
                }
            }
        });
    }
    
    private void NuevoArchivo(){
        nuevoArchivo.addActionListener(crearNuevo->{
            if (this.getTitle().equals("Nuevo Archivo")) {
                if (!panelEscritura.getAreaEditable().equals("")) {
                    int opcion=JOptionPane.showConfirmDialog(this, "Desea Guardar los cambios en el archivo");
                    if (opcion==0) {
                        GuardarArchivo guardaNuevo = new GuardarArchivo();
                        guardaNuevo.guardarNuevoArchivo(this, panelEscritura.getAreaEditable().getText());
                    }else if (opcion==1) {
                        panelEscritura.setAreaEditable(new ArrayList<String>());
                    }else{
                        JOptionPane.showMessageDialog(this, "Cancelado");
                    }
                }
            }else{
                int opcion =JOptionPane.showConfirmDialog(this, "desea guardar los cambios");
                if (opcion==0) {
                    GuardarArchivo guardaexistente = new GuardarArchivo();
                    guardaexistente.guardarArchivoExistente(this, panelEscritura.getAreaEditable().getText(), ubicacion);
                }else if(opcion==1){
                    this.setTitle("Nuevo Archivo");
                    panelEscritura.getAreaEditable().setText("");
                }
            }
        });
    }

    private void OptimizarCodigo() {
        FuncionOptimizar temp= new FuncionOptimizar();
        optimizar.addActionListener(optim->{
            panelEscritura.getAreaEditable().setText(temp.respuesta(panelEscritura.getAreaEditable().getText()));
            eliminados.clear();
            eliminados=temp.getEliminados();
            for (int i = 0; i < eliminados.size(); i++) {
                System.out.println(eliminados.get(i).toString());
            }
        });
    }
    
    private void ReporteGeneral(){
        reporteGeneral.addActionListener(l->{
            VentanaReporte temp = new VentanaReporte();
            temp.tipoReporte(reporteFinal, false, eliminados);
            temp.setVisible(true);
        });
    }

    private void AnalizarTexto() {
        analizar.addActionListener(analizarTexto->{
            //enivar el texto de Jtenxarea para analizar crear los tokens
            AnalizadorGeneral temp = new AnalizadorGeneral();
            reporteFinal=temp.ClasificarTexto(panelEscritura.getAreaEditable().getText());
            
        });
    }
}
