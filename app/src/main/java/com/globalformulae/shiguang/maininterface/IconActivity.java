package com.globalformulae.shiguang.maininterface;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.signature.StringSignature;
import com.globalformulae.shiguang.R;
import com.globalformulae.shiguang.utils.FileStorage;
import com.globalformulae.shiguang.utils.OkHttpUtil;
import com.globalformulae.shiguang.utils.PermissionsChecker;
import com.globalformulae.shiguang.utils.SPUtil;
import com.muddzdev.styleabletoastlibrary.StyleableToast;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class IconActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_PICK_IMAGE = 1; //相册选取
    private static final int REQUEST_CAPTURE = 2;  //拍照
    private static final int REQUEST_PICTURE_CUT = 3;  //剪裁图片
    private static final int REQUEST_PERMISSION = 4;  //权限请求

    private ImageView iv;       //头像预览
    private Button take_btn, album_btn,post_btn;
    private PermissionsChecker mPermissionsChecker; // 权限检测器
    static final String[] PERMISSIONS = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA};
    private Uri imageUri;//原图保存地址
    private boolean isClickCamera;
    private String imagePath;
    private String outputPath;
    private Uri outputUri;
    private String BasuUrl= OkHttpUtil.BASEURL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_icon);
        initViews();
        initListener();
        init();
    }

    private void initViews() {
        take_btn = (Button) findViewById(R.id.take_btn);
        album_btn = (Button) findViewById(R.id.album_btn);
        post_btn= (Button) findViewById(R.id.post_btn);
        iv = (ImageView) findViewById(R.id.iv);
        SharedPreferences sp=SPUtil.getSP(this,"user");
            Glide
                .with(this)
                .load(sp.getString("icon",null))
                .placeholder(R.mipmap.unlogged_icon)
                .signature(new StringSignature(String.valueOf(System.currentTimeMillis())))
                .into(iv);
        setActionBar();
    }

    private void initListener() {
        take_btn.setOnClickListener(this);
        album_btn.setOnClickListener(this);
        post_btn.setOnClickListener(this);
    }

    private void init() {
        mPermissionsChecker = new PermissionsChecker(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.take_btn:
                //检查权限(6.0以上做权限判断)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                        startPermissionsActivity();
                    } else {
                        openCamera();
                    }
                } else {
                    openCamera();
                }
                isClickCamera = true;
                break;
            case R.id.album_btn:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                        startPermissionsActivity();
                    } else {
                        selectFromAlbum();
                    }
                } else {
                    selectFromAlbum();
                }
                isClickCamera = false;
                break;
            case R.id.post_btn:
                doPostFile();
                setResult(888);
                break;
        }
    }

    /**
     * 打开系统相机
     */
    private void openCamera() {
        File file = new FileStorage().createPicFile();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(this, "com.globalformulae.shiguang.fileprovider", file);//通过FileProvider创建一个content类型的Uri
        } else {
            imageUri = Uri.fromFile(file);
        }
        Intent intent = new Intent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);//设置Action为拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//将拍取的照片保存到指定URI
        startActivityForResult(intent, REQUEST_CAPTURE);
    }

    /**
     * 从相册选择
     */
    private void selectFromAlbum() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(intent, REQUEST_PICK_IMAGE);
    }

    /**
     * 裁剪
     */
    private void cropPhoto() {
        File file = new FileStorage().createCropFile();
        outputPath=file.getAbsolutePath();
        outputUri = Uri.fromFile(file);//缩略图保存地址
        Intent intent = new Intent("com.android.camera.action.CROP");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(imageUri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("scale", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        startActivityForResult(intent, REQUEST_PICTURE_CUT);
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_PERMISSION,
                PERMISSIONS);
    }

    @TargetApi(19)
    private void handleImageOnKitKat(Intent data) {
        imagePath = null;
        imageUri = data.getData();
        if (DocumentsContract.isDocumentUri(this, imageUri)) {
            //如果是document类型的uri,则通过document id处理
            String docId = DocumentsContract.getDocumentId(imageUri);
            if ("com.android.providers.media.documents".equals(imageUri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.downloads.documents".equals(imageUri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            //如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(imageUri, null);
        } else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            //如果是file类型的Uri,直接获取图片路径即可
            imagePath = imageUri.getPath();
        }

        cropPhoto();
    }

    private void handleImageBeforeKitKat(Intent intent) {
        imageUri = intent.getData();
        imagePath = getImagePath(imageUri, null);
        cropPhoto();
    }

    private String getImagePath(Uri uri, String selection) {
        String path = null;
        //通过Uri和selection获取真实的图片路径
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_PICK_IMAGE://从相册选择
                if(data!=null){
                    if (Build.VERSION.SDK_INT >= 19) {
                        handleImageOnKitKat(data);
                    } else{
                        handleImageBeforeKitKat(data);
                    }
                }
                break;
            case REQUEST_CAPTURE://拍照
                if (resultCode == RESULT_OK) {
                    cropPhoto();
                }else {
                    Log.e("REQUEST_CAPTURE","FAILED");
                }
                break;
            case REQUEST_PICTURE_CUT://裁剪完成
                Bitmap bitmap = null;
                try {
                    if (isClickCamera) {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                    } else {
                        bitmap = BitmapFactory.decodeFile(imagePath);
                    }
                    Log.e("url",imageUri+"\n"+imagePath);
                    Bitmap icon= BitmapFactory.decodeStream(getContentResolver().openInputStream(outputUri));
                    Glide.clear(iv);
                    iv.setImageBitmap(icon);
                    post_btn.setVisibility(View.VISIBLE);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case REQUEST_PERMISSION://权限请求
                if (resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
                    finish();
                } else {
                    if (isClickCamera) {
                        openCamera();
                    } else {
                        selectFromAlbum();
                    }
                }
                break;
        }
    }


    public void doPostFile(){
        File file=new File(outputPath);
        if(!file.exists()){
            StyleableToast st3 = new StyleableToast
                    .Builder(IconActivity.this, "获取失败，请重新拍照或选择")
                    .withMaxAlpha()
                    .withBackgroundColor(getResources().getColor(R.color.colorAccent))
                    .withTextColor(Color.WHITE)
                    .withBoldText()
                    .build();
            st3.show();
            post_btn.setVisibility(View.GONE);
            return;
        }
        SharedPreferences sp= SPUtil.getSP(this,"user");
        //MediaType  所对应的parse在 mime type表中查询对应格式文件。
        MultipartBody.Builder multipartBodyBuilder=new MultipartBody.Builder();
        MultipartBody multipartBody=multipartBodyBuilder.setType(MultipartBody.FORM)
                .addFormDataPart("userid",String.valueOf(sp.getLong("userid",0)))
                .addFormDataPart("phone",sp.getString("phone",null))
                .addFormDataPart("mPhoto",sp.getString("phone","zxg")+".jpg", RequestBody.create(MediaType.parse("application/octet-stream"), file))
                .build();
        Request.Builder builder=new Request.Builder();
        Request request = builder.url(BasuUrl + "doPostIcon").post(multipartBody).build();
        OkHttpClient okHttpClient=new OkHttpClient();
        Call call=okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("123456789", "456");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int result=response.code();
                Log.e("123456789", String.valueOf(result));
                if(result==200){
                    finish();
                    setResult(888);
                }
            }
        });
    }
    private void setActionBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.icon_tb);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(888);
                finish();
            }
        });
    }
}
