package techhunt.in.techhunt;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    private EditText Ename,Epassword;
    private String name,password,position;
    private Button login;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private static final int MY_CAMERA_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(Login.this,new String[]{android.Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        }

        Ename = (EditText) findViewById(R.id.name);
        Epassword = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.loginBtn);
        pref = getSharedPreferences("pref", MODE_PRIVATE);
        editor = getSharedPreferences("pref", MODE_PRIVATE).edit();

        if(!pref.getString("username","none").equals("none")){

            Intent i;
            if(pref.getString("position","1").equals("16"))
                i=new Intent(Login.this,Winner.class);
            else
                i=new Intent(Login.this,Question.class);

            startActivity(i);
            finish();
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name=Ename.getText().toString().trim();
                password=Epassword.getText().toString().trim();

                position = pref.getString("position","1");
                editor.putString("username",name);
                editor.commit();

                if(position.equals("16")){

                    Intent i = new Intent(Login.this,Winner.class);
                    startActivity(i);
                    finish();

                }

                else if(password.equals("compiler")){

                    Ename.setText("");
                    Epassword.setText("");
                    Intent i=new Intent(Login.this,Question.class);
                    startActivity(i);
                    finish();
                }
                else
                    Toast.makeText(getApplicationContext(),"Incorrect Password",Toast.LENGTH_SHORT).show();

            }
        });

    }
}
