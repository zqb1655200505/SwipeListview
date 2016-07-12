package com.zqb.swipelistview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SwipeListview listview;
    private ArrayList<String>list=new ArrayList<String>(){
        {
            for(int i=0;i<50;i++)
            {
                add("Hello world, Hello android "+ i);
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listview= (SwipeListview) findViewById(R.id.list);
        listview.setAdapter(new ListviewAdapter(MainActivity.this,list,listview));
//        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                if(listview.canClick())
//                {
//                    Toast.makeText(MainActivity.this, list.get(position), Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
    }
}
