package teamten.tacoma.uw.edu.doit;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.TextView;

import teamten.tacoma.uw.edu.doit.DoItListDisplayFragment.OnTaskDisplayInteractionListener;
import teamten.tacoma.uw.edu.doit.model.DoItTask;

import java.util.ArrayList;
import java.util.List;

public class MyDoItTaskRecyclerViewAdapter extends RecyclerView.Adapter<MyDoItTaskRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "DoItTaskRecyclerView";

    private final List<DoItTask> mAllTasks;
    private final OnTaskDisplayInteractionListener mInteractionListener;
    private final DoItListDisplayFragment.DeleteTaskListener mDeleteListener;
    private final DoItListDisplayFragment.EditTaskTitleListener mEditListener;
    private final DoItListDisplayFragment.EditTaskDependencyListener mDependencyListener;

    public MyDoItTaskRecyclerViewAdapter(List<DoItTask> items,OnTaskDisplayInteractionListener interactionListener,
                                         DoItListDisplayFragment.DeleteTaskListener mDeleteListener,
                                         DoItListDisplayFragment.EditTaskTitleListener mEditListener,
                                         DoItListDisplayFragment.EditTaskDependencyListener mDependencyListener) {
        mAllTasks = items;
        mInteractionListener = interactionListener;
        this.mDeleteListener = mDeleteListener;
        this.mEditListener = mEditListener;
        this.mDependencyListener = mDependencyListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_doittask, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final int realPosition = holder.getAdapterPosition();
        holder.mHeldTask = mAllTasks.get(realPosition);
        String displayText = "(" + mAllTasks.get(realPosition).mTaskID+") " + mAllTasks.get(realPosition).mName;
        holder.mContentView.setText(displayText);
        switch(holder.mHeldTask.mCheckedOff){
            case 0:
                holder.mContentView.setPaintFlags(0);
                break;
            case 1:
                holder.mContentView.setPaintFlags(
                        holder.mContentView.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                break;
        }

        if(checkForGreyOut(holder.mHeldTask)){
            holder.mContentView.setTextColor(Color.LTGRAY);
        } else {
            holder.mContentView.setTextColor(Color.BLACK);
        }


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInteractionListener != null) {
                    mInteractionListener.onDoItTaskInteraction(mAllTasks.get(realPosition));
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
            alertDialogBuilder.setTitle("Task Manager");

            alertDialogBuilder.setItems(R.array.task_long_click_action_list, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    LayoutInflater li = LayoutInflater.from(v.getContext());
                    switch (which) {
                        case 0:
                            // make dialog box and send info below
                            View updateNameView = li.inflate(R.layout.update_task_descrip_prompt, null);
                            AlertDialog.Builder alertDialogBuilderUpdate = new AlertDialog.Builder(
                                    v.getContext());

                            // set update_list_title_prompt.xml_title_prompt.xml to alertdialog builder
                            alertDialogBuilderUpdate.setView(updateNameView);
                            final EditText userInput = (EditText) updateNameView.findViewById(R.id.editTextDialogUserInput);

                            // set dialog message
                            alertDialogBuilderUpdate
                                .setCancelable(false)
                                .setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                            DialogInterface dialog, int id) {
                                            // get user input and set
                                            String newTitle =  userInput.getText().toString();
                                            if (!newTitle.equals("")) {
                                                mEditListener.editTaskTitle(holder.mHeldTask.mTaskID, newTitle);
                                                Log.i("Debug", "" + holder.mHeldTask.mTaskID);
                                                holder.mHeldTask.mName= newTitle;
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
                            Log.i(TAG, "Update dependency selected");
                            View updateDependencyView = li.inflate(R.layout.update_task_dependency_prompt, null);
                            AlertDialog.Builder dependencyBuilder = new AlertDialog.Builder(
                                    v.getContext());

                            // set update_list_title_prompt.xml_title_prompt.xml to alertdialog builder
                            dependencyBuilder.setView(updateDependencyView);
                            final EditText dependencyInput = (EditText) updateDependencyView.findViewById(R.id.edit_dependency_ET);
                            dependencyInput.setRawInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);

                            // set dialog message
                            dependencyBuilder
                                    .setCancelable(false)
                                    .setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(
                                                        DialogInterface dialog, int id) {
                                                    // get user input and set
                                                    String newDependency =  dependencyInput.getText().toString();
                                                    int actualInput = Integer.parseInt(newDependency);

                                                    if(actualInput == holder.mHeldTask.mTaskID){
                                                        //TODO toast an error message or something
                                                        Log.i(TAG, "Tasks can't depend on themselves!");
                                                    } else if(!containsPassedDependency(actualInput)){
                                                        //TODO toast an error message or something
                                                        Log.i(TAG, "There's no task by that ID in this list!");
                                                    } else{
                                                        holder.mHeldTask.mDependency = actualInput;
                                                        mDependencyListener.editTaskDependency(holder.mHeldTask.mTaskID, holder.mHeldTask.mDependency);
                                                        Log.i(TAG, "Dependency Changed" + holder.mHeldTask.mDependency);
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
                            dependencyBuilder.create().show();
                            break;
                        case 2:
                            Log.i(TAG, "Remove Dependency Selected");
                            holder.mHeldTask.mDependency = -1;
                            mDependencyListener.editTaskDependency(holder.mHeldTask.mTaskID, holder.mHeldTask.mDependency);
                            Log.i(TAG, "Dependency Changed" + holder.mHeldTask.mDependency);
                            notifyDataSetChanged();
                            break;
                        case 3:
                            Log.i(TAG, "Delete selected");
                            mAllTasks.remove(realPosition);
                            notifyItemRemoved(realPosition);
                            mDeleteListener.deleteTask(holder.mHeldTask);
                            break;
                        case 4:
                            Log.i(TAG, "Dialog canceled");
                            break;
                        default:
                            Log.wtf(TAG, "How'd you get here?");
                            break;
                    }
                }
            });
            alertDialogBuilder.create().show();
            return true;
            }
        });
    }

    private boolean containsPassedDependency(int check){
        for(DoItTask t : mAllTasks){
            if(check == t.mTaskID) return true;
        }
        return false;
    }

    private boolean checkForGreyOut(DoItTask theTask){
        for(DoItTask t : mAllTasks){
            if(theTask.mDependency == t.mTaskID && t.mCheckedOff == 0) return true;
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return mAllTasks.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mContentView;
        public DoItTask mHeldTask;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mView.setClickable(true);
            mContentView = (CheckedTextView) view.findViewById(R.id.content);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
