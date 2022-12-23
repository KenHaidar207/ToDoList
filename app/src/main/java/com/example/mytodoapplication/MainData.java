package com.example.mytodoapplication;

//define table name

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "table_name")
public class MainData implements Serializable {
    //membuat column id
    @PrimaryKey(autoGenerate = true)

    private int ID;

    //Membuat column text

    @ColumnInfo(name = "text")

    private String text;

    //code untuk menghasilkan getter and setter


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


}
