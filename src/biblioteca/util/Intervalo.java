package biblioteca.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author Fabricio Souza.
 */
public class Intervalo {
    private static final DateFormat SDF = new SimpleDateFormat ("dd/MM/yyyy");
    // Calcula o número de dias entre duas datas.
    public static long calcular(String inicio, String fim){
        Date dtInicial = null;
        Date dtFinal = null;
        try {
            dtInicial = SDF.parse(inicio);
            dtFinal = SDF.parse(fim);
        } catch (ParseException ex) {
            System.out.println("Erro ao calcular data: "+ex);
        }
        
        return(dtFinal.getTime() - dtInicial.getTime() + 3600000L) / 86400000L;
    }
    //Captura a data atual.
    public static String actualDate(){
        Date data = new Date();
        String strData = SDF.format(data.getTime());
        return strData;
    }
}