package com.yc.choosepictest;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class MainActivity extends Activity {

	public static final int TAKE_PHOTO=1;
	public static final int CROP_PHOTO=2;
	private Button takePhoto;
	private Button chooseFromAlbum;
	private ImageView picture;
	private Uri imageUri;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.activity_main);
		
		takePhoto=(Button)findViewById(R.id.take_photo);
		picture=(ImageView)findViewById(R.id.picture);
		takePhoto.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, "Take Photo", Toast.LENGTH_SHORT).show();
				File outputImage=new File(Environment.getExternalStorageDirectory(),"tempImage.jpg");
				
				try{
					if(outputImage.exists()){
						
						outputImage.delete();
					}
					outputImage.createNewFile();
					
				}catch(Exception e){
					e.printStackTrace();
					
				}
				
				imageUri=Uri.fromFile(outputImage);
				Intent intent=new Intent("android.media.action.IMAGE_CAPTURE");
				
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent,TAKE_PHOTO);	
			}
		});
		
		
		chooseFromAlbum=(Button)findViewById(R.id.choose_from_album);
		chooseFromAlbum.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				File outputImage=new File(Environment.getExternalStorageDirectory(),"output_image.jpg");
				
				try{
					if(outputImage.exists()){
						outputImage.delete();
						
					}
					outputImage.createNewFile();
					
				}catch(Exception e){
					e.printStackTrace();
					
				}
				imageUri=Uri.fromFile(outputImage);
				Intent intent=new Intent("android.intent.action.GET_CONTENT");
				intent.setType("image/*");
				intent.putExtra("crop", true);
				intent.putExtra("scale", true);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent,CROP_PHOTO);
							
			}
	
		});
		
	}

	protected void onActivityResult(int requestCode,int resultCode,Intent data){
		
		switch(requestCode){
		case TAKE_PHOTO:
			if(resultCode==RESULT_OK){
				Intent intent=new Intent("com.android.camera.action.CROP");
				intent.setDataAndType(imageUri, "image/*");
				intent.putExtra("scale", true);
				intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
				startActivityForResult(intent,CROP_PHOTO);
				
				
			}
		 break;
		case CROP_PHOTO:
			if(resultCode==RESULT_OK){
				try{
					Bitmap bitmap=BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
					
					picture.setImageBitmap(bitmap);
					
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		      break;
		  default:
			  break;
		}
	}

}
