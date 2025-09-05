package com.senati.apptiendavehiculos.config;

/**
 *  la clase Config centraliza la URL del servicio backend
 *  puede alternar entre un entorno de desarrollo o producción
 *  cambiando el valor de la constante USE_PROD
 */
public class Config {
    private static final String URL_DEV = "http://192.168.56.1:3001/api/v1";
    //private static final String URL_PROD = "https://walrus-delicate-routinely.ngrok-free.app/api/v1";
    private static final String URL_PROD = "https://crud-node-tiendaveh.onrender.com/api/v1";
    private static final boolean USE_PROD = true;

    public static String getBaseUrl() {
        return USE_PROD ? URL_PROD : URL_DEV;
    }

    public static String getVehiculosEndpoint() {
        return getBaseUrl() + "/vehiculos";
    }

    public static String getVehiculoEndpointById(int id) {
        return getVehiculosEndpoint() + "/" + id;
    }
}
