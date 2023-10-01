import java.io.*;
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
          saida.println(texto);
          break;
        }else if (texto.startsWith("/send file")) {
            String[] comandoSplit = texto.split(" ");
            String nomeSocketDestinatario = comandoSplit[2];
            String caminhoArquivo = comandoSplit[3];

            File arquivo = new File(caminhoArquivo);

          // Enviar o comando e o nome do arquivo para o destinatário
          saida.println("/send file " + nomeSocketDestinatario + " " + arquivo.getName());

          // Aguardar confirmação do servidor ou do destinatário
          BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            // Enviar o conteúdo do arquivo
            FileInputStream fileInputStream = new FileInputStream(arquivo);
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
              socket.getOutputStream().write(buffer, 0, bytesRead);
            }

            fileInputStream.close();
        } else {
          saida.println(texto);
        }
      }

      teclado.close();
      saida.close();
      socket.close();
    } catch (IOException e) {
      System.err.println("Erro ao enviar dados: " + e.getMessage());
    }
  }
}
