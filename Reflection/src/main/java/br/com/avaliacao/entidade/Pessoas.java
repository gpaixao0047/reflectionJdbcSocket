package br.com.avaliacao.entidade;

import br.com.avaliacao.interfaces.DBField;
import br.com.avaliacao.interfaces.DbId;
import br.com.avaliacao.interfaces.DbTable;

@DbTable(table = "pessoas")
public class Pessoas {
	@DbId
	private Integer idPessoas = null;
	
	@DBField(colummn = "codigo")
	private Integer codigo = null;
	@DBField(colummn = "nome")
	private String nome;

	
	
	public int getCodigo() {
		return codigo;
	}

	public String getNome() {
		return nome;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getIdPessoas() {
		return idPessoas;
	}

	public void setIdPessoas(int idPessoa) {
		this.idPessoas = idPessoa;
	}



	
	
	
	
}
