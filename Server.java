import java.io.*;
import java.net.*;

//Classe servidor
public class Server {
    private static final int PORT = 7891;//Constante na pra 7891
    private static final int NUM_THREADS = 4;//Quantidade de CLientes possiveis

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(PORT);// Cria o servidor ouvindo na porta 7891
        System.out.println("Servidor ouvindo na porta " + PORT);

        //Cria as threads para cada client
        for (int i = 0; i < NUM_THREADS; i++) {
            Socket clientSocket = serverSocket.accept();//Abre  canal para aceitaro cliente
            System.out.println("Cliente conectado: " + clientSocket.getInetAddress());

            Thread thread = new Thread(new ClientHandler(clientSocket, i));//Cria uma thread para cada cliente
            thread.start();//Inicia a thread e executa a classe e função dela
        }
    }
}

class ClientHandler implements Runnable {
    private Socket socket;
    private int threadId;

    //inicializa a classe passando o clientSocket e o threadId dele que é o valor de i
    public ClientHandler(Socket socket, int threadId) {
        this.socket = socket;
        this.threadId = threadId;
    }

    public void run() {
        try (

            //socket.getInputStream obtem o fluxo de entrada de dados que chega docliente em bynario
            //InputStreamReader converte os bytes em texto caracters
            //BufferedReander Classe que Lê texto de um fluxo de entrada de caracteres
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //socket.getOutputStream pega o canal de saida de dados
            //Transforma em um escritor de texto
            //O true no final ativa o autoFlush
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String inputLine;
            do {
                System.out.println("Aguardando mensagem do cliente...");
                inputLine = in.readLine();//Le a mensagem do cliente no canal
                
                if (inputLine == null) break;//Sai do loop caso o cliente fecha a conexão sem o comando quit

                System.out.println("Mensagem recebida: " + inputLine);
                String response = inputLine + " -- mensagem do servidor. thread id = " + threadId;
                out.println(response);


            } while (!"quit".equalsIgnoreCase(inputLine));//Verifica se o client enviou quit

            System.out.println("Fechando conexão com cliente " + threadId);
            socket.close();//Fecha o socket do cliente
        } catch (IOException e) {
            System.err.println("Erro na comunicação com cliente " + threadId);
        }
    }
}
