package com.idt.syed.imageapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.idt.syed.imageapplication.data.ImageContract;
import com.idt.syed.imageapplication.utils.ImageUtil;


public class MainActivity extends AppCompatActivity {
    EditText urlString;
    Button button;
    ImageView beforeIMG;
    ImageView afterIMG;
    public ImageReciever receiver;
    TextView last;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupServiceReceiver(); //Sets up the receiver to get results from the service

        urlString= (EditText) findViewById(R.id.url);
        button = (Button) findViewById(R.id.button);
         beforeIMG = (ImageView) findViewById(R.id.before);
         afterIMG =(ImageView) findViewById(R.id.after);
        last = (TextView)  findViewById(R.id.last_img);
         uri = Uri.parse("content://"+ImageContract.CONTENT_AUTHORITY+ImageContract.PATH_IMAGE); // To access  the stored images I have used content provider


        Cursor cursor = getContentResolver().query(uri,null,null,null,null,null);
        //This shows the last processed image in the database
        if(cursor.getCount()>0 ) {
            int columnIMGIndex = cursor.getColumnIndex(ImageContract.imageEntry.url_picture);
            cursor.moveToLast();
            byte[] img = cursor.getBlob(columnIMGIndex);
            Bitmap bitmap = ImageUtil.getImage(img);
            Bitmap rotatedBitmap = ImageUtil.rotateImage(180, bitmap);

            beforeIMG.setImageBitmap(rotatedBitmap);
        }
        cursor.close();
    }

    public  void onClick(View v){


        String stringurl = urlString.getText().toString();
        ImageLinkValidator imageLinkValidator = new ImageLinkValidator();
        Boolean validURL = imageLinkValidator.validate(stringurl); // validates and returns true if  the link provided ends with a image file

        if(validURL){
            Intent intent = new Intent(this, NetworkIntentService.class);           //we start a intent service to download the image
            intent.putExtra("INTENT_URL", stringurl);
            intent.putExtra("receiver", receiver);                                                              // We setup the reciever which extents  ResultReceiver instead of
                                                                                                                                                        //Broadcast receiver because there is only one activity waiting to receive the response
            startService(intent);
        }
        else
            Toast.makeText(this,"Please enter a valid URL",Toast.LENGTH_LONG).show();
    }

    public void setupServiceReceiver() {
        receiver= new ImageReciever(new Handler());
        // This is where we specify what happens when data is received from the service
        receiver.setReceiver(new ImageReciever.Receiver() {
            @Override
            public void onReceiveResult(int resultCode, Bundle resultData) {
                if (resultCode == RESULT_OK) {
                    byte[] resultValue = resultData.getByteArray("resultValue");

                    ContentValues contentValues = new ContentValues();
                    contentValues.put(ImageContract.imageEntry.url_picture, resultValue);
                    Uri returnedUri = getContentResolver().insert(uri,contentValues);
                    last.setText("Downloaded Image:");

                    Cursor cursor = getContentResolver().query(uri,null,null,null,null,null);

                    int columnIMGIndex = cursor.getColumnIndex(ImageContract.imageEntry.url_picture);
                    cursor.moveToLast();
                    byte[] img = cursor.getBlob(columnIMGIndex);
                    Bitmap bitmap = ImageUtil.getImage(img) ;
                    beforeIMG.setImageBitmap(bitmap);
                    Bitmap rotatedBitmap = ImageUtil.rotateImage(180,bitmap);
                    afterIMG.setImageBitmap(rotatedBitmap);
                    cursor.close();
                } else
                    Toast.makeText(getApplicationContext(),"The link provided does not return a image", Toast.LENGTH_LONG).show();
            }
        });
    }
}
