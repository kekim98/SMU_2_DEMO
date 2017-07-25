package example.com.smu_2_demo;

        import android.content.ComponentName;
        import android.content.Intent;
        import android.content.ServiceConnection;
        import android.content.SharedPreferences;
        import android.os.IBinder;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.ListView;

        import java.util.Map;

public class MainActivity extends AppCompatActivity {
    DataService mService;
    boolean mBound = false;
    public ArrayAdapter<String> listadapter;
    ListView list1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list1 = (ListView) findViewById(R.id.hilist);
        listadapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        list1.setAdapter(listadapter);

        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String name = (String) adapterView.getAdapter().getItem(position);
                SharedPreferences pref = getSharedPreferences("MAIN2",MODE_PRIVATE);
                String code = pref.getString(name,"UNKNOWN");
                Intent intent = new Intent(MainActivity.this, DetailContact.class);
                intent.putExtra("name",name);
                intent.putExtra("code",code);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onStart(){
        super.onStart();
        //Intent gogoservice = new Intent(MainActivity.this,DataService.class);
        //bindService(gogoservice, mConnection, Context.BIND_AUTO_CREATE);
    }

    protected void onResume(){
        listadapter.clear();
        super.onResume();
        listadapter.notifyDataSetChanged();
        Log.d("my","onResume");
        check();
        listadapter.notifyDataSetChanged();
    }

    public void check(){

        final SharedPreferences pref = getSharedPreferences("MAIN2",MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();

        //mService.getkeyM();

        //Log.d("test","value1 :"+value1);


        Map<String, ?> values = pref.getAll();
        for (String key : values.keySet()) {
            Log.d("my", "key:" + key);
            listadapter.add(key);
        }

        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        list1,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {

                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView list1, int[] reverseSortedPositions) {
                                Log.d("deletetest","ondismiss");
                                for(int position : reverseSortedPositions) {
                                    final String hi = listadapter.getItem(position);
                                    listadapter.remove(hi);
                                    editor.remove(hi);
                                    editor.apply();
                                }
                                listadapter.notifyDataSetChanged();
                            }
                        });
        list1.setOnTouchListener(touchListener);
        list1.setOnScrollListener(touchListener.makeScrollListener());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.addmenu,menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.addbtn:
                Intent addcontact = new Intent(this,AddContact.class);
                startActivity(addcontact);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            DataService.LocalBinder binder = (DataService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mBound = false;
        }
    };

    protected void onStop(){
        super.onStop();
        if(mBound){
            unbindService(mConnection);
            mBound=false;
        }
    }
}
