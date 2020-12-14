package br.com.avaliacao.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.avaliacao.entidade.Pessoas;
import br.com.avaliacao.repository.RepositoryJson;

public class Cliente {
	public static void main(String[] args)
			throws UnknownHostException, IOException, ClassNotFoundException, JSONException {
		Socket socket = new Socket("localhost", 4999);
		Pessoas pessoa = new Pessoas();
		pessoa.setNome("Luly");
		pessoa.setCodigo(40);
		pessoa.setIdPessoas(28);
		RepositoryJson repositoryJson = new RepositoryJson();
		String json = repositoryJson.createJson(pessoa);
		OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
		JSONObject jsonObject = new JSONObject(json);
		writer.write(jsonObject.toString() + "\n");
		writer.flush();
	}
}
