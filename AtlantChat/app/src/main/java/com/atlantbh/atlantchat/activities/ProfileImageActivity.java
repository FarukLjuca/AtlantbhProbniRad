package com.atlantbh.atlantchat.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.atlantbh.atlantchat.R;
import com.atlantbh.atlantchat.api.ImgurApi;
import com.atlantbh.atlantchat.api.UserApi;
import com.atlantbh.atlantchat.model.ImageImgur;
import com.atlantbh.atlantchat.model.ImgurResponse;
import com.atlantbh.atlantchat.model.Session;
import com.atlantbh.atlantchat.model.helpers.SuccessResponseUser;
import com.atlantbh.atlantchat.utils.AppUtil;

import java.io.ByteArrayOutputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

public class ProfileImageActivity extends Activity {
    static final int REQUEST_IMAGE_CAPTURE = 1;

    // Disable register button until response arrives.
    // This will prevent two or more requests from being fired.
    private boolean enable = true;
    private String base64Image;
    private String imageUrl = "";

    @BindView(R.id.ivProfileImagePlaceholder)
    ImageView imagePlaceholder;
    @BindView(R.id.pbRegister)
    ProgressBar progressBar;
    @BindView(R.id.btRegister)
    Button registerButton;
    CircleImageView image;

    private Context getContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_image);
        ButterKnife.bind(this);

        image = (CircleImageView) findViewById(R.id.ivProfileImage);
    }

    public void changeImage(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            image.setImageBitmap(imageBitmap);

            image.setVisibility(View.VISIBLE);
            imagePlaceholder.setVisibility(View.GONE);

            Uri tempUri = getImageUri(getContext(), imageBitmap);

            Bitmap bm = BitmapFactory.decodeFile(getRealPathFromURI(tempUri));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();

            base64Image = Base64.encodeToString(b, Base64.DEFAULT);
        }
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public void registerClick(View view) {
        if (enable) {
            disableContinue();
            Retrofit retrofitImgur = AppUtil.getImgurRetrofit();
            ImgurApi imgurApi = retrofitImgur.create(ImgurApi.class);
            Call<ImgurResponse> callImgur = imgurApi.upload(new ImageImgur(base64Image, "base64"));
            callImgur.enqueue(new Callback<ImgurResponse>() {
                @Override
                public void onResponse(Response<ImgurResponse> response, Retrofit retrofit) {
                    if (response.body() != null) {
                        imageUrl = response.body().getData().getLink();
                        Log.d("***********", imageUrl);

                        Retrofit retrofitMain = AppUtil.getRetrofit();
                        UserApi userApi = retrofitMain.create(UserApi.class);
                        Call<SuccessResponseUser> call = userApi.changeProfile(Session.getUserId(), imageUrl);
                        call.enqueue(new Callback<SuccessResponseUser>() {
                            @Override
                            public void onResponse(Response<SuccessResponseUser> response, Retrofit retrofit) {
                                if (response.body() != null) {
                                    SuccessResponseUser successResponse = response.body();

                                    if (!successResponse.isSuccess()) {
                                        Toast.makeText(getContext(), "More than one user is registered with same email", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getContext(), "Successful login", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(getContext(), ChatActivity.class));
                                    }
                                } else {
                                    Log.e(AppUtil.LOG_NAME, "Server returned empty body during login");
                                }
                                enableContinue();
                            }

                            @Override
                            public void onFailure(Throwable t) {
                                t.printStackTrace();
                            }


                        });
                    } else {
                        Log.e(AppUtil.LOG_NAME, "Image upload returned empty body");
                    }
                    enableContinue();
                }

                @Override
                public void onFailure(Throwable t) {
                    t.printStackTrace();
                }
            });
        }
    }

    private void enableContinue() {
        enable = true;
        progressBar.setVisibility(View.GONE);
        registerButton.setVisibility(View.VISIBLE);
    }

    private void disableContinue() {
        enable = false;
        progressBar.setVisibility(View.VISIBLE);
        registerButton.setVisibility(View.GONE);
    }
}
