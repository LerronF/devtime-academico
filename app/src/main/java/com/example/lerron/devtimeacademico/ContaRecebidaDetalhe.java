package com.example.lerron.devtimeacademico;

import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lerron.devtimeacademico.Database.ConnectionRemoteMySQL;
import com.example.lerron.devtimeacademico.Database.Database;
import com.example.lerron.devtimeacademico.Dominio.Entidade.Configuracao;
import com.example.lerron.devtimeacademico.Dominio.RepositorioDevTime;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ContaRecebidaDetalhe extends AppCompatActivity {

    ConnectionRemoteMySQL connectionRemote;
    //region VARIAVEIS PARA A CONEXAO AO BANCO LOCAL PARA CARREGAR AS CONFIGURACOES
    private ArrayAdapter<Configuracao> adpConfiguracao;
    private com.example.lerron.devtimeacademico.Database.Database database;
    private SQLiteDatabase connLocal;
    //endregion
    private RepositorioDevTime repositorioDevtime;
    private Configuracao configuracao;
    //endregion

    private ListView listViewContaRecebida;
    private ProgressBar progressBarContaRecebida;
    private Toolbar mToolbar;
    private String dataEmissao;

    private TextView txtCodigo, txtNomeAluno, txtDescricao, txtData, txtValor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conta_recebida_detalhe);

        mToolbar = (Toolbar) findViewById(R.id.tb_main);
        mToolbar.setTitle("DevTime Sistemas");
        mToolbar.setSubtitle("BI Financeiro");
        mToolbar.setLogo(R.drawable.ic_launcher);
        setSupportActionBar(mToolbar);

        connectionRemote = new ConnectionRemoteMySQL();

        //region CARREGA AS CONFIGURACOES PARA A CONEXAO
        try {

            database = new Database(this);
            connLocal = database.getWritableDatabase();
            repositorioDevtime = new RepositorioDevTime(connLocal);
            adpConfiguracao = repositorioDevtime.buscaConfiguracao(this);

        } catch (Exception e) {

            Toast.makeText(ContaRecebidaDetalhe.this, "Erro na configuracao", Toast.LENGTH_SHORT).show();
        }

        txtCodigo = (TextView) findViewById(R.id.txtCodAluno);
        //txtNomeAluno = (TextView) findViewById(R.id.txtNomeAluno);
        txtDescricao = (TextView) findViewById(R.id.txtDescricao);
        txtData = (TextView) findViewById(R.id.txtData);
        txtValor = (TextView) findViewById(R.id.txtValor);

        progressBarContaRecebida = (ProgressBar) findViewById(R.id.progressBarContaRecebida);
        progressBarContaRecebida.setVisibility(View.GONE);
        listViewContaRecebida = (ListView) findViewById(R.id.listViewContaRecebida);

        try {

            dataEmissao = this.getIntent().getStringExtra("DataEmi");
            FillList fillList = new FillList();
            fillList.execute("");
            // Toast.makeText(ContaRecebidaDetalhe.this, data, Toast.LENGTH_SHORT).show();

        } catch (Exception e) {

            // FillList fillList = new FillList();
            // fillList.execute("");
        }
    }

    public class FillList extends AsyncTask<String, String, String> {
        String z = "";

        List<Map<String, String>> prolist = new ArrayList<Map<String, String>>();

        @Override
        protected void onPreExecute() {

            progressBarContaRecebida.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {

            progressBarContaRecebida.setVisibility(View.GONE);
            Toast.makeText(ContaRecebidaDetalhe.this, r, Toast.LENGTH_SHORT).show();

            //String[] from = { "A", "B", "C" };
            String[] from = {"A", "B", "C", "D"};
            int[] views = {
                    R.id.txtCodAluno,
                    //R.id.txtNomeAluno,
                    R.id.txtDescricao,
                    R.id.txtData,
                    R.id.txtValor};

            final SimpleAdapter ADA = new SimpleAdapter(ContaRecebidaDetalhe.this,
                    prolist, R.layout.activity_conta_recebida_detalhe_view, from,
                    views);
            listViewContaRecebida.setAdapter(ADA);
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                configuracao = adpConfiguracao.getItem(0);

                String ip = configuracao.getIp();
                String driver = configuracao.getDriver();
                String db = configuracao.getBancoDados();
                String usuario = configuracao.getUsuario();
                String senha = configuracao.getSenha();

                Connection con = connectionRemote.CONN(ip, driver, db, usuario, senha);
                if (con == null) {
                    z = "Error in connection with SQL server";
                } else {
                    String query = "SELECT CRA.CodAluno,A.Nome,CRA.Descricao,CRA.DataEmi,format(CRA.Valor,2) as Valor " +
                            "FROM ContasRecebidasAlunos CRA " +
                            "INNER JOIN Aluno A on A.Codigo = CRA.CodAluno " +
                            "WHERE CRA.DataEmi = '" + dataEmissao + "' " +
                            "ORDER BY A.Nome, Valor";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    ArrayList<String> data1 = new ArrayList<String>();
                    while (rs.next()) {

                        java.sql.Date gDia = rs.getDate("DataEmi");
                        SimpleDateFormat fDia = new SimpleDateFormat("dd");

                        java.sql.Date gMes = rs.getDate("DataEmi");
                        SimpleDateFormat fMes = new SimpleDateFormat("MM");

                        java.sql.Date gAno = rs.getDate("DataEmi");
                        SimpleDateFormat fAno = new SimpleDateFormat("yyyy");

                        Map<String, String> datanum = new HashMap<String, String>();
                        datanum.put("A", rs.getString("CodAluno") + " - " + rs.getString("Nome"));
                        //datanum.put("B", "Nome do Aluno: " + rs.getString("Nome"));
                        datanum.put("B", "Descrição: " + rs.getString("Descricao"));
                        datanum.put("C", "Data do Recebimento: " + fDia.format(gDia) + "/" + fMes.format(gMes) + "/" + fAno.format(gAno));
                        datanum.put("D", "Total: R$ " + rs.getString("Valor"));
                        //datanum.put("C", "Endereço: " + rs.getString("Endereco"));
                        //datanum.put("D", "CPF: " + rs.getString("NroInsc"));
                        //  datanum.put("C", rs.getString("ProDesc"));
                        prolist.add(datanum);
                    }


                    z = "Success";
                }
            } catch (Exception ex) {
                z = "Error retrieving data from table" + ex.getMessage();

            }
            return z;
        }
    }
}
