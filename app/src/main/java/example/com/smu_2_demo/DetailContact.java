package example.com.smu_2_demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class DetailContact extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_contact);
        TextView nameText = (TextView) findViewById(R.id.nametext);
        TextView codeText = (TextView) findViewById(R.id.codetext);

        Intent receiveintent = getIntent();
        Bundle extras = receiveintent.getExtras();
        String name = extras.getString("name","UNKNOWN");
        String code = extras.getString("code", "UNKNOWN");
        Toast.makeText(getApplicationContext(),"이름: "+name+"학번: "+code+" Open",Toast.LENGTH_SHORT).show();
        nameText.setText(name);
        codeText.setText(code);
    }
    public void onClick(View view){
        Toast.makeText(getApplicationContext(),"돌아가기",Toast.LENGTH_SHORT).show();
        finish();
    }
}
