package br.com.avaliacao.repository;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import br.com.avaliacao.conexao.Conexao;
import br.com.avaliacao.interfaces.DBField;
import br.com.avaliacao.interfaces.DbTable;

public class RepositoryJson {
	private final Connection conexao = Conexao.getConnection();

	public void insert(JSONObject json, Object obj) throws JSONException {
		Field[] fields = getFields(obj.getClass());
		String sql;
		String values = "";
		String valorFinal = "";
		DbTable persistable = obj.getClass().getAnnotation(DbTable.class);
		String nomeTabela = obj.getClass().getSimpleName();
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
		HashMap<String, Object> newKeyAndValues = new HashMap<String, Object>();
		for (String campo : keyAndValues.keySet()) {
			if (json.get(campo) != null) {
				newKeyAndValues.put(campo, json.get(campo));

			}

		}
		for (Object campo : newKeyAndValues.keySet()) {
			if (newKeyAndValues.get(campo) != null) {
				if (!values.equals("")) {
					values += ",";
				}
				if (!valorFinal.equals("")) {
					valorFinal += ",";
				}
				if (newKeyAndValues.get(campo) instanceof String) {
					valorFinal += "'" + newKeyAndValues.get(campo) + "'";
				} else {
					valorFinal += newKeyAndValues.get(campo);
				}
				values += campo;
			}

		}
		if (persistable == null) {
			sql = "INSERT INTO " + nomeTabela.toString() + "(" + values + ")" + "VALUES" + "(" + valorFinal + ")";
		} else {
			sql = "INSERT INTO " + persistable.table() + "(" + values + ")" + "VALUES" + "(" + valorFinal + ")";
		}

		try {
			PreparedStatement stmt = conexao.prepareStatement(sql);
			stmt.execute();
			stmt.close();
			System.out.println("Executado com sucesso");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("SQL -->" + sql);
	}

	

	public void findByJson(JSONObject json, Object obj) throws JSONException {
		Field[] fields = getFields(obj.getClass());
		String sql;
		String values = "";
		DbTable persistable = obj.getClass().getAnnotation(DbTable.class);
		String nomeTabela = obj.getClass().getSimpleName();
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
		HashMap<String, Object> newKeyAndValues = new HashMap<String, Object>();
		for (String campo : keyAndValues.keySet()) {
			if (json.get(campo) != null) {
				System.out.println(json.get(campo).toString());
				newKeyAndValues.put(campo, json.get(campo));

			}

		}
		for (Object campo : newKeyAndValues.keySet()) {
			if (newKeyAndValues.get(campo) != null) {
				if (!values.equals("")) {
					values += " AND ";
				}
				if (newKeyAndValues.get(campo) instanceof String) {
					values += campo + "=" + "'" + newKeyAndValues.get(campo) + "'";
				} else {
					values += campo + "=" + newKeyAndValues.get(campo);
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
			while (resultado.next()) {
				System.out.println(resultado.getRow());
			}

			stmt.execute();
			stmt.close();
			System.out.println("Executado com sucesso");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("SQL -->" + sql);
	}

	public String createJson(Object obj) throws ClassNotFoundException {
		String json;
		String values = "";
		DbTable persistable = obj.getClass().getAnnotation(DbTable.class);
		String nomeTabela = obj.getClass().getSimpleName();
		Field[] fields = getFields(obj.getClass());
		String valorFinal = "";
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
			if (keyAndValues.get(campo) != null) {
				String quase = "";
				if (!values.equals("")) {
					values += ",";
				}
				if (!valorFinal.equals("")) {
					valorFinal += ",";
				}
				if (keyAndValues.get(campo) instanceof String) {
					quase += "'" + keyAndValues.get(campo) + "'";
				} else {
					quase += keyAndValues.get(campo);
				}
				values += campo + ":" + quase;
			}

		}
		if (persistable == null) {
			json = "{" + values + valorFinal + "}";
		} else {
			json = "{" + values + valorFinal + "}";
		}

		System.out.println("JSON -->" + json);
		System.out.println(" ");
		return json;
	}

	public Object getter(Field field, Object obj) {
		HashMap<String, Object> keyAndValues = new HashMap<String, Object>();
		Method[] m = obj.getClass().getDeclaredMethods();
		for (Method method : m) {
			if (method.getName().startsWith("get") && (method.getName().length() == (field.getName().length() + 3))) {
				if (method.getName().toLowerCase().endsWith(field.getName().toLowerCase())) {
					try {
						keyAndValues.put(field.getName(), method.invoke(obj));
						return keyAndValues.put(field.getName(), method.invoke(obj));

					} catch (IllegalAccessException e) {
						
					} catch (InvocationTargetException e) {
			
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
