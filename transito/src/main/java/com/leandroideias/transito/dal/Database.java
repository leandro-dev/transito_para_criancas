package com.leandroideias.transito.dal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.leandroideias.transito.model.ItemDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leandro on 17/5/2014.
 */
public class Database extends SQLiteOpenHelper {
	private static final int VERSION = 2;
	private static final String databaseName = "Transito";
	private static final String tableName = "LITransito";

	public Database(Context context){
		super(context, databaseName, null, VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase) {
		String query = "CREATE TABLE " + tableName + " (" +
				"KEY_ID INTEGER AUTO INCREMENT PRIMARY KEY, " +
				"MUNDO INTEGER NOT NULL, " +
				"LEVEL INTEGER NOT NULL, " +
				"QUANT_ESTRELAS INTEGER NOT NULL, " +
				"QUANT_ERROS INTEGER NOT NULL, " +
				"PONTOS INTEGER NOT NULL, " +
				"UNIQUE (MUNDO, LEVEL) );";
		sqLiteDatabase.execSQL(query);
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
		String query = "DROP TABLE LITAcheOsErros";
		sqLiteDatabase.delete("LITAcheOsErros", null, null);
		onCreate(sqLiteDatabase);
	}

	public List<ItemDatabase> getListaLevels(int mundo){
		List<ItemDatabase> lista = new ArrayList<ItemDatabase>();
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(tableName, null, "MUNDO = ?", new String[]{"" + mundo}, null, null, "LEVEL");

		if(cursor.moveToFirst()){
			do{
				lista.add(obterItem(cursor));
			} while(cursor.moveToNext());
		}
		db.close();
		return lista;
	}

	public ItemDatabase obtemLevel(int mundo, int level){
		ItemDatabase item = null;
		SQLiteDatabase db = getReadableDatabase();
		Cursor cursor = db.query(tableName, null, "MUNDO = ? AND LEVEL = ?", new String[]{"" + mundo, "" + level}, null, null, null);
		if(cursor.moveToFirst()){
			item = obterItem(cursor);
		}
		db.close();
		return item;
	}

	private ItemDatabase obterItem(Cursor cursor){
		int mundo = cursor.getInt(cursor.getColumnIndex("MUNDO"));
		int level = cursor.getInt(cursor.getColumnIndex("LEVEL"));
		int quantEstrelas = cursor.getInt(cursor.getColumnIndex("QUANT_ESTRELAS"));
		int quantErros = cursor.getInt(cursor.getColumnIndex("QUANT_ERROS"));
		int pontos = cursor.getInt(cursor.getColumnIndex("PONTOS"));
		return (new ItemDatabase(mundo, level, quantEstrelas, quantErros, pontos));
	}

	public void insertLevel(int mundo, int level, int quantEstrelas, int quantErros, int pontos){
		ContentValues values = new ContentValues();
		values.put("MUNDO", mundo);
		values.put("LEVEL", level);
		values.put("QUANT_ESTRELAS", quantEstrelas);
		values.put("QUANT_ERROS", quantErros);
		values.put("PONTOS", pontos);
		SQLiteDatabase db = getWritableDatabase();
		db.insert(tableName, null, values);
		db.close();
	}

	public void updateLevel(int mundo, int level, int quantEstrelas, int quantErros, int pontos){
		ContentValues values = new ContentValues();
		values.put("MUNDO", mundo);
		values.put("LEVEL", level);
		values.put("QUANT_ESTRELAS", quantEstrelas);
		values.put("QUANT_ERROS", quantErros);
		values.put("PONTOS", pontos);
		SQLiteDatabase db = getWritableDatabase();
		db.update(tableName, values, "MUNDO = ? AND LEVEL = ?", new String[]{"" + mundo, "" + level});
		db.close();
	}
}
