package Aplicacion;
import javax.swing.*;
import java.awt.*;

public class GUIIniSesion extends JFrame {
    //ventana para iniciar sesión
    private Container body;

    private JPanel divCampoNombreUsuario;
    public JTextField campoNombreUsuario;

        private JPanel divBotonIngresar;
            public JButton botonIngresar;

    GUIIniSesion(Dimension dim){
        this.setTitle("Ingrese un nombre de usuario");
        body=this.getContentPane(); //definimos el cuerpo
        body.setBackground(new Color(92, 255, 189)); //color del cuerpo
        body.setLayout(new BoxLayout(body,BoxLayout.X_AXIS)); //Layout box horizontal

        //El body contiene dos divs acomodados horizontalmente, uno tendrá el campo input y el otro el boton

            divCampoNombreUsuario=new JPanel(); //div en el que se encuentra el campo
            divCampoNombreUsuario.setMaximumSize(new Dimension(dim.width*2/3, dim.height*1/3));//dimension del div
            body.add(divCampoNombreUsuario); //se agrega el div al cuerpo
                campoNombreUsuario=new JTextField(12); //Instanciamos el JTextField
                divCampoNombreUsuario.add(campoNombreUsuario); //se agrega el campo a su div

            divBotonIngresar=new JPanel(); //se instancia el div en que se encuentra el botón
            divBotonIngresar.setMaximumSize(new Dimension(dim.width*1/3, dim.height*1/3));//dimension del div
            body.add(divBotonIngresar); //se agrega el div al cuerpo
                botonIngresar=new JButton(); //se instancia el boton
                botonIngresar.setText("ingresar al chat"); //Se le pone un titulo al boton
                divBotonIngresar.add(botonIngresar); //se agrega el boton a su div

    }
    public  static boolean esEntradaValida(String inputString){ //valida que la entrada del usuario, sea adecuada, (verifica que haga match con una expresión regular)
        return inputString.matches("[a-zA-Z_0-9]*"); //el nombre de usuario solo podra ser de letras y numeros
    }
    public static void muestraAlertaInicioSesion(){ //es para mostra la alerta de que el
        JOptionPane.showMessageDialog(null, "El nombre de usuario debe consistir en letras, números o guión bajo, (espacios y otros signos no están permitidos)");
    }
}
