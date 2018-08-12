package com.fli.salesagentapp.fliagentapp.utils;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;

/**
 * Created by janithamh on 8/12/18.
 */

public class RequestHandler {


    public static String sendGet(String methodname, String params, Context context) throws IOException{
        String responseString ="";
        String completeurl = Constants.MAIN_URL+methodname;
        Log.e("URL GET",completeurl);
        String encoding = null;

        URL obj = new URL(completeurl);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");
        con.setRequestProperty("Fineract-Platform-TenantId","default");
        encoding = android.util.Base64.encodeToString((Utility.getCurrentUserName(context)+":"+Utility.getCurrentPassword(context)).getBytes(), android.util.Base64.NO_WRAP);
        Log.e("ENCODE", encoding);
        con.setRequestProperty  ("Authorization", "Basic " + encoding);
        int responseCode = con.getResponseCode();
        Log.e("Code",Integer.toString(responseCode));
        /*if (responseCode == HttpURLConnection.HTTP_OK) {*/

        BufferedReader in = new BufferedReader(new InputStreamReader(
                con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        responseString = response.toString();
        Log.e("String",responseString);
        in.close();

       /* }
        else {

        }
         */   return responseString;
    }

    public static String sendPost(JSONObject postobject, String methodname,Context context) throws IOException{
        String responseString ="";
        String completeurl = Constants.MAIN_URL+methodname;
        Log.e("URL POST",completeurl);
        URL obj = new URL(completeurl);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setDoInput (true);
        con.setDoOutput (true);
        con.setUseCaches (false);
        con.setRequestProperty("Content-Type","application/json");
        con.setRequestProperty("Fineract-Platform-TenantId","default");
        if(!methodname.equals(Constants.AUTHENTICATION_URL)){
        String encoding = android.util.Base64.encodeToString((Utility.getCurrentUserName(context)+":"+Utility.getCurrentPassword(context)).getBytes(), android.util.Base64.NO_WRAP);
        Log.e("ENCODE", encoding);
        con.setRequestProperty  ("Authorization", "Basic " + encoding);
        }
        con.connect();

        DataOutputStream printout = new DataOutputStream(con.getOutputStream ());
        printout.writeBytes(postobject.toString());
        printout.flush ();
        printout.close ();

        BufferedReader in = new BufferedReader(new InputStreamReader(
                con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        responseString = response.toString();
        Log.e("String",responseString);
        in.close();



        return responseString;
    }


    public static void inititateSSL() throws Exception {
        // Load CAs from an InputStream
// (could be from a resource or ByteArrayInputStream or ...)
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
// From https://www.washington.edu/itconnect/security/ca/load-der.crt
        InputStream caInput = new BufferedInputStream(new FileInputStream("load-der.crt"));
        Certificate ca;
        try {
            ca = cf.generateCertificate(caInput);
            System.out.println("ca=" + ((X509Certificate) ca).getSubjectDN());
        } finally {
            caInput.close();
        }

// Create a KeyStore containing our trusted CAs
        String keyStoreType = KeyStore.getDefaultType();
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(null, null);
        keyStore.setCertificateEntry("ca", ca);

// Create a TrustManager that trusts the CAs in our KeyStore
        String tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm();
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(tmfAlgorithm);
        tmf.init(keyStore);

// Create an SSLContext that uses our TrustManager
        SSLContext context = SSLContext.getInstance("TLS");
        context.init(null, tmf.getTrustManagers(), null);

// Tell the URLConnection to use a SocketFactory from our SSLContext
        URL url = new URL(Constants.MAIN_URL);
        HttpsURLConnection urlConnection =
                (HttpsURLConnection)url.openConnection();
        urlConnection.setSSLSocketFactory(context.getSocketFactory());
       // InputStream in = urlConnection.getInputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(
                urlConnection.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }

        String responseString = response.toString();
        Log.e("String",responseString);
        in.close();;
    }


}
