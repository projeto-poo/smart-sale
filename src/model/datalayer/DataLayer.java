package model.datalayer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DataLayer {
    protected String table = "";
    protected String primaryKey = "id";
    protected String[] required = {};
    protected Map data;
    protected Map returnMessage;

    private String behaviorToSave;
    private String query;
    private SQLException fail;

    public DataLayer(String behaviorToSave) {
        this.behaviorToSave = (behaviorToSave == null || behaviorToSave.equals("") ? "create" : behaviorToSave);
        this.data = new HashMap();
        this.returnMessage = new HashMap();
        this.query = "";
    }

    /**
     * Obtém os dados encapsulados, que são adicionados através dos métodos
     * setters das classes de modelo ou recuperados através de uma consulta no
     * banco de dados.
     *
     * @return Mapa com os dados da camada de modelo responsável.
     */
    public Map data() {
        return data;
    }

    /**
     * Obtém as mensagens personalizadas que são geradas nas classes de modelo
     * quando ocorre alguma restrição.
     *
     * @return Mapa com chaves identificadoras e suas respectivas mensagens.
     */
    public Map returnMessage() {
        return returnMessage;
    }

    /**
     * Obtém os erros gerados através de algum processo mal sucedido na
     * execução de algum comando no banco de dados.
     *
     * @return TRUE se existir conexão com um servidor e um banco de dados,
     * FALSE caso contrário.
     */
    public SQLException fail() {
        return fail;
    }

    /**
     * Prepara a query para uma consulta sem termos.
     *
     * @return Instância da própria classe para encadeamento de métodos, caso
     * queira utilizar.
     */
    public DataLayer find() {
        this.query = "SELECT * FROM " + table;
        return this;
    }

    /**
     * Prepara a query para uma consulta aplicando termos.
     *
     * @param terms Os termos da consulta: WHERE.
     * @return Instância da própria classe para encadeamento de métodos, caso
     * queira utilizar.
     */
    public DataLayer find(String terms) {
        if (terms.isEmpty()) {
            return find();
        }

        this.query = "SELECT * FROM " + table + " WHERE " + terms;
        return this;
    }

    /**
     * Executa uma consulta no banco de dados utilizando a chave primária.
     *
     * @param value Valor da chave primária.
     * @return Retorna um único registro em um Object se encontrar, NULL caso
     * contrário.
     */
    public Object findByPrimaryKey(String value) {
        return find(primaryKey + "='" + value + "'").fetch();
    }

    /**
     * Complementa a query definindo o agrupamento de dados.
     *
     * @param group Nome da coluna para GROUP BY.
     * @return Instância da própria classe para encadeamento de métodos, caso
     * queira utilizar.
     */
    public DataLayer group(String group) {
        this.query += " GROUP BY " + group;
        return this;
    }

    /**
     * Complementa a query definindo a ordem dos dados.
     *
     * @param order Nome da coluna e ordenação. Ex.: ORDER BY nomeDaColuna DESC.
     * @return Instância da própria classe para encadeamento de métodos, caso
     * queira utilizar.
     */
    public DataLayer order(String order) {
        this.query += " ORDER BY " + order;
        return this;
    }

    /**
     * Complementa a query definindo a quantidade máxima de dados que devem ser
     * retornados.
     *
     * @param limit Total de registros.
     * @return Instância da própria classe para encadeamento de métodos, caso
     * queira utilizar.
     */
    public DataLayer limit(int limit) {
        this.query += " LIMIT " + limit;
        return this;
    }

    /**
     * Complementa a query definindo a posição de onde a leitura de dados deve
     * começar.
     *
     * @param offset Valor da posição de início.
     * @return Instância da própria classe para encadeamento de métodos, caso
     * queira utilizar.
     */
    public DataLayer offset(int offset) {
        this.query += " OFFSET " + offset;
        return this;
    }

    /**
     * Executa a consulta e retorna um único registro. Este método deve ser
     * chamado sempre depois do método find(). Além de retornar o registro, ele
     * atualiza o próprio objeto que solicitou a consulta anexando os dados
     * encontrados no atributo data.
     *
     * @return Retorna um único registro em um Object se encontrar, NULL caso
     * contrário.
     */
    public Object fetch() {
        try {
            PreparedStatement preparedStatement = Connect.instance().prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                return null;
            }

            Map<String, String> columnsAsMethods = new HashMap<>();

            ResultSetMetaData columns = resultSet.getMetaData();

            for (int i = 1; i <= columns.getColumnCount(); i++) {
                columnsAsMethods.put("set" + studlyCaps(columns.getColumnName(i)), columns.getColumnName(i));
            }

            Object obj = Class.forName(getClass().getName()).getDeclaredConstructor(String.class).newInstance("update");
            Method[] objMethods = obj.getClass().getDeclaredMethods();

            for (Method method : objMethods) {
                String methodName = method.getName();
                method.setAccessible(true);

                if (columnsAsMethods.containsKey(methodName)) {
                    method.invoke(obj, resultSet.getString(columnsAsMethods.get(methodName)));
                }
            }

            return obj;
        } catch (SQLException e) {
            this.fail = e;
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Executa a consulta e retorna vários registros. Este método deve ser
     * chamado sempre depois do método find().
     *
     * @return Retorna vários registros em um ArrayList se encontrar, NULL caso
     * contrário.
     */
    public ArrayList fetchAll() {
        try {
            PreparedStatement preparedStatement = Connect.instance().prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (!resultSet.next()) {
                return null;
            }

            ArrayList list = new ArrayList();

            Map<String, String> columnsAsMethods = new HashMap<>();

            ResultSetMetaData columns = resultSet.getMetaData();

            for (int i = 1; i <= columns.getColumnCount(); i++) {
                columnsAsMethods.put("set" + studlyCaps(columns.getColumnName(i)), columns.getColumnName(i));
            }

            resultSet.beforeFirst();

            while (resultSet.next()) {
                Object obj = Class.forName(getClass().getName()).getDeclaredConstructor(String.class).newInstance("update");
                Method[] objMethods = obj.getClass().getDeclaredMethods();

                for (Method method : objMethods) {
                    String methodName = method.getName();
                    method.setAccessible(true);

                    if (columnsAsMethods.containsKey(methodName)) {
                        method.invoke(obj, resultSet.getString(columnsAsMethods.get(methodName)));
                    }
                }

                list.add(obj);
            }

            return list;
        } catch (SQLException e) {
            this.fail = e;
        } catch (InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Método responsável por salvar os dados no banco, seja cadastrou ou
     * atualização de registro.
     *
     * @return TRUE se for sucesso, FALSE caso contrário.
     */
    public boolean save() {
        try {
            if (required()) {
                throw new SQLException("Preencha os campos necessários");
            }

            String primaryKeyValue;

            if (behaviorToSave.equals("create")) {
                primaryKeyValue = create(data);

                if (primaryKeyValue == null) {
                    return false;
                }
            } else if (behaviorToSave.equals("update")) {
                if (primaryKey.equals("")) {
                    throw new SQLException("Erro ao atualizar! Verifique os dados.");
                }

                primaryKeyValue = (String) data.get(primaryKey);

                Map dataCopy = new HashMap(data);
                dataCopy.remove(primaryKey);

                if (!primaryKeyValue.isEmpty() && !update(dataCopy, primaryKey + "='" + primaryKeyValue + "'")) {
                    return false;
                }
            } else {
                throw new SQLException("O sistema se comportou de forma inesperada.");
            }

            DataLayer obj = (DataLayer) find(primaryKey + "='" + primaryKeyValue + "'").fetch();
            data.putAll(obj.data());

            this.behaviorToSave = "update";
            return true;
        } catch (SQLException e) {
            this.fail = e;
        }

        return false;
    }

    /**
     * Método responsável por cadastrar um registro no banco de dados.
     * Este método simula o design pattern Active Record para atualizar o
     * próprio objeto que solicitou o cadastro atualizando os dados do atributo
     * data com os dados inseridos e gerados na tupla do banco de dados.
     *
     * @param data Os dados que serão processados para construção da query.
     * @return Valor da chave primária da classe de modelo responsável.
     */
    private String create(Map data) {
        try {
            int i = 1;

            StringBuilder columns = new StringBuilder();
            StringBuilder values = new StringBuilder();

            for (Object key : data.keySet()) {
                columns.append(key);
                values.append("?");

                if (i != data.size()) {
                    columns.append(",");
                    values.append(",");
                }

                i++;
            }

            String query = "INSERT INTO " + table + " (" + columns + ") VALUES (" + values + ")";

            PreparedStatement preparedStatement = Connect.instance().prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            i = 1;
            for (Object value : data.values()) {
                preparedStatement.setString(i, value.toString());
                i++;
            }

            preparedStatement.execute();

            ResultSet resultSet = preparedStatement.getGeneratedKeys();

            if (!resultSet.next()) {
                return null;
            }

            return resultSet.getString(primaryKey);
        } catch (SQLException e) {
            this.fail = e;
            return null;
        }
    }

    /**
     * Método responsável por atualizar um registro no banco de dados.
     *
     * @param data Os dados que serão processados para construção da query.
     * @param terms Termos para atualização do registro.
     * @return TRUE se for atualizado com sucesso, FALSE caso contrário.
     */
    private boolean update(Map data, String terms) {
        try {
            StringBuilder params = new StringBuilder();

            int i = 1;

            for (Object key : data.keySet()) {
                params.append(key).append("=?");

                if (i != data.size()) {
                    params.append(",");
                }

                i++;
            }

            String query = "UPDATE " + table + " SET " + params + " WHERE " + terms;

            PreparedStatement preparedStatement = Connect.instance().prepareStatement(query);

            i = 1;
            for (Object value : data.values()) {
                preparedStatement.setString(i, value.toString());
                i++;
            }

            preparedStatement.executeUpdate();

            return true;
        } catch (SQLException e) {
            this.fail = e;
            return false;
        }
    }

    /**
     * Deleta um registro do banco de dados.
     *
     * @return TRUE se for deletado com sucesso, FALSE caso contrário.
     */
    public boolean destroy() {
        return delete();
    }

    /**
     * Método responsável por deletar um registro no banco de dados.
     *
     * @return TRUE se for deletado com sucesso, FALSE caso contrário.
     */
    private boolean delete() {
        try {
            String query = "DELETE FROM " + table + " where " + primaryKey + "=?";

            PreparedStatement preparedStatement = Connect.instance().prepareStatement(query);
            preparedStatement.setString(1, data.get(primaryKey).toString());
            preparedStatement.executeUpdate();

            data.clear();
            return true;
        } catch (SQLException e) {
            this.fail = e;
            return false;
        }
    }

    /**
     * Verifica se alguma campo obrigatório (definido como NOT NULL na tabela do
     * banco de dados) está vazio.
     *
     * @return TRUE se tiver campo vazio, FALSE caso contrário.
     */
    private boolean required() {
        for (String value : required) {
            if (!data.containsKey(value) || data.get(value).toString().isEmpty()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Converte uma string para o formato StudlyCaps.
     *
     * @param name Texto que será convertido.
     * @return String convertida em StudyCaps
     */
    private String studlyCaps(String name) {
        name = name.replace("_", " ");

        boolean convertNext = true;

        StringBuilder converted = new StringBuilder();

        for (char ch : name.toCharArray()) {
            if (Character.isSpaceChar(ch)) {
                convertNext = true;
            } else if (convertNext) {
                ch = Character.toTitleCase(ch);
                convertNext = false;
            } else {
                ch = Character.toLowerCase(ch);
            }

            converted.append(ch);
        }

        return converted.toString().replace(" ", "");
    }
}
