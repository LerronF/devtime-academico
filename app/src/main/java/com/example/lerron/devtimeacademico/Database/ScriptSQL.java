package com.example.lerron.devtimeacademico.Database;

/**
 * Created by Lerron on 07/12/2017.
 */

public class ScriptSQL {
    public static String getCreateProdutos() {
        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append("CREATE TABLE IF NOT EXISTS PRODUTOS( ");
        sqlBuilder.append("_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        sqlBuilder.append("DESCRICAO VARCHAR (250), ");
        sqlBuilder.append("PRECOVENDA NUMERIC(12,2), ");
        sqlBuilder.append("PRECOCUSTO NUMERIC(12,2), ");
        sqlBuilder.append("NCM VARCHAR (8), ");
        sqlBuilder.append("CEST VARCHAR (7) ");
        sqlBuilder.append("); ");

        return sqlBuilder.toString();
    }

    public static String getCreateClientes() {
        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append("CREATE TABLE IF NOT EXISTS CLIENTES( ");
        sqlBuilder.append("_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        sqlBuilder.append("NOME VARCHAR (50), ");
        sqlBuilder.append("NROINSC VARCHAR(14), ");
        sqlBuilder.append("ENDERECO VARCHAR(200), ");
        sqlBuilder.append("EMAIL VARCHAR (50), ");
        sqlBuilder.append("TEL VARCHAR (40) ");
        sqlBuilder.append("); ");

        return sqlBuilder.toString();
    }

    public static String getCreateConfiguracao() {
        StringBuilder sqlBuilder = new StringBuilder();

        sqlBuilder.append("CREATE TABLE IF NOT EXISTS CONFIGURACAOBD( ");
        sqlBuilder.append("_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");
        sqlBuilder.append("IP VARCHAR (20), ");
        sqlBuilder.append("DRIVER VARCHAR(100), ");
        sqlBuilder.append("BANCODADOS VARCHAR(50), ");
        sqlBuilder.append("USUARIO VARCHAR (50), ");
        sqlBuilder.append("SENHA VARCHAR (50) ");
        sqlBuilder.append("); ");

        return sqlBuilder.toString();
    }
}