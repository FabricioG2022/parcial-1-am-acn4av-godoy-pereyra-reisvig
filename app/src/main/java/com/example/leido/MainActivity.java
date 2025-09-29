package com.example.leido;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

/**
 * Activity principal con tabs (Leídos / Deseados) en la parte superior
 * y un botón "+" fijo abajo-derecha para añadir libros.
 */
public class MainActivity extends AppCompatActivity {

    private TextView tabLeidos;
    private TextView tabDeseados;
    private boolean isLeidosActive = true; // trackea pestaña activa

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Centrar título en ActionBar
        if (getSupportActionBar() != null) {
            ActionBar ab = getSupportActionBar();
            ab.setDisplayShowTitleEnabled(false);

            TextView tv = new TextView(this);
            tv.setText(getString(R.string.app_name));
            tv.setTextSize(20);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(ContextCompat.getColor(this, android.R.color.white));

            ActionBar.LayoutParams params = new ActionBar.LayoutParams(
                    ActionBar.LayoutParams.MATCH_PARENT,
                    ActionBar.LayoutParams.WRAP_CONTENT,
                    Gravity.CENTER
            );
            ab.setCustomView(tv, params);
            ab.setDisplayShowCustomEnabled(true);
        }

        // Referencias a tabs
        tabLeidos = findViewById(R.id.tabLeidos);
        tabDeseados = findViewById(R.id.tabDeseados);

        tabLeidos.setOnClickListener(v -> {
            replaceFragment(new FragmentLeidos());
            setActiveTab(tabLeidos, tabDeseados);
            isLeidosActive = true;
        });

        tabDeseados.setOnClickListener(v -> {
            replaceFragment(new FragmentDeseados());
            setActiveTab(tabDeseados, tabLeidos);
            isLeidosActive = false;
        });

        // Cargar por defecto el fragment Leídos
        if (savedInstanceState == null) {
            replaceFragment(new FragmentLeidos());
            setActiveTab(tabLeidos, tabDeseados);
            isLeidosActive = true;
        }

        // FAB-like button (un Button simple posicionado en layout)
        Button fab = findViewById(R.id.btnAdd);
        fab.setOnClickListener(v -> showAddLibroDialog());
    }

    private void replaceFragment(Fragment f) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, f)
                .commit();
    }

    /**
     * Muestra un AlertDialog con un layout personalizado para ingresar los campos del libro.
     * Validación mínima: título obligatorio.
     */
    private void showAddLibroDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.dialog_add_libro, null);

        final EditText etTitulo = dialogView.findViewById(R.id.inputTitulo);
        final EditText etAutor = dialogView.findViewById(R.id.inputAutor);
        final EditText etEditorial = dialogView.findViewById(R.id.inputEditorial);
        final EditText etIsbn = dialogView.findViewById(R.id.inputIsbn);
        final EditText etComentario = dialogView.findViewById(R.id.inputComentario);

        AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle(R.string.dialog_add_title)
                .setView(dialogView)
                .setNegativeButton(R.string.cancel, (d, which) -> { /* cierra automáticamente */ })
                .setPositiveButton(R.string.save, null); // override más abajo

        final AlertDialog dialog = builder.create();
        dialog.show();

        // Override click del botón positivo para validar título obligatorio
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(v -> {
            String titulo = etTitulo.getText().toString().trim();
            String autor = etAutor.getText().toString().trim();
            String editorial = etEditorial.getText().toString().trim();
            String isbn = etIsbn.getText().toString().trim();
            String comentario = etComentario.getText().toString().trim();

            if (titulo.isEmpty()) {
                Toast.makeText(MainActivity.this, R.string.error_title_required, Toast.LENGTH_SHORT).show();
                return; // no cierra el dialog
            }

            Libro nuevo = new Libro(titulo, autor, editorial, isbn, comentario);
            LibroRepository repo = LibroRepository.getInstance();

            if (isLeidosActive) {
                repo.addLeidoAtStart(nuevo);
            } else {
                repo.addDeseadoAtStart(nuevo);
            }

            // Notificar al fragment actual para que refresque su lista
            Fragment current = getSupportFragmentManager().findFragmentById(R.id.container);
            if (current instanceof FragmentLeidos) {
                ((FragmentLeidos) current).refreshList();
            } else if (current instanceof FragmentDeseados) {
                ((FragmentDeseados) current).refreshList();
            }

            dialog.dismiss(); // cerrar dialog
        });
    }

    /**
     * Ajusta la apariencia de la pestaña activa e inactiva.
     * Mantiene la misma lógica visual que en la iteración anterior.
     */
    private void setActiveTab(TextView active, TextView inactive) {
        if (active == null || inactive == null) return;

        active.setEnabled(false);
        active.setAlpha(1f);
        active.setTextColor(ContextCompat.getColor(this, R.color.tab_active_text));

        inactive.setEnabled(true);
        inactive.setAlpha(0.6f);
        inactive.setTextColor(ContextCompat.getColor(this, R.color.tab_inactive_text));
    }
}
