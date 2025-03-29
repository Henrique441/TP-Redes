import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


public class Client {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int PORT = 7891;

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(SERVER_IP, PORT);//Cria um novo socket no client com o ip e porta do server
        
        //socket.getInputStream obtem o fluxo de entrada de dados que chega do server em binario
        //InputStreamReader converte os bytes em texto caracters
        //BufferedReander Classe que Lê texto de um fluxo de entrada de caracteres 
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        //socket.getOutputStream pega o canal de saida de dados
        //Transforma em um escritor de texto
        //O true no final ativa o autoFlush
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

        //Faz a leitura do teclado
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));

        String userInput;
        do {
            System.out.print("Digite alguma coisa (para sair, digite quit): ");
            userInput = stdIn.readLine();//Lê o teclado e envia
            out.println(userInput);//Envia para o servidor

            String response = in.readLine();//Recebe a resposta do servidor e printa na tela
            System.out.println("Data received: " + response);

        } while (!"quit".equalsIgnoreCase(userInput));

        System.out.println("Fechando conexão...");
        socket.close();
    }
}
