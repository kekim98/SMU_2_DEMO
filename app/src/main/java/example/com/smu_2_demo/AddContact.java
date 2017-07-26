package example.com.smu_2_demo;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;
import java.util.regex.Pattern;

public class AddContact extends AppCompatActivity {
    final int REQ_CODE_SELECT_IMAGE=100;
    static String code1 = null;
    static String name1 = null;
    DataService mService;
    boolean mBound = false;
    public ArrayAdapter<String> listadapter;
    static String codes[] = {"0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0","0"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        final EditText nameinput = (EditText) findViewById(R.id.nameet);
        nameinput.setFilters(new InputFilter[]{filterKorAlpha});

        final EditText codeinput = (EditText) findViewById(R.id.CODETEXT3);
        Button saveButton = (Button) findViewById(R.id.button3);

        saveButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if (TextUtils.isEmpty(nameinput.getText()) && (TextUtils.isEmpty(codeinput.getText()))) {
                    Toast.makeText(getApplicationContext(), "이름과 학번 모두 입력되지 않았습니다. 이름과 학번을 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(nameinput.getText())) {
                    Toast.makeText(getApplicationContext(), "이름이 입력되지 않았습니다. 이름을 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(codeinput.getText())) {
                    Toast.makeText(getApplicationContext(), "학번이 입력되지 않았습니다. 학번을 입력하세요", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    final String name = nameinput.getText().toString();
                    final String code = codeinput.getText().toString();
                    int i = 0;
                    int j = 0;
                    final SharedPreferences pref = getSharedPreferences("MAIN2", MODE_PRIVATE);
                    final SharedPreferences.Editor editor = pref.edit();

                    Map<String, ?> values = pref.getAll();
                    for (String key : values.keySet()) {
                            codes[i] = key; // name키값을 codes배열에 저장함
                            Log.d("key", codes[i]);
                            i+=1;
                    }

                    for(String values1 : codes){
                        String name1 = pref.getString(values1,"failed"); // name1에 key에 대한 값들을 가져
                        Log.d("key1",name1);
                        if(code.equals(name1)){
                            j++;
                        }
                    }

                    Log.d("value",""+j);

                    if(j==0) {

                        pref.edit().putString(name, code).apply();

                        Toast.makeText(getApplicationContext(), "저장완료", Toast.LENGTH_SHORT).show();

                        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                        Intent intent = new Intent(AddContact.this, DetailContact.class);
                        intent.putExtra("name", name);
                        intent.putExtra("code", code);

                        PendingIntent pendingIntent = PendingIntent.getActivity(AddContact.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                        Notification.Builder mBuilder = new Notification.Builder(AddContact.this);
                        mBuilder.setSmallIcon(R.drawable.contact3);
                        mBuilder.setTicker("주소록에 " + name + "이(가) 추가되었습니다.");
                        mBuilder.setWhen(System.currentTimeMillis());
                        mBuilder.setContentTitle("주소록 추가");
                        mBuilder.setContentText("이름 : " + name);
                        mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
                        mBuilder.setContentIntent(pendingIntent);
                        mBuilder.setAutoCancel(true);

                        nm.notify(111, mBuilder.build());
                        finish();

                    }
                    else{

                        AlertDialog.Builder ad = new AlertDialog.Builder(AddContact.this);
                        ad.setTitle("경고문");
                        ad.setMessage("중복된 학번입니다. 추가하시겠습니까?");

                        ad.setPositiveButton("추가", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                final SharedPreferences pref = getSharedPreferences("MAIN2", MODE_PRIVATE);
                                final SharedPreferences.Editor editor = pref.edit();
                                pref.edit().putString(name, code).apply();

                                Toast.makeText(getApplicationContext(), "중복된 학번 저장완료", Toast.LENGTH_SHORT).show();

                                NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                Intent intent = new Intent(AddContact.this, DetailContact.class);
                                intent.putExtra("name", name);
                                intent.putExtra("code", code);

                                PendingIntent pendingIntent = PendingIntent.getActivity(AddContact.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                                Notification.Builder mBuilder = new Notification.Builder(AddContact.this);
                                mBuilder.setSmallIcon(R.drawable.contact3);
                                mBuilder.setTicker("주소록에 " + name + "이(가) 추가되었습니다.");
                                mBuilder.setWhen(System.currentTimeMillis());
                                mBuilder.setContentTitle("주소록 추가");
                                mBuilder.setContentText("이름 : " + name);
                                mBuilder.setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
                                mBuilder.setContentIntent(pendingIntent);
                                mBuilder.setAutoCancel(true);

                                nm.notify(111, mBuilder.build());

                                dialog.dismiss();
                                finish();
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
                }
            }
        });

    }

    public void onClick(View view){
        Toast.makeText(getApplicationContext(),"저장취소",Toast.LENGTH_SHORT).show();
        if(mBound){
            unbindService(mConnection);
            mBound=false;
        }
        finish();
    }

    public void clearname(View view){
        TextView namein = (TextView) findViewById(R.id.nameet);
        if(TextUtils.isEmpty(namein.getText())) {
            Toast.makeText(getApplicationContext(),"이름을 작성하지 않아 지울 수 없습니다.",Toast.LENGTH_SHORT).show();
        }
        else{
            namein.setText(null);
        }
    }
    public void clearcode(View view){
        TextView codein = (TextView) findViewById(R.id.CODETEXT3);
        if(TextUtils.isEmpty(codein.getText())) {
            Toast.makeText(getApplicationContext(),"학번을 작성하지 않아 지울 수 없습니다.",Toast.LENGTH_SHORT).show();
        }
        else {
            codein.setText(null);
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            DataService.LocalBinder binder = (DataService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
    public InputFilter filterKorAlpha = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            Pattern ps = Pattern.compile("^[ㄱ-ㅎ가-흐 A-Za-z]+$");
            if (!ps.matcher(source).matches()) {
                return "";
            }
            return null;
        }
    };

}
