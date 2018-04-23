package techhunt.in.techhunt;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blikoon.qrcodescanner.QrCodeActivity;
import com.github.tbouron.shakedetector.library.ShakeDetector;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class Question extends AppCompatActivity {

    private static final int REQUEST_CODE_QR_SCAN = 101;
    private Button scan,scan1,submit;
    private TextView question,qno;
    private EditText answer;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private ImageView image;
    private Animation fadeIn;
    private ProgressDialog mProgress;
    private String position; // The current question number that a user is on.
    private DatabaseReference mDatabase;
    private int x;


    private String[] questions={
            "At the border of heaven and hell,\n there is someone watching you can you tell?",
            "A red spot in the white sea,\n where souls meet, what a sight to see. \n A spot where the cupids surround, \n It is a tree, where the clue will be found.",
            "Can it be this easy?", // Image
            "I am round and round yet I am a square, \n I have steps that lead to nowhere.",
            "Two hours a week with a bastard and your job is done!", // Shake and textview
            "The ship has been sailed, O sailor. \n Your destination is a pillar which shows your nation's valour \n For that you need to land on a dock which will make you fit\n and see through the right window to get the hit. ",
            "18 5 3   20 9 3 11\n (Hint: Sometimes things can get messed up)",
            "Two roads diverged in a yellow wood,\n" +
                    "And sorry I could not travel both\n" +
                    "And be one traveler, long I stood\n" +
                    "And looked down one as far as I could\n" +
                    "To where it bent in the undergrowth.\n\n -Robert Frost",
            "The secret passage to the center of excellence of NMIMS Shirpur.",
            "The knowledge of science has made us down to earth,\nAnd we have been hearing the story since our birth.\n The tale of the orb, which was a sign from the divine,\n and the knight revealed the truth in time.",
            "It is the SPACE where the two UNIVERSES JAM,\n was he there too, yosemite sam?\nThe creator of shocker!\nIs he himself the shocker?",
            "TSRLNHSGANEEYTEMNRIRAEEOD\n (Hint: Don't worry! Julius CAESAR will guide the way to deCODE)",
            "Be it a LITTLE BOY or a FAT MAN, the states have always been ruthless, \n the people of the place where the sun rises first tell the tales of their mess.",
            "You thought that you killed this monster by breaking it into parts, \n but the pieces of him gave rise to a power which tore the world apart!",
            "Your journey in the last three stages of this saga has told you the harsh truth about man, \n He thinks he can make the world a better place this way,\n but actually, only the dove can."
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        scan = (Button) findViewById(R.id.scan);
        scan1 = (Button) findViewById(R.id.scan1);
        submit = (Button) findViewById(R.id.submit);
        question = (TextView) findViewById(R.id.question);
        pref = getSharedPreferences("pref", MODE_PRIVATE);
        editor = getSharedPreferences("pref", MODE_PRIVATE).edit();
        image = (ImageView) findViewById(R.id.image);
        answer = (EditText) findViewById(R.id.answer);
        qno = (TextView) findViewById(R.id.qno);

        mProgress = new ProgressDialog(this);

        mProgress.setTitle("Loading...Be patient");
        mProgress.setMessage("Please keep your internet ON\nThis can take a while");
        mProgress.setCanceledOnTouchOutside(false);
        fadeIn = new AlphaAnimation((float)0.2 , 1);
        fadeIn.setDuration(1000);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        position = pref.getString("position","1");
        question.setText(questions[Integer.parseInt(position)-1]);
        qno.setText("Level " + position);

        startHints();

        if(position.equals("16")){

            Intent i = new Intent(Question.this,Winner.class);
            startActivity(i);
            finish();

        }


        if(position.equals("3")){

            image.setVisibility(View.VISIBLE);
            scan1.setVisibility(View.VISIBLE);
            scan.setVisibility(View.INVISIBLE);
            submit.setVisibility(View.INVISIBLE);
            answer.setVisibility(View.INVISIBLE);
        }
        else if(position.equals("5")){

            image.setVisibility(View.INVISIBLE);
            scan.setVisibility(View.VISIBLE);
            scan1.setVisibility(View.INVISIBLE);
            submit.setVisibility(View.INVISIBLE);
            answer.setVisibility(View.INVISIBLE);
        }

        else if(position.equals("10")||position.equals("12")||position.equals("13")||position.equals("14")||position.equals("15")){

            answer.setVisibility(View.VISIBLE);
            submit.setVisibility(View.VISIBLE);
            image.setVisibility(View.INVISIBLE);
            scan.setVisibility(View.INVISIBLE);
            scan1.setVisibility(View.INVISIBLE);
        }
        else{
            answer.setVisibility(View.INVISIBLE);
            submit.setVisibility(View.INVISIBLE);
            image.setVisibility(View.INVISIBLE);
            scan.setVisibility(View.VISIBLE);
            scan1.setVisibility(View.INVISIBLE);

        }


        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Question.this,QrCodeActivity.class);
                startActivityForResult( i,REQUEST_CODE_QR_SCAN);

            }
        });

        scan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(Question.this,QrCodeActivity.class);
                startActivityForResult( i,REQUEST_CODE_QR_SCAN);

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View view) {


                if(position.equals("5")&&answer.getText().toString().trim().equalsIgnoreCase("sakhtlaunda")){

                    mProgress.show();
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("5").child(pref.getString("username","m")).setValue("solved").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            mProgress.dismiss();
                            int x = Integer.parseInt(position);
                            x = x + 1;
                            position = String.valueOf(x);
                            question.setText(questions[x-1]);
                            qno.setText("Level " + position);
                            editor.putString("position",position);
                            editor.commit();

                            question.startAnimation(fadeIn);

                            answer.setVisibility(View.INVISIBLE);
                            submit.setVisibility(View.INVISIBLE);
                            scan.setVisibility(View.VISIBLE);
                            image.setVisibility(View.INVISIBLE);
                            scan1.setVisibility(View.INVISIBLE);
                            answer.setText("");

                        }
                    });





                }
                else if(position.equals("10")&&answer.getText().toString().trim().equalsIgnoreCase("Apple")){


                    mProgress.show();
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("10").child(pref.getString("username","m")).setValue("solved").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            mProgress.dismiss();
                            int x = Integer.parseInt(position);
                            x = x + 1;
                            position = String.valueOf(x);
                            question.setText(questions[x-1]);
                            qno.setText("Level " + position);
                            editor.putString("position",position);
                            editor.commit();

                            question.startAnimation(fadeIn);

                            answer.setVisibility(View.INVISIBLE);
                            submit.setVisibility(View.INVISIBLE);
                            image.setVisibility(View.INVISIBLE);
                            scan.setVisibility(View.VISIBLE);
                            scan1.setVisibility(View.INVISIBLE);
                            answer.setText("");

                        }
                    });



                }


                else if(position.equals("12")&&answer.getText().toString().trim().equalsIgnoreCase("themassenergyrelationnerd")){


                    mProgress.show();
                    mDatabase = FirebaseDatabase.getInstance().getReference();

                    mDatabase.child("12").child(pref.getString("username","m")).setValue("solved").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            mProgress.dismiss();
                            int x = Integer.parseInt(position);
                            x = x + 1;
                            position = String.valueOf(x);
                            question.setText(questions[x-1]);
                            qno.setText("Level " + position);
                            editor.putString("position",position);
                            editor.commit();

                            question.startAnimation(fadeIn);

                            answer.setVisibility(View.VISIBLE);
                            submit.setVisibility(View.VISIBLE);
                            image.setVisibility(View.INVISIBLE);
                            scan.setVisibility(View.INVISIBLE);
                            scan1.setVisibility(View.INVISIBLE);
                            answer.setText("");
                            answer.setHint("4 letters");

                        }
                    });



                }

                else if(position.equals("13")&&answer.getText().toString().trim().equalsIgnoreCase("BOMB")){

                    mProgress.show();
                    mDatabase = FirebaseDatabase.getInstance().getReference();

                    mDatabase.child("13").child(pref.getString("username","m")).setValue("solved").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            mProgress.dismiss();
                            int x = Integer.parseInt(position);
                            x = x + 1;
                            position = String.valueOf(x);
                            question.setText(questions[x-1]);
                            qno.setText("Level " + position);
                            editor.putString("position",position);
                            editor.commit();

                            question.startAnimation(fadeIn);

                            answer.setVisibility(View.VISIBLE);
                            submit.setVisibility(View.VISIBLE);
                            image.setVisibility(View.INVISIBLE);
                            scan.setVisibility(View.INVISIBLE);
                            scan1.setVisibility(View.INVISIBLE);
                            answer.setText("");
                            answer.setHint("7 letters");

                        }
                    });



                }

                else if(position.equals("14")&&answer.getText().toString().trim().equalsIgnoreCase("fission")){

                    mProgress.show();
                    mDatabase = FirebaseDatabase.getInstance().getReference();

                    mDatabase.child("14").child(pref.getString("username","m")).setValue("solved").addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            mProgress.dismiss();
                            int x = Integer.parseInt(position);
                            x = x + 1;
                            position = String.valueOf(x);
                            question.setText(questions[x-1]);
                            qno.setText("Level " + position);
                            editor.putString("position",position);
                            editor.commit();

                            question.startAnimation(fadeIn);

                            answer.setVisibility(View.VISIBLE);
                            submit.setVisibility(View.VISIBLE);
                            image.setVisibility(View.INVISIBLE);
                            scan.setVisibility(View.INVISIBLE);
                            scan1.setVisibility(View.INVISIBLE);
                            answer.setText("");
                            answer.setHint("5 letters");


                        }
                    });


                }

                else if(position.equals("15")&&answer.getText().toString().trim().equalsIgnoreCase("peace")){


                    mProgress.show();
                    mDatabase = FirebaseDatabase.getInstance().getReference();
                    final Date currentTime = Calendar.getInstance().getTime();

                    mDatabase.child("15").child(pref.getString("username","m")).setValue(currentTime.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            mProgress.dismiss();

                            editor.putString("time",currentTime.toString());
                            editor.putString("position","16");
                            editor.commit();
                            Intent i = new Intent(Question.this,Winner.class);
                            startActivity(i);
                            finish();

                        }
                    });

                }

                else
                    Toast.makeText(getApplicationContext(),"Invalid Answer!",Toast.LENGTH_SHORT).show();

            }
        });


        ShakeDetector.create(this, new ShakeDetector.OnShakeListener() {

            @Override
            public void OnShake() {

                if(position.equals("5")){

                    answer.setVisibility(View.VISIBLE);
                    submit.setVisibility(View.VISIBLE);
                    answer.setHint("No spaces. 11 characters");
                    scan1.setVisibility(View.INVISIBLE);
                    image.setVisibility(View.INVISIBLE);
                    scan.setVisibility(View.INVISIBLE);

                    answer.startAnimation(fadeIn);
                    submit.startAnimation(fadeIn);

                }

            }
        });



    }

    private void startHints() {

        if(position.equals("10"))
            answer.setHint("5 letters");
        else if(position.equals("12"))
            answer.setHint("No spaces");
        else if(position.equals("13"))
            answer.setHint("4 letters");
        else if(position.equals("14"))
            answer.setHint("7 letters");
        else if(position.equals("15"))
            answer.setHint("5 letters");

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(resultCode != Activity.RESULT_OK)
        {
            if(data==null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.error_decoding_image");
            if( result!=null)
            {
                Toast.makeText(getApplicationContext(),"Scan error!",Toast.LENGTH_SHORT).show();
            }
            return;

        }
        if(requestCode == REQUEST_CODE_QR_SCAN)
        {
            if(data==null)
                return;
            //Getting the passed result
            String result = data.getStringExtra("com.blikoon.qrcodescanner.got_qr_scan_relult");
            showQuestion(result);

        }
    }

    private void showQuestion(String result) {

        x = Integer.parseInt(position);


        if(result.equals("techhunt" + position)) {

            mProgress.show();
            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child(position).child(pref.getString("username","m")).setValue("solved").addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {

                    mProgress.dismiss();
                    x = x + 1;
                    position = String.valueOf(x);
                    question.setText(questions[x-1]);
                    question.startAnimation(fadeIn);
                    qno.setText("Level " + position);
                    editor.putString("position",position);
                    editor.commit();


                    if(position.equals("3")){
                        image.setVisibility(View.VISIBLE);
                        image.setImageResource(R.drawable.que3);
                        scan1.setVisibility(View.VISIBLE);
                        scan.setVisibility(View.INVISIBLE);
                    }

                    else if(position.equals("10")){

                        scan.setVisibility(View.INVISIBLE);
                        scan1.setVisibility(View.INVISIBLE);
                        answer.setVisibility(View.VISIBLE);
                        submit.setVisibility(View.VISIBLE);
                        answer.setHint("5 letters");
                    }

                    else if(position.equals("12")){

                        scan.setVisibility(View.INVISIBLE);
                        scan1.setVisibility(View.INVISIBLE);
                        answer.setVisibility(View.VISIBLE);
                        submit.setVisibility(View.VISIBLE);
                        answer.setHint("No spaces");
                    }

                    else{

                        image.setVisibility(View.INVISIBLE);
                        scan1.setVisibility(View.INVISIBLE);
                        scan.setVisibility(View.VISIBLE);
                    }


                }
            });





        }

        else if(position.equals("5"))
            Toast.makeText(getApplicationContext(),"Think out of the box!",Toast.LENGTH_LONG).show();

        else
            Toast.makeText(getApplicationContext(),"Invalid QR code!",Toast.LENGTH_SHORT).show();


    }


    @Override
    protected void onResume() {
        super.onResume();
        ShakeDetector.start();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ShakeDetector.stop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ShakeDetector.destroy();
    }

}
