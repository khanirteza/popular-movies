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

}
