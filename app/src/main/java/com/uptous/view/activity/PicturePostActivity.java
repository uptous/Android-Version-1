package com.uptous.view.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.uptous.R;
import com.uptous.controller.apiservices.APIServices;
import com.uptous.controller.apiservices.ServiceGenerator;
import com.uptous.controller.utils.ConnectionDetector;
import com.uptous.controller.utils.Helper;
import com.uptous.controller.utils.UserPicture;
import com.uptous.model.CommnunitiesResponseModel;
import com.uptous.model.CommunityTitleModel;
import com.uptous.model.PostCommentResponseModel;
import com.uptous.sharedpreference.Prefs;
import com.uptous.view.adapter.AlbumListAdapter;
import com.uptous.view.adapter.CommunityListAdapter;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * FileName : PicturePostActivity
 * Description : User post picture show on feed
 * Dependencies : CommunityListAdapter
 */

public class PicturePostActivity extends BaseActivity implements View.OnClickListener {

    private ImageView mImageViewBack;

    private CommunityListAdapter mCommunityListAdapter;

    private Spinner mSpinnerCommunity, mSpiinerTitle;

    private TextView mTextViewAlbum, mTextViewCamera;

    private String mAlbumTitle, mImageCaption, mAuthenticationId, mAuthenticationPassword;
    public static String DEF_TITLE = "ADD NEW ALBUM";
    private List<String> titleListing = new ArrayList<String>();
    private List<Integer> titleID = new ArrayList<Integer>();
    private EditText mEditTextSubject, mEditTextFileName;
    private ImageView RequestImageView;
    private Button mButtonUpload, mButtonaddMore;
    private Boolean isNewAlbum=false;
    private int mCommunityID, mAlbumID;

    public static final int LOAD_IMAGE_RESULTS = 1;

