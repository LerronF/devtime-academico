package com.example.lerron.devtimeacademico;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.lerron.devtimeacademico.Database.ConnectionRemoteMySQL;
import com.example.lerron.devtimeacademico.Database.Database;
import com.example.lerron.devtimeacademico.Dominio.Entidade.Configuracao;
import com.example.lerron.devtimeacademico.Dominio.RepositorioDevTime;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lerron on 07/12/2017.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText txtUsuario, txtSenha;
    private Button btnEntrar, btnConfigurar;
    private ProgressBar progressBarLogin;
    private AlertDialog alerta;
    private Spinner spinnerUsuario;

    private ArrayAdapter<Configuracao> adpConfiguracao;
    private com.example.lerron.devtimeacademico.Database.Database database;
    private SQLiteDatabase conn;
    private RepositorioDevTime repositorioDevtime;
    private ConnectionRemoteMySQL connectionRemote;
    private Configuracao configuracao;
    private String nomeUsuario;
    private long lastBackPressTime = 0;
    private Toast toast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logon);

        connectionRemote = new ConnectionRemoteMySQL();

        txtUsuario = (EditText) findViewById(R.id.txtUsuario);
        txtSenha = (EditText) findViewById(R.id.txtSenha);
        btnEntrar = (Button) findViewById(R.id.btnEntrar);
        btnConfigurar = (Button) findViewById(R.id.btnConfiguracoes);

        progressBarLogin = (ProgressBar) findViewById(R.id.progressBarLogin);
        progressBarLogin.setVisibility(View.GONE);

        spinnerUsuario = (Spinner) findViewById(R.id.spinnerUsuario);

        btnEntrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DoLogin doLogin = new DoLogin();
                doLogin.execute("");

            }
        });

        btnConfigurar.setOnClickListener(this);

        //conexao com o banco de dados
        try {
            database = new Database(this);
            conn = database.getWritableDatabase();

            repositorioDevtime = new RepositorioDevTime(conn);

            adpConfiguracao = repositorioDevtime.buscaConfiguracao(this);

            configuracao = adpConfiguracao.getItem(0);

            Toast.makeText(MainActivity.this, "Conexão criada com sucesso", Toast.LENGTH_SHORT).show();

            PreencheUsuario();

        } catch (SQLException ex) {
            Toast.makeText(MainActivity.this, "Erro ao criar o banco interno " + ex.getMessage(), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            repositorioDevtime.testeInserirConfiguracao();

            adpConfiguracao = repositorioDevtime.buscaConfiguracao(this);

            configuracao = adpConfiguracao.getItem(0);
        }

        //Método do Spinner para capturar o item selecionado
        spinnerUsuario.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
                //pega nome pela posição
                nomeUsuario = parent.getItemAtPosition(posicao).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logodev_roxo)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.iconedev_roxo))
                        .setContentTitle("DevTime.Food")
                        .setContentText("Automação de Pedidos !")
                        .setVibrate(new long[]{100, 250});
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, TelaPrincipal.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)

// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        //mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        //mNotificationManager.notify(mBuilder.mNumber, mBuilder.build());

    }

    @Override
    public void onBackPressed() {
        if (this.lastBackPressTime < System.currentTimeMillis() - 4000) {
            toast = Toast.makeText(this, "Pressione o Botão Voltar novamente para fechar o Aplicativo.", Toast.LENGTH_LONG);
            toast.show();
            this.lastBackPressTime = System.currentTimeMillis();
        } else {
            if (toast != null) {
                toast.cancel();
            }
            super.onBackPressed();
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEntrar:

                Intent it = new Intent(this, TelaPrincipal.class);
                startActivityForResult(it, 0);

                Toast.makeText(MainActivity.this, "Bom trabalho !", Toast.LENGTH_SHORT).show();

                break;
            case R.id.btnConfiguracoes:

                Intent itt = new Intent(this, ConfiguracoesActivity.class);

                startActivityForResult(itt, 0);
                finish();
                Toast.makeText(MainActivity.this, "Informe os dados para a Conexão !", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void alertDialogInicial() {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Atenção");
        //define a mensagem
        builder.setMessage("Você deseja sair do Aplicativo ?");
        //define um botão como positivo
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                // Toast.makeText(MainActivity.this, "positivo=" + arg1, Toast.LENGTH_SHORT).show();
                finish();
            }
        });
        //define um botão como negativo.
        builder.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(MainActivity.this, "Bom trabalho !", Toast.LENGTH_SHORT).show();

            }
        });
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }

    private void PreencheUsuario() {

        try {

            configuracao = adpConfiguracao.getItem(0);

            String ip = configuracao.getIp();
            String driver = configuracao.getDriver();
            String db = configuracao.getBancoDados();
            String usuario = configuracao.getUsuario();
            String senha = configuracao.getSenha();

            Connection con = connectionRemote.CONN(ip, driver, db, usuario, senha);

            if (con == null) {
                Toast.makeText(MainActivity.this, "Error in connection with SQL server", Toast.LENGTH_SHORT).show();
            } else {
                String query = "Select * From Atendente Order by Nome";
                PreparedStatement ps = con.prepareStatement(query);
                ResultSet rs = ps.executeQuery();

                List<String> usuarios = new ArrayList<String>();

                while (rs.next()) {

                    //categoria.add("Código: "+rs.getString("Codigo"));
                    usuarios.add(rs.getString("Nome"));
                }

                con.close();

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_usuario, usuarios);
                spinnerUsuario.setAdapter(arrayAdapter);
            }
        } catch (Exception ex) {

            Toast.makeText(MainActivity.this, "erro " + ex.getMessage(), Toast.LENGTH_SHORT).show();
            //  z = "Error retrieving data from table" + ex.getMessage();
        }
    }

    public void finish() {

        super.finish();
    }

    public class DoLogin extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;

        // String userid = txtUsuario.getText().toString();
        String userid = nomeUsuario;
        String password = txtSenha.getText().toString();

        @Override
        protected void onPreExecute() {
            progressBarLogin.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {
            progressBarLogin.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this, r, Toast.LENGTH_SHORT).show();

            if (isSuccess) {

                // startActivity(i);
                //METODO PARA ENVIAR PARAMETROS PARA A OUTRA ACTIVITY
                Intent intent = new Intent(MainActivity.this, TelaPrincipal.class);
                String parametros = nomeUsuario;
                intent.putExtra("infos", parametros);
                startActivity(intent);
                finish();
            }

        }

        @Override
        protected String doInBackground(String... params) {
            if (userid.trim().equals("") || password.trim().equals(""))
                z = "Por favor digite seu Usuário e Senha !";
            else {
                try {

                    //conexao com o banco de dados
                    try {
                        database = new Database(MainActivity.this);
                        conn = database.getWritableDatabase();

                        repositorioDevtime = new RepositorioDevTime(conn);

                        adpConfiguracao = repositorioDevtime.buscaConfiguracao(MainActivity.this);

                        configuracao = adpConfiguracao.getItem(0);

                        //Toast.makeText(MainActivity.this, "Conexão criada com sucesso", Toast.LENGTH_SHORT).show();
                    } catch (SQLException ex) {
                        Toast.makeText(MainActivity.this, "Erro ao criar o banco interno " + ex.getMessage(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        repositorioDevtime.testeInserirConfiguracao();

                        adpConfiguracao = repositorioDevtime.buscaConfiguracao(MainActivity.this);

                        configuracao = adpConfiguracao.getItem(0);
                    }

                    String ip = configuracao.getIp();
                    String driver = configuracao.getDriver();
                    String db = configuracao.getBancoDados();
                    String usuario = configuracao.getUsuario();
                    String senha = configuracao.getSenha();

                    Connection con = connectionRemote.CONN(ip, driver, db, usuario, senha);
                    if (con == null) {
                        z = "Não foi possível conectar ao SQL server";
                    } else {
                        String query = "Select * " +
                                "From Atendente " +
                                "Where Nome='" + userid + "'and Senha='" + password + "'";
                        Statement stmt = con.createStatement();
                        ResultSet rs = stmt.executeQuery(query);

                        if (rs.next()) {
                            z = "Login successfull";
                            isSuccess = true;
                        } else {
                            z = "Invalid Credentials";
                            isSuccess = false;
                        }

                        con.close();
                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    z = "Exceptions";
                }
            }
            return z;
        }
    }
}
