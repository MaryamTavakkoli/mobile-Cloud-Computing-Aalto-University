package com.example.maryam.imagebarcode;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import java.io.InputStream;


public class MainActivity extends AppCompatActivity {

    //private static final int SELECT_PICTURE = 228;
    private static final int SELECT_GALLERY_CODE = 1;
    private static final int REQUIRE_HEIGHT = 200;
    private static final int REQUIRE_WIDTH = 200;

    Canvas canvas = new Canvas();
    //private String selectedImagePath;
    Button button;
    ImageView imageView;
    TextView textView;
    TextView textView2;
    Bitmap imgBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.btnPickPhoto);
        imageView = (ImageView) findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.txtNumPeople);
        textView2 = (TextView) findViewById(R.id.txtBarcode);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                //intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, SELECT_GALLERY_CODE);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ( requestCode == SELECT_GALLERY_CODE
                && resultCode == Activity.RESULT_OK){

            Uri selectedImageUri = data.getData();
            imgBitmap = getOriginalBitMap(selectedImageUri);
            imageView = (ImageView) findViewById(R.id.imageView);

                Frame frame = new Frame.Builder().setBitmap(imgBitmap).build();
                Detector<Face> detector = new FaceDetector.Builder(getApplicationContext())
                        .setTrackingEnabled(false)
                        .setLandmarkType(FaceDetector.ALL_LANDMARKS)
                        .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
                        .build();
                SparseArray<Face> faces = detector.detect(frame);
                textView.setText("" + faces.size());

                for (int i = 0; i < faces.size(); i++) {
                    Face eachFace = faces.valueAt(i);
                    drawBox(imgBitmap, eachFace.getPosition(), eachFace.getHeight(), eachFace.getWidth());
                }

                Frame barcodeFrame = new Frame.Builder().setBitmap(imgBitmap).build();
                BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(getApplicationContext())
                        .setBarcodeFormats(Barcode.ALL_FORMATS).build();
                SparseArray<Barcode> barcodes = barcodeDetector.detect(barcodeFrame);
                if (barcodes.size() > 0) {
                    textView2.setText("Yes");
                } else {
                    textView2.setText("No");
                }

                imageView.setImageBitmap(imgBitmap);
                }
    }

    public Bitmap drawBox(Bitmap imageBitmap, PointF position, float height, float width) {
        Paint myPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        myPaint.setStyle(Paint.Style.STROKE);
        myPaint.setColor(Color.rgb(0, 255, 0));
        myPaint.setStrokeWidth(5);

        canvas.setBitmap(imageBitmap);
        canvas.drawRect(position.x, position.y, position.x + width, position.y + height, myPaint);
        return imageBitmap;
    }

    public Bitmap getOriginalBitMap(Uri imageURI) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();

        //An inputStream can only be used once, declare another one for the real loading
        InputStream imageStream = null;
        InputStream tmpImageStream = null;

        try {
            imageStream = getContentResolver().openInputStream(imageURI);
            tmpImageStream = getContentResolver().openInputStream(imageURI);


        } catch (Exception e) {
            e.printStackTrace();
        }


        options.inJustDecodeBounds = true;


        bitmap = BitmapFactory.decodeStream(tmpImageStream, null, options);

        options.inSampleSize = calculateInSampleSize(options, REQUIRE_WIDTH, REQUIRE_HEIGHT);

        options.inJustDecodeBounds = false;

        bitmap = BitmapFactory.decodeStream(imageStream, null, options);

        Bitmap myBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(myBitmap);
        canvas.drawBitmap(bitmap, 0, 0, null);

        return myBitmap;
    }
    public int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}