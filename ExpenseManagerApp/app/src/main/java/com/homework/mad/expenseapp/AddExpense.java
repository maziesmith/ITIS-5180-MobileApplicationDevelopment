package com.homework.mad.expenseapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Home Work 2
 * Sanket Patil
 * Atul Kumar Banwar
 * AddExpense.java
 */
public class AddExpense extends Activity implements DatePickerDialog.OnDateSetListener {
    private Calendar calendar;
    private EditText edtTxtExpenseName;
    private Spinner spnrCategory;
    private EditText edtTxtAmount;
    private EditText edtTxtDate;
    private Expense expense;
    private ImageButton imgBtnReceipt;

    private Date date;
    private static final int SELECT_PICTURE = 1;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense);

        expense = new Expense();
        calendar = Calendar.getInstance(TimeZone.getDefault());
        edtTxtExpenseName = (EditText) findViewById(R.id.edit_text_expense_name);
        spnrCategory = (Spinner) findViewById(R.id.spinner_category);
        edtTxtAmount = (EditText) findViewById(R.id.edit_text_amount);
        edtTxtDate = (EditText) findViewById(R.id.edit_text_date);
        imgBtnReceipt= (ImageButton) findViewById(R.id.image_button_receipt);
    }

    public void showCalendar(View view) {
        DatePickerDialog dialog = new DatePickerDialog(AddExpense.this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        calendar.set(year, month, day);
        date = calendar.getTime();
        edtTxtDate.setText(DateFormat.format("MM-dd-yyyy", calendar.getTime()).toString());
    }

    /**
     * ImageButton handler (image picker)
     * @param view
     */
    public void onImageGalleryClicked(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE && data!=null) {
                selectedImageUri = data.getData();
                try {
                    Bitmap bitmap =  MainActivity.getBitmapFromUri(selectedImageUri, getContentResolver());
                    imgBtnReceipt.setImageBitmap(bitmap);
                    imgBtnReceipt.setAdjustViewBounds(true);
                    imgBtnReceipt.setMaxHeight(150);
                    imgBtnReceipt.setMaxWidth(150);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Add expense button handler
     * @param view
     */
    public void addExpense(View view) {
        String expenseName = edtTxtExpenseName.getText().toString();
        String category = spnrCategory.getSelectedItem().toString();
        double amount = Double.parseDouble(edtTxtAmount.getText().toString().length() != 0 ? edtTxtAmount.getText().toString() : "0");

        if (expenseName.length() == 0) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_empty_expense_name), Toast.LENGTH_SHORT).show();
        } else if (category.equals("Select Category")) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_empty_category), Toast.LENGTH_SHORT).show();
        } else if (amount == 0) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_empty_amount), Toast.LENGTH_SHORT).show();
        } else if (date == null) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_empty_date), Toast.LENGTH_SHORT).show();
        } else if (selectedImageUri ==  null) {
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.error_empty_receipt), Toast.LENGTH_SHORT).show();
        }else {
            expense.setName(expenseName);
            expense.setCategory(category);
            expense.setDate(date);
            expense.setAmount(amount);
            expense.setReceipt(selectedImageUri.toString());
            Intent intent = new Intent();
            intent.putExtra(MainActivity.EXPENSE_OBJ_KEY, expense);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
