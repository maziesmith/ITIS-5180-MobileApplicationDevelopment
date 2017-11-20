package com.homework.mad.expenseapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
* Home Work 2
* Sanket Patil
* Atul Kumar Banwar
* ShowExpense.java
*/
public class ShowExpense extends Activity {

    private TextView txtVwName;
    private TextView txtVwCategory;
    private TextView txtVwAmount;
    private TextView txtVwDate;
    private ImageView imgVwReceipt;
    private ImageButton imgBtnFirst;
    private ImageButton imgBtnPrevious;
    private ImageButton getImgBtnNext;
    private ImageButton getImgBtnLast;
    private Button btnFinish;
    private ArrayList<Expense> expenses;
    private int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_expense);

        txtVwName = (TextView) findViewById(R.id.text_view_name_value);
        txtVwCategory = (TextView) findViewById(R.id.text_view_category_value);
        txtVwAmount = (TextView) findViewById(R.id.text_view_amount_value);
        txtVwDate = (TextView) findViewById(R.id.text_view_date_value);
        imgVwReceipt = (ImageView) findViewById(R.id.image_view_receipt);
        imgBtnFirst = (ImageButton) findViewById(R.id.image_button_first);
        imgBtnPrevious = (ImageButton) findViewById(R.id.image_button_previous);
        getImgBtnNext = (ImageButton) findViewById(R.id.image_button_next);
        getImgBtnLast = (ImageButton) findViewById(R.id.image_button_last);
        btnFinish = (Button) findViewById(R.id.button_finish);

        if (getIntent().getExtras() != null) {
            expenses = (ArrayList<Expense>) getIntent().getExtras().getSerializable(MainActivity.EXPENSE_OBJS_KEY);
        }
        displayContent(currentPosition);
    }

    /**
     * processes array list of expences according to position parameter
     * @param position
     */
    private void displayContent(int position) {
        Expense expense = expenses.get(position);

        if (expense != null) {
            Uri imageUri = Uri.parse(expense.getReceipt());
            Bitmap bitmapImage = null;
            try {
                bitmapImage =  MainActivity.getBitmapFromUri(imageUri, getContentResolver());
            } catch (IOException e) {
                e.printStackTrace();
            }
            txtVwName.setText(expense.getName());
            txtVwCategory.setText(expense.getCategory());
            txtVwAmount.setText("$ " + String.valueOf(expense.getAmount()));

            DateFormat df = new SimpleDateFormat("MM/dd/yyyy");

            txtVwDate.setText(df.format(expense.getDate()));
            imgVwReceipt.setImageBitmap(bitmapImage);
            imgVwReceipt.setMaxHeight(400);
            imgVwReceipt.setMaxWidth(800);
        }
    }

    /**
     * Handler for finish button
     * @param view
     */
    public void finish(View view) {
        finish();
    }

    /**
     * Handler for go to first image button
     * @param view
     */
    public void getFirstExpense(View view) {
        if( checkExistence(currentPosition-1))
        {displayContent(0);
        currentPosition = 0;}
        else{
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_empty_already_first), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Handler for go to previous image button
     * @param view
     */
    public void getPreviousExpense(View view) {

        if (checkExistence(currentPosition - 1)) {
            displayContent(currentPosition - 1);
            currentPosition = currentPosition - 1;
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_empty_reached_first), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Handler for go to next image button
     * @param view
     */
    public void getNextExpense(View view) {
        if (checkExistence(currentPosition + 1)) {
            displayContent(currentPosition + 1);
            currentPosition = currentPosition + 1;
        } else {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_empty_reached_last), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Handler for go to last image button
     * @param view
     */
    public void getLastExpense(View view) {

        if (! (currentPosition == expenses.size() - 1)) {
            displayContent(expenses.size() - 1);
            currentPosition = expenses.size() - 1;
        }else
        {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_empty_already_last), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * checks the feasibility of requested postion
     * @param position
     * @return
     */
    private boolean checkExistence(int position) {
        if (position < expenses.size() && position >= 0) {
            return true;
        } else {
            return false;
        }
    }
}
