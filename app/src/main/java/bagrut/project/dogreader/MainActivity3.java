package bagrut.project.dogreader;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.graphics.Matrix;
import android.widget.TextView;
import java.io.IOException;
import java.io.InputStream;

public class MainActivity3 extends Activity {

    private static final int CAMERA_REQUEST = 1888;
    private static final int GALLERY_REQUEST = 1889 ;
    private ImageView imageView;
    private TextView textView ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);


        imageView = findViewById(R.id.imageView);
        textView = findViewById(R.id.textView2);

        showPictureDialog();

    }
    private void showPictureDialog() {
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Take a photo with camera"};
        pictureDialog.setItems(pictureDialogItems,
                (dialog, which) -> {
                    switch (which) {
                        case 0:
                            Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(galleryIntent, GALLERY_REQUEST);
                            break;
                        case 1:
                            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(cameraIntent, CAMERA_REQUEST);
                            break;
                    }
                });
        pictureDialog.show();
    }
    private void predictAndshow(Bitmap photo)
    {
        Pets_Emotion_Clc_Model clc = new Pets_Emotion_Clc_Model(photo,getApplicationContext() ) ;


        Bitmap originalBitmap = clc.image; //   bitmap with dimensions 128x128x3

        // Original dimensions
        int originalWidth = originalBitmap.getWidth();
        int originalHeight = originalBitmap.getHeight();

        // Target dimensions
        int targetWidth = this.imageView.getWidth();
        int targetHeight = this.imageView.getHeight();

        // Calculate the scaling factors
        float scaleWidth = ((float) targetWidth) / originalWidth;
        float scaleHeight = ((float) targetHeight) / originalHeight;

        // Create a matrix for the scaling transformation
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        // Resize the bitmap with the matrix
        Bitmap resizedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalWidth, originalHeight, matrix, true);
        imageView.setImageBitmap( resizedBitmap);
        textView.setText(clc.get_class());
        DBHandler dbHandler = new DBHandler(this);
        dbHandler.addData(clc.get_class());

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK && data != null) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            this.predictAndshow(photo);
        }
        else if (requestCode == GALLERY_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(selectedImage);
                Bitmap photo = BitmapFactory.decodeStream(inputStream);
                this.predictAndshow(photo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}




