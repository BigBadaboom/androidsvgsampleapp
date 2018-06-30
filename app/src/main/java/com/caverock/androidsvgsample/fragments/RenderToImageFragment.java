package com.caverock.androidsvgsample.fragments;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvgsample.R;

import java.util.Random;

/**
 * Demonstrates how to render an SVG to a Bitmap object user SVG.renderToCanvas().
 *
 * For the SVG we are using the "Bouquet" emoji from the Google Noto Emoji font.
 * https://github.com/googlei18n/noto-emoji/blob/master/svg/emoji_u1f490.svg
 */
public class RenderToImageFragment extends Fragment
{
   private static final String  TAG = RenderToImageFragment.class.getSimpleName();

   private static final int    WIDTH = 500;
   private static final int    HEIGHT = 500;
   private static final int    NUM_BOUQUETS = 10;
   private static final float  SCALE_MIN = 0.1f;
   private static final float  SCALE_MAX = 0.5f;

   SVG  bouquet = null;

   @Nullable
   @Override
   public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
   {
       return inflater.inflate(R.layout.content_single_image, container, false);
   }


   @Override
   public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
   {
      final TextView   titleView  = view.findViewById(R.id.title);
      final ImageView  imageView = view.findViewById(R.id.image);

      titleView.setText(R.string.title_render_to_image);

      // Load the SVG from the assets folder
      try {
         bouquet = SVG.getFromAsset(getActivity().getAssets(), "noto_emoji_u1f490.svg");
      } catch (Exception e) {
         Log.e(TAG, "Could not load flower emoji SVG", e);
      }

      // Se tab listener
      imageView.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v)
         {
            Bitmap  generatedImage = generateImage();
            imageView.setImageBitmap(generatedImage);
         }
      });

      Bitmap  generatedImage = generateImage();
      imageView.setImageBitmap(generatedImage);
   }


   private Bitmap  generateImage()
   {
      // Create a Bitmap to render our SVG to
      Bitmap  bm = Bitmap.createBitmap(500, 500, Bitmap.Config.ARGB_8888);
      // Create a Canvas to use for rendering
      Canvas canvas = new Canvas(bm);

      // Clear the background to white
      canvas.drawRGB(255, 255, 255);

      Random  random = new Random();

      for (int i=0; i<NUM_BOUQUETS; i++)
      {
         // Save the canvas state (because we are going to alter the transformation matrix)
         canvas.save();

         // Choose a random scale
         // If we don't specify a viewport box, then AndroidSVG will use the bounds of the Canvas
         // as the viewport.  So a scale of 1.0 corresponds to that size.
         // A scale of 0.1 would draw the SVG at 1/10th of the size of the Canvas/Bitmap.
         float scale = SCALE_MIN + random.nextFloat() * (SCALE_MAX - SCALE_MIN);

         // Choose a random position
         float  x = random.nextFloat();   // 0.0 -> 1.0
         float  y = random.nextFloat();

         // If we just draw the SVG at x,y, then it will be drawn below and to the right of that.
         // But we want to centre at x,y instead. We can do that by subtracting half the scale.
         x -= scale / 2;
         y -= scale / 2;

         canvas.translate(x * WIDTH, y * HEIGHT);
         canvas.scale(scale, scale);

         // Now render the SVG to the Canvas
         bouquet.renderToCanvas(canvas);

         // And restore the Canvas's matrix to what it was before we altered it
         canvas.restore();
      }

      return bm;
   }

}
