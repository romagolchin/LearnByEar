package olegkuro.learnbyear;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Елена on 07.12.2016.
 */

public class SearchActivity extends Activity {

    private ListView listView;
    ArrayList<String> data;
    Button upButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchlist);
        listView = (ListView) findViewById(R.id.lv);
        upButton = (Button) findViewById(R.id.button_up);
        upButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                listView.setSelection(0);
            }
        });
        //TODO: fillList(String[]);

        //possibly crashes
        listView.setOnClickListener((View.OnClickListener) new DrawerItemClickListener());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("data", data);
    }

    // songs - songlist
    //при каждом заполнении все данные сохраняются в data, чтобы при повороте экрана все восстановилось
    protected void fillList(String[] songs){
        data.clear();
        for (String song : songs){
            data.add(song);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.my_list_item, data);
        listView.setAdapter(adapter);
    }



    private class DrawerItemClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            selectItem(position);
        }
        //TODO add events showup SongActivity
        private void selectItem(int position){
            switch (position) {
                case 0: //0 item clicked
                    break;
                case 1:
                    break;
                default:
                    break;
            }

        }

    }


}
