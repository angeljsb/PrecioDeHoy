/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.preciodehoy.preciodehoy.backend;

import java.sql.SQLException;
import javax.servlet.ServletContextListener;
import javax.servlet.ServletContextEvent;
import java.util.TimerTask;
import java.util.Timer;
import javax.servlet.annotation.WebListener;
 
/**
 * Escucha el inicio del servlet context y ejecuta la obtención de los precios
 * según los proveedores cada 15 minutos hasta que se destruye el context
 *
 * @author Angel
 * @since v1.0.0
 */
@WebListener
public class TareaProgramatica extends TimerTask implements ServletContextListener {
    private Timer timer;
 
    @Override
    public void contextInitialized(ServletContextEvent evt) {
        TablaPrecio tp = new TablaPrecio();
        if(tp.read().length == 0){
            BuscadorMoneda[] buscadores = new BuscadorMoneda[]{
                new ScrapperDolarOficial(), 
                new BuscadorDolarToday(),
                new BuscadorLocalBitcoins()
            };
            for(BuscadorMoneda buscador:buscadores){
                try{
                    tp.create(buscador.getProveedor());
                }catch(SQLException ex){
                    System.err.println(ex);
                }
            }
        }
        
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
                tp.actualizarPrecio(buscadores[i], i+1);
            }catch(SQLException|NoEncontradoException ex){
                ex.printStackTrace(System.err);
            }
        }
    }
}