package br.com.avaliacao.repository;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import br.com.avaliacao.conexao.Conexao;
import br.com.avaliacao.interfaces.DBField;
import br.com.avaliacao.interfaces.DbTable;

public class RepositoryFindById {
	private final Connection conexao = Conexao.getConnection();
	public void findById(Object obj) {
		String sql;
		String values = "";
		DbTable persistable = obj.getClass().getAnnotation(DbTable.class);
		String nomeTabela = obj.getClass().getSimpleName();
		Field[] fields = getFields(obj.getClass());
		HashMap<String, Object> keyAndValues = new HashMap<String, Object>();
		for (Field fild : fields) {
			DBField coluna = fild.getAnnotation(DBField.class);
			if (coluna == null) {
				Object valor = getter(fild, obj);
				keyAndValues.put(fild.getName(), valor);
			} else {
				Object valor = getter(fild, obj);
				keyAndValues.put(coluna.colummn(), valor);
			}

		}
		for (Object campo : keyAndValues.keySet()) {
			if(keyAndValues.get(campo) != null ) {
				if (!values.equals("")) {
					values += " AND ";
				}
				if (keyAndValues.get(campo) instanceof String) {
					values += campo + "=" + "'" + keyAndValues.get(campo) + "'";
				} else {
					values += campo + "=" + keyAndValues.get(campo);
				}
			}
		
		}
		if (persistable == null) {
			sql = "SELECT *FROM " + nomeTabela.toString() + " WHERE " + values;
		} else {
			sql = "SELECT *FROM " + persistable.table() + " WHERE " + values;
		}
		try {
			
			PreparedStatement stmt = conexao.prepareStatement(sql);
			ResultSet resultado = stmt.executeQuery();
			while(resultado.next()) {
				int codigo = resultado.getInt("codigo");
				String nome = resultado.getString("nome");
				System.out.println();
				System.out.println("Codigo Retorado do banco: " + codigo);
				System.out.println("Nome retornado do banco: " + nome);
			}
			
			stmt.execute();
			stmt.close();
			System.out.println("Executado com sucesso");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("SQL -->" + sql);
	}

	public Object getter(Field field, Object obj) {
		Method[] m = obj.getClass().getDeclaredMethods();
		for (Method method : m) {
			if (method.getName().startsWith("get") && (method.getName().length() == (field.getName().length() + 3))) {
				if (method.getName().toLowerCase().endsWith(field.getName().toLowerCase())) {
					try {
						return method.invoke(obj);
					} catch (IllegalAccessException e) {
						System.out.println("Nao poode determinar o method " + method.getName());
					} catch (InvocationTargetException e) {
						System.out.println("Nao poode determinar o method " + method.getName());
					}
				}
			}
		}

		return null;
	}

	public Field[] getFields(Class c) {
		if (c.getSuperclass() != null) {
			Field[] superClassFields = getFields(c.getSuperclass());
			Field[] thisFields = c.getDeclaredFields();
			Field[] allFields = new Field[superClassFields.length + thisFields.length];
			System.arraycopy(superClassFields, 0, allFields, 0, superClassFields.length);
			System.arraycopy(thisFields, 0, allFields, superClassFields.length, thisFields.length);
			return allFields;

		} else {
			return c.getDeclaredFields();
		}
	}
}
