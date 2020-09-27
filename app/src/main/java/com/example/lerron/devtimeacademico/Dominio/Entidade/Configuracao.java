package com.example.lerron.devtimeacademico.Dominio.Entidade;

import java.io.Serializable;

/**
 * Created by Lerron on 07/12/2017.
 */

public class Configuracao implements Serializable {

    public static String ID = "_id";
    public static String IP = "IP";
    public static String DRIVER = "DRIVER";
    public static String BANCODADOS = "BANCODADOS";
    public static String USUARIO = "USUARIO";
    public static String SENHA = "SENHA";

    private long id;
    private String Ip;
    private String Driver;
    private String BancoDados;
    private String Usuario;
    private String Senha;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getIp() {
        return Ip;
    }

    public void setIp(String ip) {
        Ip = ip;
    }

    public String getDriver() {
        return Driver;
    }

    public void setDriver(String driver) {
        Driver = driver;
    }

    public String getBancoDados() {
        return BancoDados;
    }

    public void setBancoDados(String bancoDados) {
        BancoDados = bancoDados;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public String getSenha() {
        return Senha;
    }

    public void setSenha(String senha) {
        Senha = senha;
    }

    public String toString() {
        return Ip + " - " + Driver + " - " + BancoDados;
    }
}
