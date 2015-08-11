package com.dotmatrix.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by MohammadIrteza on 11-Aug-15.
 */ //GridView adapter
public class GridViewAdapter extends ArrayAdapter {
    private Context context;
    private LayoutInflater inflater;

    //private String[] imageUrls;
    private ArrayList<String> imageUrls = new ArrayList<>();

    public GridViewAdapter(Context context, ArrayList<String> urls) {
        super(context, R.layout.grid_item_image, urls);

        this.context = context;
        this.imageUrls = urls;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.grid_item_image, parent, false);
        }

        Picasso
                .with(context)
                .load(imageUrls.get(position))
                .fit() // will explain later
                .into((ImageView) convertView);

        return convertView;
    }

    /*
    @Override
    public void clear(){
        imageUrls.clear();
    }

    public void addAll(String[] urls){
        imageUrls.ad
    }
    */


    /*
    private Context context;
    private int layoutResourceId;
    //private LayoutInflater inflater;
    private ArrayList imageUrls = new ArrayList();
    //private final List<String> urls = new ArrayList<String>();


    public GridViewAdapter(Context context, int layoutResourceId, ArrayList urls) {
        super(context, layoutResourceId, urls);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.imageUrls = urls;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        //View view = convertView;
        if (convertView == null) {
            imageView = new ImageView(context);
            //view = inflater.inflate(R.layout.grid_item_layout, parent, false);
            //view = new ImageView(context);
            //view.setScaleType(CENTER_CROP);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso
                .with(context)
                .load(imageUrls.get(position).toString())
                .fit() // will explain later
                .into(imageView);

        return imageView;
    }


    /*
    @Override public View getView(int position, View convertView, ViewGroup parent) {
        ImageView view = (ImageView) convertView;
        if (view == null) {
            view = new ImageView(context);
            view.setScaleType(CENTER_CROP);
        }

        // Get the image URL for the current position.
        String url = getItem(position);

        // Trigger the download of the URL asynchronously into the image view.
        Picasso.with(context) //
                .load(url) //
                .placeholder(R.drawable.placeholder) //
                .error(R.drawable.placeholder) //
                .fit() //
                .tag(context) //
                .into(view);

        return view;
    }

    @Override public int getCount() {
        return 0;
        //return urls.length;
    }

    @Override public String getItem(int position) {
        return urls[position].toString();
    }

    @Override public long getItemId(int position) {
        return position;
    }
    */

}
