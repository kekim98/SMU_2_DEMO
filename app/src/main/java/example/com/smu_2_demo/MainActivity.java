package example.com.smu_2_demo;

        import android.app.NotificationManager;
        import android.content.ComponentName;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.ServiceConnection;
        import android.content.SharedPreferences;
        import android.os.IBinder;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.Menu;
        import android.view.MenuInflater;
        import android.view.MenuItem;
        import android.view.View;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.EditText;
        import android.widget.ListView;
        import android.widget.Toast;

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
                String name = (String)adapterView.getAdapter().getItem(position);
                SharedPreferences pref = getSharedPreferences("MAIN2",MODE_PRIVATE);
                String code = pref.getString(name,"UNKNOWN");
                Intent intent = new Intent(MainActivity.this, DetailContact.class);
                intent.putExtra("code",code);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart(){
        super.onStart();

    }

    protected void onResume(){
        listadapter.clear();
        super.onResume();
        listadapter.notifyDataSetChanged();
        Log.d("my","onResume");
        check();
        delete();
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

    }

    public void delete(){

        final SharedPreferences pref = getSharedPreferences("MAIN2",MODE_PRIVATE);
        final SharedPreferences.Editor editor = pref.edit();

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
                                for(final int position : reverseSortedPositions) {
                                    final String hi = listadapter.getItem(position);

                                    AlertDialog.Builder ad = new AlertDialog.Builder(MainActivity.this);
                                    ad.setTitle("삭제");
                                    ad.setMessage("삭제하겠습니까?");

                                    ad.setPositiveButton("삭제", new DialogInterface.OnClickListener() {

                                        public void onClick(final DialogInterface dialog, int which) {

                                            AlertDialog.Builder ad2 = new AlertDialog.Builder(MainActivity.this);
                                            ad2.setTitle("정말로 삭제하시겠습니까?");
                                            ad2.setMessage("이름과 똑같이 적어주십시오");

                                            final EditText et = new EditText(MainActivity.this);
                                            ad2.setView(et);

                                            ad2.setPositiveButton("삭제", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    String value = et.getText().toString();
                                                    if(hi.equals(value)){
                                                        listadapter.remove(hi);
                                                        editor.remove(hi);
                                                        editor.apply();
                                                        dialog.dismiss();
                                                    }
                                                    else{
                                                        Toast.makeText(getApplicationContext(),"잘못입력했습니다. 취소되었습니다.",Toast.LENGTH_SHORT).show();}
                                                        dialog.dismiss();
                                                }
                                            });

                                            ad2.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                }
                                            });
                                            ad2.show();
                                        }
                                    });
                                    ad.setNegativeButton("취소", new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int i) {
                                            dialog.dismiss();
                                        }

                                    });
                                    ad.show();
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
