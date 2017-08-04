package com.example.viewz_pc.sugarcanemanagementsystem;

import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AccountingFilterActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {
    private TextView date, dateLabel;
    private DateDialog dialog;
    private RadioGroup choice;
    private Button searchBtn;
    private Intent intent;
    private FragmentTransaction fragmentTransaction;
    private String selectedChoice = "farmer", fullDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounting_filter);

        selectCondition();
        setDefaultDatePicker();
    }

    public void selectCondition() {
        dateLabel = (TextView) findViewById(R.id.dateLabel);
        dateLabel.setVisibility(View.GONE);

        date = (TextView) findViewById(R.id.date);
        date.setVisibility(View.GONE);
        date.setOnClickListener(this);

        choice = (RadioGroup) findViewById(R.id.radioGroup);
        choice.setOnCheckedChangeListener(this);

        searchBtn = (Button) findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(this);
    }

    public void setDefaultDatePicker() {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        fullDate = year + "-" + (month + 1) + "-" + day;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.date:
                dialog = new DateDialog(date);
                fragmentTransaction = getSupportFragmentManager().beginTransaction();
                dialog.show(fragmentTransaction, "DatePicker");
                break;
            case R.id.searchBtn:
                if (selectedChoice.equals("farmer")) {
                    intent = new Intent(AccountingFilterActivity.this, ReceivableListActivity.class);
                } else if (selectedChoice.equals("plant")) {

                } else {
                    intent = new Intent(AccountingFilterActivity.this, AccountingModuleActivity.class);
                    intent.putExtra("DATE", date.getText().toString());
                    intent.putExtra("CONDITION", selectedChoice);
                }
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.farmerRadio:
                selectedChoice = "farmer";
                date.setText("");
                dateLabel.setVisibility(View.GONE);
                date.setVisibility(View.GONE);
                break;
            case R.id.plantRadio:
                selectedChoice = "plant";
                date.setText("");
                dateLabel.setVisibility(View.GONE);
                date.setVisibility(View.GONE);
                break;
            case R.id.cutterRadio:
                selectedChoice = "cutter";
                date.setText(fullDate);
                dateLabel.setVisibility(View.VISIBLE);
                date.setVisibility(View.VISIBLE);
                break;
            case R.id.tractorRadio:
                selectedChoice = "tractor";
                date.setText(fullDate);
                dateLabel.setVisibility(View.VISIBLE);
                date.setVisibility(View.VISIBLE);
                break;
        }
    }
}
