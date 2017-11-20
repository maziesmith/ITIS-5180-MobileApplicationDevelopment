package com.homework.mad.expenseapp;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Home Work 2
 * Sanket Patil
 * Atul Kumar Banwar
 * EditExpense.java
 */
public class EditExpense extends Activity implements DatePickerDialog.OnDateSetListener {
    private ArrayList<Expense> expenses;
    private Expense expense;
    private int editExpenceIndex = -1;
    private static final int SELECT_PICTURE = 1;

    private Button btnSelectExpense;
    private Calendar calendar;
    private EditText edtTxtExpenseName;
    private Spinner spnrCategory;
    private EditText edtTxtAmount;
    private EditText edtTxtDate;
    private ImageButton imgBtnDatePicker;
    private ImageButton imgBtnReceipt;
    private Button btnSave;

    Date date;
    Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_expense);

        expense = new Expense();
        calendar = Calendar.getInstance(TimeZone.getDefault());

        btnSelectExpense = (Button) findViewById(R.id.button_add_expense);
        edtTxtExpenseName = (EditText) findViewById(R.id.edit_text_expense_name);
        spnrCategory = (Spinner) findViewById(R.id.spinner_category);
        edtTxtAmount = (EditText) findViewById(R.id.edit_text_amount);
        edtTxtDate = (EditText) findViewById(R.id.edit_text_date);
        imgBtnDatePicker = (ImageButton) findViewById(R.id.image_button_calendar);;
        imgBtnReceipt= (ImageButton) findViewById(R.id.image_button_receipt);
        btnSave = (Button) findViewById(R.id.button_save_edit_expense);

        if (getIntent().getExtras() != null) {
            expenses = (ArrayList<Expense>) getIntent().getExtras().getSerializable(MainActivity.EXPENSE_OBJS_KEY);
        }

        edtTxtExpenseName.setEnabled(false);
        spnrCategory.setEnabled(false);
        edtTxtAmount.setEnabled(false);
        edtTxtDate.setEnabled(false);
        imgBtnDatePicker.setEnabled(false);
        imgBtnReceipt.setEnabled(false);
        btnSave.setEnabled(false);
    }

    /**
     * Calendar Button Handler to display calendar
     * @param view
     */
    public void showCalendar(View view) {
        DatePickerDialog dialog = new DatePickerDialog(EditExpense.this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
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
     * Select Expense Click Handler
     * @param view
     */
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
        editExpenceIndex = item.getItemId();
        expense = expenses.get(editExpenceIndex);

        edtTxtExpenseName.setText(expense.getName().toString());

        String[] categories = getResources().getStringArray(R.array.spinner_category);
        for (int i = 0, count = categories.length; i < count; i++) {
            if (categories[i].equals(expense.getCategory())) {
                spnrCategory.setSelection(i);
            }
        }

        edtTxtAmount.setText(String.valueOf(expense.getAmount()));
        date = expense.getDate();
        edtTxtDate.setText(DateFormat.format("MM-dd-yyyy", date).toString());

        selectedImageUri = Uri.parse(expense.getReceipt().toString());
        Bitmap receiptImg = null;
        try {
            receiptImg = MainActivity.getBitmapFromUri(selectedImageUri, getContentResolver());
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        imgBtnReceipt.setImageBitmap(receiptImg);
        imgBtnReceipt.setAdjustViewBounds(true);
        imgBtnReceipt.setMaxHeight(150);
        imgBtnReceipt.setMaxWidth(150);

        edtTxtExpenseName.setEnabled(true);
        spnrCategory.setEnabled(true);
        edtTxtAmount.setEnabled(true);
        edtTxtDate.setEnabled(true);
        imgBtnDatePicker.setEnabled(true);
        imgBtnReceipt.setEnabled(true);
        btnSave.setEnabled(true);

        return super.onContextItemSelected(item);
    }

    /**
     * Receipt Select Click Handler
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
     * Save Edited Expense Click Handler
     * @param view
     */
    public void saveEditedExpense(View view) {
        String expenseName = edtTxtExpenseName.getText().toString();
        String category = spnrCategory.getSelectedItem().toString();
        double amount = Double.parseDouble(edtTxtAmount.getText().toString().length() != 0 ? edtTxtAmount.getText().toString() : "0");
        Expense editedExpense = new Expense();

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
            editedExpense.setName(expenseName);
            editedExpense.setCategory(category);
            editedExpense.setDate(date);
            editedExpense.setAmount(amount);
            editedExpense.setReceipt(selectedImageUri.toString());

            expenses.set(editExpenceIndex, editedExpense);

            Intent intent = new Intent();
            intent.putExtra(MainActivity.EXPENSE_OBJS_KEY, expenses);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    public void cancelEdit(View view) {
        finish();
    }
}