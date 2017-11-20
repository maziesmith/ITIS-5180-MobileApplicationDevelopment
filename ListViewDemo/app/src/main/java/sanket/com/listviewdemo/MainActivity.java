package sanket.com.listviewdemo;

import sanket.com.listviewdemo.Color;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //TODO:Example1 native array
    /*String[] colors={"Red", "Blue", "Green", "White", "Black", "Orange", "Yellow","Red", "Blue", "Green", "White", "Black", "Orange", "Yellow"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ListView listView = (ListView) findViewById(R.id.listView1);
        ArrayAdapter<String> arrayAdapter= new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, colors);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "Postion "+ position+" Value "+colors[position], Toast.LENGTH_SHORT).show();
            }
        });

    }*/

    //TODO:Example2 Collection

/*
    ArrayList<Color> colors;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        colors = new ArrayList<Color>();
        colors.add(new Color("Red"));
        colors.add(new Color("white"));
        colors.add(new Color("blue"));
        colors.add(new Color("green"));
        colors.add(new Color("yellow"));
        colors.add(new Color("grey"));
        colors.add(new Color("black"));
        colors.add(new Color("cyan"));
        colors.add(new Color("magenda"));
        colors.add(new Color("skyblue"));
        colors.add(new Color("jetblack"));



        ListView listView = (ListView) findViewById(R.id.listView1);
        ArrayAdapter<Color> arrayAdapter= new ArrayAdapter<Color>(this,android.R.layout.simple_list_item_1, colors);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "Postion "+ position+" Value "+colors.get(position), Toast.LENGTH_SHORT).show();
            }
        });

    }
*/

    //TODO:Example 3 Updating the data

    /*ArrayList<Color> colors;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        colors = new ArrayList<Color>();
        colors.add(new Color("Red"));
        colors.add(new Color("white"));
        colors.add(new Color("blue"));
        colors.add(new Color("green"));
        colors.add(new Color("yellow"));
        colors.add(new Color("grey"));
        colors.add(new Color("black"));
        colors.add(new Color("cyan"));
        colors.add(new Color("magenda"));
        colors.add(new Color("skyblue"));
        colors.add(new Color("jetblack"));
        colors.add(new Color("gold"));
        colors.add(new Color("silver"));



        ListView listView = (ListView) findViewById(R.id.listView1);
        ArrayAdapter<Color> arrayAdapter= new ArrayAdapter<Color>(this,android.R.layout.simple_list_item_1, colors);
        listView.setAdapter(arrayAdapter);


        arrayAdapter.add(new Color("Purple"));
        arrayAdapter.setNotifyOnChange(true); *//*option arrayAdapter.notifyDataSetChanged();*//*
        arrayAdapter.remove(colors.get(0));
        arrayAdapter.insert(new Color("Brown"),0);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "Postion "+ position+" Value "+colors.get(position), Toast.LENGTH_SHORT).show();
            }
        });
        }
*/

    //TODO: Extending ArrayAdapter

    ArrayList<Color> colors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        colors = new ArrayList<Color>();
        colors.add(new Color("WHITE","#FFFFFF"));
        colors.add(new Color("BLACK","#000000"));
        colors.add(new Color("RED","#FF0000"));
        colors.add(new Color("YELLOW","#FFFF00"));
        colors.add(new Color("GREEN","#008000"));
        colors.add(new Color("BLUE","#0000FF"));
        colors.add(new Color("PURPLE","#800080"));


        ListView listView = (ListView) findViewById(R.id.listView1);
        ColorAdapter adapter = new ColorAdapter(this, R.layout.row_item_layout, colors);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(MainActivity.this, "Postion " + position + " Value " + colors.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
