package example.com.smu_2_demo;

        import android.content.Intent;
        import android.content.SharedPreferences;
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
    private ArrayAdapter<String> listadapter;
    public String code2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView list1 = (ListView) findViewById(R.id.mainlist);
        listadapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1);
        list1.setAdapter(listadapter);
        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                String name = (String) adapterView.getAdapter().getItem(position);
                SharedPreferences pref = getSharedPreferences("MAIN",MODE_PRIVATE);
                String code = pref.getString(name,"UNKNOWN");
                Log.d("test","item"+name);
                Log.d("test","code"+code);

                Intent intent = new Intent(MainActivity.this, DetailContact.class);
                intent.putExtra("name",name);
                intent.putExtra("code",code);
                startActivity(intent);

            }

        });
    }

    protected void onResume(){
        super.onResume();
        Log.d("my","onResume");
        refresh();
    }

    private void refresh() {
        Log.d("my","refresh");
        SharedPreferences pref = getSharedPreferences("MAIN",MODE_PRIVATE);

        listadapter.clear();
        Map<String, ?> values = pref.getAll();
        for(String key: values.keySet()){
            Log.d("my","key:"+key);
            listadapter.add(key);
        }
        listadapter.notifyDataSetChanged();
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
}
