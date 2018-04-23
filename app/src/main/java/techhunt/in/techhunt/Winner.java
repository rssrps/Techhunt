package techhunt.in.techhunt;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class Winner extends AppCompatActivity {

    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private TextView username,time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_winner);

        username = (TextView) findViewById(R.id.username);
        time = (TextView) findViewById(R.id.time);
        pref = getSharedPreferences("pref", MODE_PRIVATE);
        editor = getSharedPreferences("pref", MODE_PRIVATE).edit();

        username.setText(pref.getString("username","username"));
        time.setText(pref.getString("time","time"));

    }
}
