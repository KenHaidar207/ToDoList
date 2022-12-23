package com.example.mytodoapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
   //mengoreksi variabel

    private List<MainData> dataList;
    private Activity context;
    private  RoomDB database;

    AlertDialog.Builder builder;

    //membuat konstruktor
    public MainAdapter(List<MainData> dataList, Activity context) {
        this.dataList = dataList;
        this.context = context;
        notifyDataSetChanged();
    }

    @NonNull

    @Override
    // code untuk membuat objek pada viewholder dan menentukan layout xml yang akan digunakan
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //intilaize tampilan
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_main,parent,false);

        return new ViewHolder(view);
    }

    @Override
    // code untuk menghubungkan data yang ada dengan objek yang ada di viewholder
    public void onBindViewHolder(@NonNull MainAdapter.ViewHolder holder, int position) {
        //code untuk membuat main data
        MainData data=dataList.get(position);
        //code untuk menyimpan data ke database dengan menggunakan room
        database=RoomDB.getInstance(context);
        //mengatur text didalam textview
        holder.textView.setText(data.getText());

        holder.btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //membuat code main data yang dijalankan terlebih dahulu
                MainData d=dataList.get(holder.getAdapterPosition());
                //mendapatkan sebuah id
                int sID = d.getID();
                //mendapatkan sebuah text
                String sText = d.getText();

                //membuat dialog dialog
                Dialog dialog = new Dialog(context);
                // untuk mengatur tampilan konten
                dialog.setContentView(R.layout.dialog_update);

                //mengatur lebar

                int width= WindowManager.LayoutParams.MATCH_PARENT;
                //mengatur tinggi

                int height=WindowManager.LayoutParams.WRAP_CONTENT;
                //set layout

                dialog.getWindow().setLayout(width,height);

                //menampilkan dialog

                dialog.show();

                //untuk menetapkan variabel

                EditText editText=dialog.findViewById(R.id.edit_text);
                Button btUpdate=dialog.findViewById(R.id.bt_update);

                //mengatur text pada edit text

                editText.setText(sText);

                btUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // penutup dialog

                        dialog.dismiss();
                        //mendapatkan update text dari edit text

                        String uText=editText.getText().toString().trim();
                        //mengupdate text in db

                        database.mainDao().upate(sID,uText);
                        //pemberitahuan data yagn dihapus

                        dataList.clear();
                        dataList.addAll(database.mainDao().getAll());
                        notifyDataSetChanged();


                    }
                });


            }
        });

        holder.btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder= new AlertDialog.Builder(v.getContext());
                //code untuk mengatur pesan secara manual dan melakukan tindakan saat mengklik tombol
                builder.setMessage("Apakah anda yakin ingin menghapus list anda?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                MainData d=dataList.get(holder.getAdapterPosition());
                                //menghapus text dari database

                                database.mainDao().delete(d);
                                //pemberitahuan jika data sudah dihapus
                                int position = holder.getAdapterPosition();
                                dataList.remove(position);
                                notifyItemRemoved(position);
                                notifyItemRangeChanged(position,dataList.size());
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
                //mengatur judul
                alert.setTitle("Hapus Konfirmasi");
                alert.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        //init varibel

        TextView textView;
        ImageView btEdit,btDelete;

        public ViewHolder(@NonNull  View itemView) {
            super(itemView);
            //menentukan variable
            textView=itemView.findViewById(R.id.text_view);
            btEdit=itemView.findViewById(R.id.bt_edit);
            btDelete=itemView.findViewById(R.id.bt_delete);


        }
    }
}
