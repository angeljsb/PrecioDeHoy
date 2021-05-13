/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Clase encargada de buscar el precio actual del dolar según el banco central de
 * Venezuela
 *
 * @author Angel
 */
public class ScrapperDolarOficial extends BuscadorMoneda {
    
    /**
     * Variable que guarda el link a la pagina oficial del banco central
     */
    public static final String BCV = "http://www.bcv.org.ve/";
    
    @Override
    protected double obtenerDesdeWeb(){
        try {
            
            Document doc = Jsoup.connect(BCV).get();
            Element elemento = doc.getElementById("dolar");
            Elements result = elemento.getElementsByTag("strong");
            return Double.parseDouble(result.get(0)
                    .html()
                    .trim()
                    .replace(".", "")
                    .replace(",", "."));
            
        } catch (IOException|NumberFormatException ex) {
            System.err.println(ex.getLocalizedMessage());
        }
        
        return 0;
    }
    
}
