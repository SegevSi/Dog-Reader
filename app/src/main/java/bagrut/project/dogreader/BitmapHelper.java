package bagrut.project.dogreader;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;


public class BitmapHelper
    {
        public static float[][][] bitmapToFloatMatrix(Bitmap bitmap) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            // Assuming 3 channels for RGB
            float[][][] floatMatrix = new float[height][width][3];

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixel = bitmap.getPixel(x, y);
                    floatMatrix[y][x][0] = ((pixel >> 16) & 0xff) / 255.0f; // Red channel
                    floatMatrix[y][x][1] = ((pixel >> 8) & 0xff) / 255.0f; // Green channel
                    floatMatrix[y][x][2] = (pixel & 0xff) / 255.0f; // Blue channel
                }
            }

            return floatMatrix;
        } // also normelise the values to be between 0-1



        public static int maxIndex(float[] arr )
        {
            int max_index = 0 ;

            for(int i = 1 ; i< arr.length ; i++)
            {
                if(arr[max_index]<arr[i])
                    max_index = i ;
            }



            return max_index ;
        }

        public static Bitmap resizeBitmap2(Bitmap originalBitmap, int targetWidth, int targetHeight) {
            if (originalBitmap == null) // update
            {
                throw new IllegalArgumentException("Bitmap cannot be null");
            }


            // Original dimensions
            int originalWidth = originalBitmap.getWidth();
            int originalHeight = originalBitmap.getHeight();

            // Calculate the scaling factors
            float scaleWidth = ((float) targetWidth) / originalWidth;
            float scaleHeight = ((float) targetHeight) / originalHeight;

            // Determine the minimum scale factor to fit the target size
            float scaleFactor = Math.min(scaleWidth, scaleHeight);

            // Calculate the new dimensions
            int newWidth = Math.round(originalWidth * scaleFactor);
            int newHeight = Math.round(originalHeight * scaleFactor);

            // Create a matrix for the scaling transformation
            Matrix matrix = new Matrix();
            matrix.postScale(scaleFactor, scaleFactor);

            // Resize the bitmap with the matrix
            Bitmap resizedBitmap = Bitmap.createBitmap(originalBitmap, 0, 0, originalWidth, originalHeight, matrix, true);

            // Create a new Bitmap to pad or crop to fit the target dimensions
            Bitmap resultBitmap = Bitmap.createBitmap(targetWidth, targetHeight, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(resultBitmap);

            // If you want to pad the image to fit the target dimensions
            canvas.drawColor(Color.WHITE); // Set the padding color
            canvas.drawBitmap(resizedBitmap, (targetWidth - newWidth) / 2f, (targetHeight - newHeight) / 2f, null);

            // If you want to crop the image to fit the target dimensions
            // Bitmap croppedBitmap = Bitmap.createBitmap(resizedBitmap, (newWidth - targetWidth) / 2, (newHeight - targetHeight) / 2, targetWidth, targetHeight);
            // canvas.drawBitmap(croppedBitmap, 0, 0, null);

            return resultBitmap;
        }

    }

// needed to be inside bitmap in old version or not neccecry at all:
//        public static Bitmap resizeBitmapWithInterArea(Bitmap originalBitmap, int newWidth, int newHeight) {
//            int originalWidth = originalBitmap.getWidth();
//            int originalHeight = originalBitmap.getHeight();
//
//            float widthRatio = (float) originalWidth / newWidth;
//            float heightRatio = (float) originalHeight / newHeight;
//
//            Bitmap resizedBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888);
//
//            for (int y = 0; y < newHeight; y++) {
//                for (int x = 0; x < newWidth; x++) {
//                    int startX = (int) (x * widthRatio);
//                    int startY = (int) (y * heightRatio);
//                    int endX = (int) ((x + 1) * widthRatio);
//                    int endY = (int) ((y + 1) * heightRatio);
//
//                    int totalR = 0, totalG = 0, totalB = 0, count = 0;
//
//                    for (int i = startX; i < endX; i++) {
//                        for (int j = startY; j < endY; j++) {
//                            int pixel = originalBitmap.getPixel(i, j);
//                            totalR += (pixel >> 16) & 0xff;
//                            totalG += (pixel >> 8) & 0xff;
//                            totalB += pixel & 0xff;
//                            count++;
//                        }
//                    }
//
//                    int avgR = totalR / count;
//                    int avgG = totalG / count;
//                    int avgB = totalB / count;
//
//                    int avgPixel = (0xff << 24) | (avgR << 16) | (avgG << 8) | avgB;
//                    resizedBitmap.setPixel(x, y, avgPixel);
//                }
//            }
//
//            return resizedBitmap;
//        }


