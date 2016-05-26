package teamten.tacoma.uw.edu.doit;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import teamten.tacoma.uw.edu.doit.model.DoItList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListDetailFragment extends Fragment {

    private TextView mListTitleTextView;
    private UpdateListTitleListener mListTitleListener;
    private DoItList mListItem;

    protected static String LIST_ITEM_SELECTED = "ListItemSelected";

    public ListDetailFragment() {
        // Required empty public constructor
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof UpdateListTitleListener) {
            mListTitleListener = (UpdateListTitleListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement UpdateListTitleListener");
        }
    }
    public interface UpdateListTitleListener {
        public void updateListTitle(int theListID, String newTitle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_list_detail, container, false);
        mListTitleTextView = (TextView) view.findViewById(R.id.list_item_title);
        // long press on textview to update list's title
        mListTitleTextView.setOnLongClickListener(new View.OnLongClickListener() {

                    @Override
                    public boolean onLongClick(View v) {
                        // get prompts.xml view
                        LayoutInflater li = LayoutInflater.from(getContext());
                        View promptsView = li.inflate(R.layout.prompts, null);

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                getContext());

                        // set prompts.xml to alertdialog builder
                        alertDialogBuilder.setView(promptsView);

                        final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);

                        // set dialog message
                        alertDialogBuilder
                                .setCancelable(false)
                                .setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(
                                                    DialogInterface dialog,
                                                    int id) {
                                                // get user input and set it to
                                                // result
                                                // edit text
                                                String newTitle =  userInput.getText().toString();
                                                if (newTitle != "") {
                                                    mListTitleTextView.setText(newTitle);
                                                    // query database

                                                    mListTitleListener.updateListTitle(mListItem.getListID(), newTitle);
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

                        // create alert dialog
                        AlertDialog alertDialog = alertDialogBuilder.create();

                        // show it
                        alertDialog.show();
                        return false;
                    }
                });

        return view;
    }

    public void updateView(DoItList list) {
        if (list != null) {
            mListTitleTextView.setText(list.getTitle());
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
            mListItem = (DoItList) args.getSerializable(LIST_ITEM_SELECTED);
            updateView(mListItem);

//            updateView((DoItList) args.getSerializable(LIST_ITEM_SELECTED));
        }
    }


}
