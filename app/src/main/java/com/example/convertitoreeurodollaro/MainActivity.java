package com.example.convertitoreeurodollaro;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity {
    final String[] currency = {null};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button convertiInEuro = findViewById(R.id.convertiInEuro);
        convertiInEuro.setOnClickListener(view -> {
            onClickIn(view);
        });

        Button convertiDaEuro = findViewById(R.id.convertiDaEuro);
        convertiDaEuro.setOnClickListener(view -> {
            onClickFrom(view);
        });


        String[] items = new String[]{"USD", "GBP", "JPY", "CHF"};
        ArrayAdapter<String> arrayCurrency = new ArrayAdapter<>(this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, items);
        Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(arrayCurrency);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                currency[0] = adapterView.getItemAtPosition(i).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                currency[0] = "USD";

            }
        });
    }

    //da valuta a euro
    private void onClickIn(View view) {
        //prendo il valore inserito dall'utente
        EditText input = findViewById(R.id.quantita);
        float quantita = Float.parseFloat(input.getText().toString());

        String finalCurrency = currency[0];
        new Thread(new Runnable() {
            @Override
            public void run() {
                //thread che effettua la richiesta asincrona
                ECBRequest request = new ECBRequest();
                //getRate effettua la richiesta al server
                //fa il parsing della risposta
                //e restituisce il valore del tasso di conversione

                Double rate = request.getRate(finalCurrency);

                Handler handler = new Handler(Looper.getMainLooper());
                boolean post = handler.post(() -> {
                    //modifica all'interfaccia
                    //calcolo il valore in euro
                    DecimalFormat format = new DecimalFormat("####.000");
                    Double euro = Double.parseDouble(format.format(quantita / rate));

                    TextView result = MainActivity.this.findViewById(R.id.resultField);
                    result.setText(String.valueOf(euro));

                });
            }
        }).start();
    }


    //da valuta a euro
    private void onClickFrom(View view) {
        //prendo il valore inserito dall'utente
        EditText input = findViewById(R.id.quantita);
        float quantita = Float.parseFloat(input.getText().toString());

        String finalCurrency = currency[0];
        new Thread(new Runnable() {
            @Override
            public void run() {
                //thread che effettua la richiesta asincrona
                ECBRequest request = new ECBRequest();
                //getRate effettua la richiesta al server
                //fa il parsing della risposta
                //e restituisce il valore del tasso di conversione

                Double rate = request.getRate(finalCurrency);

                /*TextView result1 = MainActivity.this.findViewById(R.id.test);
                result1.setText(String.valueOf(finalCurrency));*/

                Handler handler = new Handler(Looper.getMainLooper());
                boolean post = handler.post(() -> {
                    //modifica all'interfaccia
                    //calcolo il valore in euro
                    DecimalFormat format = new DecimalFormat("####.000");
                    Double euro = Double.parseDouble(format.format(quantita * rate));

                    TextView result = MainActivity.this.findViewById(R.id.resultField);
                    result.setText(String.valueOf(euro));

                });
            }
        }).start();
    }
}