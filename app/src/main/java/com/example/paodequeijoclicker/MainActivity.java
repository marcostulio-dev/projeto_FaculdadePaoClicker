package com.example.paodequeijoclicker;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private TextView tvPaoCount, tvFornoCount, tvAprenCount, tvFabricaCount;
    private Button btnForno, btnAprendiz, btnFabrica;

    private ImageButton btnClick;

    private int paoCount = 0;
    private int paoPorClique = 1;
    private int autoProd = 0;

    private int fornoCount = 0;
    private int aprendizCount = 0;
    private int fabricaCount = 0;

    private SharedPreferences prefs;
    private Handler handler = new Handler();
    private Runnable autoProductionLoop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        prefs = getSharedPreferences("idle_game", MODE_PRIVATE);

        tvPaoCount = findViewById(R.id.tvPaoCount);
        tvFornoCount = findViewById(R.id.tvFornoCount);
        tvAprenCount = findViewById(R.id.tvAprenCount);
        tvFabricaCount = findViewById(R.id.tvFabricaCount);
        btnClick = findViewById(R.id.btnClick);
        btnForno = findViewById(R.id.btnForno);
        btnAprendiz = findViewById(R.id.btnAprendiz);
        btnFabrica = findViewById(R.id.btnFabrica);

        // Save do progresso
        paoCount = prefs.getInt("paoCount", 0);
        paoPorClique = prefs.getInt("paoPorClique", 1);
        autoProd = prefs.getInt("autoProd", 0);
        fornoCount = prefs.getInt("fornoCount", 0);
        aprendizCount = prefs.getInt("aprendizCount", 0);
        fabricaCount = prefs.getInt("fabricaCount", 0);
        atualizarContador();

        btnClick.setOnClickListener(v -> {
            paoCount += paoPorClique;
            atualizarContador();
        });

        btnForno.setOnClickListener(v -> {
            if (paoCount >= 100) {
                paoCount -= 100;
                paoPorClique += 1;
                fornoCount++;
                atualizarContador();
            }
        });

        btnAprendiz.setOnClickListener(v -> {
            if (paoCount >= 500) {
                paoCount -= 500;
                autoProd += 1;
                aprendizCount++;
                atualizarContador();
            }
        });

        btnFabrica.setOnClickListener(v -> {
            if (paoCount >= 2000) {
                paoCount -= 2000;
                autoProd += 10;
                fabricaCount++;
                atualizarContador();
            }
        });

        // Auto produção
        autoProductionLoop = new Runnable() {
            @Override
            public void run() {
                paoCount += autoProd;
                atualizarContador();
                handler.postDelayed(this, 1000); // a cada 1 segundo
            }
        };
        handler.post(autoProductionLoop);
    }

    private void atualizarContador() {
        tvPaoCount.setText("Pães de Queijo: " + paoCount);
        tvFornoCount.setText("Fornos: " + fornoCount);
        tvAprenCount.setText("Aprendizes: " + aprendizCount);
        tvFabricaCount.setText("Fábricas: " + fabricaCount);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("paoCount", paoCount);
        editor.putInt("paoPorClique", paoPorClique);
        editor.putInt("autoProd", autoProd);
        editor.putInt("fornoCount", fornoCount);
        editor.putInt("aprendizCount", aprendizCount);
        editor.putInt("fabricaCount", fabricaCount);
        editor.apply();
    }
}
