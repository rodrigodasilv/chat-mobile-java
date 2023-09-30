import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;

public class Emissor extends Thread {
  private Socket socket;

  public Emissor(Socket socket) {
    this.socket = socket;
  }

  public void run() {
    try {
      var teclado = new Scanner(System.in);
      var saida = new PrintStream(socket.getOutputStream());

      while (teclado.hasNextLine()) {
        var texto = teclado.nextLine();

        if (texto.equals("/sair")) {
          Servidor.clientes.remove(Servidor.buscaClientKey(socket));
          break;
        }
        saida.println(texto);
      }

      teclado.close();
      saida.close();
      socket.close();
    } catch (IOException e) {
      System.err.println("Erro ao enviar dados: " + e.getMessage());
    }
  }
}
