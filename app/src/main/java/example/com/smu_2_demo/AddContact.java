package example.com.smu_2_demo;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class AddContact extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        final TextView nameinput = (TextView) findViewById(R.id.nameet);
        final TextView codeinput = (TextView) findViewById(R.id.CODETEXT3);
        Button saveButton = (Button) findViewById(R.id.button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameinput.getText().toString();
                String code = codeinput.getText().toString();

                SharedPreferences pref = getSharedPreferences("MAIN",MODE_PRIVATE);
                pref.edit().putString(name, code).apply();
                finish();
                Log.d("my","ㅗ");
            }

        });
    }

}

 /*nameInput.setOnClickListener(new View.OnClickListener(){
            @Override
                    public void onClick(View view){
                Log.d("my","nameinput");
            }
        });

        codeInput.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                Log.d("my","codeinput");
            }
        });*/
