import Banco.DB;
import redis.clients.jedis.Jedis;

import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        PreparedStatement pState = null;
        Statement state = null;
        ResultSet result = null;
        Usuario usuario = new Usuario();
        Jedis jedis = new Jedis();
        String email;
        String nome;
        String rascunho;
        int x;

        Connection conexao = DB.abrirConexao();
        do {
            System.out.println("=================== MENU ===================");
            System.out.println("Digite 1 para Inserir um Usuário");
            System.out.println("Digite 2 para Exibir os Dados de Todos os Usuário");
            System.out.println("Digite 3 para Modificar o Nome de um Usuário");
            System.out.println("Digite 4 para Deletar um Usuário");
            System.out.println("Digite 5 para Escrever uma Postagem");
            System.out.println("Digite 6 para Ler a Postagem");
            System.out.println("Digite 7 para Encerrar o Programa:");
            Scanner scan = new Scanner(System.in);
            Scanner scan2 = new Scanner(System.in);
            x = scan.nextInt();

            switch (x) {
                case 1:
                    try {
                        String sql = "INSERT INTO usuario (nome, email) VALUES (?, ?)";
                        pState = conexao.prepareStatement(sql);

                        System.out.println("Informe o nome do usuário:");
                        scan.nextLine();
                        nome = scan.nextLine();
                        usuario.setNome(nome);

                        System.out.println("Informe o email:");
                        email = scan2.next();
                        usuario.setEmail(email);

                        pState.setString(1, usuario.getNome());
                        pState.setString(2, usuario.getEmail());

                        pState.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        DB.fecharStatement(pState);
                    }
                break;
                case 2:
                    try {
                        state = conexao.createStatement();
                        result = state.executeQuery("SELECT * FROM usuario");

                        while (result.next()) {
                            System.out.println(result.getString("nome") + ", " + result.getString("email"));
                        }
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        DB.fecharResultSet(result);
                        DB.fecharStatement(state);
                    }
                break;
                case 3:
                    try {
                        scan.nextLine();
                        scan2.nextLine();
                        String sql = "UPDATE usuario SET nome = ? WHERE (email = ?)";
                        pState = conexao.prepareStatement(sql);

                        System.out.println("Informe o Email do Usuário");
                        email = scan2.next();

                        System.out.println("Informe o Novo Nome do Usuário");
                        nome = scan.nextLine();

                        pState.setString(1, nome);
                        pState.setString(2, email);

                        pState.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        DB.fecharStatement(pState);
                    }
                break;
                case 4 :
                    try {
                        scan2.nextLine();
                        String sql = "DELETE FROM usuario WHERE (email = ?)";
                        pState = conexao.prepareStatement(sql);

                        System.out.println("Informe o Email do Usuário a ser Excluído");
                        email = scan2.next();
                        pState.setString(1, email);

                        pState.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        DB.fecharStatement(pState);
                    }
                break;
                case 5:
                    scan.nextLine();

                    System.out.println("Informe o Email do Usuário");
                    email = scan2.next();

                    System.out.println("Escreva sua Postagem:");
                    rascunho = scan.nextLine();


                    jedis.set(email, rascunho);
                    jedis.expire(email, 7200);
                break;
                case 6:
                    System.out.println("Informe o Email do Usuário");
                    email = scan2.next();

                    System.out.println(jedis.get(email));;
                break;
                case 7: System.out.println("Programa Encerrado.");
                break;
                default: System.out.println("Comando Invalido!");
            }
        } while (x != 7);
    }
}
