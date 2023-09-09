package nuevo.socket;

import javax.swing.*;

import java.awt.*;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Servidor  {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        MarcoServidor mimarco=new MarcoServidor();

        mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }
}

class MarcoServidor extends JFrame implements Runnable {

    public MarcoServidor(){

        setBounds(1200,300,280,350);

        JPanel milamina= new JPanel();

        milamina.setLayout(new BorderLayout());

        areatexto=new JTextArea();

        milamina.add(areatexto,BorderLayout.CENTER);

        add(milamina);

        setVisible(true);

        Thread mihilo=new Thread(this);

        mihilo.start();

    }



    @Override
    public void run() {
        //System.out.println("estoy a la escucha"); prueba para validar conexión de hilos

        try {
            ServerSocket servidor=new ServerSocket(9999);

            String nick, ip, mensaje;

            PaqueteEnvio paquete_recibido;

            while(true) { //bucle infinito para recibir varios mensajes

                Socket misocket = servidor.accept();

                ObjectInputStream paquete_datos=new ObjectInputStream(misocket.getInputStream());

                paquete_recibido= (PaqueteEnvio) paquete_datos.readObject();

                nick=paquete_recibido.getNick();

                ip=paquete_recibido.getIp();

                mensaje=paquete_recibido.getMensaje();

                areatexto.append("\n  " + nick + ": " + mensaje + " Para " +ip);

                Socket enviaDestinatario=new Socket(ip,9090);

                ObjectOutputStream paqueteReenvio=new ObjectOutputStream(enviaDestinatario.getOutputStream());

                paqueteReenvio.writeObject(paquete_recibido);

                paqueteReenvio.close();

                enviaDestinatario.close();

                //DataInputStream flujo_entrada = new DataInputStream(misocket.getInputStream());

                //String mensaje_texto = flujo_entrada.readUTF();

                //areatexto.append( mensaje_texto + "\n");

                misocket.close();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private	JTextArea areatexto;
}
