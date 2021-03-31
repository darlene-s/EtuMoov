package Meteo;

import org.json.JSONException;
import org.json.JSONObject;

public class weatherData {

    private String mTemperature,micon,mcity,mWeatherType;
    private int mCondition;

    public static weatherData fromJson(JSONObject jsonObject) {

        try {
            weatherData weatherD=new weatherData();
            weatherD.mcity=jsonObject.getString("name");
            weatherD.mCondition=jsonObject.getJSONArray("weather").getJSONObject(0).getInt("id");
            weatherD.mWeatherType=jsonObject.getJSONArray("weather").getJSONObject(0).getString("main");
            weatherD.micon=updateWeatherIcon(weatherD.mCondition);
            double tempResult=jsonObject.getJSONObject("main").getDouble("temp")-273.15;
            int roundedValue=(int)Math.rint(tempResult);
            weatherD.mTemperature=Integer.toString(roundedValue);
            return weatherD;
        }
        catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static String updateWeatherIcon(int condition) {
        if(condition>=0 && condition<=300) {
            return "img_neigetonerre"; // Neige + éclair (tempete)
        }
        else if(condition>=300 && condition<=500) {
            return "img_nuagepluie"; // Pluie
        }
        else if(condition>=500 && condition<=600) {
            return "img.pluietonerre"; // Pluie + tonerre
        }
        else if(condition>=600 && condition<=700) {
            return "img_neige"; // Neige de nuit
        }
        else if(condition>=701 && condition<=771) {
            return "img_brume"; // Brume
        }
        else if(condition>=772 && condition<=800) {
            return "img_nuage"; // Nuageux
        }
        else if(condition==800) {
            return "img_soleil"; // Soleil
        }
        else if(condition>=801 && condition<=804) {
            return "img_soleilnuage"; // Soleil + Nuage
        }
        else if(condition>=900 && condition<=902) {
            return "img_pluietonerre"; // pluie + tonerre (tempete)
        }
        else if(condition==903) {
            return "img_neige"; // Neige
        }
        else if(condition==904) {
            return "img_soleil"; // Soleil chaud
        }
        else if(condition>=905 && condition<=1000) {
            return "img_pluietonerre"; // giga tempete énervé pluie et tonnerre
        }
        return "dunno";
    }

    public String getmTemperature() {
        return mTemperature+"°C";
    }

    public String getMicon() {
        return micon;
    }

    public String getMcity() {
        return mcity;
    }

    public String getmWeatherType() {
        return mWeatherType;
    }
}