package nuevo.socket;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
//import java.net;



public class Cliente {

    public static void main(String[] args) {
        // TODO Auto-generated method stub

        MarcoCliente mimarco=new MarcoCliente();

        mimarco.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

}


class MarcoCliente extends JFrame{

    public MarcoCliente(){

        setBounds(600,300,280,350);

        LaminaMarcoCliente milamina=new LaminaMarcoCliente();

        add(milamina);

        setVisible(true);
    }

}

class LaminaMarcoCliente extends JPanel implements Runnable{

    public LaminaMarcoCliente(){

        String nick_usuario=JOptionPane.showInputDialog("Nick: ");

        JLabel n_nick=new JLabel("Nick:");

        add(n_nick);

        nick= new JLabel();

        nick.setText(nick_usuario);

        add(nick);

        JLabel texto=new JLabel("Para:");

        add(texto);

        ip=new JTextField(8);

        add(ip);

        campochat=new JTextArea(14,20);

        add(campochat);

        campo1=new JTextField(20);

        add(campo1);

        miboton=new JButton("Enviar");

        EnviaText mievento =new EnviaText();

        miboton.addActionListener(mievento);

        add(miboton);

        Thread mihilo=new Thread(this);

        mihilo.start();

    }

    @Override
    public void run() {
        try {
            ServerSocket servidor_cliente=new ServerSocket(9090);

            Socket cliente;

            PaqueteEnvio paqueteRecibido;

            while (true){

                cliente=servidor_cliente.accept();

                ObjectInputStream flujoentrada=new ObjectInputStream(cliente.getInputStream());

                paqueteRecibido= (PaqueteEnvio) flujoentrada.readObject();

                campochat.append("\n " + paqueteRecibido.getNick() + ": " + paqueteRecibido.getMensaje());
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }


    private class EnviaText implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            //System.out.println(campo1.getText()); poder enviar textos

            campochat.append("\n " + campo1.getText()); //saber quien es el remitente

            try {
                Socket misocket=new Socket("192.168.1.165",9999);

                PaqueteEnvio datos=new PaqueteEnvio();

                datos.setNick(nick.getText());

                datos.setIp(ip.getText());

                datos.setMensaje(campo1.getText());

                ObjectOutputStream paquete_datos=new ObjectOutputStream(misocket.getOutputStream());

                paquete_datos.writeObject(datos);

                misocket.close();

                //DataOutputStream flujo_salida=new DataOutputStream(misocket.getOutputStream());

                //flujo_salida.writeUTF(campo1.getText());

                //flujo_salida.close();


            } catch (IOException ex) {
                System.out.println(ex.getMessage());
            }
        }
    }



    private JTextField campo1, ip;

    private JLabel nick;

    private JTextArea campochat;

    private JButton miboton;

}

class PaqueteEnvio implements Serializable { //convertimos a bites todas las intancias de esta clase
    private String nick, ip, mensaje;

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}