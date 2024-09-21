package com.example.pomodorofinal;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;

public class Configuracion extends AppCompatActivity {

    private CheckBox pomodoroLargo, pomodoroMediano, pomodoroCorto;
    private Button botonGuardarConfiguracion, botonCancelarConfiguracion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        pomodoroLargo = findViewById(R.id.pomodoro_largo);
        pomodoroMediano = findViewById(R.id.pomodoro_medio);
        pomodoroCorto = findViewById(R.id.pomodoro_corto);
        botonGuardarConfiguracion = findViewById(R.id.boton_guardar_configuracion);
        botonCancelarConfiguracion = findViewById(R.id.boton_cancelar_configuracion);

        cargarConfiguraciones();

        pomodoroLargo.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                pomodoroMediano.setChecked(false);
                pomodoroCorto.setChecked(false);
            }
        });

        pomodoroMediano.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                pomodoroLargo.setChecked(false);
                pomodoroCorto.setChecked(false);
            }
        });

        pomodoroCorto.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                pomodoroLargo.setChecked(false);
                pomodoroMediano.setChecked(false);
            }
        });

        botonGuardarConfiguracion.setOnClickListener(v -> guardarConfiguraciones());
        botonCancelarConfiguracion.setOnClickListener(v -> finish());
    }

    private void cargarConfiguraciones() {
        SharedPreferences preferencias = getSharedPreferences("PomodoroPrefs", MODE_PRIVATE);
        pomodoroLargo.setChecked(preferencias.getBoolean("pomodoroLargo", false));
        pomodoroMediano.setChecked(preferencias.getBoolean("pomodoroMediano", false));
        pomodoroCorto.setChecked(preferencias.getBoolean("pomodoroCorto", false));
    }

    private void guardarConfiguraciones() {
        SharedPreferences preferencias = getSharedPreferences("PomodoroPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferencias.edit();
        editor.putBoolean("pomodoroLargo", pomodoroLargo.isChecked());
        editor.putBoolean("pomodoroMediano", pomodoroMediano.isChecked());
        editor.putBoolean("pomodoroCorto", pomodoroCorto.isChecked());
        editor.apply();

        finish();
    }

    public static int obtenerTiempoPomodoro(SharedPreferences preferencias) {
        boolean largoSeleccionado = preferencias.getBoolean("pomodoroLargo", false);
        boolean medianoSeleccionado = preferencias.getBoolean("pomodoroMediano", false);
        boolean cortoSeleccionado = preferencias.getBoolean("pomodoroCorto", false);

        if (!largoSeleccionado && !medianoSeleccionado && !cortoSeleccionado) {
            return 20 * 60 * 1000; // 20 minutos por defecto
        } else if (largoSeleccionado) {
            return 45 * 60 * 1000; // 45 minutos
        } else if (medianoSeleccionado) {
            return 25 * 60 * 1000; // 25 minutos
        } else {
            return 15 * 60 * 1000; // 15 minutos
        }
    }
}
