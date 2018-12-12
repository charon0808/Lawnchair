package ch.deletescape.lawnchair;


import android.app.WallpaperManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import android.os.Environment;
import android.view.View.OnClickListener;

import java.io.*;

public class TestActivity extends AppCompatActivity {

    private ImageView	iv;
    private Bitmap		bitmap;
    private String     photoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        iv = findViewById(R.id.picture);


        // 拍照
        findViewById(R.id.take_photo).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        findViewById(R.id.take_photo_file).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhotoFile();
            }
        });

        findViewById(R.id.go_back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.set_wallpaper).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                set_wallpaper();
            }
        });
    }

    public boolean setPhotoPath(){
        try {
            photoPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath() + "/" + System.currentTimeMillis() + ".png";  // 生成文件路径
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
    public void set_wallpaper(){
        WallpaperManager manager = WallpaperManager.getInstance(this);
        try {
            manager.setBitmap(bitmap);
            Toast.makeText(this,"set wallpaper successfully",Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            try {
                e.printStackTrace();
                Toast.makeText(this, "failed to set wallpaper", Toast.LENGTH_SHORT).show();
            }
            catch(Exception ee){
                ee.printStackTrace();
                Toast.makeText(this, "failed", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {   // 返回上一层
        Intent intent=new Intent();
        setResult(RESULT_OK, intent);
        super.onBackPressed();
    }

    public void takePhoto() {
        final  int			TAKE_PHOTO		  = 1;	// 直接展示相机返回的缩略图
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, TAKE_PHOTO);  // 调用机器相机并返回相机默认返回的缩略图
    }

    public void takePhotoFile(){
        final  int         TAKE_PHOTO_FILE = 2;   // 从文件系统中找原图
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photoFile=new File(photoPath);

        Uri photoUrl=FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".provider", photoFile); // 设置照片路径
        intent.putExtra(MediaStore.EXTRA_OUTPUT,photoUrl);
        startActivityForResult(intent,TAKE_PHOTO_FILE);   // 调用相机并返回保存在文件系统中的原图
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final  int         TAKE_PHOTO_FILE = 2;   // 从文件系统中找原图
        if (resultCode == RESULT_OK) {
            if (requestCode==TAKE_PHOTO_FILE) {
                File photoFile = new File(photoPath);
                if (photoFile.exists()) {
                    bitmap = BitmapFactory.decodeFile(photoFile.getAbsolutePath());
                    iv.setVisibility(View.VISIBLE);
                    iv.setImageBitmap(bitmap);
                    Toast.makeText(this, "image saved at " + photoPath, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "image file does not exist!", Toast.LENGTH_LONG).show();
                }
            }
            else {
                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                    System.gc();
                }
                // 获取系统默认返回图片(缩略图)
                Bundle bundle = data.getExtras();
                if (bundle!=null)
                    bitmap = (Bitmap) bundle.get("data");

                // 显示图片
                if (bitmap!=null)
                    iv.setImageBitmap(bitmap);
            }
        }
    }
}
