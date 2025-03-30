import java.io.*;
import java.net.Socket;
import java.util.Arrays;


public class Client {
    private static final String SERVER_IP = "127.0.0.1";
    private static final int PORT = 7891;

    public static void main(String[] args) throws IOException {
        Socket socket = new Socket(SERVER_IP, PORT);//Cria um novo socket no client com o ip e porta do server
        
        //Cria o canal de comunicação de saida
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        //Cria o canal de comunicação de entrada
        DataInputStream in = new DataInputStream(socket.getInputStream());

        //Faz a leitura do teclado
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        //byte[] buffer = new byte[4096]; 
        loginClient( in, out);
        String userInput;
        do {

            

            

            //recebeArquivo("testeClient.txt", buffer, in);
            //enviaArquivo( "teste.txt", buffer, out);
            System.out.print("Digite alguma coisa (para sair, digite quit): ");
            userInput = stdIn.readLine();//Lê o teclado e envia
            out.writeUTF(userInput);//Envia para o servidor
            out.flush();

            String response = in.readUTF();//Recebe a resposta do servidor e printa na tela
            System.out.println("Data received: " + response);
            

        } while (!"quit".equalsIgnoreCase(userInput));

        System.out.println("Fechando conexão...");
        socket.close();
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

    public static void loginClient(DataInputStream in, DataOutputStream out) throws IOException{
        BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
        String userInput;
        
        System.out.println("Digite o login: ");
        userInput = stdIn.readLine();//Lê o teclado
        out.writeUTF(userInput);
        while(!(in.readBoolean())){
            System.out.println("Digite o login: ");
            userInput = stdIn.readLine();//Lê o teclado
            out.writeUTF(userInput);
        }
        System.out.println("Digite a senha: ");
        userInput = stdIn.readLine();//Lê o teclado
        out.writeUTF(userInput);
        
        while(!(in.readBoolean())){
            System.out.println("Senha incorreta!!");
            System.out.println("Digite o senha: ");
            userInput = stdIn.readLine();//Lê o teclado
            out.writeUTF(userInput);
        }


    }


}
