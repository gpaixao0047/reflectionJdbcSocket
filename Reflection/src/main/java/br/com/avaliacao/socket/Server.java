package br.com.avaliacao.socket;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.avaliacao.entidade.Pessoas;
import br.com.avaliacao.repository.RepositoryJson;

public class Server {
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(4999);

		try {
			while (true) {
				Socket socket = serverSocket.accept();
				startHandler(socket);
			}
		} finally {
			serverSocket.close();
		}

	}

	private static void startHandler(final Socket socket) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					Pessoas pessoa = new Pessoas();
					OutputStreamWriter writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");
					BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8")) ;
					
					JSONObject jsonObject = new JSONObject();
					String line = reader.readLine();
					jsonObject = new JSONObject(line);
					
					System.out.println("Recebido do Cliente:\n " + jsonObject.toString(2));
				
					RepositoryJson repositoryJson = new RepositoryJson();
					repositoryJson.insert(jsonObject, pessoa);
					
				}  catch (IOException | JSONException e) {
					e.printStackTrace();
				} 					
				finally {
					closeSocket();
				}
				
			}

			private void closeSocket() {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		};
		thread.start();

		

	}

	
}
