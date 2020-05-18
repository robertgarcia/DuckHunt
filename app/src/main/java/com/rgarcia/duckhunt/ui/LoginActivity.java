package com.rgarcia.duckhunt.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.rgarcia.duckhunt.R;
import com.rgarcia.duckhunt.common.Constantes;
import com.rgarcia.duckhunt.models.User;

public class LoginActivity extends AppCompatActivity {

    EditText etNick;
    Button btnStart;
    String nick;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Instanciar la conexion a firestrore
        db = FirebaseFirestore.getInstance();

        etNick = findViewById(R.id.txt_nombre_jugador);
        btnStart = findViewById(R.id.btn_iniciar_juego);

        //Cargar tipo de fuente
        Typeface typeface = Typeface.createFromAsset(getAssets(), "pixel.ttf");
        etNick.setTypeface(typeface);
        btnStart.setTypeface(typeface);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nick = etNick.getText().toString();
                if(nick.isEmpty()){
                    etNick.setError("El nombre del jugador es obligatorio");
                } else if (etNick.length() < 3) {
                    etNick.setError("El nombre del jugador debe tener al menos 3 caracteres");
                }else {
                    addNickAndStart();
                }
            }
        });
    }

    private void addNickAndStart(){
        db.collection("users").whereEqualTo("nick", nick)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (queryDocumentSnapshots.size() > 0) {
                            etNick.setError("El nick no esta disponible!");
                        } else {
                            addNickToFirestore();
                        }
                    }
        });
    }

    private void addNickToFirestore(){
        db.collection("users")
                .add(new User(nick, 0))
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        etNick.setText("");
                        Intent i = new Intent(LoginActivity.this, GameActivity.class);
                        i.putExtra(Constantes.EXTRA_NICK, nick);
                        i.putExtra(Constantes.EXTRA_ID, documentReference.getId());
                        startActivity(i);
                    }
                });
    }
}
