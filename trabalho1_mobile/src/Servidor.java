import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Servidor {
    public static Map<String,Socket> clientes = new HashMap<String, Socket>();
  public static void main(String[] args) throws IOException {
    var servidor = new ServerSocket(9000);
    System.out.println("Servidor iniciado");

   while (true) {
     var socket = servidor.accept();
     System.out.println("Nova conexão estabelecida");
     var nome = new Scanner(socket.getInputStream()).nextLine();
    clientes.put(nome, socket);
       Servidor.log(socket.getInetAddress(), nome);
     new Receptor(socket).start();
     new Emissor(socket).start();
   }
  }
  public static void log(InetAddress IP, String name) throws IOException {
      BufferedWriter br = new BufferedWriter(new FileWriter("serverLogs.txt", true));
      LocalDateTime agora = LocalDateTime.now();
      DateTimeFormatter horarioFormatado = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
      String stringHorario = agora.format(horarioFormatado);
      String message = "[" + name + "]" + "[" + stringHorario + "] Conexão estabelecida!";
      br.write(message);
      br.newLine();
      br.flush();
  }

  public static Set clientList(){
      return Servidor.clientes.keySet();
  }

  public static String buscaClientKey(Socket socket) {
      for (Map.Entry<String, Socket> entry : Servidor.clientes.entrySet()) {
          if (entry.getValue().equals(socket)) {
              return entry.getKey();
          }
      }
      return null;
  }
}
