package com.example.leido;

import java.io.Serializable;
import java.util.UUID;

/**
 * Modelo de datos para un libro.
 */
public class Libro implements Serializable {
    private final String id;
    private final String titulo;
    private final String autor;
    private final String editorial;
    private final String isbn;
    private final String comentario;

    public Libro(String titulo, String autor, String editorial, String isbn, String comentario) {
        this.id = UUID.randomUUID().toString();
        this.titulo = titulo != null ? titulo : "";
        this.autor = autor != null ? autor : "";
        this.editorial = editorial != null ? editorial : "";
        this.isbn = isbn != null ? isbn : "";
        this.comentario = comentario != null ? comentario : "";
    }

    // Getters
    public String getId() { return id; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public String getEditorial() { return editorial; }
    public String getIsbn() { return isbn; }
    public String getComentario() { return comentario; }

    /**
     * Representación breve para mostrar en la lista: "Título — Autor"
     */
    public String toListString() {
        if (autor.isEmpty()) return titulo;
        return titulo + " — " + autor;
    }
}
