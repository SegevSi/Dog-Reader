package bagrut.project.dogreader;

import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageCapture;
import androidx.camera.core.ImageCaptureException;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.common.util.concurrent.ListenableFuture;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.os.Handler;
import android.os.Looper;

public class ContinuousCaptureActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView textView;
    private ExecutorService cameraExecutor;
    private Handler handler;
    private ImageCapture imageCapture;
    private final int captureInterval = 2000; // Capture every 2 seconds (adjust as needed)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continuous_capture);
        imageView = findViewById(R.id.image_view);
        textView = findViewById(R.id.textView);
        cameraExecutor = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());

        // Initialize ImageCapture and other camera components here
        startCamera();
    }

    private void startCamera() {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture = ProcessCameraProvider.getInstance(this);

        cameraProviderFuture.addListener(() -> {
            try {
                ProcessCameraProvider cameraProvider = cameraProviderFuture.get();

                // Unbind any previous use cases before binding
                cameraProvider.unbindAll();

                // Set up Preview and ImageCapture use cases
                Preview preview = new Preview.Builder().build();
                imageCapture = new ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .build();

                // Select back camera
                CameraSelector cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;

                // Bind the use cases to the camera with lifecycle
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageCapture);

                // Start capturing images continuously
                startContinuousCapture();

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));
    }

    private void startContinuousCapture() {
        // Schedule continuous image capture every 'captureInterval' milliseconds
        handler.postDelayed(this::captureAndAnalyzeImage, captureInterval);
    }

    private void captureAndAnalyzeImage() {
        imageCapture.takePicture(ContextCompat.getMainExecutor(this), new ImageCapture.OnImageCapturedCallback() {
            @Override
            public void onCaptureSuccess(@NonNull ImageProxy image) {
                Bitmap bitmap = imageToBitmap(image);
                if (bitmap != null) {
                    analyzeImage(bitmap);
                }
                image.close();

                // Continue capturing images
                handler.postDelayed(ContinuousCaptureActivity.this::captureAndAnalyzeImage, captureInterval);
            }

            @Override
            public void onError(@NonNull ImageCaptureException exception) {
                exception.printStackTrace();
                // Continue capturing images even on error
                handler.postDelayed(ContinuousCaptureActivity.this::captureAndAnalyzeImage, captureInterval);
            }
        });
    }

    private void analyzeImage(Bitmap bitmap) {
        Pets_Emotion_Clc_Model clc = new Pets_Emotion_Clc_Model(bitmap, getApplicationContext());

        DBHandler dbHandler = new DBHandler(getApplicationContext());
        dbHandler.addData(clc.get_class());

        Bitmap originalBitmap = clc.image; // original bitmap with dimensions 128x128x3

        if (originalBitmap == null) {
            return;
        }

        int originalWidth = originalBitmap.getWidth();
        int originalHeight = originalBitmap.getHeight();

        int targetWidth = imageView.getWidth();
        int targetHeight = imageView.getHeight();

        float scaleWidth = ((float) targetWidth) / originalWidth;
        float scaleHeight = ((float) targetHeight) / originalHeight;

        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        Bitmap resizedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalWidth, originalHeight, matrix, true);
        imageView.setImageBitmap(resizedBitmap);
        textView.setText(clc.get_class());
    }

    private Bitmap imageToBitmap(ImageProxy image) {
        try {
            ImageProxy.PlaneProxy plane = image.getPlanes()[0];
            ByteBuffer buffer = plane.getBuffer();
            byte[] bytes = new byte[buffer.remaining()];
            buffer.get(bytes);
            return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraExecutor.shutdown();
        handler.removeCallbacksAndMessages(null); // Stop the continuous capture when activity is destroyed
    }
}





