package teamten.tacoma.uw.edu.doit;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import teamten.tacoma.uw.edu.doit.model.DoItList;

import java.util.List;

public class MyDoItListRecyclerViewAdapter extends RecyclerView.Adapter<MyDoItListRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "DoItListRecyclerView";
    private final List<DoItList> listOfListsData;
    private final StationFragment.OnDoItStationFragmentInteractionListener mListener;
    private StationFragment.DeleteListClickListener mDeleteListListener;
    private StationFragment.UpdateListTitleListener mListTitleListener;

    public MyDoItListRecyclerViewAdapter(List<DoItList> items, StationFragment.OnDoItStationFragmentInteractionListener listener,
                                         StationFragment.DeleteListClickListener deleteListListener,
                                         StationFragment.UpdateListTitleListener listTitleListener) {
        listOfListsData = items;
        mListener = listener;
        mDeleteListListener = deleteListListener;
        mListTitleListener = listTitleListener;
    }

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
                    mListener.onDoItListItemInteraction(holder.mListItem);
                    notifyDataSetChanged();
                }
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override

            public boolean onLongClick(final View v) {
                Log.i(TAG, "RecyclerAdapter: item clicked on LONG CLICK");
                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());

                // set title
                alertDialogBuilder.setTitle("List Action");

                alertDialogBuilder.setItems(R.array.pick_list_action, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        switch (which) {
                            case 0:
                                // make dialog box and send info below
                                LayoutInflater li = LayoutInflater.from(v.getContext());
                                View promptsView = li.inflate(R.layout.update_list_title_prompt, null);
                                AlertDialog.Builder alertDialogBuilderUpdate = new AlertDialog.Builder(
                                        v.getContext());

                                // set update_list_title_prompt.xml_title_prompt.xml to alertdialog builder
                                alertDialogBuilderUpdate.setView(promptsView);

                                final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextTitleDialogUserInput);

                                // set dialog message
                                alertDialogBuilderUpdate
                                        .setCancelable(false)
                                        .setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(
                                                            DialogInterface dialog, int id) {
                                                        // get user input and set
                                                        String newTitle =  userInput.getText().toString();
                                                        if (newTitle.length() > 0 && !newTitle.equals("") ) {
                                                            // query database
                                                            mListTitleListener.updateListTitle(holder.mListItem.getListID(), newTitle);
//
                                                            holder.mListItem.setTitle(newTitle);
                                                            notifyDataSetChanged();
                                                        } else {
                                                            Toast.makeText(v.getContext(), "Title must be at least 1 character long.", Toast.LENGTH_LONG)
                                                                    .show();
                                                        }
                                                    }
                                                })
                                        .setNegativeButton("Cancel",
                                                new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id) {
                                                        dialog.cancel();
                                                    }
                                                });
                                AlertDialog alertDialogUpdate = alertDialogBuilderUpdate.create();
                                alertDialogUpdate.show();
                                break;
                            case 1:
                                listOfListsData.remove(position);
                                notifyItemRemoved(position);
                                mDeleteListListener.itemClickedToBeDeleted(holder.mListItem);
                                break;
                            case 2:
                                System.out.println("Dialog button clicked: Cancel");
                                break;
                            default:
                                break;
                        }
                    }
                });
                alertDialogBuilder.create().show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfListsData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitleView;
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
    }
}