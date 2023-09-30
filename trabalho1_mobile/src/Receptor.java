import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Scanner;

public class Receptor extends Thread {
  private Socket socket;

  public Receptor(Socket socket) {
    this.socket = socket;
  }

  public void run(){
    try {
      var saida = new Scanner(socket.getInputStream());

      while (saida.hasNextLine()) {
        var texto = saida.nextLine();
        var cliente = new PrintStream(socket.getOutputStream());
        if(texto.equals("/users")){
          cliente.println(Servidor.clientList());
        }else if (texto.contains("/send message")){
          String[] textoSplit = texto.split(" ");
          Socket receptor = Servidor.clientes.get(textoSplit[2]);

          StringBuilder mensagem = new StringBuilder();
          for (var i = 3; i < textoSplit.length; i++) {
            mensagem.append(textoSplit[i]).append(" ");
          }

          var clienteReceptor = new PrintStream(receptor.getOutputStream());
          clienteReceptor.println("[" + Servidor.buscaClientKey(socket) + "]: " + mensagem);
        }
        System.out.println(texto);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}