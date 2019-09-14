package com.example.screentouch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConstraintLayout constraintLayout = findViewById(R.id.cl);
        tv = findViewById(R.id.textView);
        constraintLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    Toast.makeText(MainActivity.this,"палец опустили",Toast.LENGTH_SHORT).show();
                }
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    Toast.makeText(MainActivity.this,"палец подняли",Toast.LENGTH_SHORT).show();
                }
                String s = "pointers count:"+motionEvent.getPointerCount();
                for(int i=0; i < motionEvent.getPointerCount(); ++i){
                    s+="\n added i="+i+" id=" + motionEvent.getPointerId(i)+" p="+motionEvent.getPressure(i)
                            + " area=" + motionEvent.getSize(i)
                            +"(x,y)="+motionEvent.getX(i)+", "+motionEvent.getY(i);
                }
                tv.setText(s);
                return true;
            }
        });
    }
}
