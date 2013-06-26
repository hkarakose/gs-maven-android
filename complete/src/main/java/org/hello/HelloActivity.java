package org.hello;

import org.joda.time.LocalTime;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class HelloActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        TextView textView = (TextView) findViewById(R.id.text_view);

        LocalTime currentTime = new LocalTime();
        textView.setText("The current local time is: " + currentTime);
    }

}
