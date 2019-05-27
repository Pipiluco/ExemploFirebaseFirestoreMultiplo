package org.sistemafiesc.lucasftecnico.exemplofirebasefirestoremultiplo;

import com.google.firebase.firestore.Exclude;

import java.util.List;
import java.util.Map;

public class Nota {
    private String documentoId;
    private String titulo;
    private String descricao;
    private int prioridade;
    Map<String, Boolean> tags;

    public Nota() {
    }

    public Nota(String titulo, String descricao, int prioridade, Map<String, Boolean> tags) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.tags = tags;
    }

    @Exclude
    public String getDocumentoId() {
        return documentoId;
    }

    public void setDocumentoId(String documentoId) {
        this.documentoId = documentoId;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getPrioridade() {
        return prioridade;
    }

    public void setPrioridade(int prioridade) {
        this.prioridade = prioridade;
    }

    public Map<String, Boolean> getTags() {
        return tags;
    }

    public void setTags(Map<String, Boolean> tags) {
        this.tags = tags;
    }
}
