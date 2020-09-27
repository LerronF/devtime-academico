package com.example.lerron.devtimeacademico;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.lerron.devtimeacademico.Domain.Car;
import com.example.lerron.devtimeacademico.Fragments.CarFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lerron on 09/12/2017.
 */

public class TelaPrincipal extends AppCompatActivity {
    private static String TAG = "LOG";
    private Toolbar mToolbar;
    private Toolbar mToolbarBottom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela_principal);

        mToolbar = (Toolbar) findViewById(R.id.tb_main);
        mToolbar.setTitle("DevTime Sistemas");
        mToolbar.setSubtitle("Módulo BI");
        mToolbar.setLogo(R.drawable.ic_launcher);
        setSupportActionBar(mToolbar);

        mToolbarBottom = (Toolbar) findViewById(R.id.inc_tb_bottom);
        mToolbarBottom.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent it = null;

                switch (menuItem.getItemId()) {
                    case R.id.action_facebook:
                        it = new Intent(Intent.ACTION_VIEW);
                        it.setData(Uri.parse("http://www.facebook.com"));
                        break;
                    case R.id.action_youtube:
                        it = new Intent(Intent.ACTION_VIEW);
                        it.setData(Uri.parse("http://www.youtube.com"));
                        break;
                    case R.id.action_google_plus:
                        it = new Intent(Intent.ACTION_VIEW);
                        it.setData(Uri.parse("http://plus.google.com"));
                        break;
                    case R.id.action_linkedin:
                        it = new Intent(Intent.ACTION_VIEW);
                        it.setData(Uri.parse("http://www.linkedin.com"));
                        break;
                    case R.id.action_whatsapp:
                        it = new Intent(Intent.ACTION_VIEW);
                        it.setData(Uri.parse("http://www.whatsapp.com"));
                        break;
                }

                startActivity(it);
                return true;
            }
        });
        mToolbarBottom.inflateMenu(R.menu.menu_bottom);

        mToolbarBottom.findViewById(R.id.iv_settings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(TelaPrincipal.this, "Settings pressed", Toast.LENGTH_SHORT).show();
            }
        });


        // FRAGMENT
        CarFragment frag = (CarFragment) getSupportFragmentManager().findFragmentByTag("mainFrag");
        if (frag == null) {
            frag = new CarFragment();
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.rl_fragment_container, frag, "mainFrag");
            ft.commit();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_second_activity) {
            // startActivity(new Intent(this, SecondActivity.class));

            Intent it = new Intent(this, SecondActivity.class);
            startActivityForResult(it, 0);
        }

        return super.onOptionsItemSelected(item);
    }


    public List<Car> getSetCarList(int qtd) {
        String[] models = new String[]{"Módulo Financeiro", "Módulo Acadêmico", "Corvette", "Pagani Zonda", "Porsche 911 Carrera", "BMW 720i", "DB77", "Mustang", "Camaro", "CT6"};
        String[] brands = new String[]{"Vizualização de Resultados", "Controle de Matrículas", "Chevrolet", "Pagani", "Porsche", "BMW", "Aston Martin", "Ford", "Chevrolet", "Cadillac"};
        int[] photos = new int[]{R.drawable.financeiro, R.drawable.academico, R.drawable.corvette, R.drawable.paganni_zonda, R.drawable.porsche_911, R.drawable.bmw_720, R.drawable.db77, R.drawable.mustang, R.drawable.camaro, R.drawable.ct6};
        List<Car> listAux = new ArrayList<>();

        for (int i = 0; i < qtd; i++) {
            Car c = new Car(models[i % models.length], brands[i % brands.length], photos[i % models.length]);
            listAux.add(c);
        }
        return (listAux);
    }
}

