/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package backend;

import java.sql.SQLException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Angel
 */
public class ControlUsuario {
    
    public static Usuario revisar(Cookie[] cookies) {

        if (cookies == null) {
            return new Usuario();
        }

        Cookie id = null,
                auth = null;

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals("user_id")) {
                id = cookie;
            }
            if (cookie.getName().equals("auth_code")) {
                auth = cookie;
            }
        }

        if (id == null || auth == null) {
            return new Usuario();
        }

        TablaUsuario tu = new TablaUsuario();
        try {
            Usuario usuario = tu.autenticar(Integer.parseInt(id.getValue()), Integer.parseInt(auth.getValue()));
            return usuario;
        } catch (SQLException | NoEncontradoException ex) {
            id.setMaxAge(0);
            auth.setMaxAge(0);
        }

        return new Usuario();
    }
    
    public static Usuario getUsuarioActual(HttpServletRequest request){
        HttpSession session = request.getSession();
        
        Usuario user = (Usuario) session.getAttribute("user");
        
        if(user == null || user.getId() == 0){
            user = revisar(request.getCookies());
        }
        
        return user;
    }
    
}
