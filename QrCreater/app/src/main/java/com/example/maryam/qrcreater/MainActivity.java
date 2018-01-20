package com.example.maryam.qrcreater;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.encoder.QRCode;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText editText = (EditText)findViewById(R.id.txtInput);
        Button button = (Button)findViewById(R.id.btnCreateBarcode);
        final ImageView imageView = (ImageView) findViewById(R.id.imgBarcode);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                try {
                    Bitmap bitmap = TextToImageEncode (text);
                    imageView.setImageBitmap(bitmap);
                }
                catch (WriterException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    Bitmap TextToImageEncode(String Value) throws WriterException{
        BitMatrix bitMatrix;
        try {
            bitMatrix = new MultiFormatWriter().encode(Value,BarcodeFormat.DATA_MATRIX.QR_CODE,500,500, null);
        }
        catch (IllegalArgumentException Illegalargumentexception) {
            return null;
        }
        int bitMatrixWidth = bitMatrix.getWidth();
        int bitMatrixHeight = bitMatrix.getHeight();
        int [] pixels = new int[bitMatrixWidth*bitMatrixHeight];
        for (int y = 0; y < bitMatrixHeight; y ++)
        {
            int offset = y*bitMatrixWidth;
            for(int x = 0; x < bitMatrixWidth; x++)
            {
                pixels[offset+x] = bitMatrix.get(x,y)? Color.BLACK: Color.WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(bitMatrixWidth,bitMatrixHeight, Bitmap.Config.ARGB_4444);
        bitmap.setPixels(pixels, 0, 500, 0, 0, bitMatrixWidth, bitMatrixHeight);
        return bitmap;


    }
}
