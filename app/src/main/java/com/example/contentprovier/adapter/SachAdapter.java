package com.example.contentprovier.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.contentprovier.R;
import com.example.contentprovier.Sach;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SachAdapter extends ArrayAdapter<Sach> {

    private List<Sach> sachList;
    private Context context;
    private int resource;

    public SachAdapter(@NonNull Context context, int resource, @NonNull List<Sach> sachList) {
        super(context, resource, sachList);
        this.context = context;
        this.resource = resource;
        this.sachList = sachList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = LayoutInflater.from(context).inflate(this.resource, null);

        Sach sach = this.sachList.get(position);

        TextView tvId = view.findViewById(R.id.item_sach_tv_sachId);
        TextView tvTen = view.findViewById(R.id.item_sach_tv_ten);
        TextView tvArthor = view.findViewById(R.id.item_sach_tv_tacgia);

        tvId.setText(String.valueOf(sach.getId()));
        tvTen.setText(sach.getTitle());
        tvArthor.setText(sach.getAuthor());

        return view;
    }
}
