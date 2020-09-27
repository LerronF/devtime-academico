package com.example.lerron.devtimeacademico;

import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lerron.devtimeacademico.Database.ConnectionRemoteMySQL;
import com.example.lerron.devtimeacademico.Database.Database;
import com.example.lerron.devtimeacademico.Dominio.Entidade.Configuracao;
import com.example.lerron.devtimeacademico.Dominio.RepositorioDevTime;

import java.sql.Connection;

/**
 * Created by Lerron on 07/12/2017.
 */

public class ConfiguracoesActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private EditText txtIP, txtDriver, txtBancoDados, txtUsuario, txtSenha;
    private ListView listViewConfiguracao;

    private ArrayAdapter<Configuracao> adpConfiguracao;
    private com.example.lerron.devtimeacademico.Database.Database database;
    private SQLiteDatabase conn;
    private RepositorioDevTime repositorioDevtime;
    private Configuracao configuracao;

    private ConnectionRemoteMySQL connectionRemote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracoes_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        connectionRemote = new ConnectionRemoteMySQL();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Salvar();
                Snackbar.make(view, "Salvo com sucesso !", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        txtIP = (EditText) findViewById(R.id.txtIP);
        txtDriver = (EditText) findViewById(R.id.txtDriver);
        txtBancoDados = (EditText) findViewById(R.id.txtBancoDados);
        txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        txtSenha = (EditText) findViewById(R.id.txtSenha);

        listViewConfiguracao = (ListView) findViewById(R.id.listViewConfiguracao);
        listViewConfiguracao.setOnItemClickListener(this);

        //conexao com o banco de dados
        try {
            database = new Database(this);
            conn = database.getWritableDatabase();

            repositorioDevtime = new RepositorioDevTime(conn);

            //repositorioAgenda.testeInserirContato();

            adpConfiguracao = repositorioDevtime.buscaConfiguracao(this);

            listViewConfiguracao.setAdapter(adpConfiguracao);

            Toast.makeText(ConfiguracoesActivity.this, "Conexão criada com sucesso", Toast.LENGTH_SHORT).show();
        } catch (SQLException ex) {
            Toast.makeText(ConfiguracoesActivity.this, "Erro ao criar o banco interno " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_configuracoes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_salvar_alteracoes) {
            Salvar();
            return true;
        } else if (id == R.id.action_testar_conexao) {
            TestarConexaoLocal();
            return true;
        } else if (id == R.id.action__excluir_alteracoes) {
            Excluir();
            return true;
        } else if (id == R.id.action_testar_conexao_remota) {
            TestarConexaoRemoto();
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        adpConfiguracao = repositorioDevtime.buscaConfiguracao(this);

        listViewConfiguracao.setAdapter(adpConfiguracao);
    }

    //EVENTO CLICA NO LISTVIEW E PREENCHE OS CAMPPOS NO MESMO ACTIVITY
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        configuracao = adpConfiguracao.getItem(position);

        PreencheDados();
    }

    private void PreencheDados() {
        txtIP.setText(configuracao.getIp());
        txtDriver.setText(configuracao.getDriver());
        txtBancoDados.setText(configuracao.getBancoDados());
        txtUsuario.setText(configuracao.getUsuario());
        txtSenha.setText(configuracao.getSenha());
    }

    private void Salvar() {
        try {
            configuracao.setIp(txtIP.getText().toString());
            configuracao.setDriver(txtDriver.getText().toString());
            configuracao.setBancoDados(txtBancoDados.getText().toString());
            configuracao.setUsuario(txtUsuario.getText().toString());
            configuracao.setSenha(txtSenha.getText().toString());

            if (configuracao.getId() == 0)
                repositorioDevtime.inserirConfiguracao(configuracao);
            else
                repositorioDevtime.alterarConfiguracao(configuracao);

            adpConfiguracao = repositorioDevtime.buscaConfiguracao(this);

            listViewConfiguracao.setAdapter(adpConfiguracao);

            Toast.makeText(ConfiguracoesActivity.this, "Configurações Salvas !", Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Toast.makeText(ConfiguracoesActivity.this, "Erro ao salvar os dados " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void Excluir() {
        try {
            repositorioDevtime.excluirConfiguracao(configuracao.getId());

            adpConfiguracao = repositorioDevtime.buscaConfiguracao(this);

            listViewConfiguracao.setAdapter(adpConfiguracao);
        } catch (SQLException ex) {
            Toast.makeText(ConfiguracoesActivity.this, "Erro ao excluir os dados " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void TestarConexaoLocal() {
        try {
            database = new Database(this);
            conn = database.getWritableDatabase();

            repositorioDevtime = new RepositorioDevTime(conn);

            Toast.makeText(ConfiguracoesActivity.this, "Conexão local criada com sucesso", Toast.LENGTH_SHORT).show();

            conn.close();
        } catch (SQLException ex) {
            Toast.makeText(ConfiguracoesActivity.this, "Erro ao conectar o banco interno " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void TestarConexaoRemoto() {
        configuracao = adpConfiguracao.getItem(0);

        String ip = configuracao.getIp();
        String driver = configuracao.getDriver();
        String db = configuracao.getBancoDados();
        String usuario = configuracao.getUsuario();
        String senha = configuracao.getSenha();

        Connection con = connectionRemote.CONN(ip, driver, db, usuario, senha);

        if (con == null) {
            Toast.makeText(ConfiguracoesActivity.this, "Não foi possível conectar ao SQL server", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ConfiguracoesActivity.this, "Conectado ao servidor remoto!", Toast.LENGTH_SHORT).show();

            Intent itt = new Intent(this, MainActivity.class);

            startActivityForResult(itt, 0);

            finish();
        }
    }

    public void finish() {

        super.finish();
    }
}
