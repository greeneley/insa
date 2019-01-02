package com.example.ikau.td3.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabase extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION   = 2;
    public static final String DATABASE_NAME   = "Favorites.db";
    public static final String TABLE_NAME      = "favorites";
    public static final String PKEY            = "id";
    public static final String COLUMN_SECRET   = "secret";
    public static final String COLUMN_PHOTO_ID = "photo_id";

    public MyDatabase(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String query = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, %s TEXT, %s TEXT)",
                TABLE_NAME, PKEY, COLUMN_PHOTO_ID, COLUMN_SECRET);
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public long insertNewFavorite(SQLiteDatabase db, String photo_id, String secret)
    {
        if(this.contains(db, photo_id, secret)) return -1;

        // Ajout des données
        ContentValues values = new ContentValues();
        values.put(MyDatabase.COLUMN_PHOTO_ID, photo_id);
        values.put(MyDatabase.COLUMN_SECRET, secret);

        // Insertion + retour de l'id de la ligne insérée
        return db.insert(MyDatabase.TABLE_NAME, null, values);
    }

    public int deleteFromFavorite(SQLiteDatabase db, String photo_id, String secret)
    {
        if(!this.contains(db, photo_id, secret)) return -1;

        // Paramétrages
        String selection       =  String.format("%s = ? AND %s = ?", COLUMN_PHOTO_ID, COLUMN_SECRET);
        String[] selectionArgs = { photo_id, secret };
        return db.delete(TABLE_NAME, selection, selectionArgs);
    }

    public boolean contains(SQLiteDatabase db, String photo_id, String secret)
    {
        // Paramétrages
        String   selection     = String.format("%s = ? AND %s = ?", COLUMN_PHOTO_ID, COLUMN_SECRET);
        String[] selectionArgs = new String[]{photo_id, secret};

        Cursor cursor = db.query(
                TABLE_NAME,
                null,
                selection,     // Paramétrages WHERE
                selectionArgs, // Valeur des WHERE
                null, null, null
        );

        boolean contains = cursor.moveToFirst();
        cursor.close();
        return contains;
    }
}
