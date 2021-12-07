package com.assignment.utils;

import java.security.SecureRandom;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.security.auth.login.Configuration;
import java.security.cert.X509Certificate;
import java.util.Map;

import org.primefaces.PrimeFaces;
import org.primefaces.shaded.json.JSONException;
import org.primefaces.shaded.json.JSONObject;
import javax.ws.rs.core.MediaType;

import com.assignment.entity.User;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.core.util.MultivaluedMapImpl;
import javax.ws.rs.core.MultivaluedMap;

	
public class Global {

	
	public static void showMessage(String message){
		showMessage(message, "Info", FacesMessage.SEVERITY_INFO);

	}
	public static void showMessage(String message,Severity severity){
		String title = "Info";
		if(severity == FacesMessage.SEVERITY_WARN){
			title = "Warning";
		}else if(severity == FacesMessage.SEVERITY_ERROR){
			title = "Error";
		}else if(severity == FacesMessage.SEVERITY_FATAL){
			title = "Error";
		}

		showMessage(message, title, severity);
	}
	
	public static User getUser(){
		Map<String, Object> sessionMap = FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
		return (User)sessionMap.get("infos");
	}
	
	public static void showMessage(String message,String title,Severity severity){
		FacesContext.getCurrentInstance().addMessage(null,
				new FacesMessage(severity, title, message));
		PrimeFaces.current().ajax().update("growl");

	}
	
    public static void redirectUserTo(String page) {

//      RequestContext.getCurrentInstance().execute("window.onbeforeunload = null;window.location.href = '" + page + "'");
      PrimeFaces.current().executeScript("window.onbeforeunload = null;window.location.href = '" + FacesContext.getCurrentInstance().getExternalContext().getRequestContextPath()  + page + "'");

  }

    public static JSONObject getQueryResponse(String serviceName, JSONObject pairs) throws JSONException {
        Client restClient = new Client();
        WebResource webResource = restClient.resource(Constant.END_POINT + serviceName);
        
        if(pairs!=null){
        	 for(int i = 0; i<pairs.names().length(); i++){
        		 webResource = webResource.queryParam(pairs.names().getString(i) ,  (String) pairs.get(pairs.names().getString(i)));
             }
        }
       
//        System.out.println(webResource.getURI());
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            ;
        }

        ClientResponse resp = webResource.accept(MediaType.APPLICATION_JSON_TYPE).type(MediaType.APPLICATION_JSON_TYPE).get(ClientResponse.class);
        JSONObject respo = new JSONObject(resp.getEntity(String.class));
        

        return respo;

    }
    
    public static JSONObject postAndRec(String endpoint, String serviceName, JSONObject obj) throws JSONException {
        //post and wait for response result
        WebResource webResource = initResource(serviceName, endpoint);
//        ClientResponse response = webResource.
//                type(MediaType.APPLICATION_FORM_URLENCODED_TYPE).
//                post(ClientResponse.class, formData);

        
        ClientResponse response = webResource.
                type(MediaType.APPLICATION_JSON).
                entity(obj.toString())
                .post(ClientResponse.class)                
                ;
        String responseText = response.getEntity(String.class);
        JSONObject resp;
        try {
            resp = new JSONObject(responseText);
        } catch (Exception ex) {
            resp = new JSONObject();
//            resp.put("resultId", Configuration.FAILED_RESULT);
        }

//        if (Integer.toString(resp.getInt("resultId")).equals(Configuration.SESSION_INACTIVE) || Integer.toString(resp.getInt("resultId")).equals(Configuration.SESSION_EXPIRED)) {
//            PrimeFaces.current().executeScript("PF('dlgDialogExpired').show();");
//
//        }
        return new JSONObject(responseText);

    }
    
    private static WebResource initResource(String service, String endpoint) {
        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            public void checkClientTrusted(X509Certificate[] certs, String authType) {
            }

            public void checkServerTrusted(X509Certificate[] certs, String authType) {
            }
        }};
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAllCerts, new SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
        } catch (Exception e) {
            ;
        }
        Client restClient = new Client();
        return restClient.
                resource(endpoint
                        + service);
    }
}
