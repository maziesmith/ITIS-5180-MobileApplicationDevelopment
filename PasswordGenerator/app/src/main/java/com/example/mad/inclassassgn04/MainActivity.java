package com.example.mad.inclassassgn04;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.ContextMenu;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends Activity {
    private TextView txtViewPwdCountValue;
    private TextView txtViewPwdLengthValue;
    private TextView txtViewPwd;
    private SeekBar skBarPwdCount;
    private SeekBar skBarPwdLength;
    private Button btnGeneratePwdThread;
    private Button btnGeneratePwdAsync;
    private int pwdCount = 1;
    private int pwdLength = 8;

    private Handler handler;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtViewPwdCountValue = (TextView) findViewById(R.id.text_view_pwd_count_value);
        txtViewPwdLengthValue = (TextView) findViewById(R.id.text_view_pwd_length_value);
        txtViewPwd = (TextView) findViewById(R.id.text_view_pwd_value);
        skBarPwdCount = (SeekBar) findViewById(R.id.seek_bar_pwd_count);
        skBarPwdLength = (SeekBar) findViewById(R.id.seek_bar_pwd_length);
        btnGeneratePwdThread = (Button) findViewById(R.id.button_generate_pwd_thread);
        btnGeneratePwdAsync = (Button) findViewById(R.id.button_generate_pwd_async);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMax(1);
        progressDialog.setMessage("Generating Passwords...");
        progressDialog.setCancelable(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

        skBarPwdCount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pwdCount = progress + 1;
                txtViewPwdCountValue.setText(String.valueOf(pwdCount));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        skBarPwdLength.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pwdLength = progress + 8;
                txtViewPwdLengthValue.setText(String.valueOf(pwdLength));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    public void generatePasswordThread(final View view) {
        progressDialog.setMax(pwdCount);

        handler = new Handler(new Handler.Callback() {
            Bundle bundle;
            CharSequence[] passwords = new CharSequence[pwdCount];
            PopupMenu popupMenu = new PopupMenu(MainActivity.this, view);

            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case GeneratePasswordUsingThread.PASSWORD_GENERATION_START:
                        progressDialog.show();
                        progressDialog.setProgress(0);
                        break;
                    case GeneratePasswordUsingThread.PASSWORD_GENERATION_PROGRESS:
                        bundle = (Bundle)msg.obj;
                        int index = ((int)bundle.get("INDEX"));
                        passwords[index] = ((String)bundle.get("PASSWORD"));
                        progressDialog.setProgress(index +1);
                        break;
                    case GeneratePasswordUsingThread.PASSWORD_GENERATION_END:
                        int index2 = ((int)bundle.get("INDEX"));
                        if ((index2 + 1) == pwdCount) {
                            progressDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                            builder.setTitle("Passwords");
                            builder.setItems(passwords, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int which) {
                                    //Toast.makeText(getApplicationContext(), which, Toast.LENGTH_LONG).show();
                                    txtViewPwd.setText(passwords[which]);
                                }

                            });

                            builder.show();
                            //Toast.makeText(getApplicationContext(), passwords.toString(), Toast.LENGTH_LONG).show();
                        }
                        break;
                }

                return false;
            }
        });

        ExecutorService pool = Executors.newFixedThreadPool(2);
        for (int index = 0; index < pwdCount; index++) {
            pool.execute(new GeneratePasswordUsingThread(pwdLength, index));
        }
    }

    public void generatePasswordAsync(View view) {
        new GetPasswordWorker().execute(pwdCount, pwdLength);
    }

    class GeneratePasswordUsingThread implements Runnable {
        static final int PASSWORD_GENERATION_START = 1;
        static final int PASSWORD_GENERATION_PROGRESS = 2;
        static final int PASSWORD_GENERATION_END = 3;
        int pwdLength = 0;
        int index= 0;

        GeneratePasswordUsingThread(int pwdLength, int index) {
            this.pwdLength = pwdLength;
            this.index = index;
        }

        @Override
        public void run() {
            Message msg = new Message();
            msg.what = PASSWORD_GENERATION_START;
            handler.sendMessage(msg);

            String pwd = Util.getPassword(this.pwdLength);
            Bundle bundle = new Bundle();
            bundle.putString("PASSWORD", pwd);
            bundle.putInt("INDEX", index);
            msg = new Message();
            msg.obj = bundle;
            msg.what = PASSWORD_GENERATION_PROGRESS;
            handler.sendMessage(msg);

            msg = new Message();
            bundle = new Bundle();
            bundle.putInt("INDEX", index);
            msg.obj = bundle;
            msg.what = PASSWORD_GENERATION_END;
            handler.sendMessage(msg);
        }
    }




    // Async
    class GetPasswordWorker extends AsyncTask<Integer, Integer, Void>
    {
        String password=null;
        AlertDialog.Builder alertDialogPassword;
        ArrayList<String> passwordList= new ArrayList<String>();
        @Override
        protected Void doInBackground(Integer... params) {
            for ( int i =1; i<=params[0]; i++)
            {
                password=Util.getPassword(params[1]);
                passwordList.add(password);
                publishProgress(i+1);
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(pwdCount);
            progressDialog.setCancelable(false);
            progressDialog.setMessage(getResources().getString(R.string.progress_dialog_message));
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            progressDialog.dismiss();
            alertDialogPassword = new AlertDialog.Builder(MainActivity.this);
            alertDialogPassword.setTitle("Passwords");
            final CharSequence[] csPasswords = passwordList.toArray(new CharSequence[passwordList.size()]);
            alertDialogPassword. setItems(csPasswords, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int which) {
                    txtViewPwd.setText(csPasswords[which]);
                }
            });
            alertDialogPassword.show();
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            progressDialog.setProgress(values[0]);
        }
    }
}
