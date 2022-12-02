package Aplicacion;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import java.awt.event.*;
public class AplicacionChat {

    //Como no tiene constructor (no se instancía), todos los atributos son static para poder usarlos
    public static GUIPrincipal interfazChat;
    public static GUIIniSesion interfazInicioSesion;
    public static   backendChat backend;
    public static boolean sesionIniciada; //para indicar si la sesión se ha iniciado

    public static String nombreUsuario;

    public static void main(String[] args){
        sesionIniciada=false;

        //Lanzamos la GUI para iniciar sesión (la ventana donde el usuario ingresa el nombre de usuario):
        lanzarGUIInicioSesion();

        //Escucha para el botón de la interfaz de inicio de sesión:
        interfazInicioSesion.botonIngresar.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                nombreUsuario=interfazInicioSesion.campoNombreUsuario.getText(); //obtenemos el nombre de usuario

                if(GUIIniSesion.esEntradaValida(nombreUsuario) && !nombreUsuario.equals("")){
                    interfazInicioSesion.dispose(); //cerramos la interfaz de inicio de sesión
                    sesionIniciada=true;
                    System.out.println("La sesión se ha iniciado");

                    lanzarGUIPrincipal(); //lanzamos la interfaz
                    interfazChat.setTitle("Chat de "+ nombreUsuario); //ponemos titulo a la ventana


                    backend=new backendChat(nombreUsuario, interfazChat); //instanciamos al clienteT

                    GUIPrincipal.muestraInstrucciones();//mostramos una breve instrucción al usuario
                }else{
                    GUIIniSesion.muestraAlertaInicioSesion();
                }
            }
        });

    }

    public static void lanzarGUIPrincipal(){
        Dimension dimGUIPrincipal = Toolkit.getDefaultToolkit().getScreenSize(); //tamaño completo
        dimGUIPrincipal.width=1080;
        dimGUIPrincipal.height=720; //establecemos dimensiones adecuadas

        interfazChat =new GUIPrincipal(dimGUIPrincipal); //instanciamos una interfaz (class interfazCliente)
        interfazChat.setDefaultCloseOperation(GUIPrincipal.EXIT_ON_CLOSE);
        interfazChat.setSize(dimGUIPrincipal);
        interfazChat.setResizable(false);
        interfazChat.setVisible(true);

    }

    public static void lanzarGUIInicioSesion(){
        Dimension dimGUIInicioSesion =Toolkit.getDefaultToolkit().getScreenSize();
        dimGUIInicioSesion.height=dimGUIInicioSesion.height*1/7;
        dimGUIInicioSesion.width=dimGUIInicioSesion.width*1/4;

        interfazInicioSesion= new GUIIniSesion(dimGUIInicioSesion);
        interfazInicioSesion.setDefaultCloseOperation(GUIIniSesion.DISPOSE_ON_CLOSE);
        interfazInicioSesion.setSize(dimGUIInicioSesion);
        interfazInicioSesion.setResizable(false);
        interfazInicioSesion.setVisible(true);
    }

}
