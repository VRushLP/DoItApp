package teamten.tacoma.uw.edu.doit;

import android.content.Context;
import android.support.v7.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import teamten.tacoma.uw.edu.doit.model.DoItList;

import java.util.List;

public class MyDoItListRecyclerViewAdapter extends RecyclerView.Adapter<MyDoItListRecyclerViewAdapter.ViewHolder> {

    private final List<DoItList> listOfListsData;
    private final StationFragment.OnDoItStationFragmentInteractionListener mListener;
    private StationFragment.DeleteListClickListener mDeleteListListener;

    public MyDoItListRecyclerViewAdapter(List<DoItList> items, StationFragment.OnDoItStationFragmentInteractionListener listener, StationFragment.DeleteListClickListener deleteListListener) {
        listOfListsData = items;
        mListener = listener;
        mDeleteListListener = deleteListListener;
    }

//    public interface OnItemClickListener {
//        void onItemClicked(View v);
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_doitlist, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
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
                    notifyDataSetChanged();
                }
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                System.out.println("RecyclerAdapter: item clicked on LONG CLICK");
                if (null != mDeleteListListener) {
                    mDeleteListListener.itemClickedToBeDeleted(holder.mListItem, listOfListsData);
//                    listOfListsData.remove(position);
//                    notifyItemRemoved(position);
                    notifyDataSetChanged();
                }
                return true;
            }
        });

    }


    @Override
    public int getItemCount() {
        return listOfListsData.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public final TextView mTitleView;
//        public final TextView mTaskContentView;
        public DoItList mListItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitleView = (TextView) view.findViewById(R.id.list_title_view);

        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitleView.getText() + "'";
        }


        @Override
        public void onClick(View v) {

        }
    }

}
