package Aplicacion;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.DatagramPacket;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class backendChat {
    public  String nombreUsuario;
    public  final String dirMulticast  = "230.0.0.1"; //dir clase D valida, grupo al que nos vamos a unir
    private   final int ptoMulticast = 9013;//puerto multicast
    private MulticastSocket socketChat;

    private InetAddress grupoMulticast;


    public Thread hiloEscucha;


    //Elementos de la interfaz gráfica que deben ser usados por el backend:
    public  GUIPrincipal interfazChat; //Composición interfaz->backend, para que desde este backend podamos modificar cosas de la interfaz



    backendChat(String name, GUIPrincipal GUIChat){
        //Atributos:
        interfazChat=GUIChat;

        nombreUsuario=name;

        try {
            socketChat=new MulticastSocket(ptoMulticast); //instanciamos el socket
            grupoMulticast=InetAddress.getByName(dirMulticast); //se intenta resolver la dirección multicast
            socketChat.joinGroup(grupoMulticast); //unimos nuestro socket al grupo multicast

        }catch (Exception e){
            e.printStackTrace();
        }

        //enviamos un saludo
        enviaSaludo();

        //Ejecutamos el hilo de escucha:
        hiloEscucha=new escuchaMensajes();
        hiloEscucha.start();

        //Agregamos el evento del botón que envía mensajes:
        interfazChat.btnEnviarMsj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                //Cuando se da click, mandamos a llamar al hilo de envio de mensajes
                String textoTextArea=interfazChat.areaEscribirMsj.getText().trim(); //leemos el texto escrito en el JTextArea, y le quitamos espacios al principio y al final usando trim


                if(!textoTextArea.equals("") && textoTextArea!=null){ //

                    String mensajeConMetadatos=agregaMetadatos(textoTextArea); //llamamos a la función que agrega metadatos al mensaje (coloca el origen y destino del mensaje)

                    enviaMensaje envio=new enviaMensaje(mensajeConMetadatos); //Creamos el hilo de envío
                    envio.start();  //ponemos el estilo en estado Runnable

                    interfazChat.areaEscribirMsj.setText("");//limpiamos el area de escritura del mensaje

                }else {
                    GUIPrincipal.muestraAdvertencia();//MOSTRAMOS LA ADVERTENCIA PARA QUE ESCRIBA AL MENOS UN CARACTER EN EL TEXT AREA
                }
            }
        });

    }

    public void enviaSaludo (){ //para enviar un saludo a nuestro
        String saludo="::::::::::El usuario "+nombreUsuario+" se ha unido al chat";
        String mensaje=agregaMetadatos(saludo);
        System.out.println(mensaje);
        enviaMensaje env=new enviaMensaje(mensaje);
        env.start();

        interfazChat.areaMsjsRecibidos.append(":::::::::::::Te has unido al chat::::::::::");
        interfazChat.imprimeSeparador();
    }

    public  class escuchaMensajes extends Thread {
        public void run() { //Cuerpo de ejecucion del hilo que Escucha mensajes
            while (true) {


                byte[] bufferFlujoEntrada = new byte[7000]; //buffer para el flujo de entrada del socket

                DatagramPacket packetRecibido = new DatagramPacket(bufferFlujoEntrada, bufferFlujoEntrada.length); //instanciamos un espacio para el paquete que se recibirá en el buffer de entrada

                try {
                    socketChat.receive(packetRecibido); //recibimos el paquete mediante la operación bloqueante receive
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                byte[] bytesRecibidos=packetRecibido.getData(); //obtenemos el cuerpo del datagrama recibido
                //paquete recibido, pero el cuerpo del datagrama está en formato de bytes


                String mensajeRecibido=new String(bytesRecibidos,0,packetRecibido.getLength(), Charset.forName(("ISO-8859-1"))); //pasamos a string los bytes del cuerpo del datagrama (pasamos el mensaje a formato string)



                //packetRecibido.getlenghth devuelve el tamaño del cuerpo del datagrama

                /*El mensaje va desde la posición 0 del arreglo de bytes recibidos hasta la posición packetRecibido,
                mucho ojo, porque como nuestro buffer es de 7000 bytes,
                 si no indicamos el rango que queremos convertir a String,
                 entonces la String estará llena de espacios vacíos hasta la posición 7500*/


                System.out.println("Mensaje recibido: "+mensajeRecibido);

                String emisorMsj=limpiarMetacaracteres(getEmisor(mensajeRecibido));
                String destinatarioMsj=limpiarMetacaracteres(getDestinatario(mensajeRecibido));
                String cuerpoMsj=getCuerpoMensaje(mensajeRecibido);

                System.out.println("Mensaje separado: ");
                System.out.println("Emisor: "+emisorMsj);
                System.out.println("Destinatario: "+destinatarioMsj);
                System.out.println("Cuerpo: "+ cuerpoMsj);


                if(destinatarioMsj.equals("todos") ){ //si es para todos,
                    interfazChat.areaMsjsRecibidos.append("De "+emisorMsj+": "+cuerpoMsj); //mostramos el mensaje en el area
                    interfazChat.imprimeSeparador();

                } else if(destinatarioMsj.equals(nombreUsuario) || emisorMsj.equals(nombreUsuario)){ //si el destinatario es este usuario, entonces se le muestra el mensaje

                    //Si no es para todos, solo lo mostramos al destinatario (indicando que es mensaje directo)
                    interfazChat.areaMsjsRecibidos.append("De "+emisorMsj+" para "+ destinatarioMsj+" (Msj directo) "+": "+cuerpoMsj); //mostramos el mensaje en el area
                    interfazChat.imprimeSeparador();
                }

            }
        }
    }


    public  class enviaMensaje extends   Thread{
        String mensaje;
        enviaMensaje(String msj){
            mensaje=msj;
        }

        public void run(){ //Cuerpo de ejecucion del hilo que Envia mensajes

            byte []bytesMensaje=mensaje.getBytes(); //obtenemos los bytes de la cadena

            DatagramPacket packet = new DatagramPacket(bytesMensaje,mensaje.length(), grupoMulticast , ptoMulticast);

            try {
                socketChat.send(packet);
                System.out.println("Enviando: '"+ mensaje+"'  con un TTL= "+socketChat.getTimeToLive());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public String agregaMetadatos(String msj){
        /*
        * Un mensaje tendrá su cuerpos y sus metadatos
        * Los metadatos serán el usuario origen y el usuario destino
        * El usuario emisor irá al principio con la siguiente notación:   <%usuarioemisor%>
        * El usuario destino irá después con la siguiente notacion: <usuariodestino>
        * El cuerpo del mensaje irá después de los dos metadatos anteriores
        *
        * La cadena del mensaje quedaría:
        * <%Usuario_emisor%><usuario_destino> cuerpo_del_mensaje
        *
        *
        * En caso de que al ingresar el mensaje no se haya especificado un destino, entonces por defecto se colocará
        * el destino <todos>
        * */

        String tmp;

        if(tieneDestinatarioEspecifico(msj)){
            tmp="<%"+nombreUsuario+"%>" +msj; //agregamos unicamente el origen
        }else {
            //pero si no tiene destinatario especifico, entonces le agregamos por defecto, <todos>
            tmp=   "<%"+nombreUsuario+"%>"  +  "<todos>" + msj;
        }

        return tmp;
    }
    public boolean tieneDestinatarioEspecifico(String msj){ //para saber si un mensaje escrito por el usuario tiene destinatario especifico
        Pattern patronPicoParentesis=Pattern.compile("<([a-zA-Z_0-9]*)+>",Pattern.CASE_INSENSITIVE);
        Matcher matcher =patronPicoParentesis.matcher(msj);


        return matcher.find(); //regresamos si en el mensaje se especificó un <usuario> al principio del mensaje
    }

    public String getDestinatario(String msj){

        Pattern patronPicoParentesis=Pattern.compile("<([a-zA-Z_0-9])+>",Pattern.CASE_INSENSITIVE);
        Matcher matcher =patronPicoParentesis.matcher(msj);

        matcher.find();

        return matcher.group(); //devolvemos la substring que coincida con el patrón dado <usuario_destino> (para identificar el usuario destino)
    }


    public String getEmisor(String msj){
        Pattern patronPicoParentesis=Pattern.compile("<%([a-zA-Z_0-9])+%>",Pattern.CASE_INSENSITIVE);
        Matcher matcher =patronPicoParentesis.matcher(msj);

        matcher.find();

        return matcher.group(); //devolvemos la substring que coincida con el patrón dado
    }

    public String limpiarMetacaracteres(String txt){
           //Elimina los metacaractares que se usaron para definir al emisor y destinatario
            String tmp;
            tmp=txt.replaceAll("<%","");
            tmp=tmp.replaceAll("%>","");
            tmp=tmp.replaceAll("<","");
            tmp=tmp.replaceAll(">","");
            return tmp;
    }
    public String getCuerpoMensaje(String msj){
        //El cuerpo es complementario a los metadatos, es decir, si borramos los metadatos, quedaría únicamente el cuerpo

        String destino=getDestinatario(msj);
        String emisor=getEmisor(msj);

        String cuerpo=msj.replaceAll(destino,""); //quitamos el metadato del destino
        cuerpo=cuerpo.replaceAll(emisor, ""); //quitamos metadato del emisor

        //entonces queda únicamente el puro cuerpo
        return cuerpo;

    }

}
