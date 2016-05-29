package teamten.tacoma.uw.edu.doit;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import teamten.tacoma.uw.edu.doit.DoItListDisplayFragment.OnTaskDisplayInteractionListener;
import teamten.tacoma.uw.edu.doit.model.DoItTask;

import java.util.List;

public class MyDoItTaskRecyclerViewAdapter extends RecyclerView.Adapter<MyDoItTaskRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "DoItTaskRecyclerView";
    private final List<DoItTask> mValues;
    private final OnTaskDisplayInteractionListener mListener;

    public MyDoItTaskRecyclerViewAdapter(List<DoItTask> items, DoItListDisplayFragment.OnTaskDisplayInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_doittask, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
//        holder.mItem = mValues.get(position);
//        holder.mIdView.setText(mValues.get(position).id);
        holder.mContentView.setText(mValues.get(position).mName);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onDoItTaskInteraction(mValues.get(holder.getAdapterPosition()));
                    notifyDataSetChanged();
                }
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Log.i(TAG, "RecyclerAdapter: Task long click");
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
//        public final TextView mIdView;
        public final TextView mContentView;
        public ViewHolder(View view) {
            super(view);
            mView = view;
//            mIdView = (TextView) view.findViewById(R.id.id);
            mContentView = (CheckedTextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
