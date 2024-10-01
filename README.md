
# Dog Reader App

## Overview
This Android application identifies and classifies emotions of dogs in images using TensorFlow Lite models. It processes the input image by filtering out the background, classifies the dog’s emotion, and displays the results. A pie chart shows the distribution of emotions stored in the local SQLite database.

## Features
- **Emotion Classification**: Classifies emotions as `angry`, `happy`, `relaxed`, or `sad`.
- **Background Filtering**: A segmentation model (`PetsSegmentationModel`) filters out the background, ensuring only the dog is analyzed, the dog emotion clasification model get a background filtered image as a result the predictions are more acurate .
- **Data Visualization**: Displays a pie chart showing the emotion distribution.
- **Data Persistence**: Uses SQLite to store and retrieve emotion data.
- **Data pipeline**: capture pictures of dogs using the device's camera or select an image from the gallery.


## Installation
1. Clone the repository.
2. Import the project into Android Studio.
3. Build and run the app on a device or emulator.



## Usage
  ## Screens:
  ### 1. Home Screen (MainActivity)
  On opening the app, users are given the option to:
  - Instantly capture an image of a dog for emotion analysis.
  - View statistics of captured emotions.
  - Enable continuous emotion analysis from a live camera feed.
  
  ### 2. Emotion Analysis (MainActivity3)
  - Users can either capture a new picture or select one from the gallery.
  - Once an image is selected, the app analyzes the dog's emotion and displays the result alongside the image with black background.
  
  ### 3. Emotion Statistics (MainActivity2)
  - Displays a pie chart showing the statistics of emotions analyzed so far.
  - Users can delete all recorded statistics with the clear button.
  
  ### 4. Continuous Capture (ContinuousCaptureActivity)
  - Allows continuous photo capture of dogs for real-time emotion analysis and display them.
  - when press the predict button the   captured image is immediately analyzed, the predicted  emotion and the filtered image  are  displayed on the corner of  screen.
  

## License
This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.


