package com.valorin.task;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.bukkit.scheduler.BukkitRunnable;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class GlobalGameTimes extends BukkitRunnable {
	private int times = -1;
	
	public int getValue() {
		return times;
	}
	
	public void run() {
		try {
			String url = "https://service.zhanshi123.me/plugins/dantiao.php";   
	        String json = loadJson(url); 
	        JsonObject jsonObject = new JsonParser().parse(json).getAsJsonObject();
	        JsonElement gameTimesStr = jsonObject.get("message");
	        times = Integer.parseInt(gameTimesStr.getAsString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String loadJson (String url) {  
        StringBuilder json = new StringBuilder();  
        try {  
            URL urlObject = new URL(url);  
            URLConnection uc = urlObject.openConnection();  
            uc.setConnectTimeout(30000);
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));  
            String inputLine = null;  
            while ( (inputLine = in.readLine()) != null) {  
                json.append(inputLine);  
            }  
            in.close();  
        } catch (MalformedURLException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return json.toString();  
    }
}
