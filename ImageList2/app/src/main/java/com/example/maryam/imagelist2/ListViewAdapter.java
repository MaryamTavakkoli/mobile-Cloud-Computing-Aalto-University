package com.example.maryam.imagelist2;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Maryam on 24/10/2017.
 */


class ListViewAdapter extends ArrayAdapter<String> {
    private final Context context;
    private final List<String> itemname;
    private final List<String>  imgid;

    public ListViewAdapter(Context context, List<String>  itemname, List<String>  imgid) {
        super(context, R.layout.list_item, itemname);

        this.context = context;
        this.itemname = itemname;
        this.imgid = imgid;


    }


    @Override
    public int getViewTypeCount() {
        return 2;
    }

//    @Override
//    public int getItemViewType(int position) {
//        return type;
//    }

    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        final View rowView;


        rowView = inflater.inflate(R.layout.list_item, null, true);

        TextView txtTitle = (TextView) rowView.findViewById(R.id.textview);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageview);

        txtTitle.setText(itemname.get(position));

        Picasso.with(context)
                .load(imgid.get(position))
                .into(imageView);

        return rowView;

    }
}
