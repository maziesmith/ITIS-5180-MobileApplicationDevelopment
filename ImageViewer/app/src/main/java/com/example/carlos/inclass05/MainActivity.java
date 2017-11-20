/*
Authors:
-Carlos Campos
-Rama Vamshi Krishna Dhulipala
-Bharat Pothina
-Sanket Patil

Assigment: InClass05
Group: 6
Table: 3

 */


package com.example.carlos.inclass05;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements GetImagesData.IData {


    TextView tvKeyword;
    Button btnGo;
    ImageView ivPictures, ivPrev, ivNext;
    AlertDialog adKeywords;
    ProgressDialog adLoading, adLoadingPictures;
    //String currentResult;
    ArrayList<String> imageURLs;
    int currentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewsById();

        ivPrev.setEnabled(false);
        ivNext.setEnabled(false);

        setEventHandlers();
        configureProgressDiaglog();
        configurePictureProgressDiaglog();
    }

    private void findViewsById()
    {
        tvKeyword = (TextView) findViewById(R.id.tvKeyword);
        btnGo = (Button) findViewById(R.id.btnGo);
        ivPictures = (ImageView) findViewById(R.id.ivPictures);
        ivPrev = (ImageView) findViewById(R.id.ivPrev);
        ivNext = (ImageView) findViewById(R.id.ivNext);
    }

    private void setEventHandlers()
    {
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                configureAlertDialog();
                adKeywords.show();
            }
        });

        ivPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPosition == 0)
                {
                    currentPosition = (imageURLs.size()-1);
                }
                else
                {
                    currentPosition = currentPosition - 1;
                }

                new GetImages(MainActivity.this).execute(imageURLs.get(currentPosition));
            }
        });

        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentPosition<(imageURLs.size()-1))
                {
                    currentPosition = currentPosition + 1;
                }
                else if (currentPosition == (imageURLs.size()-1))
                {
                    currentPosition = 0;
                }
                new GetImages(MainActivity.this).execute(imageURLs.get(currentPosition));
            }
        });
    }

    private void configureAlertDialog()
    {
        AlertDialog.Builder builder;
        ArrayList<String> keywords = new ArrayList<String>();
        keywords.add("UNCC");
        keywords.add("Android");
        keywords.add("Winter");
        keywords.add("Aurora");
        keywords.add("Wonders");
        final CharSequence[] csKeywords = keywords.toArray(new CharSequence[keywords.size()]);
        builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.choose)).
                setItems(csKeywords, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (isConnectedOnline())
                        {
                            tvKeyword.setText(csKeywords[which].toString());
                            new GetImagesData(MainActivity.this).execute(csKeywords[which].toString());
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Check your internet connection.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        adKeywords = builder.create();
    }

    private void configureProgressDiaglog()
    {
        adLoading = new ProgressDialog(this);
        adLoading.setMessage(getResources().getString(R.string.Loading));
    }

    private void configurePictureProgressDiaglog()
    {
        adLoadingPictures = new ProgressDialog(this);
        adLoadingPictures.setMessage(getResources().getString(R.string.Loading_photo));
    }


    @Override
    public void getImagesFromDictionary(String result) {
        imageURLs = new ArrayList<String>();
        imageURLs.addAll(Arrays.asList(result.split(";")));
        imageURLs.remove(0);
        currentPosition = 0;

        if (!isConnectedOnline())
        {
            Toast.makeText(MainActivity.this, "Check your internet connection.", Toast.LENGTH_SHORT).show();
            return;
        }


        if (imageURLs.size() == 0)
        {
            ivPrev.setEnabled(false);
            ivNext.setEnabled(false);
            ivPictures.setImageBitmap(null);
            Toast.makeText(MainActivity.this, "No images found.", Toast.LENGTH_SHORT).show();
        }
        else if (imageURLs.size() == 1)
        {
            ivPrev.setEnabled(false);
            ivNext.setEnabled(false);
            new GetImages(MainActivity.this).execute(imageURLs.get(currentPosition));
        }
        else
        {
            ivPrev.setEnabled(true);
            ivNext.setEnabled(true);
            new GetImages(MainActivity.this).execute(imageURLs.get(currentPosition));
        }

    }

    private boolean isConnectedOnline()
    {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
        {
            return true;
        }
        else
        {
            return false;
        }
    }

}
