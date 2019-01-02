package com.example.ikau.td3.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * https://developer.android.com/training/data-storage/sqlite
 * Classe personnalisée représentant une BDD SQLite à base niveau avec une unique table 'favorites'.
 */
public class MyDatabase extends SQLiteOpenHelper
{

    /* ==============================================================
     *                          Propriétés
     * ==============================================================
     */
    /**
     * La version de la BDD. À incrémenter chaque fois que des changements nécessitent un drop table.
     */
    public static final int DATABASE_VERSION   = 7; // > 1 et ne doit pas décrémenter

    /**
     * Le nom de la base de données.
     */
    public static final String DATABASE_NAME   = "Favorites.db";

    // Variables arbitraires pour faciliter l'accès à une table.
    public static final String TABLE_NAME      = "favorites";
    public static final String PKEY            = "id";
    public static final String COLUMN_SECRET   = "secret";
    public static final String COLUMN_PHOTO_ID = "photo_id";


    /* ==============================================================
     *                         Constructeur
     * ==============================================================
     */
    public MyDatabase(Context context)
    {
        // Factory indique si la BDD est créée via une classe de haut niveau
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /* ==============================================================
     *                          Overrides
     * ==============================================================
     */
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String query = String.format("CREATE TABLE %s (%s INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, %s TEXT, %s TEXT)",
                TABLE_NAME, PKEY, COLUMN_PHOTO_ID, COLUMN_SECRET);
        db.execSQL(query);
    }

    /**
     * Automatiquement appelée lorsque le numéro de version change.
     * @param db Instance utilisable de la BDD.
     * @param oldVersion Ancienne version.
     * @param newVersion Nouvelle version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    /* ==============================================================
     *                       Méthodes publiques
     * ==============================================================
     */

    /**
     * Insère une nouvelle ligne dans les favoris si elle n'existe pas déjà.
     * @param db Une instance ouverte de la BDD.
     * @param photo_id L'id de la photo.
     * @param secret Le secret associé à la photo et son id.
     * @return Le numéro de la ligne nouvellement insérée.
     */
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

    /**
     * Supprime une ligne dans les favoris si elle existe.
     * @param db Une instance ouverte de la BDD.
     * @param photo_id L'id de la photo.
     * @param secret Le secret associé à la photo et son id.
     * @return Le numéro de la ligne supprimée.
     */
    public int deleteFromFavorite(SQLiteDatabase db, String photo_id, String secret)
    {
        if(!this.contains(db, photo_id, secret)) return -1;

        // Paramétrages
        String selection       =  String.format("%s = ? AND %s = ?", COLUMN_PHOTO_ID, COLUMN_SECRET);
        String[] selectionArgs = { photo_id, secret };
        return db.delete(TABLE_NAME, selection, selectionArgs);
    }

    /**
     * Indique si une ligne avec les données spécifiées existe déjà.
     * @param db Une instance ouverte de la BDD.
     * @param photo_id L'id de la photo.
     * @param secret Le secret associé à la photo et son id.
     * @return true si une ligne existe, false sinon.
     */
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