    private Helper mHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_post);

        initView();


        if (ConnectionDetector.isConnectingToInternet(PicturePostActivity.this)) {
            getApiCommunityList();
        } else {
            showToast(getString(R.string.network_error));
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_view_album:
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, LOAD_IMAGE_RESULTS);
                break;
            case R.id.addbutton:
                AddImageOption();

                break;
            case R.id.image_view_back:
                finish();
                break;

            case R.id.button_upload:
                mHelper.keyBoardHidden(PicturePostActivity.this);
                if (mAlbumTitle != "")
                    mAlbumTitle = mEditTextFileName.getText().toString().replace("\n", "<br>");//Image Caption

                mImageCaption = mEditTextSubject.getText().toString().replace("\n", "<br>"); //Album title


                if (mImageCaption.length() > 0) {
                    uploadedImageCount = 0;//Reset count
                    if (isImageAdded()) {
                        showProgressDialog("Please wait.. This may take a few minutes");
                        if (isNewAlbum) {
//                            for(String path:ImagePathList){
//                                postNewPicturePost(path);
                           // for (int j = 0; j < ImagePathList.size(); j++) {
                            if(ImagePathList.size()>0){
                                String path = ImagePathList.get(0);
                                postNewPicturePost(path,ImagePathList);
                            }
                            //}
                        } else {
                            //for(String path:ImagePathList){
                            for (int j = 0; j < ImagePathList.size(); j++) {
                                String path = ImagePathList.get(j);
                                Log.i("PicturePostActivity", "trying upload " + j);
                                postEditPicturePost(path);
                            }
                            //}
                        }
                    } else {
                        showToast("Please select image");
                    }

                } else {
                    showToast(getString(R.string.fill_all_field));
                }
                break;
        }
    }


    private void AddImageOption() {
        LinearLayout imgLayout = (LinearLayout) findViewById(R.id.layout_image);
        int childCount = imgLayout.getChildCount();
        if (childCount < 5) {

            LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
            View ll = inflater.inflate(R.layout.row_image, null);
            ll.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT));
            ImageView addImage = (ImageView) ll.findViewById(R.id.image);
            addImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selectImage((ImageView) view);
                }
            });
            imgLayout.addView(ll);


            if (childCount == 4)
                findViewById(R.id.addbutton).setVisibility(View.GONE);
        }

    }

    private boolean isImageAdded() {
        if (ImagePathList.size() > 0)
            return true;
        else {
            return false;
        }
    }

    // It will store all the path associated with the selected image
    private List<String> ImagePathList = new ArrayList<>(5);

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == LOAD_IMAGE_RESULTS && resultCode == RESULT_OK && null != data) {

            Bitmap yourImage;
            Uri selectedImageUri = data.getData();
            try {
                if (selectedImageUri == null) {
                    Bundle extra2 = data.getExtras();
                    yourImage = extra2.getParcelable("data");
                } else {
                    yourImage = new UserPicture(selectedImageUri, getContentResolver()).getBitmap();
                }

                // convert bitmap to byte
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                if (yourImage != null) {
                    yourImage.compress(Bitmap.CompressFormat.PNG, 100, stream);
                }
                byte imageInByte[] = stream.toByteArray();

                String mImagePath = Base64.encodeToString(imageInByte, Base64.DEFAULT);
                ImagePathList.add(mImagePath);
                try {
                    RequestImageView.setImageBitmap(yourImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void initView() {
        mHelper = new Helper();

        //Local Variables Initialization
        TextView textViewTitle = (TextView) findViewById(R.id.text_view_title);
        LinearLayout linearLayoutNavigation = (LinearLayout) findViewById(R.id.imgmenuleft);
        ImageView imageViewFilter = (ImageView) findViewById(R.id.image_view_down);
        //Global Variables Initialization
        mEditTextFileName = (EditText) findViewById(R.id.edit_text_file_name_picture_post);
        mEditTextSubject = (EditText) findViewById(R.id.edit_text_subject_picture_post);

        mImageViewBack = (ImageView) findViewById(R.id.image_view_back);

        mSpinnerCommunity = (Spinner) findViewById(R.id.spinner_Community);
        mSpiinerTitle = (Spinner) findViewById(R.id.spinner_title);
        configSpinner(mSpiinerTitle);

        mTextViewAlbum = (TextView) findViewById(R.id.text_view_album);
        mTextViewCamera = (TextView) findViewById(R.id.text_view_camera);

        mButtonUpload = (Button) findViewById(R.id.button_upload);
        mButtonaddMore = (Button) findViewById(R.id.addbutton);

        textViewTitle.setText(R.string.picture_post);
        imageViewFilter.setVisibility(View.GONE);
        linearLayoutNavigation.setVisibility(View.GONE);
        mImageViewBack.setVisibility(View.VISIBLE);
        textViewTitle.setVisibility(View.VISIBLE);
        AddImageOption();
        clickListenerOnViews();

        getData();
    }

    private AlbumListAdapter SpinnerAlbumAdapter;

    private void configSpinner(final Spinner spinner) {

        // titleListing.add(DEF_TITLE);
        SpinnerAlbumAdapter = new AlbumListAdapter((Activity) this, titleListing);

        //   SpinnerAlbumAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(SpinnerAlbumAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedtext = spinner.getItemAtPosition(i).toString();
                if (selectedtext.equals(DEF_TITLE)) {
                    isNewAlbum = true;
                    mAlbumID = titleID.get(i);
                    mEditTextSubject.setVisibility(View.VISIBLE);
                    mEditTextSubject.setText("");
                    mEditTextFileName.setVisibility(View.VISIBLE);
                    mEditTextFileName.setText("");
                } else {
                    isNewAlbum = false;
                    mAlbumID = titleID.get(i);
                    mEditTextSubject.setVisibility(View.GONE);
                    mEditTextSubject.setText(selectedtext);
                    mEditTextFileName.setVisibility(View.GONE);
                    mEditTextFileName.setText(selectedtext);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    // Method to Get data from SharedPreference
    private void getData() {
        mAuthenticationId = Prefs.getAuthenticationId(this);
        mAuthenticationPassword = Prefs.getAuthenticationPassword(this);
    }

    //Method to set on clickListener on views
    private void clickListenerOnViews() {
        mButtonUpload.setOnClickListener(this);
        mTextViewAlbum.setOnClickListener(this);
        mTextViewCamera.setOnClickListener(this);
        mImageViewBack.setOnClickListener(this);
        mButtonaddMore.setOnClickListener(this);
    }

    // Get webservice to get communities
    private void getApiCommunityList() {
        showProgressDialog();
        APIServices service =
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
        Call<List<CommnunitiesResponseModel>> call = service.GetCommunity();

        call.enqueue(new Callback<List<CommnunitiesResponseModel>>() {
            @Override
            public void onResponse(Call<List<CommnunitiesResponseModel>> call, Response<List<CommnunitiesResponseModel>> response) {
                try {
                    hideProgressDialog();

                    if (response.body() != null) {
                        final List<CommnunitiesResponseModel> eventResponseModels = response.body();

                        CommnunitiesResponseModel resultsEntity = new CommnunitiesResponseModel();
                        resultsEntity.setName("SELECT COMMUNITY");
                        resultsEntity.setId("-1");
                        eventResponseModels.add(eventResponseModels.size(), resultsEntity);
                        mCommunityListAdapter = new CommunityListAdapter(PicturePostActivity.this, eventResponseModels);
                        mSpinnerCommunity.setAdapter(mCommunityListAdapter);
                        mSpinnerCommunity.setSelection(mCommunityListAdapter.getCount());
                        mSpinnerCommunity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                mCommunityID = eventResponseModels.get(i).getId();
                                if (mCommunityID != 0)
                                    getTitlebyCommunity(mCommunityID);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    } else {
                        showLogOutDialog();
                    }

                } catch (Exception e) {
                    Log.d("onResponse", "There is an error");
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Call<List<CommnunitiesResponseModel>> call, Throwable t) {
                Log.d("onFailure", t.toString());
                hideProgressDialog();
                showToast(getString(R.string.error));
            }

        });
    }

    int uploadedImageCount = 0;

    // Post webservice to post Picture in new Album
    private void postNewPicturePost(String Image,final List<String> imagePathList) {
        APIServices service =
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
        Call<PostCommentResponseModel> call = service.PostNewPicture(mImageCaption, mCommunityID, mAlbumTitle, "File", Image);


        call.enqueue(new retrofit2.Callback<PostCommentResponseModel>() {
            @Override
            public void onResponse(Call<PostCommentResponseModel> call, Response<PostCommentResponseModel> response) {
                PostCommentResponseModel body = response.body();
                if (body != null) {
                            if (response.isSuccessful()) {
                        mAlbumID= body.getAlbumId();
                        Log.i("PicturePostActivity", "mAlbumID " +mAlbumID);
                        Prefs.setPicturePost(PicturePostActivity.this, "picture");
                        Prefs.setFeed(PicturePostActivity.this, null);
                        uploadedImageCount++;
                        for(int i=1;i<imagePathList.size();i++)
                        postEditPicturePost(imagePathList.get(i));

                    } else
                        showToast(getString(R.string.error) + response.toString());
                }
            }

            @Override
            public void onFailure(Call<PostCommentResponseModel> call, Throwable t) {
                hideProgressDialog();
                showToast(getString(R.string.error));


            }
        });
    }


    // Post webservice to post Picture in specific album
    private void postEditPicturePost(String ImagePath) {
       //

        APIServices service =
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
        Call<PostCommentResponseModel> call = service.PostPictureinAlbum(mAlbumID, mCommunityID, mAlbumTitle, "File", ImagePath);


        call.enqueue(new retrofit2.Callback<PostCommentResponseModel>() {
            @Override
            public void onResponse(Call<PostCommentResponseModel> call, Response<PostCommentResponseModel> response) {


                if (response.body() != null) {

                    if (response.isSuccessful()) {
                        Prefs.setPicturePost(PicturePostActivity.this, "picture");

                        Prefs.setFeed(PicturePostActivity.this, null);
                        Log.i("PicturePostActivity", "uploaded ... success " + uploadedImageCount);
                        uploadedImageCount++;
                        if (uploadedImageCount >= ImagePathList.size()) {
                            hideProgressDialog();
                            finish();
                        }

                        //finish();

                    } else {
                        showToast(getString(R.string.error) + response.toString());
                        Log.e("PicturePostActivity", "uploaded ... failed level 2 " + response.message());
                    }
                }
            }

            @Override
            public void onFailure(Call<PostCommentResponseModel> call, Throwable t) {
                hideProgressDialog();
                showToast(getString(R.string.error));
                Log.e("PicturePostActivity", "uploaded ... failed level 1" + t.toString());

            }
        });
    }

    //Image Selector
    private void selectImage(ImageView view) {
        RequestImageView = view;
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, LOAD_IMAGE_RESULTS);
                } else if (items[item].equals("Choose from Library")) {
                    Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, LOAD_IMAGE_RESULTS);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    // private List<CommunityTitleModel> titleList=new ArrayList<>();
    private void getTitlebyCommunity(int mCommunityID) {
        showProgressDialog();
        APIServices service =
                ServiceGenerator.createService(APIServices.class, mAuthenticationId, mAuthenticationPassword);
        Call<List<CommunityTitleModel>> call = service.GetTitleInCommunity(mCommunityID);

        call.enqueue(new retrofit2.Callback<List<CommunityTitleModel>>() {
            @Override
            public void onResponse(Call<List<CommunityTitleModel>> call, Response<List<CommunityTitleModel>> response) {

                if (response.body() != null) {
                    populateTitleListing(response.body());
                } else hideProgressDialog();
            }


            @Override
            public void onFailure(Call<List<CommunityTitleModel>> call, Throwable t) {
                hideProgressDialog();
                showToast(getString(R.string.error));
            }

        });
    }

    private void populateTitleListing(final List<CommunityTitleModel> communityList) {


        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                titleListing.clear();
                titleID.clear();
                titleListing.add(DEF_TITLE);
                titleID.add(0);
            }

            @Override
            protected Void doInBackground(Void... voids) {
                for (int i = 0; i < communityList.size(); i++) {
                    titleListing.add(communityList.get(i).getTitle().replaceAll("%20", " "));
                    titleID.add(communityList.get(i).getId());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                hideProgressDialog();
                mSpiinerTitle.setSelection(0);
                SpinnerAlbumAdapter.notifyDataSetChanged();
            }
        }.execute();

    }


}

