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
 * Fragment que muestra la lista de libros leídos.
 * Ahora permite eliminar un libro mediante long-press + confirmación.
 */
public class FragmentLeidos extends Fragment {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private final List<String> displayItems = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_leidos, container, false);
        listView = root.findViewById(R.id.listLeidos);

        adapter = new ArrayAdapter<>(requireContext(),
                android.R.layout.simple_list_item_1,
                displayItems);
        listView.setAdapter(adapter);

        // Cargar datos iniciales desde el repositorio
        refreshList();

        // Long click: confirmar eliminación
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            final int pos = position;
            final String itemLabel = displayItems.get(position);

            new AlertDialog.Builder(requireContext())
                    .setTitle("Eliminar")
                    .setMessage("¿Eliminar \"" + itemLabel + "\" de Leídos?")
                    .setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Libro removed = LibroRepository.getInstance().removeLeidoAt(pos);
                            if (removed != null) {
                                refreshList();
                                Toast.makeText(requireContext(), "\"" + removed.getTitulo() + "\" eliminado", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(requireContext(), "No se pudo eliminar el libro", Toast.LENGTH_SHORT).show();
                            }
                        }
                    })
                    .setNegativeButton("Cancelar", null)
                    .show();

            return true;
        });

        return root;
    }

    /**
     * Refresca la lista desde LibroRepository.
     */
    public void refreshList() {
        displayItems.clear();
        List<Libro> leidos = LibroRepository.getInstance().getLeidos();
        for (Libro l : leidos) {
            displayItems.add(l.toListString());
        }
        if (adapter != null) adapter.notifyDataSetChanged();
    }
}