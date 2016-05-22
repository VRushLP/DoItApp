package teamten.tacoma.uw.edu.doit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import teamten.tacoma.uw.edu.doit.model.DoItList;

import java.util.List;

public class MyDoItListRecyclerViewAdapter extends RecyclerView.Adapter<MyDoItListRecyclerViewAdapter.ViewHolder> implements View.OnClickListener {


    private final List<DoItList> listOfListsData;
    private final StationFragment.OnDoItStationFragmentInteractionListener mListener;
    private final View.OnClickListener mWiewListener;


    public MyDoItListRecyclerViewAdapter(List<DoItList> items, StationFragment.OnDoItStationFragmentInteractionListener listener) {

        listOfListsData = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_doitlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // getting particular item from list
        holder.mListItem = listOfListsData.get(position);
        holder.mTitleView.setText(listOfListsData.get(position).getTitle());

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mListItem);
                }
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (mWiewListener != null) {

                    holder.mListItem = listOfListsData.get(position);
                    mWiewListener.(view, position);
                }
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfListsData.size();
    }

    @Override
    public void onClick(View v) {
        //Log.d("View: ", v.toString());
        //Toast.makeText(v.getContext(), mTextViewTitle.getText() + " position = " + getPosition(), Toast.LENGTH_SHORT).show();
        if (v.equals()){
            removeAt(getPosition());
        }else if (mItemClickListener != null) {
            mItemClickListener.onItemClick(v, getAdapterPosition());
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final TextView mTitleView;
//        public final TextView mTaskContentView;
        public DoItList mListItem;
        private AdapterView.OnItemClickListener mItemClickListener;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.list_title_view);
//            mTaskContentView = (TextView) view.findViewById(R.id.list_task_content_view);

            mTitleView.setOnClickListener(this);
            mView.setOnClickListener(this);

//            mTitleView.setOnLongClickListener(new View.OnLongClickListener() {
//                @Override
//                public boolean onLongClick(View view) {
//                    if (mItemClickListener != null) {
//
//                        holder.mListItem = listOfListsData.get(position);
//                        mItemClickListener.onItemClick(view, getAdapterPosition());
//                    }
//                    return false;
//                }
//            });
        }

        public void setOnItemClickListener(final AdapterView.OnItemClickListener mItemClickListener) {
            this.mItemClickListener = mItemClickListener;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }

//        @Override
//        public void onClick(View v) {
//            //Log.d("View: ", v.toString());
//            //Toast.makeText(v.getContext(), mTextViewTitle.getText() + " position = " + getPosition(), Toast.LENGTH_SHORT).show();
//            if(v.equals(mTitleView)){
//                removeAt(getPosition());
//            }else if (mItemClickListener != null) {
//                mItemClickListener.onItemClick(v, getAdapterPosition());
//            }
//        }
//
//        public void removeAt(int position) {
//            listOfListsData.remove(position);
//            notifyItemRemoved(position);
//            notifyItemRangeChanged(position, listOfListsData.size());
//        }
    }
}
