package com.caverock.androidsvgsample.fragments;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGImageView;
import com.caverock.androidsvgsample.R;
import com.caverock.androidsvgsample.utils.CachingImageLoader;

import java.util.HashMap;
import java.util.Map;

/**
 * AndroidSVG does not yet have animation support.  However v1.3 adds a new feature to provide
 * additional CSS rules at render time.  If you combine that with Android ValueAnimator, you can
 * create animations!
 *
 * The smoothness of the animation may vary depending on your device, the version of Android, and
 * the complexity of the SVG image.
 */

/*
 * Plate of orange slices image from https://stocksnap.io/photo/DGWXFPOPPE
 * Photographer rawpixel.com, License: CC0.
 */
public class AnimationLineDrawAndImageFragment extends Fragment
{
   private static final int    ANIM_DURATION = 1500;   // milliseconds
   private static final float  ANIM_LINES_DRAW_END = 0.9f;
   private static final float  ANIM_IMAGE_FADE_START = 0.8f;

   private static final HashMap<String,Integer>  pathLengths;

   static {
      // Map of path lengths for all the shapes in the svg we want to draw
      pathLengths = new HashMap<>();
      pathLengths.put("#plate-lg", 2794);
      pathLengths.put("#plate-sm", 1903);
      pathLengths.put(".orange", 903);     // largest of all the orange paths
   }


   @Nullable
   @Override
   public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
   {
      return inflater.inflate(R.layout.content_single_svg_image, container, false);
   }


   @Override
   public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
   {
      final TextView      title  = view.findViewById(R.id.title);
      final SVGImageView  oranges = view.findViewById(R.id.svg_image);

      title.setText(R.string.title_line_draw_and_image_animation);
      oranges.setContentDescription(getActivity().getString(R.string.description_line_draw_and_image_animation));

      SVG.registerExternalFileResolver(new CachingImageLoader(getActivity().getAssets()));
      oranges.setCSS("ellipse, path, image { opacity: 0; }");   // Hide line when first displayed
      oranges.setImageAsset("oranges.svg");

      oranges.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v)
         {
            beginAnimation(oranges);
         }
      });

      beginAnimation(oranges);
   }


   private void  beginAnimation(final SVGImageView oranges)
   {
      ValueAnimator va = ValueAnimator.ofFloat(0f, 1f);
      va.setDuration(ANIM_DURATION);

      va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

         // Called at each step of the animation
         public void onAnimationUpdate(ValueAnimator animation) {
            // Get the progress of the animation (0..1)
            float progress = (Float) animation.getAnimatedValue();


            StringBuilder  sb = new StringBuilder();

            float imageOpacity = mapRangeClamped(progress, ANIM_IMAGE_FADE_START, 1f, 0f, 1f);
            sb.append(String.format("#image { opacity: %.2f; } ", imageOpacity));

            // Loop through all the defined paths in the pathLengths map, calculate the dash offset
            // for this point in time, and update the CSS for the SVGImageView
            for (Map.Entry<String,Integer> entry: pathLengths.entrySet())
            {
               // Calculate the correct stroke-dashoffset at this point in the animation (ANIM_PATH_LENGTH -> 0)
               int   pathLength = entry.getValue();
               // Line drawing happens for forst 90% (0.9) of progress.
               // Here we map that 0..0.9 rage to the path length
               // 0   -> pathLength
               // 0.9 -> 0
               int   dashOffset = Math.round(mapRangeClamped(progress,0f, ANIM_LINES_DRAW_END, (float)pathLength, 0));
               sb.append(String.format("%s { stroke-dasharray: %d %d; stroke-dashoffset: %d; } ", entry.getKey(), pathLength, pathLength, dashOffset));
            }
            // Update the CSS of the SVGImageView.  It will result in the SVG being updated.
            oranges.setCSS(sb.toString());
         }
      });

      va.start();
   }


   /*
    * Map a value in the range [inMin..inMax] to the range [outMin..outMax].
    * Assumes that inMin <= inMax
    */
   private float mapRangeClamped(Float value, float inMin, float inMax, float outMin, float outMax)
   {
      value = Math.max(inMin, Math.min(inMax, value));
      float  inRange = inMax - inMin;
      float  outRange = outMax - outMin;
      return outMin + (value - inMin) * outRange / inRange;
   }

}
