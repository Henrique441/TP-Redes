import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

public class Users {
    public ArrayList<User> usuarios = new ArrayList<User>();

    //Inicializ alista de usuarios
    public Users() throws IOException {
        FileInputStream arquivo = new FileInputStream("users.txt");
        BufferedReader reader = new BufferedReader(new InputStreamReader(arquivo));
        String linhaNome, linhaSenha;
        System.out.println("construtor");
        while((linhaNome = reader.readLine()) != null){
            linhaSenha = reader.readLine();
            if(linhaSenha == null){
                break;
            }
            System.out.println("construtor Users "+ linhaNome + linhaSenha);
            usuarios.add(new User(linhaNome, linhaSenha));
        }

    }

    //Procura o usuario na lista se não tiver resorna -1 
    public int procuraLogin(String nome){
        System.out.println("nome no procura Login" + nome);
        int indice = -1;
        for(int i = 0; i < usuarios.size(); i++){
            System.out.println("for" + i + " " + usuarios.get(i).getNome());
            if((usuarios.get(i).getNome().strip()).compareTo(nome) == 0){
                System.out.println("nome no procura Login no If" + usuarios.get(i).getNome());
                indice = i;
            }
        }
        return indice;
    }

}


