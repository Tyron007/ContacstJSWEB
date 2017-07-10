package com.example.jswebstage1.contactjsweb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by Jsweb Stage1 on 10/07/2017.
 */

public class ContactEdit extends Activity implements View.OnClickListener {

    private EditText    nameText;
    private EditText    addressText;
    private EditText    mobileText;
    private EditText    homeText;
    private Button      submitButton;
    private Long        rowId;


    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Bundle extras = getIntent().getExtras();
        if (extras != null)
        {
            //get all the values for the corresponding keys
           String name = extras.getString(DBHelper.KEY_NAME);
           String address = extras.getString(DBHelper.KEY_ADDRESS);
           String mobile = extras.getString(DBHelper.KEY_MOBILE);
           String home = extras.getString(DBHelper.KEY_HOME);
           rowId = extras.getLong(DBHelper.KEY_ROW_ID);
        }
        setContentView(R.layout.contact_edit);
        EditText nameText = (EditText) findViewById(R.id.textName);
        EditText addressText = (EditText) findViewById(R.id.textAddress);
        EditText mobileText = (EditText) findViewById(R.id.textMobile);
        EditText homeText = (EditText) findViewById(R.id.textHome);
        submitButton = (Button) findViewById(R.id.BtnSave);
        submitButton.setOnClickListener(this);
        rowId = null;
    }


    @Override
    public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString(DBHelper.KEY_NAME, nameText.getText().toString());
            bundle.putString(DBHelper.KEY_ADDRESS, addressText.getText().toString());
            bundle.putString(DBHelper.KEY_MOBILE, mobileText.getText().toString());
            bundle.putString(DBHelper.KEY_HOME, homeText.getText().toString());
            if (rowId != null) {
                bundle.putLong(DBHelper.KEY_ROW_ID, rowId);
            }
            Intent intent = new Intent();
            intent.putExtra(null, bundle);
            setResult(RESULT_OK, intent);
            Log.e("what","result set");
            finish();
        }
}
