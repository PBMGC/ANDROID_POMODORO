package com.example.pomodorofinal;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class Agregar extends AppCompatActivity {
    private EditText entradaTarea;
    private Button botonGuardar, botonCancelar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);

        entradaTarea = findViewById(R.id.entrada_tarea);
        botonGuardar = findViewById(R.id.boton_guardar);
        botonCancelar = findViewById(R.id.boton_cancelar);

        botonGuardar.setOnClickListener(v -> guardarTarea());
        botonCancelar.setOnClickListener(v -> finish());
    }

    private void guardarTarea() {
        String tarea = entradaTarea.getText().toString().trim();

        if (!tarea.isEmpty()) {
            SharedPreferences preferencias = getSharedPreferences("PomodoroPrefs", MODE_PRIVATE);
            int tiempoPomodoro = 0;

            if (preferencias.getBoolean("pomodoroLargo", false)) {
                tiempoPomodoro = 45 * 60 * 1000;
            } else if (preferencias.getBoolean("pomodoroMedio", false)) {
                tiempoPomodoro = 25 * 60 * 1000;
            } else if (preferencias.getBoolean("pomodoroCorto", false)) {
                tiempoPomodoro = 15 * 60 * 1000;
            }

            Intent intent = new Intent();
            intent.putExtra("nombre_tarea", tarea);
            intent.putExtra("tiempo_tarea", tiempoPomodoro);
            setResult(RESULT_OK, intent);

            finish();
        } else {
            entradaTarea.setError("Por favor, ingrese una tarea.");
        }
    }
}
