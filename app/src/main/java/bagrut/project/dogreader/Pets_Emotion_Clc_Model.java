package bagrut.project.dogreader;



import android.graphics.Bitmap;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;
import org.tensorflow.lite.DataType ;
import java.io.IOException;
import bagrut.project.dogreader.ml.PetsEmotionClassification;
import android.content.Context;

public class Pets_Emotion_Clc_Model
{
    private final Context context ;
    private final static String[] classes = new String[] {"angry","happy","relaxed","sad"} ;
    private int index_class ;

    private boolean dog_in_pic ;
    public Bitmap image ;
    public Pets_Emotion_Clc_Model(Bitmap bitmap, Context context)
    {
        this.context = context ;
        this.predict(bitmap) ;

    }

    private void predict(Bitmap bitmap)// set index_class with the predicted class and the image atribute with a bitmap that got filtered with the petssegmentationmodel (pixcels that does not represent the pet are black)
    {
        DataProccesing proccesed_data = new DataProccesing(bitmap, this.context);
        this.image = proccesed_data.getshow();
        this.dog_in_pic = proccesed_data.dog_in_the_pic;
        float[][][] x_3d = BitmapHelper.bitmapToFloatMatrix(proccesed_data.getshow()) ;
        if (!proccesed_data.modelworked) {
            this.index_class = -1;
        }
        else {
        try{



            PetsEmotionClassification model =  PetsEmotionClassification.newInstance(this.context);

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 128, 128,3}, DataType.FLOAT32);
            int height = 128;
            int width = 128;
            int chanels = 3 ;

            // Flatten the input float[][][] array into a 1-dimensional float array
            float[] flattenedArray = new float[ height * width * chanels];
            int index = 0;

            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                      for(int g = 0; g < chanels ; g++){
                          flattenedArray[index++] = x_3d[i][j][g];
                      }
                }
            }
            inputFeature0.loadArray(flattenedArray);

            // Runs model inference and gets result.
            PetsEmotionClassification.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();
            float[] y_pred = outputFeature0.getFloatArray();
            this.index_class = BitmapHelper.maxIndex(y_pred) ;
            // Releases model resources if no longer used.
            model.close();

            }
        catch (IOException e)
        {
            this.index_class = -1 ;
        }

        }
    }


    public String get_class()
    {   if(!this.dog_in_pic){return "didnt capture a dog ";}
        if(this.index_class == -1)
            return "model did not work" ;
        return classes[this.index_class] ;
    }

    public int getIndex_class()
    {
        return this.index_class ;
    }
}


