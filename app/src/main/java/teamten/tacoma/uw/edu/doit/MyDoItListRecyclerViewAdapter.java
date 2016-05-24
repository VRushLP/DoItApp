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
//        mContext = context;
    }

    public interface OnItemClickListener {
        void onItemClicked(View v);
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
                    mListener.onListFragmentInteraction(holder.mListItem);

                }
            }
        });

        holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                System.out.println("RecyclerAdapter: item clicked on LONG CLICK");
                if (null != mDeleteListListener) {
                    mDeleteListListener.itemClickedToBeDeleted(holder.mListItem);
                    listOfListsData.remove(position);
                    notifyItemRemoved(position);
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


//    public void deleteList(String url) {
//        AddList_AsyncTask task = new AddList_AsyncTask();
//        task.execute(new String[]{url.toString()});
//
//        // Takes you back to the previous fragment by popping the current fragment out.
//        getSupportFragmentManager().popBackStackImmediate();
//    }
//
//    private class DeleteListAsyncTask extends AsyncTask<String, Void, String> {
//
//        @Override
//        protected void onPreExecute() {
//            super.onPreExecute();
//        }
//
//        @Override
//        protected String doInBackground(String... urls) {
//            String response = "";
//            HttpURLConnection urlConnection = null;
//            for (String url : urls) {
//                try {
//                    URL urlObject = new URL(url);
//                    urlConnection = (HttpURLConnection) urlObject.openConnection();
//
//                    InputStream content = urlConnection.getInputStream();
//
//                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
//                    String s = "";
//                    while ((s = buffer.readLine()) != null) {
//                        response += s;
//                    }
//
//                } catch (Exception e) {
//                    response = "Unable to add list, Reason: "
//                            + e.getMessage();
//                    Log.wtf("wtf", e.getMessage());
//                } finally {
//                    if (urlConnection != null)
//                        urlConnection.disconnect();
//                }
//            }
//            return response;
//        }
//
//        /**
//         * It checks to see if there was a problem with the URL(Network) which is when an
//         * exception is caught. It tries to call the parse Method and checks to see if it was successful.
//         * If not, it displays the exception.
//         *
//         * @param result
//         */
//        @Override
//        protected void onPostExecute(String result) {
//            // Something wrong with the network or the URL.
//            try {
//                JSONObject jsonObject = new JSONObject(result);
//                String status = (String) jsonObject.get("result");
//                if (status.equals("success")) {
//                    Toast.makeText(getActivity(), "List successfully deleted!"
//                            , Toast.LENGTH_LONG)
//                            .show();
//                } else {
//                    Toast.makeText(getActivity(), "Failed to delete: "
//                                    + jsonObject.get("error")
//                            , Toast.LENGTH_LONG)
//                            .show();
//                }
//            } catch (JSONException e) {
//                Toast.makeText(getActivity(), "Something wrong with the data" +
//                        e.getMessage(), Toast.LENGTH_LONG).show();
//            }
//        }
//    }
}
