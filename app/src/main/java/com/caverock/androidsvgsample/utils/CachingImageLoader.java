package com.caverock.androidsvgsample.utils;

import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.caverock.androidsvg.SVGExternalFileResolver;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 * Simple external asset resolver that loads and caches decoded images
 */
public class CachingImageLoader extends SVGExternalFileResolver
{
   private AssetManager            assetManager;
   private HashMap<String,Bitmap>  cache = new HashMap<>();


   @SuppressWarnings({"WeakerAccess", "unused"})
   public CachingImageLoader(AssetManager assetManager)
   {
      super();
      this.assetManager = assetManager;
   }


   /**
    * Attempt to find the specified image file in the "assets" folder and return a decoded Bitmap.
    */
   @Override
   public Bitmap resolveImage(String filename)
   {
      // Ignore invalid requests
      if (filename == null || filename.isEmpty())
         return null;
      // Check file is in cache first
      if (cache.containsKey(filename))
         return cache.get(filename);
      // Try loading it from assets folder
      try
      {
         // Get the image as an InputStream
         InputStream istream = assetManager.open(filename);
         // Attempt to decode it
         Bitmap  image = BitmapFactory.decodeStream(istream);
         // Store it in the cache
         cache.put(filename, image);
         // and return it
         return image;
      }
      catch (IOException e1)
      {
         return null;
      }
   }
}
