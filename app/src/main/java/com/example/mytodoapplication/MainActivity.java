package com.example.mytodoapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    Button btAdd,btReset;
    RecyclerView recyclerView;

    List<MainData> dataList=new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    RoomDB database;

    MainAdapter mainAdapter;

    AlertDialog.Builder builder;

    // code untuk menyimpan data yang ad di dalam fragmen
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText=findViewById(R.id.edit_text);
        btAdd=findViewById(R.id.bt_add);
        btReset=findViewById(R.id.bt_reset);
        recyclerView=findViewById(R.id.recycler_view);

        //code untuk menyimpan data ke database dengan menggunakan room

        database=RoomDB.getInstance(this);
        //menyimpan nilai databse ke dalam datalist
        dataList=database.mainDao().getAll();

        //code untuk membuat linear layout manager
        linearLayoutManager =new LinearLayoutManager(this);

        //code untuk mengatur layout manager
        recyclerView.setLayoutManager(linearLayoutManager);
        //code untuk membuat adapter
        mainAdapter=new MainAdapter(dataList,MainActivity.this);
        //code untuk mengatur adapter
        recyclerView.setAdapter(mainAdapter);

        // code untuk mengeksekusi code di dalam button
        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //code untuk menaruh string pada edit text

                String sText=editText.getText().toString().trim();
                if(!sText.equals("")){
                    //setelah textnya itu null
                    //code untuk membuat main data
                    MainData data=new MainData();
                    //code untuk mengatur text pada main data
                    data.setText(sText);
                    //memasukan text kedalam databse
                    database.mainDao().insert(data);
                    // untuk mengedit text
                    editText.setText("");
                    //untuk membuat notifikasi sukses menambahkan
                    dataList.clear();
                    Toast.makeText(MainActivity.this,"Sukses Menambahkan!",Toast.LENGTH_LONG).show();

                    dataList.addAll(database.mainDao().getAll());
                    mainAdapter.notifyDataSetChanged();

                }else{
                    builder= new AlertDialog.Builder(MainActivity.this);
                    //code untuk mengatur pesan secara manual dan melakukan tindakan saat mengklik tombol
                    builder.setMessage("Teks tidak boleh kosong!!")
                            .setCancelable(false)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });
                    //membuat box dialog
                    AlertDialog alert = builder.create();
                    //mengatur judul
                    alert.setTitle("Tindakan Tidak Valid");
                    alert.show();
                }
            }
        });
        btReset.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               builder= new AlertDialog.Builder(v.getContext());
               //code untuk mengatur pesan secara manual dan melakukan tindakan saat mengklik tombol
               builder.setMessage("Apakah anda yaking ingin menghapus semua list anda?")
                       .setCancelable(false)
                       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int id) {

                               //menghapus semua data dari database
                               database.mainDao().reset(dataList);
                               //perintah untuk memberitahi jika semua data di hapus
                               dataList.clear();
                               dataList.addAll(database.mainDao().getAll());
                               mainAdapter.notifyDataSetChanged();
                           }
                       })
                       .setNegativeButton("No", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int id) {
                               //  code fungsi untuk tombol No
                               dialog.cancel();
                           }
                       });
               //Membuat dialog box
               AlertDialog alert = builder.create();
               //mengatur judul secara manual
               alert.setTitle("Setel Ulang Konfirmasi");
               alert.show();

           }
        });
    }
}