package com.example.myfirstapp;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class OpenHabControl {
    RequestQueue reqQue;

    //todo: legg disse variablene i SharedPreferences
    public String OHabIP;
    public String OHabPort;


    //send getCacheDir() fra main
public OpenHabControl(File cacheDir){

    //todo: stopp requestqueue hvis den finnes, og bruk constructor som reque restart.  ???
    //volley request setup
    Cache cache = new DiskBasedCache(cacheDir, 1024*1024); //1MB cache
    Network network = new BasicNetwork(new HurlStack());
    reqQue = new RequestQueue(cache, network);
    reqQue.start();


    //todo: legg disse variablene i SharedPreferences
    OHabIP = "10.0.1.120";
    OHabPort = "8080";
}

//Openhab tar i mot tall i strings eller kommandoer. Eks: ON/OFF, 0-100(Dimming)m.m. (sjekk REST API)
public void itemControl(String itemName, String body){
    String url = "http://" + OHabIP + ":" + OHabPort + "/rest/items/" + itemName;
    //String url = "http://10.0.1.120:8080/rest/items/Stue"; //test url todo: fjern
    stdRequest(url, body);
}


    //StringRequest med headers som gjør at openhab er glad.
    void stdRequest(String url, String body){
        StringRequest jsonRequest = new StringRequest(1,url, new Response.Listener<String>()
        {
            @Override
            public void onResponse(String response) {
            }
        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headerArgs = new HashMap<String, String>();
                headerArgs.put("Content-Type", "text/plain");
                headerArgs.put("Accept", "application/json");
                headerArgs.put("Connection", "keep-alive");
                return headerArgs;
            }
            @Override
            public byte[] getBody() {
                return body.getBytes();
            }
        };
        reqQue.add(jsonRequest);
    }

    public void dim(String name, String direction, int amount){


    }




}
