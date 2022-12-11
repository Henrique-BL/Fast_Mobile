package br.com.fast.DAO;
import androidx.room.TypeConverter;

import java.util.Date;

public class Convertores {

    @TypeConverter
    public static Date paraData(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long paraTimeStamp(Date date) {
        return date == null ? null : date.getTime();
    }
}
