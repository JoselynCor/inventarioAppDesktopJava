/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ecuatask.actualizadormaven2.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Anibal
 */
public class LeerConfig {
    public static String getAppVersion() throws IOException {

       

        //to load application's properties, we use this class
        Properties mainProperties = new Properties();

        FileInputStream file;

        //the base folder is ./, the root of the main.properties file  
        String path = "./util/main.properties";

        //load the file handle for main.properties
        file = new FileInputStream(path);

        //load all the properties from this file
        mainProperties.load(file);

        //we have loaded the properties, so close the file handle
        file.close();

        //retrieve the property we are intrested, the app.version
        String versionString = mainProperties.getProperty("app.version");

        return versionString;
    }
    
    private static String getData(String archivo, String propiedad) throws IOException {        
        
        return LeerConfig.getData("./util/","main.properties",propiedad);

    }
    public static boolean getExistConfigUser(){
        File file = new File(EscribirArchivos.getLOCALAPPDATA());    
        return file.exists();
    }
    
    private static String getData(String raiz,String archivo, String propiedad) throws IOException {



        //to load application's properties, we use this class
        Properties mainProperties = new Properties();

        FileInputStream file;

        //the base folder is ./, the root of the main.properties file  
        String path = raiz+archivo;

        //load the file handle for main.properties
        file = new FileInputStream(path);

        //load all the properties from this file
        mainProperties.load(file);

        //we have loaded the properties, so close the file handle
        file.close();

        //retrieve the property we are intrested, the app.version
        String versionString = mainProperties.getProperty(propiedad);

        return versionString;
    }

    public static String getMaquina() throws IOException {
        return LeerConfig.getData("main.properties","app.maquina");
    }
    public static String getUsuario() throws IOException {
        return LeerConfig.getData("main.properties","app.user");
    } 
    public static String getPassword() throws IOException {
        return LeerConfig.getData("main.properties","app.password");
    } 
    public static String getSite() throws IOException {
        return LeerConfig.getData("main.properties","app.site");
    } 
    
    public static String getClienteId() throws IOException {
        return LeerConfig.getData("main.properties","app.client_id");
    }
    
    public static String getClienteSecret() throws IOException {
        return LeerConfig.getData("main.properties","app.client_secret");
    }
    public static String getOpcionesCheckListId() throws IOException {
        return LeerConfig.getData("main.properties","opciones_check_lists.opciones_check_list_id.Aplicaciones");
    } 
    
    public static boolean isInteger(String s) {
    try { 
        Integer.parseInt(s); 
    } catch(NumberFormatException | NullPointerException e) { 
        return false; 
    }
    // only got here if we didn't return false
    return true;
}
    
    public static com.ecuatask.actualizadormaven2.model.TokenResponse getToken() {
        com.ecuatask.actualizadormaven2.model.TokenResponse tk = new com.ecuatask.actualizadormaven2.model.TokenResponse();
        String dir = EscribirArchivos.getLOCALAPPDATA();
        try {
            
            tk.setAccessToken(LeerConfig.getData(dir,"", "access_token"));
            String numero=LeerConfig.getData(dir,"", "expires_in");
            if(LeerConfig.isInteger(numero)){
                tk.setExpiresIn(Integer.parseInt(numero));
            }else {
                tk.setExpiresIn(0);
            }
            tk.setRefreshToken(LeerConfig.getData(dir,"", "refresh_token"));
            tk.setToken_type(LeerConfig.getData(dir,"", "token_type"));
            

        } catch (IOException ex) {
            Logger.getLogger(LeerConfig.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tk;
    }
}
