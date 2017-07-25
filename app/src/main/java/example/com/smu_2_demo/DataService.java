package example.com.smu_2_demo;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class DataService extends Service {
    // Binder given to clients
    private IBinder mBinder = new LocalBinder();
    private static String name1 = null;
    private static String code1 = null;
    SharedPreferences pref = getSharedPreferences("MAIN2", MODE_PRIVATE);
    /**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */

    public class LocalBinder extends Binder {
        public DataService getService() {
            // Return this instance of LocalService so clients can call public methods
            Log.d("test","work?");
            return DataService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        String name = intent.getStringExtra("name");
        String code = intent.getStringExtra("code");
        name1 = name;
        code1 = code;
        pref.edit().putString(name, code).apply();
        Log.d("test","name:"+name+"code:"+code);

        return mBinder;
    }

    public void deletekey(){

        SharedPreferences.Editor editor = pref.edit();
        editor.remove(name1);
        editor.apply();
    }

    public void getkeyM(){
        Log.d("test","return name:"+name1);
        Intent sendkey = new Intent(DataService.this,MainActivity.class);
        sendkey.putExtra("name",name1);
        sendkey.putExtra("code",code1);
    }
}
