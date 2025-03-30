
public class User {
    private String nome;
    private String senha;

    public User(String nome, String senha){
        this.nome = nome;
        this.senha = senha;
    }

    public boolean verificaSenha(String senha_para_verificar){
        if((senha.compareTo(senha_para_verificar)) == 0){
            return true;
        }else{
            return false;
        }
    }

    public String getNome(){
        return nome;

    }
    public String getSenha(){
        return senha;
    }



}