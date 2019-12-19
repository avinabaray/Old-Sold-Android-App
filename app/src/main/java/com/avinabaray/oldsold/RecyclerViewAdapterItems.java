package com.avinabaray.oldsold;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecyclerViewAdapterItems extends RecyclerView.Adapter<RecyclerViewAdapterItems.ViewHolder> {

    private static final String TAG = "RecyclerViewAdapterItem";
    public static final String USER_ID = "user_id";
    public static final String USER_ROLE = "user_role";
    public static final String COMMENT_MSG = "comment_msg";
    private CollectionReference mDocRefToAddComment;
    private DocumentReference mDocRefToShowComment;
    private Context mContext;

    private ArrayList<String> itemPhotos = new ArrayList<>();
    private ArrayList<String> itemCatgs = new ArrayList<>();
    private ArrayList<String> itemTitles = new ArrayList<>();
    private ArrayList<String> itemDescs = new ArrayList<>();
    private ArrayList<String> seller_id = new ArrayList<>();
    private ArrayList<String> item_id = new ArrayList<>();

    public RecyclerViewAdapterItems(Context mContext, ArrayList<String> itemPhotos, ArrayList<String> itemCatgs, ArrayList<String> itemTitles, ArrayList<String> itemDescs, ArrayList<String> seller_id, ArrayList<String> item_id) {
        this.mContext = mContext;
        this.itemPhotos = itemPhotos;
        this.itemCatgs = itemCatgs;
        this.itemTitles = itemTitles;
        this.itemDescs = itemDescs;
        this.seller_id = seller_id;
        this.item_id = item_id;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: called.");

        Glide.with(mContext)
                .asBitmap()
                .load(itemPhotos.get(position))
                .into(holder.imageViewItemPhoto);
        holder.textViewItemCatg.setText(itemCatgs.get(position));
        holder.textViewItemTitle.setText(itemTitles.get(position));
        holder.textViewItemDesc.setText(itemDescs.get(position));

        mDocRefToShowComment = FirebaseFirestore.getInstance().collection("items").document(item_id.get(position));

        mDocRefToShowComment.collection("comments").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                // Comments present for this item
                StringBuilder commentsToShow = new StringBuilder();
                if (!queryDocumentSnapshots.isEmpty()) {
                    for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        commentsToShow.append("\n").append(documentSnapshot.getString("user_role")).append(": ").append(documentSnapshot.getString("comment_msg"));
                    }
                } else {
                    commentsToShow.append("No comments yet...\nBe the first to add one.");
                }
                holder.textViewShowComment.setText(commentsToShow.toString());
            }
        });

        // OnClickListener to perform action on click
        holder.imageViewSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on: " + itemTitles.get(position));
                String commentMsg = holder.editTextAddComment.getText().toString();

                if (commentMsg.isEmpty()) {
                    Toast.makeText(mContext, "Write a comment first", Toast.LENGTH_SHORT).show();
                } else {
                    mDocRefToAddComment = FirebaseFirestore.getInstance().collection("items").document(item_id.get(position)).collection("comments");

                    Map<String, Object> comment = new HashMap<String, Object>();
                    comment.put(USER_ID, MainActivity.CURRENT_USER_ID);
                    comment.put(USER_ROLE, MainActivity.CURRENT_USER_ROLE);
                    comment.put(COMMENT_MSG, commentMsg);

                    mDocRefToAddComment.add(comment).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentReference> task) {
                            if (task.isSuccessful()) {
                                Log.d("STATUS", "Comment saved successfully");
                                Toast.makeText(mContext, "Comment added successfully", Toast.LENGTH_SHORT).show();
                                holder.editTextAddComment.setText("");
                            } else {
                                Log.d("STATUS", task.getException().getMessage());
                                Toast.makeText(mContext, "Oops... Something went wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
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
        ImageView imageViewSend;
        EditText editTextAddComment;
        TextView textViewShowComment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recyclerItemLinearLayout = itemView.findViewById(R.id.recyclerItemLinearLayout);

            imageViewItemPhoto = itemView.findViewById(R.id.imageViewItemPhoto);
            textViewItemCatg = itemView.findViewById(R.id.textViewItemCatg);
            textViewItemTitle = itemView.findViewById(R.id.textViewItemTitle);
            textViewItemDesc = itemView.findViewById(R.id.textViewItemDesc);
            imageViewSend = itemView.findViewById(R.id.imageViewSend);
            editTextAddComment = itemView.findViewById(R.id.editTextAddComment);
            textViewShowComment = itemView.findViewById(R.id.textViewShowComment);

            textViewItemDesc.setMovementMethod(new ScrollingMovementMethod());

        }

    }

}
