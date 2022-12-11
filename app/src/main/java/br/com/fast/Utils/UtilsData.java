package br.com.fast.Utils;

import android.text.format.DateFormat;
import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import android.os.Build;

public class UtilsData {

    public static String formatarData(Context contexto, Date data){

        SimpleDateFormat formatadorData;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {

            String padrao = DateFormat.getBestDateTimePattern(Locale.getDefault(), "MM/dd/yyyy");

            formatadorData = new SimpleDateFormat(padrao);

        }else{

            formatadorData = (SimpleDateFormat) DateFormat.getMediumDateFormat(contexto);
        }

        return formatadorData.format(data);
    }
    public static Date formatarString(Context contexto, String data)  {

        try {
            SimpleDateFormat formatadorData;
            Date date = new Date(data);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {

                String padrao = DateFormat.getBestDateTimePattern(Locale.getDefault(), "MM/dd/yyyy");

                formatadorData = new SimpleDateFormat(padrao);

            }else{

                formatadorData = (SimpleDateFormat) DateFormat.getMediumDateFormat(contexto);
            }

            return formatadorData.parse(data);

        }catch (Exception e){

        }
        return null;
    }
}

