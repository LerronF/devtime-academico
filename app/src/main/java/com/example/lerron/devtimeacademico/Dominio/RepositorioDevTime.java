package com.example.lerron.devtimeacademico.Dominio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;

import com.example.lerron.devtimeacademico.Database.ConnectionRemoteMySQL;
import com.example.lerron.devtimeacademico.Dominio.Entidade.Configuracao;

/**
 * Created by Lerron on 07/12/2017.
 */

public class RepositorioDevTime {

    private SQLiteDatabase conn;

    private ConnectionRemoteMySQL connectionRemote;

    public RepositorioDevTime(SQLiteDatabase conn) {
        this.conn = conn;
    }

    //CLASSE CONFIGURACAO
    private ContentValues preencheConfiguracaoContentValues(Configuracao configuracao) {
        ContentValues values = new ContentValues();

        values.put("Ip", configuracao.getIp());
        values.put("Driver", configuracao.getDriver());
        values.put("BancoDados", configuracao.getBancoDados());
        values.put("Usuario", configuracao.getUsuario());
        values.put("Senha", configuracao.getSenha());

        return values;
    }

    public void inserirConfiguracao(Configuracao configuracao) {
        ContentValues values = preencheConfiguracaoContentValues(configuracao);

        conn.insertOrThrow("CONFIGURACAOBD", null, values);
    }

    public void alterarConfiguracao(Configuracao configuracao) {
        ContentValues values = preencheConfiguracaoContentValues(configuracao);

        conn.update("CONFIGURACAOBD", values, " _id = ? ", new String[]{String.valueOf(configuracao.getId())});
    }

    public void excluirConfiguracao(long id) {
        conn.delete("CONFIGURACAOBD", " _id = ? ", new String[]{String.valueOf(id)});
    }

    public void testeInserirConfiguracao() {
        ContentValues values = new ContentValues();
        values.put("Ip", "0.0.0.0");
        values.put("Driver", "com.mysql.jdbc.Driver");
        values.put("BancoDados", "CMPM");
        values.put("Usuario", "todos");
        values.put("Senha", "devtime");
        conn.insertOrThrow("CONFIGURACAOBD", null, values);
    }


    public ArrayAdapter<Configuracao> buscaConfiguracao(Context context) {
        ArrayAdapter<Configuracao> adpConfiguracao = new ArrayAdapter<Configuracao>(context, android.R.layout.simple_list_item_1);

        Cursor cursor = conn.query("CONFIGURACAOBD", null, null, null, null, null, null);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();

            do {
                Configuracao configuracao = new Configuracao();

                configuracao.setId(cursor.getLong(cursor.getColumnIndex(Configuracao.ID)));
                configuracao.setIp(cursor.getString(cursor.getColumnIndex(Configuracao.IP)));
                configuracao.setDriver(cursor.getString(cursor.getColumnIndex(Configuracao.DRIVER)));
                configuracao.setBancoDados(cursor.getString(cursor.getColumnIndex(Configuracao.BANCODADOS)));
                configuracao.setUsuario(cursor.getString(cursor.getColumnIndex(Configuracao.USUARIO)));
                configuracao.setSenha(cursor.getString(cursor.getColumnIndex(Configuracao.SENHA)));

                adpConfiguracao.add(configuracao);

            } while (cursor.moveToNext());
        }

        return adpConfiguracao;
    }

}
