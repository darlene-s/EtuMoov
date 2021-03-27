package com.example.etumoov.NavigationMap.NaviMap;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
/**
 * @author EtuMoov Team
 * Classe APIDistanceMatrix qui permet de faire des requêtes à l'API Google Distance Matrix
 * pour obtenir le temps nécessaire pour atteindre la destination en arrière-plan.
 *
 */
public class APIDistanceMatrix extends AsyncTask<String, Void, String>  {
    ProgressDialog pd;
    Context mContext;
    Double duration;
    Geo geo1;

    public APIDistanceMatrix(TravelTime travelTime) {
    }

    /**
     * Constructeur GeoTask pour obtenir le contexte
     * @param mContext
     */
    public void GeoTask(Context mContext) {
        this.mContext = mContext;
        geo1= (Geo) mContext;
    }
    /**
     * Fonction onPreExecute() : Exécutée avant la fonction "doInBackground"
     * (String...params) elle permet d'afficher le message de chargement
     */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pd=new ProgressDialog(mContext);
        pd.setMessage("Chargement...");
        pd.setCancelable(false);
        pd.show();
    }

    /**
     * Fonction onPostExecute : Exécutée après l'exécution de "doInBackground(String...params)"
     * afin d'écarter la boîte de dialogue de progression affichée et d'appeler "setDouble(Double)"
     * @param aDouble
     */
    @Override
    protected void onPostExecute(String aDouble) {
        super.onPostExecute(aDouble);
        if(aDouble!=null)
        {
            geo1.setDouble(aDouble);
            pd.dismiss();
        }
        else
            Toast.makeText(mContext, "Une erreur est survenue! Veuillez reessayer avec des valeurs correctes", Toast.LENGTH_SHORT).show();
    }

    /**
     * Fonction doInBackground : Permet de préparer en arrière plan la connexion HTTP pour
     * la reqûete à l'API Google Distance Matrix
     * @param params
     * @return
     */
    @Override
    protected String doInBackground(String... params) {
        try {
            URL url=new URL(params[0]);
            HttpURLConnection con= (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            int statuscode=con.getResponseCode();
            if(statuscode==HttpURLConnection.HTTP_OK)
            {
                BufferedReader br=new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder sb=new StringBuilder();
                String line=br.readLine();
                while(line!=null)
                {
                    sb.append(line);
                    line=br.readLine();
                }
                String json=sb.toString();
                Log.d("JSON",json);
                JSONObject root=new JSONObject(json);
                JSONArray array_rows=root.getJSONArray("rows");
                Log.d("JSON","array_rows:"+array_rows);
                JSONObject object_rows=array_rows.getJSONObject(0);
                Log.d("JSON","object_rows:"+object_rows);
                JSONArray array_elements=object_rows.getJSONArray("elements");
                Log.d("JSON","array_elements:"+array_elements);
                JSONObject  object_elements=array_elements.getJSONObject(0);
                Log.d("JSON","object_elements:"+object_elements);
                JSONObject object_duration=object_elements.getJSONObject("duration");
                JSONObject object_distance=object_elements.getJSONObject("distance");

                Log.d("JSON","object_duration:"+object_duration);
                return object_duration.getString("value")+","+object_distance.getString("value");

            }
        } catch (MalformedURLException e) {
            Log.d("error", "error1");
        } catch (IOException e) {
            Log.d("error", "error2");
        } catch (JSONException e) {
            Log.d("error","error3");
        }


        return null;
    }
    interface Geo{
        void setDouble(String min);
    }
}
