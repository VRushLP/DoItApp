package teamten.tacoma.uw.edu.doit;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import teamten.tacoma.uw.edu.doit.Data.DoItAppDB;
import teamten.tacoma.uw.edu.doit.model.DoItList;

/**
 * A fragment representing a list of Items. (Verbose View)
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnDoItStationFragmentInteractionListener}
 * interface.
 */
public class StationFragment extends Fragment {

    private static final String TAG = "StationFragment";
    //private String mUserID;

    //private static final String ARG_COLUMN_COUNT = "1";
    private String listURL = "http://cssgate.insttech.washington.edu/~_450atm10/android/station.php?cmd=station";

    private RecyclerView mRecyclerView;
    private DoItAppDB mStationDB;
    private List<DoItList> mListOfDoItLists;

    private OnDoItStationFragmentInteractionListener mListener;
    private DeleteListClickListener mDeleteListListener;
    private UpdateListTitleListener mListTitleListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mStationDB = new DoItAppDB(this.getContext());

        // adding userID to obtain their specific data
        SharedPreferences mSharedPreferences = this.getActivity().getSharedPreferences(getString(R.string.PREFS_FILE)
                , Context.MODE_PRIVATE);

        String userIdSharePref = mSharedPreferences.getString(getString(R.string.PREFS_USER_ID), null);
        Log.d(TAG, userIdSharePref);
        StringBuilder listURLBuilder = new StringBuilder();

        listURLBuilder.append("&userID=");
        listURLBuilder.append(userIdSharePref);
        listURL += listURLBuilder;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doitlist_list, container, false);
        getActivity().setTitle(getString(R.string.station_fragment_title));

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        }

        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.i(TAG, listURL);
            DownloadListsTask task = new DownloadListsTask();
            task.execute(listURL);
        }
        else {
            Toast.makeText(view.getContext(),
                    "No network connection available. Displaying locally stored data",
                    Toast.LENGTH_SHORT) .show();

            if (mStationDB == null) {
                mStationDB = new DoItAppDB(getActivity());
            }
            if (mListOfDoItLists == null) {
                mListOfDoItLists = mStationDB.getDoItLists();
            }

            mRecyclerView.setAdapter(new MyDoItListRecyclerViewAdapter(mListOfDoItLists, mListener,
                                                mDeleteListListener, mListTitleListener));

        }

        try {
            InputStream inputStream = getActivity().openFileInput(
                    getString(R.string.PREFS_FILE));

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                // shows user's email and password
//                Toast.makeText(getActivity(), stringBuilder.toString(), Toast.LENGTH_SHORT)
//                        .show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.task_add_floating_button);
        fab.hide();

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.action_share);
         item.setVisible(false);
     }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnDoItStationFragmentInteractionListener) {
            mListener = (OnDoItStationFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnDoItStationFragmentInteractionListener");
        }

        if (context instanceof  DeleteListClickListener) {
            mDeleteListListener = (DeleteListClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement DeleteListClickListener");
        }

        if (context instanceof UpdateListTitleListener) {
            mListTitleListener = (UpdateListTitleListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement UpdateListTitleListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnDoItStationFragmentInteractionListener {
        void onDoItListItemInteraction(DoItList item);
    }

    public interface DeleteListClickListener {
        void deleteDoItList(DoItList item);
    }

    public interface UpdateListTitleListener {
        void updateListTitle(int theListID, String newTitle);
    }

    private class DownloadListsTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String response = "";
            HttpURLConnection urlConnection = null;
            URL urlObject=null;
            for (String url : urls) {
                try {

                    urlObject = new URL(url);
                    urlConnection = (HttpURLConnection) urlObject.openConnection();
                    InputStream content = urlConnection.getInputStream();
                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {

                    response = "Unable to download the lists, Reason: "
                            + e.getClass();

                }
                finally {
                    if (urlConnection != null)
                        urlConnection.disconnect();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            // Something wrong with the network or the URL.
            if (result.startsWith("Unable to")) {
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }

            List<DoItList> list = new ArrayList<DoItList>();
            result = DoItList.parseAllLists(result, list);
            // Something wrong with the JSON returned.
            if (result != null) {
//                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
//                        .show();
                return;
            }

            // Everything is good, show the list of courses.
            if (!list.isEmpty()) {
                mRecyclerView.setAdapter(new MyDoItListRecyclerViewAdapter(list, mListener,
                                                        mDeleteListListener, mListTitleListener));

                if (mStationDB == null) {
                    mStationDB = new DoItAppDB(getActivity());
                }

                // Delete old data so that you can refresh the local
                // database with the network data.
                mStationDB.deleteStation();

                // Also, add to the local database
                for (int i=0; i<list.size(); i++) {
                    DoItList single_list = list.get(i);
                    mStationDB.insertList(single_list.getListID(), single_list.getTitle(),
                            single_list.getIsDeleted());
                }
            }
        }
    }
}