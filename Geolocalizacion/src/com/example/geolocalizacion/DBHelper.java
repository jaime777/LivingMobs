package com.example.geolocalizacion;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

		/**
		 * Ruta por defecto de la base de datos
		 * */
	   @SuppressLint("SdCardPath") private static String DB_PATH="/data/data/com.example.geolocalizacion/";
		private static String DB_NAME="movimientos.db";
		private SQLiteDatabase myDataBase;
		private final Context myContext;
		
		/**
		 * almecenara los registro cuando se realize la consulta
		 */
		public static final String[] campos=new String[]{"tipoGeo","lat","lon","bateria"};
		public static final String[] camposInfo=new String[]{"marca","Email","modelo"};
//		public static ArrayList<String> tipoGeo=new ArrayList<String>();
//		public static ArrayList<String> lat=new ArrayList<String>();
//		public static ArrayList<String> lon=new ArrayList<String>();
		
		/**
		 * Constructor
		 * Crea el objeto que nos permitira controla la apertura de la base de datos
		 * 
		 */
		public DBHelper(Context contexto) {
			super(contexto, DB_NAME, null, 1);
			this.myContext = contexto;
		}
		
		/**
		 * Crea una base de datos vacia en el sistema y la reescribe 
		 * con nuestro fichero de base de datos
		 */
		public void createDataBase() throws IOException{
			boolean dbExist=checkDataBase();
			if(dbExist){
				//Si Existe no hacemos nada!
			}
			else {
				/**
				 * Llamando a este metodo se crea la base de datos vacia en la ruta
				 * por defecto del sistema de nuestra aplicacion por lo que pdoremos
				 * sobreescribirla con nuestra base de datos.
				 */
				this.getReadableDatabase();
				
				try {
					copyDataBase();
				} catch (IOException e){
					throw new Error("Error copiando database");
				}
			}
		}
		
		private boolean checkDataBase(){
			SQLiteDatabase checkDB=null;
			
			try{
				String myPath = DB_PATH + DB_NAME;
				checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
			} catch (SQLException e){
				//Base de datos no creada todavia
			}
			return checkDB != null ? true : false;
		}
		
		public void openDataBase() throws SQLException {
			/**
			 * Abre la base de datos
			 */
			String myPath = DB_PATH + DB_NAME;
			myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
		}
			
		private void copyDataBase() throws IOException{
			/**
			 * Ruta a la base de datos vacia recien creada
			 */
			OutputStream myOutputDB = new FileOutputStream("" + DB_PATH + DB_NAME);
			/**
			 * Abrimos el fichero de base de datos como entrada
			 */
			InputStream myInputDB = myContext.getAssets().open(DB_NAME);
			
			/**
			 * Transferimos los bytes desde el fichero de entrada al de salida
			 */
			byte[] buffer = new byte[1024];
			int lenght;
			while((lenght = myInputDB.read(buffer))>0){
				myOutputDB.write(buffer,0,lenght);
			}
			
			/**
			 * Liberamos los stream
			 */
			myOutputDB.flush();
			myOutputDB.close();
			myInputDB.close();
		}
		
/*		public void getRegistros(){
			Cursor c=myDataBase.query("edificios", campos, null, null, null, null, null);
			//Cursor c = db.query("edificios",campos,null,null,null,null,"_id");
			//Asegurando que existe almenos un registro
			nombre.clear(); lat.clear(); lon.clear();
			if(c.moveToFirst()){
				//recorriendo el cursos para obtener los registros
				do{
					nombre.add(c.getString(0));
					lat.add(c.getString(1));
					lon.add(c.getString(2));
				}while(c.moveToNext());
			}
		}*/
		
		/**
		 * insertamos en la base de datos la latitud y longitud obtenida
		 * @param lat
		 * @param lon
		 * @param tipo
		 */
		public void insertarMov(String lat, String lon, String tipo, String Bateria){
			ContentValues nuevoRegistro = new ContentValues();
			nuevoRegistro.put(campos[0],tipo);	//Tipo de geolocalizacion
			nuevoRegistro.put(campos[1],lat);	//latitud
			nuevoRegistro.put(campos[2],lon);	//Longitud
			nuevoRegistro.put(campos[3],Bateria);	//Nivel de Bateria
			myDataBase.insert("moviendo", null, nuevoRegistro);	//Insertando el registro en la base de datos
		}
		
	
		 /**
		  * Insertamos la informacion dle dispositivo
		  * @param info
		  */
		public void insertInfoDisp(String[] info){
			ContentValues nuevoRegistro = new ContentValues();
			nuevoRegistro.put(camposInfo[0],info[0]);	//Marca del dispositivo
			nuevoRegistro.put(camposInfo[1],info[1]);	//Email del dispositivo
			nuevoRegistro.put(camposInfo[2],info[2]);	//Modelo del dispositivo
			myDataBase.insert("InfoDisp", null, nuevoRegistro);	//Insertando el registro en la base de datos
		}

		public synchronized void close() {
			if (myDataBase != null)
				myDataBase.close();
			super.close();
	    }
		
		public void onCreate(SQLiteDatabase db) {
			
		}


		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			
		}
}
