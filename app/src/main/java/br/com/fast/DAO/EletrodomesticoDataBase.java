package br.com.fast.DAO;

import android.content.Context;
import android.sax.Element;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import br.com.fast.R;
import br.com.fast.model.Eletrodomestico;
import br.com.fast.model.Local;

@Database(entities = {Eletrodomestico.class, Local.class}, version = 1, exportSchema = false)
@TypeConverters(Convertores.class)
public abstract class EletrodomesticoDataBase extends RoomDatabase {
    public abstract EletrodomesticoDAO eletroDAO();
    public abstract LocalDAO localDAO();
    private static EletrodomesticoDataBase instancia;

    //Garantindo singularidade do banco de dadods
    public static EletrodomesticoDataBase getDataBase(final Context context){
        if(instancia ==null){
            synchronized (EletrodomesticoDataBase.class){
                if(instancia==null){

                    RoomDatabase.Builder builder = Room.databaseBuilder(context,
                                                EletrodomesticoDataBase.class,
                                                "eletrodomestico.db");
                    builder.addCallback(new Callback() {
                        @Override
                        public void onCreate(@NonNull SupportSQLiteDatabase db) {
                            super.onCreate(db);
                            Executors.newSingleThreadExecutor().execute(new Runnable() {
                                @Override
                                public void run() {
                                    carregaLocaisIniciais(context);
                                    /*db.execSQL("CREATE TRIGGER valor_total AFTER INSERT ON eletros" +
                                            "    BEGIN" +
                                            "    UPDATE locais SET valor = (SELECT SUM (potencia * tempo) FROM eletros,locais WHERE locais.id = eletros.localID GROUP BY locais.id ORDER BY localID DESC)  WHERE locais.id  IN (SELECT locais.id FROM eletros,locais WHERE locais.id = eletros.localID );" +
                                            "    END;");*/

                                }
                            });
                        }
                    });

                    instancia = (EletrodomesticoDataBase) builder.build();
                }

            }
        }
        return instancia;
    }
    private static void carregaLocaisIniciais(final Context context){

        String[] locais = context.getResources().getStringArray(R.array.locaisIniciais);

        for (String nome : locais) {

            Local local = new Local(nome, new Date(), 0);

            instancia.localDAO().criar(local);
        }
    }

}
