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
 * Long press sobre un item -> opciones: Marcar como leído / Eliminar / Cancelar.
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

        // Long click: mostrar diálogo con opciones
        listView.setOnItemLongClickListener((parent, view, position, id) -> {
            final int pos = position;
            final String itemLabel = displayItems.get(position);

            new AlertDialog.Builder(requireContext())
                    .setTitle(itemLabel)
                    .setItems(new CharSequence[]{"Marcar como leído", "Eliminar", "Cancelar"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            LibroRepository repo = LibroRepository.getInstance();
                            switch (which) {
                                case 0: // Marcar como leído
                                    Libro moved = repo.removeDeseadoAt(pos);
                                    if (moved != null) {
                                        repo.addLeidoAtStart(moved);
                                        refreshList();
                                        notifyLeidosIfPresent();
                                        Toast.makeText(requireContext(), "\"" + moved.getTitulo() + "\" marcado como leído", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(requireContext(), "No se pudo mover el libro", Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                case 1: // Eliminar
                                    Libro removed = repo.removeDeseadoAt(pos);
                                    if (removed != null) {
                                        refreshList();
                                        Toast.makeText(requireContext(), "\"" + removed.getTitulo() + "\" eliminado de Deseados", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(requireContext(), "No se pudo eliminar el libro", Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                case 2: // Cancelar
                                default:
                                    // no hacer nada
                                    break;
                            }
                        }
                    })
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
        List<Libro> deseados = LibroRepository.getInstance().getDeseados();
        for (Libro l : deseados) {
            displayItems.add(l.toListString());
        }
        if (adapter != null) adapter.notifyDataSetChanged();
    }

    /**
     * Busca instancia de FragmentLeidos en el FragmentManager y la refresca si existe.
     */
    private void notifyLeidosIfPresent() {
        if (getActivity() == null) return;
        List<Fragment> frags = getActivity().getSupportFragmentManager().getFragments();
        for (Fragment f : frags) {
            if (f instanceof FragmentLeidos) {
                ((FragmentLeidos) f).refreshList();
            }
        }
    }
}