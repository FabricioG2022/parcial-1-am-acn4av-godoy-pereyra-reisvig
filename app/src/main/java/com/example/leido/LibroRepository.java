package com.example.leido;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Repositorio en memoria que guarda listas de libros leidos y deseados.
 * Simple singleton para esta iteración (no persistente).
 */
public class LibroRepository {

    private static LibroRepository instance;
    private final List<Libro> leidos = new ArrayList<>();
    private final List<Libro> deseados = new ArrayList<>();

    private LibroRepository() {
        // Pre-popular con los ejemplos solicitados
        leidos.add(new Libro("Cien años de soledad", "Gabriel García Márquez", "Sudamericana", "", ""));
        leidos.add(new Libro("El nombre de la rosa", "Umberto Eco", "Sudamericana", "", ""));
        // Nota: añadimos en el orden deseado; como mostramos insertando en 0,
        // para que "Cien años..." quede arriba lo agregamos primero aquí y luego "El nombre..."
        // pero en el fragment mostraremos colección tal cual esté en la lista.
        deseados.add(new Libro("Rayuela", "Julio Cortázar", "Sudamericana", "", ""));
    }

    public static synchronized LibroRepository getInstance() {
        if (instance == null) {
            instance = new LibroRepository();
        }
        return instance;
    }

    // Leídos
    public synchronized List<Libro> getLeidos() {
        return Collections.unmodifiableList(leidos);
    }

    public synchronized void addLeidoAtStart(Libro libro) {
        leidos.add(0, libro);
    }

    // Deseados
    public synchronized List<Libro> getDeseados() {
        return Collections.unmodifiableList(deseados);
    }

    public synchronized void addDeseadoAtStart(Libro libro) {
        deseados.add(0, libro);
    }

    // Métodos de utilidad (opcional): borrar, buscar.. (no implementados ahora)
}
