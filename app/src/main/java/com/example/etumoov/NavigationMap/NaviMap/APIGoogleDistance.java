package com.example.etumoov.NavigationMap.NaviMap;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class APIGoogleDistance {
    public String getTravelTime(String depart, String arrivee){

        OkHttpClient okHttpClient = new OkHttpClient();
        final String[] schedule = {""};
        final CountDownLatch countDownLatch = new CountDownLatch(2);
        Callback callback = new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                countDownLatch.countDown();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {

                    String myResponse = response.body().string();
                    try {
                        JSONObject object = new JSONObject(myResponse);
                        JSONObject result = object.getJSONObject("result");
                        JSONArray array = result.getJSONArray("schedules");
                        for(int i=0;i<array.length();i++){
                            schedule[0] += array.getJSONObject(i).getString("duration");
                        }
                        countDownLatch.countDown();
                    } catch (JSONException e) {
                        countDownLatch.countDown();
                        e.printStackTrace();
                    }


                }
                else{
                    countDownLatch.countDown();
                }

            }
        };

        String url ="https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins="+ depart +"&destinations="+ arrivee+"&key=AIzaSyAwCUwkCcE-9QysXg4Tq1lIi0IP-3sq8nU";
        Request request = new Request.Builder().url(url).build();
        countDownLatch.countDown();
        okHttpClient.newCall(request).enqueue(callback);

        try {
            countDownLatch.await();
            return schedule[0];
        } catch (InterruptedException e) {
            e.printStackTrace();
            return schedule[0];
        }
    }
}
