package com.example.geolocalizacion;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {

	/**
	 * Declarando Variables
	 */
	String[] datosMovil=new String[3];
	//private DBHelper BD; //Objeto que controla la base de datos
	TextView coordenadas; //Mostrara las coordenadas
	Button iniciarService; //Boton que iniciara el servicio de localizacion
	Button detenerService; //Boton que detendra el servicio de localizacion
	private LocationManager mLocationManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		BD = new DBHelper(this);
//		/**
//		 * Creando y abriendo la base de datos
//		 */
//		try{
//			BD.createDataBase(); //creando la base de datos
//			BD.openDataBase(); //Abriendo la base de datos
//		} 
//		catch(IOException e){
//			e.printStackTrace();
//		}
//		/**
//		 * Obteniendo datos del dispositivo
//		 */
//		datosMovil[0] = Build.MANUFACTURER; //Obteniendo marca de dispositivo
//		datosMovil[1] = getEmail(getBaseContext());	//Obteniendo el email del dispositivo
//		datosMovil[2] = Build.MODEL;	//Obteniendo Modelo de dispositivo
//		datosMovil[2] = cargaBateria()+" %"; //Obteniendo el nivel de carga del dispositivo
//		
//		/**
//		 * Mandando a base de datos los dato del movil
//		 */
//		BD.insertInfoDisp(datosMovil);
		
		setContentView(R.layout.activity_main);
		System.out.println(Build.VERSION.RELEASE); //Obtiene Version de Android del dispositivo
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		coordenadas = (TextView) findViewById(R.id.Coordenadas);
		iniciarService = (Button) findViewById(R.id.Iniciar);
		iniciarService.setOnClickListener(new View.OnClickListener() {
		public void onClick(View view) {
			if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
				showDialogGPS("GPS apagado", "Deseas activarlo?");
			}else{
				  /**
				   * Se inicia el servicio de geolocalizacion
				   */
				//Geolocalizarme.mainActivity = MainActivity.this;
				startService(new Intent(MainActivity.this,Geolocalizarme.class));
				bloquearBoton(true);
				}
			}
		});
		detenerService = (Button) findViewById(R.id.Detener);
		detenerService.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				/**
				 * Se detiene el servicio de geolocalizacion
				 */
				bloquearBoton(false);
				stopService(new Intent(MainActivity.this,Geolocalizarme.class));
				coordenadas.setText(getString(R.string.esperando));//regresamos a texto default

			}
		});
	}
//	
//	/**
//	 * Metodo que obtiene la cuenta de google del dispositivo
//	 * @param accountManager
//	 * @return
//	 */
//    private static Account getAccount(AccountManager accountManager) {
//        Account[] accounts = accountManager.getAccountsByType("com.google");
//        Account account;
//        if (accounts.length > 0) {
//          account = accounts[0];      
//        } else {
//          account = null;
//        }
//        return account;
//      }
//    
//    /**
//     * Metodo para obtener el email del dipositivo
//     * @param context
//     * @return
//     */
//    public static String getEmail(Context context) {
//        AccountManager accountManager = AccountManager.get(context); 
//        Account account = getAccount(accountManager);
//
//        if (account == null) {
//          return "No hay cuentas.";
//        } else {
//          return account.name;
//        }
//    }
    
//    /**
//     * metodo para obtener el nivel actual de la bateria
//     * @return
//     */
//    public int cargaBateria() 
//    { 
//    	try { 
//    		IntentFilter batIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED); 
//    		Intent battery = this.registerReceiver(null, batIntentFilter); 
//    		int nivelBateria = battery.getIntExtra(BatteryManager.EXTRA_LEVEL, -1); 
//    		return nivelBateria; 
//    	} 
//    	catch (Exception e) {  
//    		return 0; 
//    	}
//    } 

	protected void onPause() {
		unregisterReceiver(onBroadcast);
		super.onPause();
		//BD.close(); //Cierra la base de datos
	}

	@Override
	protected void onResume() {
		registerReceiver(onBroadcast, new IntentFilter("key"));
		super.onResume();
		//BD.openDataBase();	//Abre la base de datos
	}

	
	/**
	 * manejo de transmiciones
	 */
	private BroadcastReceiver onBroadcast = new BroadcastReceiver() {
		@Override
		public void onReceive(Context ctxt, Intent i) {
			
				/**
				 * blanqueamos el texto de las coordenadas si esta el texto default
				 */
				if (coordenadas.getText().equals(getString(R.string.esperando))) {
					coordenadas.setText("");
				}
				
				String datos = i.getStringExtra("coordenadas");//obtenemos las coordenadas envidas del servicioGeolocalizaci—n
				String[] tokens = datos.split(";");//separamos por tocken
				coordenadas.append("latitud: " + tokens[0]+ " longitud: " + tokens[1]);
				coordenadas.append("\n");//agregamos salto de linea
				//BD.insertarMov(tokens[0], tokens[1], "GPS", cargaBateria()+" %");
	
				/*JSONObject cadena = new JSONObject(); // Creamos un objeto de tipo JSON
				try {
					// Le asignamos los datos que necesitemos
					cadena.put("lat", tokens[0]); //latitud
					cadena.put("lng", tokens[1]);//longitud

				} catch (JSONException e) {
					e.printStackTrace();
				}

				//generamos la conexi—n con el servidor y mandamos las coordenads
//				socket.emit("locmsg", cadena);*/
				
		}
	};

	
	/**
	 * Metodo que bloquea los botones
	 * @param b
	 */
	public void bloquearBoton(boolean b){
		if(b==true){
			iniciarService.setEnabled(false);
			detenerService.setEnabled(true);
		}else{
			iniciarService.setEnabled(true);
			detenerService.setEnabled(false);
		}
		
	}
	/**
	 * Muestra el dialogo en caso de que el GPS este apagado
	 * 
	 * @param titulo Titulo del dialogo
	 * @param message Mensaje del dialogo
	 */
	public void showDialogGPS(String title, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(title);
        builder.setMessage(message);
		builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Intent settingsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
				settingsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
				startActivity(settingsIntent);
			}
		});
		builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				dialog.cancel();
			}
		});
		
		AlertDialog dialog = builder.create();
		dialog.show();
		dialog.setCancelable(false);
		dialog.setCanceledOnTouchOutside(false);
	}
}