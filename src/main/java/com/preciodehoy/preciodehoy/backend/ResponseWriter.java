/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.preciodehoy.preciodehoy.backend;

import com.preciodehoy.preciodehoy.beans.RestResponse;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.MediaType;
import org.json.JSONObject;

/**
 * Clase para escribir respuestas de tipo application/json de frma sencilla
 *
 * @author Angel
 * @since v1.0.1
 */
public class ResponseWriter {
    
    /**
     * Obtiene un pequeño mensaje según el código de status de una respuesta
     * 
     * @param sc El código de status
     * @return Un pequeño mensaje correspondiente al código
     * @since v1.0.1
     */
    public static String getResponseMessage(int sc){
        switch(sc){
            case HttpServletResponse.SC_CONTINUE:
                return "continue";
            case HttpServletResponse.SC_SWITCHING_PROTOCOLS:
                return "server_switching_protocols";
            case HttpServletResponse.SC_OK:
                return "successful_request";
            case HttpServletResponse.SC_CREATED:
                return "successful_created";
            case HttpServletResponse.SC_ACCEPTED:
                return "accepted_request";
            case HttpServletResponse.SC_NON_AUTHORITATIVE_INFORMATION:
                return "invalid_origin";
            case HttpServletResponse.SC_NO_CONTENT:
                return "empty_response";
            case HttpServletResponse.SC_RESET_CONTENT:
                return "should_reset";
            case HttpServletResponse.SC_PARTIAL_CONTENT:
                return "partial_response";
            case HttpServletResponse.SC_MULTIPLE_CHOICES:
                return "multiples_choices";
            case HttpServletResponse.SC_MOVED_PERMANENTLY:
                return "moved_permanently";
            case HttpServletResponse.SC_FOUND:
                return "moved_temporarily";
            case HttpServletResponse.SC_SEE_OTHER:
                return "see_other";
            case HttpServletResponse.SC_NOT_MODIFIED:
                return "not_modified";
            case HttpServletResponse.SC_USE_PROXY:
                return "proxy_required";
            case HttpServletResponse.SC_TEMPORARY_REDIRECT:
                return "temporary_redirect";
            case HttpServletResponse.SC_BAD_REQUEST:
                return "bad_request";
            case HttpServletResponse.SC_UNAUTHORIZED:
                return "authentication_required";
            case HttpServletResponse.SC_PAYMENT_REQUIRED:
                return "payment_required";
            case HttpServletResponse.SC_FORBIDDEN:
                return "forbidden_request";
            case HttpServletResponse.SC_NOT_FOUND:
                return "not_found";
            case HttpServletResponse.SC_METHOD_NOT_ALLOWED:
                return "method_not_allowed";
            case HttpServletResponse.SC_NOT_ACCEPTABLE:
                return "not_acceptable_response";
            case HttpServletResponse.SC_PROXY_AUTHENTICATION_REQUIRED:
                return "proxy_authentication_required";
            case HttpServletResponse.SC_REQUEST_TIMEOUT:
                return "request_timeout";
            case HttpServletResponse.SC_CONFLICT:
                return "conflict";
            case HttpServletResponse.SC_GONE:
                return "not_longer_available";
            case HttpServletResponse.SC_LENGTH_REQUIRED:
                return "content_length_required";
            case HttpServletResponse.SC_PRECONDITION_FAILED:
                return "precondition_failed";
            case HttpServletResponse.SC_REQUEST_ENTITY_TOO_LARGE:
                return "request_too_large";
            case HttpServletResponse.SC_REQUEST_URI_TOO_LONG:
                return "request_uri_too_long";
            case HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE:
                return "unsupported_media_type";
            case HttpServletResponse.SC_REQUESTED_RANGE_NOT_SATISFIABLE:
                return "requested_range_not_satisfiable";
            case HttpServletResponse.SC_EXPECTATION_FAILED:
                return "expectation_failed";
            case HttpServletResponse.SC_INTERNAL_SERVER_ERROR:
                return "server_error";
            case HttpServletResponse.SC_NOT_IMPLEMENTED:
                return "not_implemented";
            case HttpServletResponse.SC_BAD_GATEWAY:
                return "bad_gateway";
            case HttpServletResponse.SC_SERVICE_UNAVAILABLE:
                return "server_overloaded";
            case HttpServletResponse.SC_GATEWAY_TIMEOUT:
                return "gateway_timeout";
            case HttpServletResponse.SC_HTTP_VERSION_NOT_SUPPORTED:
                return "http_version_not_supported";
            default:
                return "";
        }
    }
    
    /**
     * Crea un objeto de respuesta estandarizado para ser enviado como
     * json en la respuesta del servidor
     * 
     * @param sc El código de status
     * @param message Un mensaje a enviar como cuerpo de la respuesta
     * @return El objeto RestResponse
     */
    public static RestResponse createRestResponse(int sc, String message){
        RestResponse res = new RestResponse();
        res.setStatus(sc);
        res.setMessage(getResponseMessage(sc));
        res.setBody(message);
        return res;
    }
    
    private HttpServletResponse response;
    
    public ResponseWriter(HttpServletResponse response){
        this.response = response;
    }
    
    public HttpServletResponse getResponse(){
        return this.response;
    }
    
    public void sendError(int sc, String message){
        sendRestResponse( createRestResponse(sc, message) );
    }
    
    public void sendRestResponse(RestResponse body){
        this.getResponse().setStatus(body.getStatus());
        
        this.send(body);
    }
    
    public void send(Object body){
        JSONObject jsonBody = new JSONObject(body);
        
        this.getResponse().setContentType(MediaType.APPLICATION_JSON);
        this.getResponse().setCharacterEncoding("UTF-8");
        try(PrintWriter out = this.getResponse().getWriter()){
            out.print(jsonBody);
        }catch (IOException e){
            System.err.print(e);
        }
    }
    
}
