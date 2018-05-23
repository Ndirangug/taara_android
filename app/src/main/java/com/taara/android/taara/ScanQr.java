package com.taara.android.taara;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

public class ScanQr extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);
    }

   /* public void scan(View view) {
        TextView error = findViewById(R.id.error);
        ImageView qr = findViewById(R.id.qr);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.qr);
        qr.setImageBitmap(bitmap);

        BarcodeDetector detector = new BarcodeDetector.Builder(getApplicationContext())
                                                        .setBarcodeFormats(Barcode.DATA_MATRIX | Barcode.QR_CODE)
                                                        .build();
        if (!detector.isOperational()){
            error.setText("Not ready!!!");

        }else {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<Barcode> barcodeSparseArray = detector.detect(frame);
            Barcode thiscode = barcodeSparseArray.valueAt(0);
            TextView textView = findViewById(R.id.txtContent);
            textView.setText(thiscode.rawValue);
        }
    }*/
}
