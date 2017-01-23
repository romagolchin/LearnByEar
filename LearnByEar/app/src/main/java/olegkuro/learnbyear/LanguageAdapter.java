package olegkuro.learnbyear;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Roman on 18/01/2017.
 */
public class LanguageAdapter extends RecyclerView.Adapter<LanguageAdapter.LanguageHolder> {

    private LayoutInflater inflater;
    private List<String> data = new ArrayList<>();
    private Context context;
    private String langGroup;

    public LanguageAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }


    @Override
    public LanguageHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LanguageHolder(parent);
    }

    @Override
    public void onBindViewHolder(LanguageHolder holder, int position) {
        holder.language.setText(new Locale(data.get(position)).getDisplayLanguage());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    public void add(String item) {
        data.add(item);
        notifyItemInserted(getItemCount() - 1);
        if (context instanceof LanguageActivity) {
            ((LanguageActivity) context).addToSharedPreferences(langGroup, item);
        }
    }

    public void remove(int position) {
        String code = data.get(position);
        data.remove(position);
        notifyItemRemoved(position);
        if (context instanceof LanguageActivity) {
            ((LanguageActivity) context).removeFromSP(langGroup, code);
        }
    }

    public void moveToStart(int position) {
        String tmp = data.get(position);
        data.set(position, data.get(0));
        data.set(0, tmp);
        notifyDataSetChanged();
        if (context instanceof LanguageActivity) {
            ((LanguageActivity) context).setDefaultSP(tmp);
        }
    }

    protected class LanguageHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public static final int REMOVE = 0;
        public static final int SET_DEFAULT = 1;

        private int position;
        public final TextView language;

        public LanguageHolder(ViewGroup parent) {
            super(inflater.inflate(R.layout.language_item, parent, false));
            itemView.setOnCreateContextMenuListener(this);
            language = (TextView) itemView.findViewById(R.id.text_view_language);
        }

        private MenuItem.OnMenuItemClickListener listener = new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case REMOVE: {
                        remove(position);
                        break;
                    }
                    case SET_DEFAULT: {
                        moveToStart(position);
                        break;
                    }
                }
                return false;
            }
        };

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            position = getAdapterPosition();
            MenuItem removeItem = contextMenu.add(Menu.NONE, Menu.NONE, 0, "Remove");
            MenuItem setAsDefaultItem = contextMenu.add(Menu.NONE, Menu.NONE, 1, "Set as default");
            removeItem.setOnMenuItemClickListener(listener);
            setAsDefaultItem.setOnMenuItemClickListener(listener);
        }
    }

}
