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
 * Fragment que muestra la lista de libros deseados.
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

        // No implementamos movimiento entre listas en esta iteración.

        return root;
    }

    /**
     * Refresca la lista desde LibroRepository.
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
