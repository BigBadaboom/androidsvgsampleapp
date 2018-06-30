package com.caverock.androidsvgsample.fragments;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.caverock.androidsvg.SVGImageView;
import com.caverock.androidsvgsample.R;

/**
 * AndroidSVG does not yet have animation support.  However v1.3 adds a new feature to provide
 * additional CSS rules at render time.  If you combine that with Android ValueAnimator, you can
 * create animations!
 *
 * The smoothness of the animation may vary depending on your device, the version of Android, and
 * the complexity of the SVG image.
 */
public class AnimationLineDrawFragment extends Fragment
{
   private static final int  ANIM_DURATION = 3000;      // milliseconds
   private static final int  ANIM_PATH_LENGTH = 3018;   // the total length of the doodle path


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
      final SVGImageView  doodle = view.findViewById(R.id.svg_image);

      title.setText(R.string.title_line_draw_animation);
      doodle.setCSS("#doodle { opacity: 0; }");   // Hide line when first displayed
      doodle.setImageAsset("7doodle.svg");
      doodle.setContentDescription(getActivity().getString(R.string.description_line_draw_animation));

      ValueAnimator va = ValueAnimator.ofFloat(0f, 1f);
      va.setDuration(ANIM_DURATION);
      va.setRepeatCount(ValueAnimator.INFINITE);
      va.setRepeatMode(ValueAnimator.REVERSE);

      va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

         // Called at each step of the animation
         public void onAnimationUpdate(ValueAnimator animation) {
            // Get the progress of the animation (0..1)
            Float progress = (Float) animation.getAnimatedValue();
            // Calculate the correct stroke-dashoffset at this point in the animation (ANIM_PATH_LENGTH -> 0)
            int   dashOffset = Math.round(ANIM_PATH_LENGTH * (1 - progress));
            // Update the CSS of the SVGImageView. Will result in the SVG being updated.
            String css = String.format("#doodle { stroke-dasharray: %d %d; stroke-dashoffset: %d; }", ANIM_PATH_LENGTH, ANIM_PATH_LENGTH, dashOffset);
            doodle.setCSS(css);
         }
      });

      va.start();
   }

}
