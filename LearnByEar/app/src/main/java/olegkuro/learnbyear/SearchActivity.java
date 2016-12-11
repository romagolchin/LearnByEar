package olegkuro.learnbyear;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Елена on 07.12.2016.
 */

public class SearchActivity extends BaseActivity {

    private ListView listView;
    ArrayList<String> data;
    Button upButton;
    Button searchButton;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.searchlist);
        listView = (ListView) findViewById(R.id.lv);
        searchButton = (Button) findViewById(R.id.start_search);
        searchButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                editText = (EditText) findViewById(R.id.request);
                String request = editText.getText().toString();
                data = doRequest(request);
                fillList();
            }
        });

        //simply turns back to the first item
        upButton = (Button) findViewById(R.id.button_up);
        upButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                listView.setSelection(0);
            }
        });

        //possibly crashes
        //click item list listener
        listView.setOnClickListener((View.OnClickListener) new DrawerItemClickListener());
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList("data", data);
    }

    // songs - songlist
    //при каждом заполнении все данные сохраняются в data, чтобы при повороте экрана все восстановилось
    protected void fillList(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.my_list_item, data);
        listView.setAdapter(adapter);
    }

    private ArrayList<String> doRequest(String inp){
        ArrayList<String> ret = null;
        //ret = ...
        return ret;
    }


    private class DrawerItemClickListener implements ListView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id){
            selectItem(position);
        }
        //TODO each item should open new SongActivity
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
