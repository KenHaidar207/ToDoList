package com.example.mytodoapplication;

//add database entities
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;

@Database(entities = {MainData.class},version = 1,exportSchema = false)

public abstract class RoomDB extends RoomDatabase {
    //Membuat contoh database

    private static RoomDB databse;

    //mendefinisikan nama database

    private static String DATABASE_NAME="database";

    public  synchronized static RoomDB getInstance(Context context){
        if(databse==null){
            //ketika db null
            //inisialisasi db
            databse= Room.databaseBuilder(context.getApplicationContext(),RoomDB.class,DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }

        //kembali ke db
        return databse;
    }

    //membuat Dao

    public abstract MainDao mainDao();

}
