package br.com.fast.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import br.com.fast.model.Local;

@Dao
public interface LocalDAO {
    @Insert
    long criar(Local local);
    @Update
    void atualizar(Local local);
    @Delete
    void excluir(Local local);
    @Query("SELECT * FROM locais WHERE id = :id")
    Local buscarID(long id);
    @Query("SELECT * FROM locais ORDER BY nome ASC")
    List<Local> buscarTodos();


}
