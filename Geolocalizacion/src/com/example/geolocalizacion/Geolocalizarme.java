package com.example.geolocalizacion;

import java.io.IOException;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

public class Geolocalizarme extends Service implements Runnable{
	/**
	 * Declaracion de variables
	 */
	//protected static MainActivity mainActivity;
	String[] datosMovil=new String[3];
	private DBHelper BD; //Objeto que controla la base de datos
	private LocationManager mLocationManager;
	private MyLocationListener mLocationListener;
	private Location currentLocation = null;
	private Thread thread;
	
    @Override
    public void onCreate() {
    	Toast.makeText(this,"Servicio creado", Toast.LENGTH_SHORT).show();
        super.onCreate();
        BD = new DBHelper(this);
        try{
  			BD.createDataBase(); //creando la base de datos
  			BD.openDataBase(); //Abriendo la base de datos
  		} 
  		catch(IOException e){
  			e.printStackTrace();
  		}
		/**
		 * Obteniendo datos del dispositivo
		 */
		datosMovil[0] = Build.MANUFACTURER; //Obteniendo marca de dispositivo
		datosMovil[1] = getEmail(getBaseContext());	//Obteniendo el email del dispositivo
		datosMovil[2] = Build.MODEL;	//Obteniendo Modelo de dispositivo
		/**
		 * Mandando a base de datos los dato del movil
		 */
		BD.insertInfoDisp(datosMovil);
        
      	mLocationListener = new MyLocationListener();
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intenc, int flags, int idArranque) {
         // Toast.makeText(this,"Servicio arrancado "+ idArranque,Toast.LENGTH_SHORT).show();  
          obtenerSenalGPS();
          return START_STICKY;
    }

	@Override
    public void onDestroy() {
		if (mLocationManager != null)
			if (mLocationListener != null)
				mLocationManager.removeUpdates(mLocationListener);
		
        Toast.makeText(this,"Servicio detenido ",Toast.LENGTH_SHORT).show();
    	    super.onDestroy();
       
    }

    @Override
    public IBinder onBind(Intent intencion) {
          return null;
    }
    
    /**
     * handler
     */
    private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// mLocationManager.removeUpdates(mLocationListener);
			updateLocation(currentLocation);
		}
	};
	
	/**
	 * Metodo que obtiene la cuenta de google del dispositivo
	 * @param accountManager
	 * @return
	 */
    private static Account getAccount(AccountManager accountManager) {
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0) {
          account = accounts[0];      
        } else {
          account = null;
        }
        return account;
      }
    
    /**
     * Metodo para obtener el email del dipositivo
     * @param context
     * @return
     */
    public static String getEmail(Context context) {
        AccountManager accountManager = AccountManager.get(context); 
        Account account = getAccount(accountManager);

        if (account == null) {
          return "No hay cuentas.";
        } else {
          return account.name;
        }
    }

	/**
	 * metodo para actualizar la localizacion
	 * 
	 * @param currentLocation
	 * @return void
	 */
	public void updateLocation(Location currentLocation) {
		if (currentLocation != null) {
			double latitud = Double.parseDouble(currentLocation.getLatitude() + ""); //Obtiene la latitud
			double longitud = Double.parseDouble(currentLocation.getLongitude() + ""); //Obtiene la longitud

//			Location locationA = new Location("punto A");
//			locationA.setLatitude(latitud_inicial);
//			locationA.setLongitude(longitud_inicial);
//
//			Location locationB = new Location("punto B");
//			locationB.setLatitude(latitud);
//			locationB.setLongitude(longitud);
//
//			float distance = locationA.distanceTo(locationB);
//			latitud_inicial = latitud;
//			longitud_inicial = longitud;
//
//			distancia_acumulada += distance;
			BD.insertarMov(latitud+"", longitud+"", "GPS", cargaBateria()+" %");
			getApplicationContext().sendBroadcast(
					new Intent("key").putExtra("coordenadas", latitud + ";"
							+ longitud + ";"));
		}
	}
	
    /**
     * metodo para obtener el nivel actual de la bateria
     * @return
     */
    public int cargaBateria() 
    { 
    	try { 
    		IntentFilter batIntentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED); 
    		Intent battery = this.registerReceiver(null, batIntentFilter); 
    		int nivelBateria = battery.getIntExtra(BatteryManager.EXTRA_LEVEL, -1); 
    		return nivelBateria; 
    	} 
    	catch (Exception e) {  
    		return 0; 
    	}
    } 
	
	/**
	 * Hilo de la aplicacion para cargar las cordenadas del usuario
	 */
	public void run() {
		if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Looper.prepare();
			mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 300000, 0, mLocationListener);
			Looper.loop();
			Looper.myLooper().quit();
		} /*else {
			mainActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(getApplicationContext(), "GPS apagado inesperadamente", Toast.LENGTH_LONG).show();			
				}
			});
		}*/
	}
	
	/**
	 * Metodo para Obtener la señal del GPS
	 */
	private void obtenerSenalGPS() {
		thread = new Thread(this);
		thread.start();
	}

	/**
	 * Metodo para asignar las cordenadas del usuario
	 * */
	private void setCurrentLocation(Location loc) {
		currentLocation = loc;
	}

	/**
	 * Metodo para obtener las cordenadas del GPS
	 */
	private class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location loc) {
			//Log.d("finura",loc.getAccuracy()+"");
			if (loc != null) {
				setCurrentLocation(loc);
				handler.sendEmptyMessage(0);
			}
		}

		/**
		 * metodo que revisa si el GPS esta apagado
		 */
		public void onProviderDisabled(String provider) {
//			mainActivity.runOnUiThread(new Runnable() {
//				@Override
//				public void run() {
//					Toast.makeText(getApplicationContext(), "GPS apagado inesperadamente", Toast.LENGTH_LONG).show();			
//				}
//			});
		}

		// @Override
		public void onProviderEnabled(String provider) {
		}

		// @Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}
}
