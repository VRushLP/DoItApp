package teamten.tacoma.uw.edu.doit;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;

import teamten.tacoma.uw.edu.doit.DoItListDisplayFragment.OnTaskDisplayInteractionListener;
import teamten.tacoma.uw.edu.doit.model.DoItTask;

import java.util.List;

public class MyDoItTaskRecyclerViewAdapter extends RecyclerView.Adapter<MyDoItTaskRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "DoItTaskRecyclerView";
    private final List<DoItTask> mValues;
    private final OnTaskDisplayInteractionListener mInteractionListener;
    private final DoItListDisplayFragment.DeleteTaskListener mDeleteListener;
    private final DoItListDisplayFragment.EditTaskTitleListener mEditListener;

    public MyDoItTaskRecyclerViewAdapter(List<DoItTask> items,
                                         OnTaskDisplayInteractionListener interactionListener,
                                         DoItListDisplayFragment.DeleteTaskListener mDeleteListener,
                                         DoItListDisplayFragment.EditTaskTitleListener mEditListener) {
        mValues = items;
        mInteractionListener = interactionListener;
        this.mDeleteListener = mDeleteListener;
        this.mEditListener = mEditListener;
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
        holder.heldTask = mValues.get(position);
        holder.mContentView.setText(mValues.get(position).mName);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInteractionListener != null) {
                    mInteractionListener.onDoItTaskInteraction(mValues.get(holder.getAdapterPosition()));
                    notifyDataSetChanged();
                }
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View v) {
            Log.i(TAG, "RecyclerAdapter: Task long click");

            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(v.getContext());
            // set title
            alertDialogBuilder.setTitle("Task Manager.");

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
                            final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);

                            // set dialog message
                            alertDialogBuilderUpdate
                                .setCancelable(false)
                                .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                            DialogInterface dialog, int id) {
                                            // get user input and set
                                            String newTitle =  userInput.getText().toString();
                                            if (newTitle != "") {
                                                mEditListener.editTaskTitle(holder.heldTask.mTaskID, newTitle);
                                                Log.i("Debug", "" + holder.heldTask.mTaskID);
                                                holder.heldTask.mName= newTitle;
                                                notifyDataSetChanged();
                                            }
                                        }
                                    })
                            .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(
                                            DialogInterface dialog,
                                            int id) {
                                        dialog.cancel();
                                    }
                                });
                            AlertDialog alertDialogUpdate = alertDialogBuilderUpdate.create();
                            alertDialogUpdate.show();
                            break;
                        case 1:
                            Log.i(TAG, "Delete selected");
//                            listOfListsData.remove(position);
//                            notifyItemRemoved(position);
//                            mDeleteListListener.itemClickedToBeDeleted(holder.mListItem);
                            break;
                        case 2:
                            Log.i(TAG, "Dialog canceled");
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
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public DoItTask heldTask;

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
