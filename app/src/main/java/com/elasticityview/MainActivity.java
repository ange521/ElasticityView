package com.elasticityview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;

public class MainActivity extends AppCompatActivity {
    private MyListView mMylistView;
    private String[] data = new String[30];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_text);

        for (int i =0;i<30;i++){
            data[i] = ""+i;
        }

        mMylistView = (MyListView) findViewById(R.id.myListView);
        mMylistView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                data));
    }
}
