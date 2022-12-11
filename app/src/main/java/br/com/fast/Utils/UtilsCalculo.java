package br.com.fast.Utils;

import java.text.DecimalFormat;
import java.util.List;

import br.com.fast.model.Eletrodomestico;

public class UtilsCalculo {
    public static double calcularGasto(List<Eletrodomestico> eletros){
            double consumo = 0;
            Double gasto = 0.0;
            double tarifa = 0.46589;

            for(Eletrodomestico eletro : eletros){

                consumo += (eletro.getTempo() * eletro.getPotencia());
            }
            gasto = (consumo * tarifa * 30) /1000;

            return gasto;

    }
}
