package com.alexandr.owtest;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private static final String urlA = "http://demo3005513.mockable.io/api/v1/entities/getAllIds";
    private static final String urlB = "http://demo3005513.mockable.io/api/v1/object/";

    ArrayList<String> idList;

    int counter = 0;
    String task1, task2, task3;

    private TextView tv;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv = findViewById(R.id.textView);
        iv = findViewById(R.id.imageView);
        WebView wv = findViewById(R.id.webView);

        idList = new ArrayList<>();

        GetIds getIds = new GetIds();
        getIds.execute();

        Button button = findViewById(R.id.buttonNext);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (counter >= 4)
                    counter = 0;

                counter++;

                switch (counter){
                    case 1:
                        tv.setVisibility(View.VISIBLE);

                        tv.setText(task1);
                        break;
                    case 2:
                        wv.setVisibility(View.VISIBLE);
                        tv.setVisibility(View.INVISIBLE);

                        wv.loadUrl(task2);
                        break;
                    case 3:
                        iv.setVisibility(View.VISIBLE);
                        wv.setVisibility(View.INVISIBLE);

                        Picasso.get().load(task3).into(iv);
                        break;
                    case 4:
                        iv.setVisibility(View.INVISIBLE);

                        // ignore
                        break;
                    default:
                        break;
                }
            }
        });

    }

    private class GetIds extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            Handler handler = new Handler();
            String jsonString = handler.httpServiceCall(urlA);

            if (jsonString != null) {
                try {
                    JSONObject jsonObject = new JSONObject(jsonString);
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jsonObject1 = data.getJSONObject(i);
                        String id = jsonObject1.getString("id");

                        idList.add(id);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            GetTask getTask = new GetTask();
            getTask.execute();
            return null;
        }
    }

    private class GetTask extends AsyncTask<Void,Void,Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i < idList.size(); i++) {
                Handler handler = new Handler();
                String jsonString = handler.httpServiceCall(urlB + idList.get(i));

                if (jsonString != null) {
                    try {

                        JSONObject jsonObject = new JSONObject(jsonString);
                        String type = jsonObject.getString("type");

                        switch (type){
                            case "text":
                                task1 = jsonObject.getString("message");
                                break;
                            case "webview":
                                task2 = jsonObject.getString("url");
                                break;
                            case "image":
                                task3 = jsonObject.getString("url");
                                break;
                            default:
                                break;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
    }
}