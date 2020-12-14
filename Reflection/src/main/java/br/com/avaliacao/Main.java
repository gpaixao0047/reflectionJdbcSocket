package br.com.avaliacao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.avaliacao.entidade.Pessoas;
import br.com.avaliacao.repository.RepositoryDelete;
import br.com.avaliacao.repository.RepositoryFinal;
import br.com.avaliacao.repository.RepositoryFindById;
import br.com.avaliacao.repository.RepositoryJson;

public class Main {
	public static void main(String[] args) throws ClassNotFoundException, UnknownHostException, IOException, JSONException {
		Pessoas p = new Pessoas();
		Scanner menu = new Scanner(System.in);
		boolean controle = true;
		while (controle) {

			System.out.print("##--         Menu          --##\n\n");
			System.out.print("|-------------------------------|\n");
			System.out.print("| Opção 1 - Cadastrar           |\n");
			System.out.print("| Opção 2 - Buscar por Id       |\n");
			System.out.print("| Opção 3 - Deletar por Id      |\n");
			System.out.print("| Opção 4 - Criar Json          |\n");
			System.out.print("| Opção 5 - Cadastro Json socket|\n");
			System.out.print("| Opção 6 - Sair                |\n");
			System.out.print("|-------------------------------|\n");
			System.out.print("Digite uma opção: ");

			int opcao = menu.nextInt();

			switch (opcao) {
			case 1:
				System.out.println("Digite o nome: ");
				p.setNome(menu.next());
				System.out.println("Digite o codigo: ");
				p.setCodigo(menu.nextInt());
				RepositoryFinal repositoryInsert = new RepositoryFinal();
				repositoryInsert.insert(p);
				break;

			case 2:
				System.out.println("Digite o id: ");
				p.setIdPessoas(menu.nextInt());
				RepositoryFindById repositoryFind = new RepositoryFindById();
				repositoryFind.findById(p);
				break;

			case 3:
				System.out.println("Digite o id para ser deletado: ");
				p.setIdPessoas(menu.nextInt());
				RepositoryDelete reposirotyDelete = new RepositoryDelete();
				reposirotyDelete.delete(p);
				break;

			case 4:
				System.out.println("Digite o nome: ");
				p.setNome(menu.next());
				System.out.println("Digite o codigo: ");
				p.setCodigo(menu.nextInt());
				RepositoryJson repositoryJson = new RepositoryJson();
				repositoryJson.createJson(p);
				break;

			case 5:
				Socket socket = new Socket("localhost", 4999);
				System.out.println("Digite o id: ");
				p.setIdPessoas(menu.nextInt());
				System.out.println("Digite o nome: ");
				p.setNome(menu.next());
				System.out.println("Digite o codigo: ");
				p.setCodigo(menu.nextInt());
				RepositoryJson repositoryJsonNew = new RepositoryJson();
				String json = repositoryJsonNew.createJson(p);
				OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
				JSONObject jsonObject = new JSONObject(json);
				writer.write(jsonObject.toString() + "\n");
				writer.flush();
				
				break;

			default:
				System.out.print("\nOpção Inválida!");
				break;
			}
		}
	}

}
