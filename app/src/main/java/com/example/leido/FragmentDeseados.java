package com.example.leido;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment que muestra la lista de libros deseados.
 * Long-press sobre un ítem -> confirmar -> mover a Leídos.
 */
public class FragmentDeseados extends Fragment {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private final List<String> displayItems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_deseados, container, false);
        listView = root.findViewById(R.id.listDeseados);

        adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1,
                displayItems);
        listView.setAdapter(adapter);

        // Cargar datos iniciales desde el repositorio
        refreshList();

        // Long click: confirmar y mover a Leídos
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            // Obtener título para mostrar en el diálogo
            final int pos = position;
            final String itemLabel = displayItems.get(position);

            new AlertDialog.Builder(requireContext())
                    .setTitle("Marcar como leído")
                    .setMessage("¿Marcar \"" + itemLabel + "\" como leído?")
                    .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Remover de Deseados y añadir a Leidos
                            LibroRepository repo = LibroRepository.getInstance();
                            Libro moved = repo.removeDeseadoAt(pos);
                            if (moved != null) {
                                repo.addLeidoAtStart(moved);

                                // Refrescar esta lista (Deseados)
                                refreshList();

                                // Intentar refrescar FragmentLeidos si está presente
                                // (puede estar o no cargado en el container)
                                if (getActivity() != null) {
                                    List<Fragment> frags = getActivity().getSupportFragmentManager().getFragments();
                                    for (Fragment f : frags) {
                                        if (f instanceof FragmentLeidos) {
                                            ((FragmentLeidos) f).refreshList();
                                        }
                                    }
                                }

                                Toast.makeText(requireContext(), "\"" + moved.getTitulo() + "\" marcado como leído", Toast.LENGTH_SHORT).show();
                            } else {
                                // Posición inválida (caso muy raro)
                                Toast.makeText(requireContext(), "No se pudo mover el libro (posición inválida)", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();

            return true; // consumimos el evento
        });

        return root;
    }

    /**
     * Actualiza la lista desde LibroRepository.
     */
    public void refreshList() {
        displayItems.clear();
        List<Libro> deseados = LibroRepository.getInstance().getDeseados();
        for (Libro l : deseados) {
            displayItems.add(l.toListString());
        }
        if (adapter != null) adapter.notifyDataSetChanged();
    }
}
