package com.example.sanket.http_demo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                if(isConnectedOnline())
                {
                    Toast.makeText(MainActivity.this, "Connected", Toast.LENGTH_SHORT).show();
                    //new GetData().execute("http://rss.cnn.com/rss/cnn_tech.rss");
                    //new GetImage().execute("https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png");

                    RequestParams params = new RequestParams("POST", "http://dev.theappsdr.com/lectures/params.php");
                    params.addParam("key1", "value1 value1");
                    params.addParam("key2", "value2");
                    params.addParam("key3", "value3");
                    params.addParam("key4", "value4");

                    new GetDataWithParams().execute(params);
                }else
                {
                    Toast.makeText(MainActivity.this, "No network Connection", Toast.LENGTH_SHORT).show();
                }
            }


        });
    }

    private class GetData extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {

            BufferedReader br=null;
            try {
                URL url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("GET");

                //Bitmap bp = BitmapFactory.decodeStream(con.getInputStream());

                 br= new BufferedReader(new InputStreamReader(con.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line ="";

                while((line=br.readLine())!=null)
                {
                    sb.append(line+ "\n");
                }
                return  sb.toString();

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if(br!=null)
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s !=null)
            {
                Log.d("demo", s);
            }else
            {
                Log.d("demo", "Null Data");
            }
        }
    }


    private class GetImage extends AsyncTask<String, Void, Bitmap> {

        InputStream is=null;
        @Override
        protected Bitmap doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();

                con.setRequestMethod("GET");
                is =con.getInputStream();
                Bitmap bp = BitmapFactory.decodeStream(con.getInputStream());

                return bp;

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(is!=null)
                {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bitmap s) {
            super.onPostExecute(s);

            if(s !=null)
            {
                ImageView iv= (ImageView) findViewById(R.id.imageView);
                iv.setImageBitmap(s);
            }else
            {
                Log.d("demo", "Null Data");
            }
        }
    }


    private class GetDataWithParams extends AsyncTask<RequestParams, Void, String> {

        @Override
        protected String doInBackground(RequestParams... params) {
            BufferedReader br=null;
            try {
                HttpURLConnection con = params[0].setupConnection();
                br= new BufferedReader(new InputStreamReader(con.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line ="";

                while((line=br.readLine())!=null)
                {
                    sb.append(line+ "\n");
                }
                return  sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if(br!=null)
                        br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if(s !=null)
            {
                Log.d("demo", s);
            }else
            {
                Log.d("demo", "Null Data");
            }
        }
    }


    private boolean isConnectedOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
       NetworkInfo ni= cm.getActiveNetworkInfo();
        if(ni !=null && ni.isConnected())
        {
            return true;
        }else
        {
            return false;
        }
    }
}

//http://rss.cnn.com/rss/cnn_tech.rss

