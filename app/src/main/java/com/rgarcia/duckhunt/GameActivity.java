package com.rgarcia.duckhunt;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Point;
import android.graphics.Typeface;
import android.nfc.cardemulation.HostNfcFService;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.rgarcia.duckhunt.common.Constantes;

import java.util.Random;

public class GameActivity extends AppCompatActivity {
    TextView tvCounterDucks, tvTimer, tvNick;
    ImageView ivDuck;
    int counter = 0;
    int anchoPantalla, altoPantalla;
    Random aleatorio;
    boolean gameOver = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        //Iniciamos los componentes
        initView();
        eventos();
        initPantalla();
        moveDuck();
        initConteo();
    }

    private void initConteo() {
        new CountDownTimer(60000, 1000){
            @Override
            public void onTick(long millisUntilFinished) {
                long segundosRestantes = millisUntilFinished / 1000;
                tvTimer.setText(segundosRestantes + "s");
            }

            @Override
            public void onFinish() {
                tvTimer.setText("0s");
                gameOver = true;
                mostrarGameOver();
            }
        }.start();
    }

    private void mostrarGameOver() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Has conseguido cazar " + counter + " patos").setTitle("Game Over");
        builder.setPositiveButton("Reiniciar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                counter = 0;
                tvCounterDucks.setText("0");
                gameOver = false;
                initConteo();
                moveDuck();
            }
        });
        builder.setNegativeButton("Salir", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void initPantalla() {
        // Obtener el tamaño del dispositivo
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        anchoPantalla = size.x;
        altoPantalla = size.y;

        // Inicializamos el objeto para generar numeros aleatorios
        aleatorio = new Random();
    }

    private void initView() {
        tvCounterDucks = findViewById(R.id.textViewCounter);
        tvTimer = findViewById(R.id.textViewTimer);
        tvNick = findViewById(R.id.textViewNickname);
        ivDuck = findViewById(R.id.imageViewDuck);

        //Aplicamos el tipo de fuente
        Typeface typeface = Typeface.createFromAsset(getAssets(), "pixel.ttf");
        tvCounterDucks.setTypeface(typeface);
        tvTimer.setTypeface(typeface);
        tvNick.setTypeface(typeface);

        //Extras
        Bundle extras = getIntent().getExtras();
        String nick = extras.getString(Constantes.EXTRA_NICK);
        tvNick.setText(nick);
    }

    private void eventos() {
        ivDuck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!gameOver) {
                    counter++;
                    tvCounterDucks.setText(String.valueOf(counter));
                    ivDuck.setImageResource(R.drawable.duck_clicked);
                    moveDuck();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ivDuck.setImageResource(R.drawable.duck);
                            moveDuck();
                        }
                    }, 500);
                }
            }
        });
    }

    private void moveDuck() {
        int minimo = 0;
        int maximoX = anchoPantalla - ivDuck.getWidth();
        int maximoY = altoPantalla - ivDuck.getHeight();

        // Generarmos 2 numeros aleatorios uno para cada coordenada
        int randomX = aleatorio.nextInt(((maximoX - minimo) + 1) + minimo);
        int randomY = aleatorio.nextInt(((maximoY - minimo) + 1) + minimo);

        // Movemos el pato con los numeros aleatorios
        ivDuck.setX(randomX);
        ivDuck.setY(randomY);
    }
}
