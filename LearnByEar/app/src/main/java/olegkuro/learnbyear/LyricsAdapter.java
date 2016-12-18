package olegkuro.learnbyear;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by Roman on 18/12/2016.
 */

public class LyricsAdapter extends CommonAdapter<String> {
    @Override
    public LineHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return LineHolder.newInstance(parent, inflater);
    }

    @Override
    public void onBindVHImpl(CommonViewHolder holder, int position) {
        if (holder instanceof LineHolder)
            ((LineHolder) holder).lyricsLine.setText(data.get(position));
    }

    private static class LineHolder extends CommonAdapter.CommonViewHolder {
        private EditText lyricsLine;

        public LineHolder(View itemView) {
            super(itemView);
            lyricsLine = (EditText) itemView.findViewById(R.id.lyrics_line);
        }

        public static LineHolder newInstance(ViewGroup parent, LayoutInflater inflater) {
            View itemView = inflater.inflate(R.layout.search_result_item, parent);
            return new LineHolder(itemView);
        }

        @Override
        public void bindImpl() {
            lyricsLine.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            TextView textView = (TextView) view;
                            int index = textView.getOffsetForPosition(motionEvent.getX(), motionEvent.getY());
                            int lineNumber = getAdapterPosition();
                            return true;
                        }
                        default:
                            return false;
                    }
                }
            });
        }
    }
}
