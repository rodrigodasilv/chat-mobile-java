import java.io.*;
import java.net.Socket;
import java.util.Arrays;
import java.util.Objects;
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
        }else if(texto.equals("/sair")){
          Servidor.clientes.remove(Servidor.buscaClientKey(socket));
        }else if(texto.startsWith("/send file")) {
          String[] mensagemSplit = texto.split(" ");
          String nomeArquivo = mensagemSplit[3];

          FileOutputStream fileOutputStream = new FileOutputStream(nomeArquivo);
          byte[] buffer = new byte[1024];
          int bytesRead;

          InputStream inputStream = socket.getInputStream();
          while ((bytesRead = inputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, bytesRead);
          }

          fileOutputStream.close();

          String nomeDestinatario = mensagemSplit[2];

          // Reencaminhar o arquivo para o destinatário

          Socket destinatario = Servidor.clientes.get(nomeDestinatario);

          File arquivo = new File(nomeArquivo);

          FileInputStream fileInputStream = new FileInputStream(arquivo);

          OutputStream outputStream = destinatario.getOutputStream();

          var saida2 = new PrintStream(outputStream);
          saida2.println("/receive file " + nomeArquivo);
          bytesRead=0;
          while ((bytesRead = fileInputStream.read(buffer)) != -1) {
            System.out.println("Escrevendo arquivo!");
            outputStream.write(buffer, 0, bytesRead);
          }
          fileInputStream.close();
          outputStream.close();

        }else if(texto.startsWith("/receive file")){
          String[] mensagemSplit = texto.split(" ");
          String nomeArquivo = mensagemSplit[2];

          FileOutputStream fileOutputStream = new FileOutputStream(nomeArquivo);
          byte[] buffer = new byte[1024];
          int bytesRead;

          InputStream inputStream = socket.getInputStream();
          while ((bytesRead = inputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, bytesRead);
          }
          fileOutputStream.close();
        }
        System.out.println(texto);
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
