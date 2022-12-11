package br.com.fast.model;

import static androidx.room.ForeignKey.CASCADE;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "eletros", foreignKeys = @ForeignKey(onDelete = CASCADE,entity = Local.class,
                                                        parentColumns = "id",
                                                        childColumns = "localID"))
public class Eletrodomestico {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    private String nome;
    @NonNull
    private int potencia;
    private int volts;
    private boolean estado;
    @NonNull
    private int tempo;
    private int localID;

    public Eletrodomestico(){

    }
    public Eletrodomestico(String nome, int potencia, int volts, boolean estado, int tempo, int localID) {
        this.nome = nome;
        this.potencia = potencia;
        this.volts = volts;
        this.estado = estado;
        this.tempo = tempo;
        this.localID = localID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getPotencia() {
        return potencia;
    }

    public void setPotencia(int potencia) {
        this.potencia = potencia;
    }

    public int getVolts() {
        return volts;
    }

    public void setVolts(int volts) {
        this.volts = volts;
    }

    public boolean getEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public int getTempo() {
        return tempo;
    }

    public void setTempo(int tempo) {
        this.tempo = tempo;
    }

    public int getLocalID() {
        return localID;
    }

    public void setLocalID(int localID) {
        this.localID = localID;
    }

    @Override
    public String toString() {
        return getId() + " - " + getNome() + " - " +  getPotencia() + " - " + getVolts() + " - " +
                getEstado();
    }


}
