package olegkuro.learnbyear;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by Roman on 18/01/2017.
 */
class LanguageAdapter extends CommonAdapter<String> {

    public LanguageAdapter(Context context) {
        super(context);
    }

    @Override
    public void onBindVHImpl(CommonViewHolder holder, int position) {
        try {
            String language = data.get(position);
            if (holder instanceof LanguageHolder) {
                ((LanguageHolder) holder).language.setText(language);
            }
        } catch (IndexOutOfBoundsException e) {
            Log.w("onBindVHImpl", e);
        }
    }

    @Override
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return LanguageHolder.newInstance(parent, inflater);
    }

    private static class LanguageHolder extends CommonViewHolder {
        public final TextView language;

        public LanguageHolder(View itemView) {
            super(itemView);
            language = (TextView) itemView.findViewById(R.id.language);
        }

        public static LanguageHolder newInstance(ViewGroup parent, LayoutInflater inflater) {
            final View itemView = inflater.inflate(R.layout.language_item, parent, false);
            return new LanguageHolder(itemView);
        }
    }

}
