package olegkuro.learnbyear;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

/**
 * activity for searching, adding and editing grammar rules
 */

public class RulesActivity extends BaseActivity {
    static class Rule {
        public String name;
        public String explanation;
        List<Integer> selections;
    }
    private RecyclerView recyclerRules;
    private CommonAdapter<Rule> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rules);
        recyclerRules = (RecyclerView) findViewById(R.id.recycler_rules);
        adapter = new RuleAdapter(this);
        adapter.setListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        boolean res = super.onCreateOptionsMenu(menu);
        menu.add(0, 0, 0, "Add").setIcon(R.drawable.ic_add_circle_outline_white_24dp)
                .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_ALWAYS)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        return false;
                    }
                });
        return super.onCreateOptionsMenu(menu);
    }

}