//        public static Bitmap drawTextOnBitmap(String text, Bitmap originalBitmap) {
//            // Create a new bitmap with the same dimensions as the original bitmap
//            Bitmap bitmapWithText = Bitmap.createBitmap(originalBitmap.getWidth(), originalBitmap.getHeight(), Bitmap.Config.ARGB_8888);
//
//            // Create a canvas from the new bitmap
//            Canvas canvas = new Canvas(bitmapWithText);
//
//            // Draw the original bitmap onto the canvas
//            canvas.drawBitmap(originalBitmap, 0, 0, null);
//
//            // Create a paint object for drawing text
//            Paint paint = new Paint();
//            paint.setColor(Color.RED); // Set text color
//            paint.setTextSize(100); // Set text size
//
//            // Calculate the position to draw the text (you may need to adjust this based on your requirements)
//            int x = originalBitmap.getWidth()/2 ;
//            int y = originalBitmap.getHeight()/3 ; // Draw text at the bottom of the bitmap
//
//            // Draw the text on the canvas
//            canvas.drawText(text, x, y, paint);
//
//            return bitmapWithText;
//        }
//        private static float avg(float[] arr)
//        {
//            float sum = 0 ;
//            for(int i = 0; i < arr.length ; i++)
//            {
//                sum+= arr[i] ;
//            }
//           return sum/arr.length ;
//        }
//        public static float[][] Grey_Scale(float[][][] mat )
//        {
//            float[][] grey_mat = new float[mat.length][mat[0].length] ;
//            for (int i = 0; i < mat.length; i++)
//            {
//                for (int j = 0; j < mat[i].length; j++)
//                {
//                    grey_mat[i][j] = avg(mat[i][j]) ;
//                }
//
//            }
//
//            return grey_mat ;
//        }
//        public static Bitmap resizebiggerBitmap(Bitmap bitmap, int newWidth, int newHeight) {
//            int width = bitmap.getWidth();
//            int height = bitmap.getHeight();
//            float scaleWidth = ((float) newWidth) / width;
//            float scaleHeight = ((float) newHeight) / height;
//            Matrix matrix = new Matrix();
//            matrix.postScale(scaleWidth, scaleHeight);
//            Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
//            return resizedBitmap;
//        }
//        public static  Bitmap  imageProxyToBitmap(ImageProxy imageProxy)
//        {
//            ImageProxy.PlaneProxy[] planes = imageProxy.getPlanes();
//            if (planes.length == 0) {
//                return null;
//            }
//            ByteBuffer buffer = planes[0].getBuffer();
//            byte[] bytes = new byte[buffer.remaining()];
//            buffer.get(bytes);
//            int width = imageProxy.getWidth();
//            int height = imageProxy.getHeight();
//            YuvImage yuvImage = new YuvImage(bytes, ImageFormat.YUV_420_888, width, height, null);
//            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//            yuvImage.compressToJpeg(new Rect(0, 0, width, height), 100, stream);
//            byte[] jpegBytes = stream.toByteArray();
//            return BitmapFactory.decodeByteArray(jpegBytes, 0, jpegBytes.length);
//        }