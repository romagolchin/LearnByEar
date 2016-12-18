package olegkuro.learnbyear;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Roman on 18/12/2016.
 */

public abstract class CommonAdapter<T> extends RecyclerView.Adapter<CommonAdapter.CommonViewHolder> {
    // RecyclerView has no OnItemClickListener (unlike ListView) so custom listener is used
    protected OnItemClickListener listener;
    protected LayoutInflater inflater;
    protected List<T> data = new ArrayList<T>();

    public CommonAdapter() {
    }

    public CommonAdapter(Context context) {
        this.inflater = LayoutInflater.from(context);
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void setData(List<T> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public abstract void onBindVHImpl(CommonViewHolder holder, int position);

    @Override
    public void onBindViewHolder(CommonViewHolder holder, int position) {
        holder.bind(position, listener);
        onBindVHImpl(holder, position);
    }

    protected static abstract class CommonViewHolder extends RecyclerView.ViewHolder {
        public CommonViewHolder(View itemView) {
            super(itemView);
        }


        public void bind(final int position, @Nullable final OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null)
                        listener.onItemClick(position);
                }
            });
        }

        public void bindImpl() {}
    }


}
