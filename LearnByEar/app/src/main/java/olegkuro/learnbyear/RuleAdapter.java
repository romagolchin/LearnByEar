package olegkuro.learnbyear;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

/**
 * Created by Roman on 19/12/2016.
 */

public class RuleAdapter extends CommonAdapter<RulesActivity.Rule> {
    public RuleAdapter(Context context) {
        super(context);
    }

    @Override
    public RuleHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return RuleHolder.newInstance(parent, inflater);
    }

    @Override
    public void onBindVHImpl(CommonViewHolder holder, int position) {
        if (holder instanceof RuleHolder) {
            ((RuleHolder) holder).name.setText(data.get(position).name);
            ((RuleHolder) holder).description.setText(data.get(position).explanation);
        }
    }

    private static class RuleHolder extends CommonAdapter.CommonViewHolder {
        private EditText name;
        private EditText description;

        public RuleHolder(View itemView) {
            super(itemView);
            name = (EditText) itemView.findViewById(R.id.rule_name);
            description = (EditText) itemView.findViewById(R.id.rule_description);
        }

        public static RuleHolder newInstance(ViewGroup parent, LayoutInflater inflater) {
            View itemView = inflater.inflate(R.layout.rule_item, parent);
            return new RuleHolder(itemView);
        }
    }
}
