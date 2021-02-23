/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

/**
 *
 * @author Angel
 */
import java.sql.SQLException;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import java.util.TimerTask;
import java.util.Timer;
import javax.servlet.annotation.WebListener;
 
@WebListener
public class TareaProgramatica extends TimerTask implements ServletContextListener {
    private Timer timer;
 
    @Override
    public void contextInitialized(ServletContextEvent evt) {
        timer = new Timer();
        timer.schedule(this, 0, 15*60*1000);
    }
 
    @Override
    public void contextDestroyed(ServletContextEvent evt) {
        timer.cancel();
    }
    
    @Override
    public void run() {
        BuscadorMoneda[] buscadores = new BuscadorMoneda[]{
            new ScrapperDolarOficial(), 
            new BuscadorDolarToday(),
            new BuscadorLocalBitcoins()
        };
        
        TablaPrecio tp = new TablaPrecio();
        
        for(int i=0; i<buscadores.length; i++){
            try{
                if(tp.actualizarPrecio(buscadores[i], i+1)){
                    System.out.println(buscadores[i].getClass().getSimpleName() 
                            + " actualizó el precio");
                }else{
                    System.out.println(buscadores[i].getClass().getSimpleName() 
                            + " no actualizó el precio");
                }
            }catch(SQLException|NoEncontradoException ex){
                System.err.println(ex);
            }
        }
    }
}