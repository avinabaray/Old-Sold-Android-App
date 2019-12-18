package com.avinabaray.oldsold;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class SellerLoggedIn extends BaseActivity implements AdapterView.OnItemSelectedListener {

    public static final String I_CATEGORY = "i_category";
    public static final String I_TITLE = "i_title";
    public static final String I_DESC = "i_desc";
    public static final String SELLER_ID = "seller_id";
    public static final String I_IMAGE = "i_image";
    private EditText editTextItemTitle;
    private EditText editTextItemDesc;
    private ImageView imageViewSelectedImg;
    private String itemTitle;
    private String itemDesc;
    private String itemPhotoName;
    private String itemCatg = "Mobile Phones";
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST = 71;

    private AlertDialog.Builder alertBuilder;
    private CollectionReference mDocRef = FirebaseFirestore.getInstance().collection("items");
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;
    CommonMethods commonMethods = new CommonMethods();

    @Override
    public void onBackPressed() {
        // Uncomment the line below to enable BACK button
//        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_logged_in);
        alertBuilder = new AlertDialog.Builder(SellerLoggedIn.this);

        // Initialize Storage objects
        firebaseStorage = FirebaseStorage.getInstance();
        storageReference = firebaseStorage.getReference();

        // Initialize Views
        editTextItemTitle = findViewById(R.id.editTextItemTitle);
        editTextItemDesc = findViewById(R.id.editTextItemDesc);
        imageViewSelectedImg = findViewById(R.id.imageViewSelectedImg);

        // Setup Dropdown Menu
        List<String> itemCategories = new ArrayList<String>();
        itemCategories.add("Mobile Phones");
        itemCategories.add("Furniture");
        itemCategories.add("Home Appliances");
        itemCategories.add("Gadgets");

        Spinner spinnerItemCategory = findViewById(R.id.spinnerItemCategory);
        ArrayAdapter<String> itemCategoryAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                itemCategories
        );
        spinnerItemCategory.setAdapter(itemCategoryAdapter);
        spinnerItemCategory.setOnItemSelectedListener(this);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null )
        {
            filePath = data.getData();
            Log.wtf("File name", filePath.toString());
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageViewSelectedImg.setImageBitmap(bitmap);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public void selectPhoto(View view) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    public void uploadItem(View view) {
        itemTitle = editTextItemTitle.getText().toString();
        itemDesc = editTextItemDesc.getText().toString();

        itemPhotoName = MainActivity.CURRENT_USER_ID + "_" + UUID.randomUUID().toString();

        // Validating if all the fields are filled
        if(itemTitle.isEmpty() || itemDesc.isEmpty()) {
            commonMethods.createAlert(alertBuilder, "Please fill all the fields");
            return;
        }

        // Photo to be uploaded to Firebase Storage
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            final StorageReference ref = storageReference.child("item_images/"+ itemPhotoName);
            UploadTask uploadTask = ref.putFile(filePath);

            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        Log.wtf("URL", downloadUri.toString());
                        itemPhotoName = downloadUri.toString();

                        filePath = null;
                        imageViewSelectedImg.setImageResource(0);

                        // Data to be pushed to Firestore
                        Map<String, Object> itemDetails = new HashMap<String, Object>();
                        itemDetails.put(SELLER_ID, MainActivity.CURRENT_USER_ID);
                        itemDetails.put(I_CATEGORY, itemCatg);
                        itemDetails.put(I_TITLE, itemTitle);
                        itemDetails.put(I_IMAGE, itemPhotoName);
                        itemDetails.put(I_DESC, itemDesc);

                        mDocRef.add(itemDetails).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentReference> task) {
                                progressDialog.dismiss();
                                if (task.isSuccessful()) {
                                    editTextItemTitle.setText("");
                                    editTextItemDesc.setText("");
                                    commonMethods.createAlert(alertBuilder, "Item uploaded successfully!");
                                } else {
                                    String errorMsg = task.getException().getMessage() + " Try Again...";
                                    commonMethods.createAlert(alertBuilder, errorMsg);
                                }
                            }
                        });


                    } else {
                        // Handle failures
                        commonMethods.createAlert(alertBuilder, "Failed " + task.getException().getMessage());
                    }
                }
            });

//            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                    filePath = null;
//                    imageViewSelectedImg.setImageResource(0);
//
//                    // Data to be pushed to Firestore
//                    Map<String, Object> itemDetails = new HashMap<String, Object>();
//                    itemDetails.put(SELLER_ID, MainActivity.CURRENT_USER_ID);
//                    itemDetails.put(I_CATEGORY, itemCatg);
//                    itemDetails.put(I_TITLE, itemTitle);
//                    itemDetails.put(I_IMAGE, itemPhotoName);
//                    itemDetails.put(I_DESC, itemDesc);
//
//                    mDocRef.add(itemDetails).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
//                        @Override
//                        public void onComplete(@NonNull Task<DocumentReference> task) {
//                            progressDialog.dismiss();
//                            if (task.isSuccessful()) {
//                                editTextItemTitle.setText("");
//                                editTextItemDesc.setText("");
//                                commonMethods.createAlert(alertBuilder, "Item uploaded successfully!");
//                            } else {
//                                String errorMsg = task.getException().getMessage() + " Try Again...";
//                                commonMethods.createAlert(alertBuilder, errorMsg);
//                            }
//                        }
//                    });
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    progressDialog.dismiss();
//                    commonMethods.createAlert(alertBuilder, "Failed " + e.getMessage());
//                }
//            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
//                    double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
//                    progressDialog.setMessage("Uploaded " + (int)progress + "%");
//                }
//            });

        } else {
            commonMethods.createAlert(alertBuilder, "Please select a photo to upload the item");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        switch (pos) {
            case 0:
                itemCatg = "Mobile Phones";
                break;
            case 1:
                itemCatg = "Furniture";
                break;
            case 2:
                itemCatg = "Home Appliances";
                break;
            case 3:
                itemCatg = "Gadgets";
                break;
            default:
                itemCatg = "Unknown";
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // Do nothing
    }
}
