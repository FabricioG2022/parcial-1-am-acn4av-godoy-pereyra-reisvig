package com.example.leido;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment que muestra la lista de libros leídos.
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

        // Opcional: long click para eliminar (simple ejemplo)
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            // No implementamos borrado obligatorio por ahora; dejamos como ejercicio futuro.
            return true;
        });

        return root;
    }

    /**
     * Refresca la lista desde LibroRepository.
     * Llamar cada vez que cambian los datos (por ejemplo, tras añadir).
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
