package br.com.fast.DAO;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.fast.model.Eletrodomestico;

@Dao
public interface EletrodomesticoDAO {
    @Insert
    long criar(Eletrodomestico eletrodomestico);
    @Update
    void atualizar(Eletrodomestico eletrodomestico);
    @Delete
    void excluir(Eletrodomestico eletrodomestico);

    @Query("SELECT * FROM eletros WHERE id = :id")
    Eletrodomestico buscarID(long id);
    @Query("SELECT * FROM eletros ORDER BY nome  ASC")
    List<Eletrodomestico> buscarTodos();
    @Query("SELECT * FROM eletros WHERE localID =:localID")
    List<Eletrodomestico> buscarPorLocal(long localID);
    @Query("SELECT * FROM eletros WHERE estado = 1 and localID=:localID")
    List<Eletrodomestico> buscarAtivados(long localID);
}
