package bagrut.project.dogreader;



import android.graphics.Bitmap;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import org.tensorflow.lite.DataType ;
import java.io.IOException;
import android.util.Log;
import bagrut.project.dogreader.ml.PetsSemanticSegmentationModel;
import android.content.Context;

public class DataProccesing
    {
        private Bitmap show ;
        private Context context;
        public  boolean modelworked ;
        public  boolean dog_in_the_pic ;

        public DataProccesing(Bitmap image,Context context)
        {   this.context = context ;
            this.show = BitmapHelper.resizeBitmap2(image,128,128) ;
            try {
                this.filter_background();
                Log.d("MyTag", "works");
                this.modelworked =true ;
            }
            catch (Exception e)
            {
                Log.d("MyTag", "failes");
                this.modelworked = false ;
            }

        }
        public Bitmap getshow() {return this.show ;}


        private void filter_background() throws Exception
        {


            try {
                PetsSemanticSegmentationModel model =  PetsSemanticSegmentationModel.newInstance(this.context);

                // Creates inputs for reference.
                TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 128, 128, 3}, DataType.FLOAT32);
                int height = 128;
                int width = 128;
                int channels = 3;

                // Flatten the input float[][][] array into a 1-dimensional float array
                float[][][] inputArray = BitmapHelper.bitmapToFloatMatrix(this.show) ;  // Your existing float[][][] array
                float[] flattenedArray = new float[ height * width * channels];
                int index = 0;

                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        for (int c = 0; c < channels; c++) {
                            flattenedArray[index++] = inputArray[i][j][c];
                        }
                    }
                }
                inputFeature0.loadArray(flattenedArray);

                // Runs model inference and gets result.
                PetsSemanticSegmentationModel.Outputs outputs = model.process(inputFeature0);
                TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
                float[] outputData = outputFeature0.getFloatArray();
                // Releases model resources if no longer used.
                model.close();
                // Reshape the output data into a 4D float[][][][] array
                float[][][] outputArray = new float[height][width][channels];
                int index2 = 0;

                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        for (int c = 0; c < channels; c++) {
                            outputArray[i][j][c] = outputData[index2++];
                        }
                    }
                }
                int count = 0 ;
                for (int i = 0; i < height; i++) {
                    for (int j = 0; j < width; j++) {
                        if(BitmapHelper.maxIndex(outputArray[i][j]) == 1 )
                        {// Set pixel color to black (fully opaque) where the pet is not existing
                            this.show.setPixel(j,i, 0xFF000000);
                            count++;
                        }
                    }
                }
                this.dog_in_the_pic = (count/(128*128))<0.95 ;
            } catch (IOException e) {
                // TODO Handle the exception
                e.printStackTrace();
                throw new Exception() ;
            }






        }

    }
