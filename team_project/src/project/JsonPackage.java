package project;

import org.json.JSONArray;
import org.json.JSONObject;

public interface JsonPackage
{

    static String JPPort(String type)
    {

        int port1=2019;
        int port2=2020;
        int port3=2021;
        JSONObject json=new JSONObject();
        json.put("port1",port1);
        json.put("port2",port2);
        json.put("port3",port3);
        return json.toString();
    }
}

