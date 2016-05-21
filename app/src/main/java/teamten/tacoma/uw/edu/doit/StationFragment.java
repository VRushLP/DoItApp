package teamten.tacoma.uw.edu.doit;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import teamten.tacoma.uw.edu.doit.Data.StationDB;
import teamten.tacoma.uw.edu.doit.model.DoItList;

/**
 * A fragment representing a list of Items. (Verbose View)
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnDoItStationFragmentInteractionListener}
 * interface.
 */
public class StationFragment extends Fragment {


    private int mColumnCount = 1;
    private String mUserID;

    //private static final String ARG_COLUMN_COUNT = "1";
    private String listURL
            = "http://cssgate.insttech.washington.edu/~_450atm10/android/station.php?cmd=station";

    private RecyclerView mRecyclerView;

    private StationDB mStationDB;
    private List<DoItList> mListOfDoItLists;

    private OnDoItStationFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public StationFragment() {
    }

//    // TODO: Customize parameter initialization
//    @SuppressWarnings("unused")
//    public static StationFragment newInstance(int columnCount) {
//        StationFragment fragment = new StationFragment();
//        Bundle args = new Bundle();
//        args.putInt(ARG_COLUMN_COUNT, columnCount);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        if (getArguments() != null) {
//            mColumnCount = getArguments().getInt(mColumnCount);
//        }
        mStationDB = new StationDB(this.getContext());

        // adding userID to obtain their specific data
        SharedPreferences mSharedPreferences = this.getActivity().getSharedPreferences(getString(R.string.LOGIN_PREFS)
                , Context.MODE_PRIVATE);

        mUserID = mSharedPreferences.getString("USERID", null);
        listURL = listURL + "&userID=" + mUserID;

//        userEmail = mSharedPreferences.getString("@string/userEmail", null);
//        listURL = listURL + "&email=" + userEmail;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doitlist_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            mRecyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                mRecyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

        }


        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            DownloadListsTask task = new DownloadListsTask();
            task.execute(new String[]{listURL});
        }
        else {
            Toast.makeText(view.getContext(),
                    "No network connection available. Displaying locally stored data",
                    Toast.LENGTH_SHORT) .show();

            if (mStationDB == null) {
                mStationDB = new StationDB(getActivity());
            }
            if (mListOfDoItLists == null) {
                mListOfDoItLists = mStationDB.getDoItLists();
            }
            mRecyclerView.setAdapter(new MyDoItListRecyclerViewAdapter(mListOfDoItLists, mListener));

        }

        try {
            InputStream inputStream = getActivity().openFileInput(
                    getString(R.string.LOGIN_FILE));

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                Toast.makeText(getActivity(), stringBuilder.toString(), Toast.LENGTH_SHORT)
                        .show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
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
        void onListFragmentInteraction(DoItList item);
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
                    Log.wtf("Help", "1");

                    InputStream content = urlConnection.getInputStream();
                    Log.wtf("Help", "2");

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
            result = DoItList.parseListOfTasksJSON(result, list);
            // Something wrong with the JSON returned.
            if (result != null) {
                Toast.makeText(getActivity().getApplicationContext(), result, Toast.LENGTH_LONG)
                        .show();
                return;
            }

            // Everything is good, show the list of courses.
            if (!list.isEmpty()) {
                mRecyclerView.setAdapter(new MyDoItListRecyclerViewAdapter(list, mListener));

                if (mStationDB == null) {
                    mStationDB = new StationDB(getActivity());
                }

                // Delete old data so that you can refresh the local
                // database with the network data.
                mStationDB.deleteStation();

                // Also, add to the local database
                for (int i=0; i<list.size(); i++) {
                    DoItList single_list = list.get(i);
                    mStationDB.insertStation(single_list.getTitle(),
                            single_list.getIsDeleted());
                }
            }
        }
    }

}