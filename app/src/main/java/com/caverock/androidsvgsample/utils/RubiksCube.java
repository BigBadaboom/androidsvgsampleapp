package com.caverock.androidsvgsample.utils;

import android.arch.lifecycle.ViewModel;
import android.util.Log;

import java.util.Arrays;
import java.util.Random;
import java.util.Stack;


// Face numbering of cubes:
//
//         / \
//       /     \
//     /         \
//    |\    0    /|
//  4 |  \     /  | 3
//    |    \ /    |
//    |  1  |  2  |
//     \    |    /
//       \  |  /
//      5  \ /
//
//   0 = yellow, 1 = red, 2 = green, 3 = orange, 4 = blue, 5 = white

// Tile numbering per face:
//
// TOP & BOT:      0               LEFT & RIGHT:   0              2
//               3   1                             3  1        1  5
//             6   4   2                           6  4  2  0  4  8
//               7   5                                7  5  3  7
//                 8                                     8  6
//
//                               BACK LEFT & RIGHT:      0  2
//                                                    1  3  5  1
//                                                 2  4  6  8  4  0
//                                                 5  7        7  3
//                                                 8              6


public class RubiksCube extends ViewModel
{
   private int[][]         cubeTileColours;
   private boolean         shuffling = true;
   private Stack<Integer>  shuffleSequence = null;
   private Random          random = new Random();

   public static final int  TOP = 0;
   public static final int  LEFT = 1;
   public static final int  RIGHT = 2;
   public static final int  BACK_RIGHT = 3;
   public static final int  BACK_LEFT = 4;
   public static final int  BOTTOM = 5;


   public RubiksCube()
   {
      cubeTileColours  = new int[6][9];
      reset();
   }


   /**
    * Reset the cube to an unscrambled state.
    */
   public void  reset()
   {
      for (int face = TOP; face <= BOTTOM; face++) {
         for (int i = 0; i < 9; i++) {
            cubeTileColours[face][i] = face;
         }
      }
   }


   /**
    * Get the colour of particular tile.
    */
   public int  getColour(int face, int tile)
   {
      return cubeTileColours[face][tile];
   }


   /**
    * Perform one step in a cube shuffle.
    */
   public void shuffleOneStep()
   {
      if (shuffleSequence == null)
      {
         shuffleSequence = new Stack<>();
         reset();
      }
      else if (shuffling)
      {
         // Pick a random face to rotate and a rotation direction
         int      move = random.nextInt(12);
         int      face = move % 6;
         boolean  clockwise = move < 6;

         // Perform the rotation
         rotateFace(face, clockwise);
         // Store the action so we can unwind the shuffle later
         shuffleSequence.push(move);

         if (shuffleSequence.size() > 10 && random.nextInt(15) == 0)
            shuffling = false;
      }
      else
      {
         // Unwind the shuffling (solve the cube)
         int move = shuffleSequence.pop();
         int      face = move % 6;
         boolean  clockwise = !(move < 6);   // rotate opposite way to when we were shuffling

         // Perform the rotation
         rotateFace(face, clockwise);

         // If we have full unwound, then start shuffling again
         if (shuffleSequence.size() == 0)
            shuffling = true;
      }
   }


   /**
    * Rotate the given face clockwise or anti-clockwise.
    */
   private void  rotateFace(int face, boolean clockwise)
   {
      int count = clockwise ? 1 : 3;   // An anti-clockwise turn is the same as three clockwise turns
      while (count-- > 0)
      {
         switch (face) {
            case TOP: rotateTop(); break;
            case LEFT: rotateLeft(); break;
            case RIGHT: rotateRight(); break;
            case BACK_RIGHT: rotateBackRight(); break;
            case BACK_LEFT: rotateBackLeft(); break;
            case BOTTOM: rotateBottom(); break;
         }
      }
   }

   /*
    * Rotate the top face clockwise a quarter turn
    */
   private void  rotateTop()
   {
      // Top face
      rotateFace(TOP);
      // Top row of tiles
      rotateRowTile(0);
      rotateRowTile(1);
      rotateRowTile(2);
   }

   /*
    * Rotate the bottom face clockwise a quarter turn
    */
   private void  rotateBottom()
   {
      // Bottom face
      rotateFace(BOTTOM);
      // Bottom row of tiles
      rotateRowTile(6);
      rotateRowTile(7);
      rotateRowTile(8);
   }


    /*
    * Rotate the left face clockwise a quarter turn
    */
   private void  rotateLeft()
   {
      // Left face
      rotateFace(LEFT);
      // Adjacent side face tiles
      int t = cubeTileColours[TOP][6];
      cubeTileColours[TOP][6] = cubeTileColours[BACK_LEFT][8];
      cubeTileColours[BACK_LEFT][8] = cubeTileColours[BOTTOM][8];
      cubeTileColours[BOTTOM][8] = cubeTileColours[RIGHT][0];
      cubeTileColours[RIGHT][0] = t;
      t = cubeTileColours[TOP][7];
      cubeTileColours[TOP][7] = cubeTileColours[BACK_LEFT][5];
      cubeTileColours[BACK_LEFT][5] = cubeTileColours[BOTTOM][7];
      cubeTileColours[BOTTOM][7] = cubeTileColours[RIGHT][3];
      cubeTileColours[RIGHT][3] = t;
      t = cubeTileColours[TOP][8];
      cubeTileColours[TOP][8] = cubeTileColours[BACK_LEFT][2];
      cubeTileColours[BACK_LEFT][2] = cubeTileColours[BOTTOM][6];
      cubeTileColours[BOTTOM][6] = cubeTileColours[RIGHT][6];
      cubeTileColours[RIGHT][6] = t;
   }


