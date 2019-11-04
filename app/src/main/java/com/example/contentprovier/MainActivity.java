package com.example.contentprovier;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.contentprovier.adapter.SachAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String CONTENT_URI = "content://com.example.contentprovier/DATA";
    private static final Uri providerUri=Uri.parse(CONTENT_URI);

    private EditText et_id, et_title, et_author;
    private Button bt_luu, bt_select, bt_exit, bt_update, bt_delete;

    private GridView gr_display;
    private SachAdapter adapter;
    private List<Sach> sachList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et_id = findViewById(R.id.edtMs);
        et_title = findViewById(R.id.edtTd);
        et_author = findViewById(R.id.edtTg);
        gr_display = findViewById(R.id.grid);
        bt_luu = findViewById(R.id.btnSae);
        bt_select = findViewById(R.id.btnSelect);
        bt_exit = findViewById(R.id.btnexit);
        bt_update = findViewById(R.id.btnUp);
        bt_delete = findViewById(R.id.btnDe);

        // init GridView
        this.sachList = new ArrayList<>();
        this.adapter = new SachAdapter(this, R.layout.item_sach, this.sachList);
        this.gr_display.setAdapter(adapter);

        // set su kien khi click vao 1 item cua GridView
        this.gr_display.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Sach sach = sachList.get(position); // get Sach tu sachList ra theo position da click
                et_id.setText(String.valueOf(sach.getId())); // set ID lai cho EditText
                et_title.setText(sach.getTitle()); // set title lai cho EditText
                et_author.setText(sach.getAuthor()); // set author lai cho EditText
            }
        });

        // Click button Select
        bt_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<Sach> list = new ArrayList<>();
                Cursor cursor = getContentResolver().query(
                        providerUri, // select tu table nao... tuong ung voi URI
                        null, // select ....
                        null, // where
                        null, // where params
                        "title"); // order by

                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        Sach sach = new Sach(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
                        list.add(sach);
                    }
                    while (cursor.moveToNext());

                    sachList.clear(); // clear list sach
                    sachList.addAll(list); // add lai list sach vua select duoc
                    adapter.notifyDataSetChanged(); // update lai GridView
                } else {
                    Toast.makeText(getApplicationContext(), "Khong co ket qua", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // click button add
        bt_luu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values=new ContentValues();
                values.put("id", Integer.parseInt(et_id.getText().toString()));
                values.put("title", et_title.getText().toString());
                values.put("author", et_author.getText().toString());

                // Insert book
                getContentResolver().insert(providerUri,values);

                Toast.makeText(MainActivity.this,"Thanh cong",Toast.LENGTH_SHORT).show();
            }
        });

        // click button upgrade
        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues values=new ContentValues();
                values.put("title", et_title.getText().toString());
                values.put("author", et_author.getText().toString());

                getContentResolver().update(providerUri,values,"id=?", new String[] {et_id.getText().toString()});
                Toast.makeText(MainActivity.this, "Thanh cong !", Toast.LENGTH_SHORT).show();
            }
        });

        // click button delete
        bt_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContentResolver().delete(providerUri, "id=?", new String[] {et_id.getText().toString()});
                Toast.makeText(MainActivity.this, "Thanh cong !", Toast.LENGTH_SHORT).show();
            }
        });

        // click button exit
        bt_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogConfirmExit();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // bat su kien nut back
        dialogConfirmExit();
    }

    private void dialogConfirmExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Exit");
        builder.setMessage("Thoat ung dung ?");
        builder.setCancelable(false); // khong cho click outside tat dialog
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // tat dialog
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss(); // tat dialog
                finish(); // exit ung dung
            }
        });
        builder.create().show(); // show dialog
    }
}
