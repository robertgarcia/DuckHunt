package com.rgarcia.duckhunt;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.rgarcia.duckhunt.common.Constantes;

public class LoginActivity extends AppCompatActivity {

    EditText etNick;
    Button btnStart;
    String nick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etNick = findViewById(R.id.txt_nombre_jugador);
        btnStart = findViewById(R.id.btn_iniciar_juego);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nick = etNick.getText().toString();

                if(nick.isEmpty()){
                    etNick.setError("El nombre del jugador es obligatorio");
                } else if (etNick.length() < 3) {
                    etNick.setError("El nombre del jugador debe tener al menos 3 caracteres");
                }else {
                    Intent i = new Intent(LoginActivity.this, GameActivity.class);
                    i.putExtra(Constantes.EXTRA_NICK, nick);
                    startActivity(i);
                }
            }
        });
    }
}
