package com.dotmatrix.popularmovies;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment {

    public MovieDetailActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        TextView tvMovieTitle = (TextView) rootView.findViewById(R.id.tv_movie_title);
        TextView tvPlot = (TextView) rootView.findViewById(R.id.tv_plot);
        TextView tvRating = (TextView) rootView.findViewById(R.id.tv_rating);
        TextView tvReleaseDate = (TextView) rootView.findViewById(R.id.tv_release_date);
        ImageView ivPoster = (ImageView) rootView.findViewById(R.id.iv_poster);

        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra("MOVIE_DETAIL")){
            Bundle movieDetail = intent.getBundleExtra("MOVIE_DETAIL");
            tvMovieTitle.setText(movieDetail.getString(getString(R.string.ORIGINAL_TITLE)));
            tvPlot.setText(movieDetail.getString(getString(R.string.OVERVIEW)));
            tvRating.setText(movieDetail.getString(getString(R.string.VOTE_AVERAGE)));
            tvReleaseDate.setText(movieDetail.getString(getString(R.string.RELEASE_DATE)));
            new ImageLoadTask(movieDetail.getString(getString(R.string.BACKDROP_PATH)), ivPoster).execute();
        }

        return rootView;
    }


    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }

    }
}
