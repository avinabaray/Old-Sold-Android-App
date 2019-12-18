package com.avinabaray.oldsold;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class RecyclerViewAdapterItems extends RecyclerView.Adapter<RecyclerViewAdapterItems.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapterItem";
    private Context mContext;

    private ArrayList<String> itemPhotos = new ArrayList<>();
    private ArrayList<String> itemCatgs = new ArrayList<>();
    private ArrayList<String> itemTitles = new ArrayList<>();
    private ArrayList<String> itemDescs = new ArrayList<>();

    public RecyclerViewAdapterItems(Context mContext, ArrayList<String> itemPhotos, ArrayList<String> itemCatgs, ArrayList<String> itemTitles, ArrayList<String> itemDescs) {
        this.mContext = mContext;
        this.itemPhotos = itemPhotos;
        this.itemCatgs = itemCatgs;
        this.itemTitles = itemTitles;
        this.itemDescs = itemDescs;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        Glide.with(mContext)
                .asBitmap()
                .load(itemPhotos.get(position))
                .into(holder.imageViewItemPhoto);
        holder.textViewItemCatg.setText(itemCatgs.get(position));
        holder.textViewItemTitle.setText(itemTitles.get(position));
        holder.textViewItemDesc.setText(itemDescs.get(position));


        // OnClickListener to perform action on click
        holder.recyclerItemLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + itemTitles.get(position));
                Toast.makeText(mContext, itemTitles.get(position), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        // No. of times to iterate the RecyclerView
        return itemTitles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout recyclerItemLinearLayout;

        ImageView imageViewItemPhoto;
        TextView textViewItemCatg;
        TextView textViewItemTitle;
        TextView textViewItemDesc;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recyclerItemLinearLayout = itemView.findViewById(R.id.recyclerItemLinearLayout);

            imageViewItemPhoto = itemView.findViewById(R.id.imageViewItemPhoto);
            textViewItemCatg = itemView.findViewById(R.id.textViewItemCatg);
            textViewItemTitle = itemView.findViewById(R.id.textViewItemTitle);
            textViewItemDesc = itemView.findViewById(R.id.textViewItemDesc);

        }

    }

}
