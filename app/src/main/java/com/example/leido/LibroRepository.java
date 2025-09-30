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

    /**
     * Remueve y retorna el libro en la posición dada de la lista de leídos.
     * Retorna null si la posición es inválida.
     */
    public synchronized Libro removeLeidoAt(int index) {
        if (index < 0 || index >= leidos.size()) return null;
        return leidos.remove(index);
    }

    // Deseados
    public synchronized List<Libro> getDeseados() {
        return Collections.unmodifiableList(deseados);
    }

    public synchronized void addDeseadoAtStart(Libro libro) {
        deseados.add(0, libro);
    }

    /**
     * Remueve y devuelve el libro en la posición dada dentro de la lista de deseados.
     * Retorna null si la posición es inválida.
     */
    public synchronized Libro removeDeseadoAt(int index) {
        if (index < 0 || index >= deseados.size()) return null;
        return deseados.remove(index);
    }
}