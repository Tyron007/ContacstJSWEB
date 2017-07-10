package com.example.jswebstage1.contactjsweb;

import android.Manifest;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.view.MenuItem;

/**
 * Created by Jsweb Stage1 on 10/07/2017.
 */

public class Contact extends ListActivity {

    private static final int CONTACT_CREATE = 0;
    private static final int CONTACT_EDIT = 1;
    private static final int INSERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private DBHelper dbHelper;
    private Cursor c;
    public LinearLayout contentViewLayout;
    private LinearLayout.LayoutParams contentViewLayoutParams;
    private Context context;

    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};


    public  boolean isStoragePermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("YES","Permission is granted");
                return true;
            } else {

                Log.v("YES","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("YES","Permission is granted");
            return true;
        }
    }

    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        isStoragePermissionGranted();
        context = getApplicationContext();
        setContentView(R.layout.contact);
        contentViewLayout = (LinearLayout)findViewById(R.id.beta);
        contentViewLayoutParams = new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
        dbHelper = new DBHelper(this);
        fillData();
    }

    //load data from sql database into contact at loading page
    private void fillData() {
        //c = dbHelper.fetchAllRows();
        startManagingCursor(c);
        ListAdapter adapter = new SimpleCursorAdapter(this,
                R.layout.contact_row, c, new String[] { DBHelper.KEY_NAME,
                DBHelper.KEY_MOBILE }, new int[] { R.id.name,
                R.id.phonenumber });
        setListAdapter(adapter);
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        menu.add(0, INSERT_ID, R.string.menu_insert, "t");
        menu.add(0, DELETE_ID, R.string.menu_delete, "p");
        return true;
    }

    private void createContact() {
        Intent i = new Intent(this, ContactEdit.class);
        startActivity(i);
    }


    @Override protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String name = data.getExtras().getString(DBHelper.KEY_NAME);
            String address = data.getExtras().getString(DBHelper.KEY_ADDRESS);
            String mobile = data.getExtras().getString(DBHelper.KEY_MOBILE);
            String home = data.getExtras().getString(DBHelper.KEY_HOME);
            switch (requestCode) {
                case CONTACT_CREATE:
                    dbHelper.createRow(name, address, mobile, home);
                    fillData();
                    break;
                case CONTACT_EDIT:
                    Long rowId = data.getExtras().getLong(DBHelper.KEY_ROW_ID);
                    if (rowId != null){
                        dbHelper.updateRow(rowId, name, address, mobile, home);
                    }
                    fillData();
                    break;
            }
        }
    }


    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        Intent i = new Intent(this, ContactEdit.class);
        i.putExtra(DBHelper.KEY_ROW_ID, c.getLong(c.getColumnIndex(DBHelper.KEY_ROW_ID)));
        i.putExtra(DBHelper.KEY_NAME, c.getString(c.getColumnIndex(DBHelper.KEY_NAME)));
        i.putExtra(DBHelper.KEY_ADDRESS, c.getString(c.getColumnIndex(DBHelper.KEY_ADDRESS)));
        i.putExtra(DBHelper.KEY_MOBILE, c.getString(c.getColumnIndex(DBHelper.KEY_MOBILE)));
        i.putExtra(DBHelper.KEY_HOME, c.getString(c.getColumnIndex(DBHelper.KEY_HOME)));
        startActivity(i);
    }

    public boolean onMenuItemSelected(int featureId, MenuItem item) {
        super.onMenuItemSelected(featureId, item);
        switch (item.getItemId()) {
            case INSERT_ID:
                createContact();
                break;
            case DELETE_ID:
                dbHelper.deleteRow(c.getLong(c.getColumnIndex(DBHelper.KEY_ROW_ID)));
                fillData();
                break;
        }
        return true;
    }

}
