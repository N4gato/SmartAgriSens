package com.example.pc.smartagrisens;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothSocket;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    // private  String path = IntentExample.getChemin();
    private BluetoothDevice device = null;// le périphérique (le module bluetooth)
    private BluetoothSocket socket = null;
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private final static int REQUEST_CODE_ENABLE_BLUETOOTH = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

// vérifier si le device possède le module bluetooth et l'activer
        if (bluetoothAdapter == null)
            Toast.makeText(MainActivity.this, "Pas de Bluetooth", Toast.LENGTH_SHORT).show();
        // else
        // Toast.makeText(MainActivity.this, "Avec Bluetooth", Toast.LENGTH_SHORT).show();

        if (!bluetoothAdapter.isEnabled()) {
            Intent enableBlueTooth = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBlueTooth, REQUEST_CODE_ENABLE_BLUETOOTH);
        }

        // On récupère la liste des périphériques associés
        Set<BluetoothDevice> setpairedDevices = BluetoothAdapter.getDefaultAdapter().getBondedDevices();
        BluetoothDevice[] pairedDevices = (BluetoothDevice[]) setpairedDevices.toArray(new BluetoothDevice[setpairedDevices.size()]);

        // On parcourt la liste pour trouver notre module bluetooth
        for (int i = 0; i < pairedDevices.length; i++) {
            // On teste si ce périphérique contient le nom du module bluetooth connecté au microcontrôleur
            if (pairedDevices[i].getName().contains("BlueNRG")) {

                device = pairedDevices[i];
                try {
                    // On récupère le socket de notre périphérique
                    socket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
                   // receiveStream = socket.getInputStream();// Canal de réception (valide uniquement après la connexion)
                   // sendStream = socket.getOutputStream();// Canal d'émission (valide uniquement après la connexion)

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            }
        }

        // Pour créer une autre activité
        Button choisir = (Button) findViewById(R.id.button);
        choisir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent theIntent = new Intent(MainActivity.this, IntentExample.class);
                // Puis on lance l'intent !
                startActivity(theIntent);
            }
        });
    }


}

