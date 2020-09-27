package com.example.lerron.devtimeacademico;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.lerron.devtimeacademico.Database.ConnectionRemoteMySQL;
import com.example.lerron.devtimeacademico.Database.Database;
import com.example.lerron.devtimeacademico.Dominio.Entidade.Configuracao;
import com.example.lerron.devtimeacademico.Dominio.RepositorioDevTime;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Lerron on 10/12/2017.
 */

public class Financeiro extends AppCompatActivity {
    ConnectionRemoteMySQL connectionRemote;
    String proid, data;
    private Toolbar mToolbar;
    private Toolbar mToolbarBottom;
    private ListView listViewClientes;
    private ProgressBar progressBarClientes;
    private ArrayAdapter<Configuracao> adpConfiguracao;
    private com.example.lerron.devtimeacademico.Database.Database database;
    private SQLiteDatabase connLocal;
    private RepositorioDevTime repositorioDevtime;
    private Configuracao configuracao;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financeiro);

        mToolbar = (Toolbar) findViewById(R.id.tb_main);
        mToolbar.setTitle("DevTime Sistemas");
        mToolbar.setSubtitle("BI Financeiro");
        mToolbar.setLogo(R.drawable.ic_launcher);
        setSupportActionBar(mToolbar);


        connectionRemote = new ConnectionRemoteMySQL();

        progressBarClientes = (ProgressBar) findViewById(R.id.progressBarFinanceiro);
        progressBarClientes.setVisibility(View.GONE);
        listViewClientes = (ListView) findViewById(R.id.listViewFinanceiro);

        proid = "";

        database = new Database(this);
        connLocal = database.getWritableDatabase();

        repositorioDevtime = new RepositorioDevTime(connLocal);

        adpConfiguracao = repositorioDevtime.buscaConfiguracao(this);

        FillList fillList = new FillList();
        fillList.execute("");
    }

    public class FillList extends AsyncTask<String, String, String> {
        String z = "";

        List<Map<String, String>> prolist = new ArrayList<Map<String, String>>();

        @Override
        protected void onPreExecute() {

            progressBarClientes.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(String r) {

            progressBarClientes.setVisibility(View.GONE);
            Toast.makeText(Financeiro.this, r, Toast.LENGTH_SHORT).show();

            //String[] from = { "A", "B", "C" };
            String[] from = {"A", "B", "D", "E"};
            int[] views = {R.id.txtData, R.id.txtValor, R.id.txtQtdRecebimento, R.id.txtQtdIsento};
            final SimpleAdapter ADA = new SimpleAdapter(Financeiro.this,
                    prolist, R.layout.activity_financeiro_view, from,
                    views);
            listViewClientes.setAdapter(ADA);

            listViewClientes.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1,
                                        int arg2, long arg3) {
                    HashMap<String, Object> obj = (HashMap<String, Object>) ADA
                            .getItem(arg2);
                    data = (String) obj.get("C");

                    //METODO PARA ENVIAR PARAMETROS PARA A OUTRA ACTIVITY
                    Intent intent = new Intent(Financeiro.this, ContaRecebidaDetalhe.class);
                    intent.putExtra("DataEmi", data);

                    startActivity(intent);

                    finish();
                }
            });


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
                    String query = "Select CRA.DataEmi, " +
                            "(SELECT Count(CRDA.numero) as QTD " +
                            "FROM ContasRecebidasAlunos CRDA " +
                            "WHERE CRDA.VALOR > 0 AND CRDA.DataEmi = CRA.DataEmi) as QtdRecebido, " +
                            "(SELECT Count(CRDA.numero) as QTD " +
                            "FROM ContasRecebidasAlunos CRDA " +
                            "WHERE CRDA.VALOR = 0 AND CRDA.DataEmi = CRA.DataEmi) as QtdIsento, " +
                            "format(Sum(CRA.Valor ),2) as TotalDia " +
                            "From ContasRecebidasAlunos CRA " +
                            "Group By CRA.DataEmi desc";
                    PreparedStatement ps = con.prepareStatement(query);
                    ResultSet rs = ps.executeQuery();

                    ArrayList<String> data1 = new ArrayList<String>();
                    while (rs.next()) {
                        // data =  rs.getString("DataEmi");

                        Date gDia = rs.getDate("DataEmi");
                        SimpleDateFormat fDia = new SimpleDateFormat("dd");

                        Date gMes = rs.getDate("DataEmi");
                        SimpleDateFormat fMes = new SimpleDateFormat("MM");

                        Date gAno = rs.getDate("DataEmi");
                        SimpleDateFormat fAno = new SimpleDateFormat("yyyy");

                        Map<String, String> datanum = new HashMap<String, String>();
                        datanum.put("A", "Data do Recebimento: " + fDia.format(gDia) + "/" + fMes.format(gMes) + "/" + fAno.format(gAno));
                        datanum.put("B", "Total do Dia: R$ " + rs.getString("TotalDia"));
                        data = rs.getString("DataEmi");
                        datanum.put("C", rs.getString("DataEmi"));
                        datanum.put("D", rs.getString("QtdRecebido"));
                        datanum.put("E", rs.getString("QtdIsento"));
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