    /*
    * Rotate the right face clockwise a quarter turn
    */
   private void  rotateRight()
   {
      // Face
      rotateFace(RIGHT);
      // Adjacent side face tiles
      int t = cubeTileColours[TOP][8];
      cubeTileColours[TOP][8] = cubeTileColours[LEFT][8];
      cubeTileColours[LEFT][8] = cubeTileColours[BOTTOM][2];
      cubeTileColours[BOTTOM][2] = cubeTileColours[BACK_RIGHT][0];
      cubeTileColours[BACK_RIGHT][0] = t;
      t = cubeTileColours[TOP][5];
      cubeTileColours[TOP][5] = cubeTileColours[LEFT][5];
      cubeTileColours[LEFT][5] = cubeTileColours[BOTTOM][5];
      cubeTileColours[BOTTOM][5] = cubeTileColours[BACK_RIGHT][3];
      cubeTileColours[BACK_RIGHT][3] = t;
      t = cubeTileColours[TOP][2];
      cubeTileColours[TOP][2] = cubeTileColours[LEFT][2];
      cubeTileColours[LEFT][2] = cubeTileColours[BOTTOM][8];
      cubeTileColours[BOTTOM][8] = cubeTileColours[BACK_RIGHT][6];
      cubeTileColours[BACK_RIGHT][6] = t;
   }


    /*
    * Rotate the back-right face clockwise a quarter turn
    */
   private void  rotateBackRight()
   {
      // Face
      rotateFace(BACK_RIGHT);
      // Adjacent side face tiles
      int t = cubeTileColours[TOP][2];
      cubeTileColours[TOP][2] = cubeTileColours[RIGHT][8];
      cubeTileColours[RIGHT][8] = cubeTileColours[BOTTOM][0];
      cubeTileColours[BOTTOM][0] = cubeTileColours[BACK_LEFT][0];
      cubeTileColours[BACK_LEFT][0] = t;
      t = cubeTileColours[TOP][1];
      cubeTileColours[TOP][1] = cubeTileColours[RIGHT][5];
      cubeTileColours[RIGHT][5] = cubeTileColours[BOTTOM][1];
      cubeTileColours[BOTTOM][1] = cubeTileColours[BACK_LEFT][3];
      cubeTileColours[BACK_LEFT][3] = t;
      t = cubeTileColours[TOP][0];
      cubeTileColours[TOP][0] = cubeTileColours[RIGHT][2];
      cubeTileColours[RIGHT][2] = cubeTileColours[BOTTOM][2];
      cubeTileColours[BOTTOM][2] = cubeTileColours[BACK_LEFT][6];
      cubeTileColours[BACK_LEFT][6] = t;
   }


    /*
    * Rotate the back-left face clockwise a quarter turn
    */
   private void  rotateBackLeft()
   {
      // Left face
      rotateFace(BACK_LEFT);
      // Adjacent side face tiles
      int t = cubeTileColours[TOP][0];
      cubeTileColours[TOP][0] = cubeTileColours[BACK_RIGHT][8];
      cubeTileColours[BACK_RIGHT][8] = cubeTileColours[BOTTOM][6];
      cubeTileColours[BOTTOM][6] = cubeTileColours[LEFT][0];
      cubeTileColours[LEFT][0] = t;
      t = cubeTileColours[TOP][3];
      cubeTileColours[TOP][3] = cubeTileColours[BACK_RIGHT][5];
      cubeTileColours[BACK_RIGHT][5] = cubeTileColours[BOTTOM][3];
      cubeTileColours[BOTTOM][3] = cubeTileColours[LEFT][3];
      cubeTileColours[LEFT][3] = t;
      t = cubeTileColours[TOP][6];
      cubeTileColours[TOP][6] = cubeTileColours[BACK_RIGHT][2];
      cubeTileColours[BACK_RIGHT][2] = cubeTileColours[BOTTOM][0];
      cubeTileColours[BOTTOM][0] = cubeTileColours[LEFT][6];
      cubeTileColours[LEFT][6] = t;
   }




   // Rotate the tiles on the given fave clockwise
   private void  rotateFace(int face)
   {
      int t = cubeTileColours[face][0];
      cubeTileColours[face][0] = cubeTileColours[face][6];
      cubeTileColours[face][6] = cubeTileColours[face][8];
      cubeTileColours[face][8] = cubeTileColours[face][2];
      cubeTileColours[face][2] = t;
      t = cubeTileColours[face][1];
      cubeTileColours[face][1] = cubeTileColours[face][3];
      cubeTileColours[face][3] = cubeTileColours[face][7];
      cubeTileColours[face][7] = cubeTileColours[face][5];
      cubeTileColours[face][5] = t;
Log.d("FOO", "++++ "+Arrays.toString(cubeTileColours[face]));
   }

   private void rotateRowTile(int tile)
   {
      int t = cubeTileColours[LEFT][tile];
      cubeTileColours[LEFT][tile] = cubeTileColours[RIGHT][tile];
      cubeTileColours[RIGHT][tile] = cubeTileColours[BACK_RIGHT][tile];
      cubeTileColours[BACK_RIGHT][tile] = cubeTileColours[BACK_LEFT][tile];
      cubeTileColours[BACK_LEFT][tile] = t;
   }

}
