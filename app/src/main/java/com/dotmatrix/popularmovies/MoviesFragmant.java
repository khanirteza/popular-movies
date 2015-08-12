package com.dotmatrix.popularmovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MoviesFragmant extends Fragment {

    private GridViewAdapter mGridViewAdapter;

    private ArrayList<String> urls = new ArrayList<>();
    //private  String[] urls = new String[20];
    private Bundle[] resultBundle;

    // These are the names of the JSON objects that need to be extracted.
/*    final String TMDB_RESULTS = "results";
    final String BACKDROP_PATH = "backdrop_path";
    final String ORIGINAL_TITLE = "original_title";
    final String OVERVIEW = "overview";
    final String RELEASE_DATE = "release_date";
    final String VOTE_AVERAGE = "vote_average";*/


    public MoviesFragmant() {
    }

    public void onStart(){
        super.onStart();
        updateMovie();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragmant_movies, container, false);

        mGridViewAdapter = new GridViewAdapter(rootView.getContext(), urls);
        final GridView gridView = (GridView) rootView.findViewById(R.id.gridView);
        gridView.setAdapter(mGridViewAdapter);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                //ImageItem item = (ImageItem) parent.getItemAtPosition(position);
                Log.v("image", gridView.getItemAtPosition(position).toString());
                Intent intent = new Intent(getActivity(), MovieDetailActivity.class).putExtra("MOVIE_DETAIL", resultBundle[position]);
                startActivity(intent);
                //Log.v("item", String.valueOf(position));
            }
        });



            return rootView;
    }


    /**
     * Take the String representing the complete forecast in JSON Format and
     * pull out the data we need to construct the Strings needed for the wireframes.
     *
     * Fortunately parsing is easy:  constructor takes the JSON string and converts it
     * into an Object hierarchy for us.
     */
    private Bundle[] getMovieDataFromJson(String MovieJsonStr)
            throws JSONException {

        JSONObject movieJson = new JSONObject(MovieJsonStr);
        JSONArray movieArray = movieJson.getJSONArray(getString(R.string.TMDB_RESULTS));


        resultBundle = new Bundle[movieArray.length()];
        for(int i = 0; i < movieArray.length(); i++) {

            String backdropPath;
            String originalTitle;
            String overview;
            String releaseDate;
            String voteAverage;

            // Get the JSON object representing the day
            JSONObject movieInfo = movieArray.getJSONObject(i);


            backdropPath = movieInfo.getString(getString(R.string.BACKDROP_PATH));
            originalTitle = movieInfo.getString(getString(R.string.ORIGINAL_TITLE));
            overview = movieInfo.getString(getString(R.string.OVERVIEW));
            releaseDate = movieInfo.getString(getString(R.string.RELEASE_DATE));
            voteAverage = movieInfo.getString(getString(R.string.VOTE_AVERAGE));

            Bundle bundle = new Bundle();

            bundle.putString(getString(R.string.BACKDROP_PATH), backdropPath);
            bundle.putString(getString(R.string.ORIGINAL_TITLE), originalTitle);
            bundle.putString(getString(R.string.OVERVIEW), overview);
            bundle.putString(getString(R.string.RELEASE_DATE), releaseDate);
            bundle.putString(getString(R.string.VOTE_AVERAGE), voteAverage);

            resultBundle[i] = bundle;

        }

        return resultBundle;

    }

    //creating async task
    private class FetchMovieTask extends AsyncTask<String, Void, Bundle[]> {
        @Override
        protected Bundle[] doInBackground(String... sortBy){
            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String movieJsonStr = null;

            String apiKey = getString(R.string.TMDB_API_KEY);

            try {
                // Construct the URL for the TMDB query
                final String TMDB_BASE_URL = "http://api.themoviedb.org/3/discover/movie";
                final String QUERY_PARAM = "sort_by";
                final String API_PARAM = "api_key";
                Uri builtUri = Uri.parse(TMDB_BASE_URL).buildUpon()
                        .appendQueryParameter(QUERY_PARAM, sortBy[0])
                        .appendQueryParameter(API_PARAM, apiKey)
                        .build();

                URL url = new URL(builtUri.toString());
                //Log.v("Uri", builtUri.toString());

                // Create the request to TMDB, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                movieJsonStr = buffer.toString();
                //Log.v("result", forecastJsonStr);
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attempting
                // to parse it.
                movieJsonStr = null;
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }

            try{
                return getMovieDataFromJson(movieJsonStr);
            } catch (JSONException e){
                Log.e("JSON error", e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Bundle[] result) {
            if (result != null){
                urls = new ArrayList<>();
                //urls = new String[result.length];

                for (int i = 0; i < result.length; i++){
                    urls.add("http://image.tmdb.org/t/p/w342/" + result[i].getString(getString(R.string.BACKDROP_PATH)));
                    //urls.add("http://image.tmdb.org/t/p/w185/" + result[i].getString(BACKDROP_PATH));
                    //Log.v("url", urls[i]);
                }


                mGridViewAdapter.clear();
                mGridViewAdapter.addAll(urls);
                mGridViewAdapter.notifyDataSetChanged();
            }

        }
    }


    public void updateMovie(){
        FetchMovieTask movieTask = new FetchMovieTask();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String movie_sort_by = preferences.getString(getString(R.string.pref_movie_sort_by_key), getString(R.string.pref_movie_sort_by_default));
        movieTask.execute(movie_sort_by);
    }


}
