package teamten.tacoma.uw.edu.doit;


import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import teamten.tacoma.uw.edu.doit.Data.StationDB;
import teamten.tacoma.uw.edu.doit.model.DoItList;


public class ListDialogFragment extends DialogFragment {

    int mNum;
    DoItList item;
    String mUserID;
    Context mContext;


//    public ListDialogFragment() {
//        // Required empty public constructor
//    }

    public static ListDialogFragment newInstance(int num, DoItList item, List<DoItList> theData, String userID) {
        ListDialogFragment f = new ListDialogFragment();
        // Supply num and serializable object input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        args.putSerializable("onLongClickedItem", item);
        args.putString("userID", userID);
//        args.putParcelableArrayList("data", theData);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity().getApplicationContext();
        mNum = getArguments().getInt("num");
        item = (DoItList) getArguments().getSerializable("onLongClickedItem");
        mUserID = getArguments().getString("userID");

        // Pick a style based on the num.
        int style = DialogFragment.STYLE_NORMAL, theme = 0;
        switch ((mNum-1)%6) {
            case 1: style = DialogFragment.STYLE_NO_TITLE; break;
            case 2: style = DialogFragment.STYLE_NO_FRAME; break;
            case 3: style = DialogFragment.STYLE_NO_INPUT; break;
            case 4: style = DialogFragment.STYLE_NORMAL; break;
            case 5: style = DialogFragment.STYLE_NORMAL; break;
            case 6: style = DialogFragment.STYLE_NO_TITLE; break;
            case 7: style = DialogFragment.STYLE_NO_FRAME; break;
            case 8: style = DialogFragment.STYLE_NORMAL; break;
        }
        switch ((mNum-1)%6) {
            case 4: theme = android.R.style.Theme_Holo; break;
            case 5: theme = android.R.style.Theme_Holo_Light_Dialog; break;
            case 6: theme = android.R.style.Theme_Holo_Light; break;
            case 7: theme = android.R.style.Theme_Holo_Light_Panel; break;
            case 8: theme = android.R.style.Theme_Holo_Light; break;
        }
        setStyle(style, theme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_dialog, container, false);
        View tv = v.findViewById(R.id.text);
        ((TextView)tv).setText("Dialog #" + mNum + ": using style " );

        // Watch for dismiss button
        Button dismissButton = (Button)v.findViewById(R.id.button_dismiss_dialog);
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //When button is clicked, call up to owning activity.
//                ((StationActivity)getActivity()).showDialog();
                dismiss();
            }
        });

        // Watch for delete button
        Button deleteButton = (Button)v.findViewById(R.id.button_delete_list);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("ONCLICKED ITEM in listDialogFrag: " + item.getListID());
                String url = buildDeleteListURL(v, "delete");

                UpdateOrDeleteList_AsyncTask task = new UpdateOrDeleteList_AsyncTask("delete");
                task.execute(new String[]{url.toString()});

                dismiss();
            }
        });

        return v;
    }

    private String buildDeleteListURL(View v, String cmd) {
        String listURL = "http://cssgate.insttech.washington.edu/~_450atm10/android/station.php?cmd=";
        listURL += cmd;



        StringBuilder sb = new StringBuilder(listURL);

        try {

            //Bundle data = getArguments();

//            String cmd = data.getString();
//            sb.append("cmd=");
//            sb.append(cmd);

            if (cmd.equals("delete") ) {
                int listID = item.getListID();
                sb.append("&listID=");
                sb.append(listID);

            } else if (cmd.equals("update")) {
                String title = item.getTitle();
                sb.append("&title=");
                sb.append(title);
            }
                String userID = mUserID;
                sb.append("&userID=");
                sb.append(userID);

            Log.i("ListDialogFragment", sb.toString());

        }
        catch(Exception e) {
            Toast.makeText(v.getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        Log.i("ListDialogFrag:Delete", new String(sb));
        return sb.toString();
    }


    private class UpdateOrDeleteList_AsyncTask extends AsyncTask<String, Void, String> {


        private String TASK_ACTION;

        public UpdateOrDeleteList_AsyncTask(String taskAction) {
            TASK_ACTION = taskAction;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }




        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            for (String url : urls) {
                try {
                    URL urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();

                    InputStream content = urlConnection.getInputStream();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    response = "Unable to " + TASK_ACTION + " list, Reason: "
                            + e.getMessage();
                    Log.wtf("wtf", e.getMessage());
                } finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        /**
         * It checks to see if there was a problem with the URL(Network) which is when an
         * exception is caught. It tries to call the parse Method and checks to see if it was successful.
         * If not, it displays the exception.
         *
         * @param result
         */
        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            try {
                JSONObject jsonObject = new JSONObject(result);
                String status = (String) jsonObject.get("result");
                if (status.equals("success")) {
                    Toast.makeText(mContext, "List successfully "+TASK_ACTION+"ed!"
                            , Toast.LENGTH_LONG)
                            .show();
                } else {
                    Toast.makeText(mContext, "Failed to "+TASK_ACTION+": "
                                    + jsonObject.get("error")
                            , Toast.LENGTH_LONG)
                            .show();
                }
            } catch (JSONException e) {
                Toast.makeText(mContext, "Something wrong with the data: " +
                        e.getMessage(), Toast.LENGTH_LONG).show();
            }

        }
    }


}
