package com.caverock.androidsvgsample.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.caverock.androidsvg.SVGImageView;
import com.caverock.androidsvgsample.R;
import com.caverock.androidsvgsample.utils.RubiksCube;

import java.util.Random;

public class ColourChangeFragment extends Fragment
{
   SVGImageView  lego = null;
   SVGImageView  cube = null;


   @Nullable
   @Override
   public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
   {
      return inflater.inflate(R.layout.content_colour_change, container, false);
   }


   @Override
   public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
   {
      lego = view.findViewById(R.id.lego);
      lego.setOnClickListener(new LegoClickListener());

      cube = view.findViewById(R.id.cube);
      cube.setOnClickListener(new CubeClickListener());
   }


   //===============================================================================================
   // Lego image routines


   private class LegoClickListener implements View.OnClickListener
   {
      String[]  brickColours = new String[] {"#1698ec", "#fef134", "#95e881", "#ea272e"};

      @Override
      public void onClick(View v)
      {
         shuffleArray(brickColours);
         String css = String.format(".brick1 { fill:%s; } .brick2 { fill:%s; } .brick3 { fill:%s; } .brick4 { fill:%s; }", brickColours[0], brickColours[1], brickColours[2], brickColours[3]);
         ColourChangeFragment.this.lego.setCSS(css);
      }

   }


   // Fisher-Yates shuffle
   static void  shuffleArray(String[] array)
   {
      Random  rnd = new Random();
      for (int i = array.length - 1; i > 0; i--)
      {
         int index = rnd.nextInt(i + 1);
         // Simple swap
         String temp = array[index];
         array[index] = array[i];
         array[i] = temp;
      }
   }


   //===============================================================================================
   // Cube image routines

   private static String[]  faceColours = new String[] {"#f9ca2e", "#c72e33", "#297537", "#f36021", "#255ea2", "#ffffff"};  // yellow, red, green, orange, blue, white


   private class CubeClickListener implements View.OnClickListener
   {
      @Override
      public void onClick(View v)
      {
         RubiksCube  cube = ViewModelProviders.of(ColourChangeFragment.this.getActivity()).get(RubiksCube.class);
         cube.shuffleOneStep();
         updateCubeCSS(cube);
      }

   }

   private void updateCubeCSS(RubiksCube cube)
   {
      StringBuilder  css = new StringBuilder();
      doFace(css, cube, RubiksCube.TOP, 'T');
      doFace(css, cube, RubiksCube.LEFT, 'L');
      doFace(css, cube, RubiksCube.RIGHT, 'R');
      ColourChangeFragment.this.cube.setCSS(css.toString());
   }

   private void doFace(StringBuilder css, RubiksCube cube, int face, char cssClassTilePrefix)
   {
      for (int tile = 0; tile < 9; tile++) {
         css.append(".");
         css.append(cssClassTilePrefix);
         css.append(tile);
         css.append(" {fill: ");
         css.append(faceColours[cube.getColour(face, tile)]);
         css.append("} ");
      }
   }


}
