package com.example.pc.smartagrisens;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toolbar;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by pc on 16/02/2016.
 */
public class Analyse extends AppCompatActivity {

    private double[][] values = new double[600][2];
    private int values_count = 0;

    //String sp = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        String sp=null;
        if (getIntent().hasExtra("sfile")) {
            sp = getIntent().getStringExtra("sfile");
       }
       // String sp = IntentExample.chemin;
        loadSpectrum(sp);
        DataBaseHelper db = new DataBaseHelper(this);
        String om = getOM();

        db.insertData("OM", om);
       // viewAll();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Résultats"));
        tabLayout.addTab(tabLayout.newTab().setText("Interp"));
        tabLayout.addTab(tabLayout.newTab().setText("Recom"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


       // TextView monTexte = (TextView) findViewById(R.id.VOM);
      //  monTexte.setText(String.format("%.2f", r) + "%");

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });






    }

public void viewAll(){
    DataBaseHelper db = new DataBaseHelper(this);
    Cursor res = db.getAllData();
    if (res.getCount()==0){
        //show message
        showMessage("Error","Nothing found");
    }

   StringBuffer buffer = new StringBuffer();
    while(res.moveToNext()){
       buffer.append("Name: "+ res.getString(1)+"\n");
       buffer.append("Value: " + res.getString(2) + "\n");
   }
    //show all data
    showMessage("Historique OM", buffer.toString());
    return;
}

   public void showMessage(String title, String Message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

    public String getOM() {
        double r = calculateOM() * 100;
        String om = String.format("%.2f", r);
        return om;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

       /*  @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.analyse);
        String sp = IntentExample.chemin;
        //String sp = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/T100.Spectrum";
        loadSpectrum(sp);
        double r = calculateOM() * 100;
        setContentView(R.layout.analyse);
        TextView monTexte = (TextView) findViewById(R.id.VOM);
        monTexte.setText(String.format("%.2f", r) + "%");

   Button OM = (Button) findViewById(R.id.button3);
        OM.setOnClickListener(new View.OnClickListener() {
         @Override
           public void onClick(View v) {
               Intent iom = new Intent(Analyse.this, com.class);
               startActivity(iom);
           }
        });

    }*/


    double calculateOM(){
        double r = 0;
        int den = 0;
        for (int i = 0; i < values_count; i++) {
            if (values[i][0] >= 1550 && values[i][0] <= 1890) {
                r += values[i][1];
                den++;
            }
        }
        if (den == 0) return 0;
        return 10000 * Math.pow(den / r, 3.5);
    }

    void loadSpectrum(String spectrumPath) {
        FileInputStream in = null;
        try {
            in = new FileInputStream(spectrumPath);
            if (in == null)
                return;
            skipLine(in);
            for (int i = 0; ; i++) {
                double[] r = getSample(in);
                values[i][0] = r[0];
                if (r[1] == 1) break;
                r = getSample(in);
                values[i][1] = r[0];
                if (r[1] == 1) break;
                values_count++;
            }

            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    double[] getSample(FileInputStream in) throws IOException {
        byte[] data = new byte[128];
        int i = 0;
        double eof = 0;
        for (; ; i++) {
            if (in.read(data, i, 1) == -1) {
                eof = 1;
                break;
            }
            if (data[i] == '\n' || data[i] == '\t')
                break;
        }
        String r = "0";
        if (eof != 1)
            r = new String(data, 0, i);
        return new double[]{Double.parseDouble(r), eof};
    }

    void skipLine(FileInputStream in) throws IOException {
        byte[] data = new byte[1];
        while (data[0] != '\n')
            if (in.read(data, 0, 1) == -1)
                return;
    }
}