import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

//Classe servidor
public class Server {
    private static final int PORT = 7891;//Constante na pra 7891
    private static final int NUM_THREADS = 20;//Quantidade de CLientes possiveis

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
        serverSocket.close();
    }
}

class ClientHandler implements Runnable {
    private Socket socket;
    private int threadId;
    private Users users;
    

    //inicializa a classe passando o clientSocket e o threadId dele que é o valor de i
    public ClientHandler(Socket socket, int threadId)  throws IOException  {
        this.socket = socket;
        this.threadId = threadId;
        users = new Users();
    }

    public void run() {
        try (
            //Cria o canal de comunicação de saida
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            //Cria o canal de comunicação de entrada
            DataInputStream in = new DataInputStream(socket.getInputStream());
        ) {
            //byte[] buffer = new byte[4096];//Onde os dados vão ficar armazenados
            //Verificação de login do usuario
            loginServer( in, out);
            String inputLine;
            do {
                
                //recebeArquivo("testeClient.txt", buffer, in);
                //enviaArquivo( "teste.txt", buffer, out);

                System.out.println("Aguardando mensagem do cliente...");
                

                inputLine = in.readUTF();//Le a mensagem do cliente no canal
                if (inputLine == null) break;//Sai do loop caso o cliente fecha a conexão sem o comando quit

                System.out.println("Mensagem recebida: " + inputLine);
                String response = inputLine + " -- mensagem do servidor. thread id = " + threadId;
                out.writeUTF(response);
                out.flush();

                
            }while( !("quit".equalsIgnoreCase(inputLine)));//Verifica se o client enviou quit
            
            System.out.println("Fechando conexão com cliente " + threadId);
            socket.close();//Fecha o socket do cliente
        } catch (IOException e) {
            System.err.println("Erro na comunicação com cliente " + threadId);
        }
    }

    //Processamento do login por parte do server
    public void loginServer(DataInputStream in, DataOutputStream out) throws IOException {
        String inputLine;
        int indice;
        inputLine = in.readUTF();
        while((indice = users.procuraLogin(inputLine)) == -1){
            out.writeBoolean(false);
            inputLine = in.readUTF();
        }
        out.writeBoolean(true);//Avisa que encontrou o login
        inputLine = in.readUTF();
        //out.writeBoolean(true);
        while(!(users.usuarios.get(indice).verificaSenha(inputLine))){
            System.out.println("Entrou Loop login");
            
            out.writeBoolean(false);
            inputLine = in.readUTF();
        }
        out.writeBoolean(true);//Avisa que a senha está correta
        
    }

    public static void enviaArquivo(String nome, byte[] buffer, DataOutputStream out){
        
        try{
            int quantBytes;//Quantidade de bytes que realmente foram lidos
            File arqTeste = new File("teste.txt");
            long tam = arqTeste.length();
            
            FileInputStream arquivo = new FileInputStream("teste.txt");
            
            out.writeLong(tam);
            
            //Loop que transfere os dados do arquivo para o canal
            while((quantBytes = arquivo.read(buffer)) != -1){
                out.write(buffer, 0, quantBytes);//escreve os bytes no canal do cliente
                out.flush();
            }
            arquivo.close();
            
        }catch (IOException e) {
            System.err.println("Erro de leitura/escrita: " + e.getMessage());
        }

    }

    public static void recebeArquivo(String nome, byte[] buffer, DataInputStream in){
        
        try{
            long tam;
            tam = in.readLong();
            
            long tamRestante = tam;
            FileOutputStream arquivo = new FileOutputStream(nome);
            
            int quantBytes = 0;//Quantidade de bytes que realmente foram lidos
            //Loop que transfere os dados do canal para o arquivo
            while( tamRestante > 0 ){
                quantBytes = in.read(buffer);
                tamRestante -= quantBytes;
                
                System.out.println("Lendo " + quantBytes + " bytes");
                arquivo.write(buffer, 0, quantBytes);
                
            }
            System.out.println("Lendo " + quantBytes + " bytes");
            System.out.println("saiu do LOOP");
            arquivo.close();
        } catch (FileNotFoundException e) {
            System.err.println("Arquivo não encontrado: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Erro de leitura/escrita: " + e.getMessage());
        }
    }


}
