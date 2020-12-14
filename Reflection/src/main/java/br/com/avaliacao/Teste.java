package br.com.avaliacao;

import br.com.avaliacao.entidade.Pessoas;
import br.com.avaliacao.repository.RepositoryDelete;
import br.com.avaliacao.repository.RepositoryFinal;
import br.com.avaliacao.repository.RepositoryFindById;
import br.com.avaliacao.repository.RepositoryJson;

public class Teste {
	public static void main(String[] args) throws Throwable {
		Pessoas ps = new Pessoas();
		ps.setNome("Primrio Json");
		ps.setCodigo(34);
		//ps.setIdPessoas(4);
		RepositoryJson repositorio = new RepositoryJson();
	
		repositorio.createJson(ps);

	}
}
