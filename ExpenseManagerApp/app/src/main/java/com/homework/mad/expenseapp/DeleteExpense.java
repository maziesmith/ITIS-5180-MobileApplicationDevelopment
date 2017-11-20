package com.homework.mad.expenseapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Home Work 2
 * Atul Kumar Banwar
 * Sanket Patil
 * DeleteExpense.java
 */

public class DeleteExpense extends Activity {
    private ArrayList<Expense> expenses;
    private Expense expense;
    private int editExpenseIndex = -1;
    private static final int SELECT_PICTURE = 1;

    private Button btnSelectExpense;
    private Calendar calendar;
    private EditText edtTxtExpenseName;
    private Spinner spnrCategory;
    private EditText edtTxtAmount;
    private EditText edtTxtDate;
    private ImageButton imgBtnDatePicker;
    private ImageButton imgBtnReceipt;
    private Button btnDelete;

    Date date;
    Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_expense);

        expense = new Expense();
        calendar = Calendar.getInstance(TimeZone.getDefault());

        btnSelectExpense = (Button) findViewById(R.id.button_add_expense);
        edtTxtExpenseName = (EditText) findViewById(R.id.edit_text_expense_name);
        spnrCategory = (Spinner) findViewById(R.id.spinner_category);
        edtTxtAmount = (EditText) findViewById(R.id.edit_text_amount);
        edtTxtDate = (EditText) findViewById(R.id.edit_text_date);
        imgBtnDatePicker = (ImageButton) findViewById(R.id.image_button_calendar);;
        imgBtnReceipt= (ImageButton) findViewById(R.id.image_button_receipt);
        btnDelete = (Button) findViewById(R.id.button_delete_expense);

        if (getIntent().getExtras() != null) {
            expenses = (ArrayList<Expense>) getIntent().getExtras().getSerializable(MainActivity.EXPENSE_OBJS_KEY);
        }

        edtTxtExpenseName.setEnabled(false);
        spnrCategory.setEnabled(false);
        edtTxtAmount.setEnabled(false);
        edtTxtDate.setEnabled(false);
        imgBtnDatePicker.setEnabled(false);
        imgBtnReceipt.setEnabled(false);
        btnDelete.setEnabled(false);
    }

    public void selectExpense(View view) {
        if (expenses != null && expenses.size() != 0) {
            registerForContextMenu(view);
            openContextMenu(view);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Pick an Expense");

        for (int i = 0; i < expenses.size(); i++) {
            menu.add(0, i, 0, expenses.get(i).getName());
        }
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        editExpenseIndex = item.getItemId();
        expense = expenses.get(editExpenseIndex);

        edtTxtExpenseName.setText(expense.getName().toString());

        String[] categories = getResources().getStringArray(R.array.spinner_category);
        for (int i = 0, count = categories.length; i < count; i++) {
            if (categories[i].equals(expense.getCategory())) {
                spnrCategory.setSelection(i);
            }
        }

        edtTxtAmount.setText(String.valueOf(expense.getAmount()));
        edtTxtDate.setText(DateFormat.format("MM-dd-yyyy", expense.getDate()).toString());

        Uri receipt = Uri.parse(expense.getReceipt().toString());
        Bitmap receiptImg = null;
        try {
            receiptImg = MainActivity.getBitmapFromUri(receipt, getContentResolver());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        imgBtnReceipt.setImageBitmap(receiptImg);
        imgBtnReceipt.setAdjustViewBounds(true);
        imgBtnReceipt.setMaxHeight(150);
        imgBtnReceipt.setMaxWidth(150);

        btnDelete.setEnabled(true);

        return super.onContextItemSelected(item);
    }

    /**
     * Delete button handler
     * @param view
     */
    public void deleteExpense(View view) {
        expenses.remove(editExpenseIndex);

        Intent intent = new Intent();
        intent.putExtra(MainActivity.EXPENSE_OBJS_KEY, expenses);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * Cancel button handler
     * @param view
     */
    public void cancelDelete(View view) {
        finish();
    }
}
