package com.caverock.androidsvgsample;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.view.MenuItem;

import com.caverock.androidsvgsample.fragments.AboutFragment;
import com.caverock.androidsvgsample.fragments.AnimationLineDrawAndImageFragment;
import com.caverock.androidsvgsample.fragments.AnimationLineDrawFragment;
import com.caverock.androidsvgsample.fragments.ColourChangeFragment;
import com.caverock.androidsvgsample.fragments.MainFragment;
import com.caverock.androidsvgsample.fragments.RenderToImageFragment;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener
{
   int  currentFragment = -1;

   @Override
   protected void onCreate(Bundle savedInstanceState)
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);
      initNavigationDrawer();

      setContentFragment(new MainFragment());
   }


   @SuppressWarnings("StatementWithEmptyBody")
   @Override
   public boolean onNavigationItemSelected(MenuItem item)
   {
      // Handle navigation view item clicks here.
      int  id = item.getItemId();

      if (id != currentFragment)
         closeNavDrawer();

      if (id == R.id.nav_main) {
         setContentFragment(new MainFragment());
      } else if (id == R.id.nav_render_svg_to_image) {
         setContentFragment(new RenderToImageFragment());
      } else if (id == R.id.nav_colour_change) {
         setContentFragment(new ColourChangeFragment());
      } else if (id == R.id.nav_line_draw_animation) {
         setContentFragment(new AnimationLineDrawFragment());
      } else if (id == R.id.nav_line_draw_and_image_animation) {
         setContentFragment(new AnimationLineDrawAndImageFragment());

      } else if (id == R.id.nav_about) {
         setContentFragment(new AboutFragment());
      }

//      } else if (id == R.id.nav_send) {
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);

      closeNavDrawer();
      return true;
   }



   private void setContentFragment(Fragment fragment)
   {
      FragmentTransaction  ft = getSupportFragmentManager().beginTransaction();
      ft.replace(R.id.content_frame, fragment);
      ft.commit();
   }

}
