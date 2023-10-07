import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {
  public static void main(String[] args) throws IOException {
    var socket = new Socket("192.168.0.100", 9000);

    var teclado = new Scanner(System.in);
    var server = new PrintStream(socket.getOutputStream());
    System.out.println("Nome: ");
    String nome = teclado.nextLine();
    server.println(nome);
    System.out.println("Conexão estabelecida com o servidor");


    new Receptor(socket).start();
    new Emissor(socket).start();
  }
}