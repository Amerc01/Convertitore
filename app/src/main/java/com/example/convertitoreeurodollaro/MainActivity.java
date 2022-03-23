package com.example.convertitoreeurodollaro;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RadioGroup radioGroup = findViewById(R.id.radioGroup);
        final int b = radioGroup.getCheckedRadioButtonId();
        final RadioButton r = findViewById(b);
        final String currency = r.getTag().toString();


        Button convertiInEuro = findViewById(R.id.convertiInEuro);
        convertiInEuro.setOnClickListener(view -> {
            onClickIn(view, currency);
        });

        Button convertiDaEuro = findViewById(R.id.convertiDaEuro);
        convertiInEuro.setOnClickListener(view -> {
            onClickFrom(view, currency);
        });
    }

    //da valuta a euro
    private void onClickIn(View view, String currency) {
        //prendo il valore inserito dall'utente
        EditText input = findViewById(R.id.quantita);
        float quantita = Float.parseFloat(input.getText().toString());


        new Thread(new Runnable() {
            @Override
            public void run() {
                //thread che effettua la richiesta asincrona
                ECBRequest request = new ECBRequest();
                //getRate effettua la richiesta al server
                //fa il parsing della risposta
                //e restituisce il valore del tasso di conversione

                Double rate = request.getRate(currency);

                Handler handler = new Handler(Looper.getMainLooper());
                boolean post = handler.post(() -> {
                    //modifica all'interfaccia
                    //calcolo il valore in euro
                    Double euro =  quantita / rate;

                    TextView result = MainActivity.this.findViewById(R.id.resultField);
                    result.setText(String.valueOf(euro));

                });
            }
        }).start();
    }

    //da euro a valuta
    private void onClickFrom(View view, String currency) {

        //prendo il valore inserito dall'utente
        EditText input = findViewById(R.id.quantita);
        float quantita = Float.parseFloat(input.getText().toString());


        new Thread(new Runnable() {
            @Override
            public void run() {
                //thread che effettua la richiesta asincrona
                ECBRequest request = new ECBRequest();
                //getRate effettua la richiesta al server
                //fa il parsing della risposta
                //e restituisce il valore del tasso di conversione

                Double rate = request.getRate(currency);

                Handler handler = new Handler(Looper.getMainLooper());
                boolean post = handler.post(() -> {
                    //modifica all'interfaccia
                    //calcolo il valore in euro
                    Double valuta =  quantita * rate;

                    TextView result = MainActivity.this.findViewById(R.id.resultField);
                    result.setText(String.valueOf(valuta));

                });
            }
        }).start();
    }
}