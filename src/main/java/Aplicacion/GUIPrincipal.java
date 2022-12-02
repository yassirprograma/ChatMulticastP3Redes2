package Aplicacion;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class GUIPrincipal  extends JFrame {
    private JPanel divChat;//layout: box
        private JScrollPane divScrollMsjsRecibidos;
            public  JTextArea areaMsjsRecibidos;//TextArea

    private JPanel divEscribirMensaje; //layout: box
        private JScrollPane divScrollEscribirMsj;
            public JTextArea areaEscribirMsj;

        public JButton btnEnviarMsj;

    public Font fuente;

    public GUIPrincipal(Dimension dim){

        fuente= new Font("Arial",Font.BOLD, 15 ); //fuente para títulos

        Container cuerpoVentana=getContentPane();
        cuerpoVentana.setBackground(new Color(92, 255, 189));
        cuerpoVentana.setLayout(new BoxLayout(cuerpoVentana,BoxLayout.Y_AXIS)); //Dividimos el cuerpo de la ventana en 2 partes verticalmente

        //Componentes del cuerpoVentena:
            divChat=new JPanel();
            divChat.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            divChat.setBorder(new EmptyBorder(10,10,10,10));
            divChat.setBackground(new Color(92, 255, 189));
            divChat.setMaximumSize(new Dimension(dim.width, dim.height*3/4));
            divChat.setLayout(new BoxLayout(divChat,BoxLayout.X_AXIS));  //Box, despliegue vertical
            cuerpoVentana.add(divChat);
            //Componentes del divChat:
                divScrollMsjsRecibidos=new JScrollPane();
                divScrollMsjsRecibidos.setMaximumSize(new Dimension(dim.width, dim.height*3/4));
                divChat.add(divScrollMsjsRecibidos);
                //Componentes del divScrollMsjsRecibidos
                    areaMsjsRecibidos=new JTextArea();
                    areaMsjsRecibidos.setBackground(new Color(203, 239, 229));
                    areaMsjsRecibidos.setEditable(false);
                    areaMsjsRecibidos.setFont(fuente);
                    areaMsjsRecibidos.setBorder(new EmptyBorder(20,20,20,20));
                    divScrollMsjsRecibidos.setViewportView(areaMsjsRecibidos);
                //Terminan componentes del divScrollMsjsRecibidos

            //Terminan componentes del divChat

            divEscribirMensaje=new JPanel();
            divEscribirMensaje.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            divEscribirMensaje.setBorder(new EmptyBorder(10,10,10,10));
            divEscribirMensaje.setMaximumSize(new Dimension(dim.width, dim.height*1/4));
            divEscribirMensaje.setBackground(new Color(92, 255, 189));
            divEscribirMensaje.setLayout(new BoxLayout(divEscribirMensaje,BoxLayout.X_AXIS));
            cuerpoVentana.add(divEscribirMensaje);
            //Componentes del divEscribirMensaje:

                    divScrollEscribirMsj=new JScrollPane();
                    divScrollEscribirMsj.setMaximumSize(new Dimension(dim.width*5/6, dim.height*1/4));
                    divScrollEscribirMsj.setBackground(new Color(92, 255, 189));
                    divScrollEscribirMsj.setBorder(new EmptyBorder(10,10,10,10));
                    divEscribirMensaje.add(divScrollEscribirMsj);
                    //componentes del divScrollEscribirMensaje:
                        areaEscribirMsj=new JTextArea();
                        areaEscribirMsj.setBorder(new EmptyBorder(10,10,10,10));
                        areaEscribirMsj.setMaximumSize(new Dimension(dim.width*5/6, dim.height*1/4));
                        areaEscribirMsj.setFont(fuente);
                        areaEscribirMsj.setBackground(new Color(224, 255, 240));
                        divScrollEscribirMsj.setViewportView(areaEscribirMsj);


                    //terminan divScrollEscribirMensaje:

                    btnEnviarMsj=new JButton();
                    btnEnviarMsj.setText("Enviar mensaje");
                    divEscribirMensaje.add(btnEnviarMsj);

            //Terminan componentes del divEscribirMensaje

        //Terminan componentes del cuerpoVentana

    }

    public  void imprimeSeparador(){
        areaMsjsRecibidos.append("\n_____________________________________________________________________________________________________________________________\n");
    }
    public  static void muestraInstrucciones(){
        JOptionPane.showMessageDialog(null, "Para enviar un mensaje a un destinatario específico, escriba:  <el_nombre_del_usuario_destino> , seguido del cuerpo de su mensaje  ");
    }
    public  static void muestraAdvertencia(){ //para cuando el usuario quiera enviar un mensaje vacío
        JOptionPane.showMessageDialog(null, "Escriba un mensaje válido");
    }


}
