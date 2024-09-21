package com.example.pomodorofinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView textoTemporizador, textoTareaActual;
    private Button botonIniciar, botonPausar, botonAgregarTarea, botonConfiguracion;
    private String tareaActual;
    private CountDownTimer temporizador;
    private boolean temporizadorCorriendo = false;
    private List<tarea> listaTareas;
    private SharedPreferences preferenciasCompartidas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textoTemporizador = findViewById(R.id.texto_temporizador);
        textoTareaActual = findViewById(R.id.tarea_actual);
        botonIniciar = findViewById(R.id.boton_iniciar);
        botonPausar = findViewById(R.id.boton_pausar);
        botonAgregarTarea = findViewById(R.id.boton_agregar_tarea);
        botonConfiguracion = findViewById(R.id.boton_configuracion);

        listaTareas = new ArrayList<>();
        preferenciasCompartidas = getSharedPreferences("PomodoroPrefs", MODE_PRIVATE);

        botonIniciar.setOnClickListener(v -> iniciarPomodoro());
        botonPausar.setOnClickListener(v -> pausarPomodoro());
        botonAgregarTarea.setOnClickListener(v -> abrirActividadAgregarTarea());
        botonConfiguracion.setOnClickListener(v -> abrirActividadConfiguracion());

        cargarTareaActual();
        actualizarTareaActual();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cargarTareaActual();
        actualizarTareaActual();
    }

    private void iniciarPomodoro() {
        if (temporizadorCorriendo || listaTareas.isEmpty()) return;

        tarea tareaActualObj = listaTareas.get(0);
        int tiempoPomodoro = Configuracion.obtenerTiempoPomodoro(preferenciasCompartidas);

        temporizador = new CountDownTimer(tiempoPomodoro, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                actualizarTextoTemporizador((int) millisUntilFinished);
            }

            @Override
            public void onFinish() {
                temporizadorCorriendo = false;
                textoTemporizador.setText("¡Tiempo terminado!");
                listaTareas.remove(0);
                if (!listaTareas.isEmpty()) {
                    iniciarPomodoro();
                }
            }
        }.start();

        temporizadorCorriendo = true;
        tareaActual = tareaActualObj.getNombre();
        actualizarTareaActual();
    }

    private void pausarPomodoro() {
        if (temporizador != null) {
            temporizador.cancel();
            temporizadorCorriendo = false;
        }
    }

    private void actualizarTextoTemporizador(int tiempoRestante) {
        int minutos = (tiempoRestante / 1000) / 60;
        int segundos = (tiempoRestante / 1000) % 60;
        textoTemporizador.setText(String.format("%02d:%02d", minutos, segundos));
    }

    private void abrirActividadAgregarTarea() {
        Intent intent = new Intent(this, Agregar.class);
        startActivityForResult(intent, 1);
    }

    private void abrirActividadConfiguracion() {
        Intent intent = new Intent(this, Configuracion.class);
        startActivity(intent);
    }

    private void cargarTareaActual() {
        SharedPreferences sharedPreferences = getSharedPreferences("PomodoroPrefs", MODE_PRIVATE);
        tareaActual = sharedPreferences.getString("currentTask", "Tarea Actual");
    }

    private void actualizarTareaActual() {
        textoTareaActual.setText("Tarea Actual: " + tareaActual);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String nombreTarea = data.getStringExtra("task_name");

            listaTareas.add(new tarea(nombreTarea, 0));

            tareaActual = nombreTarea;
            actualizarTareaActual();
            Toast.makeText(this, "Tarea añadida: " + nombreTarea, Toast.LENGTH_SHORT).show();
        }
    }
}
