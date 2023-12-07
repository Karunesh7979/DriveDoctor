package com.csis.drivedoctor;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.csis.drivedoctor.model.MyUserData;
import com.csis.drivedoctor.postAuth.AddCarActivity;
import com.csis.drivedoctor.postAuth.EditProfileActivity;
import com.csis.drivedoctor.preauthDatabase.DatabaseHelper;
import com.csis.drivedoctor.Helpers.ModelHelper;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.csis.drivedoctor.databinding.ActivityNavigationMainBinding;

public class NavigationMainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityNavigationMainBinding binding;

    MyUserData userData;
    ImageView imageProfile;
    ImageView editProfile;
    TextView emailTextView;
    TextView nameTextView;
    NavigationView navigationView;

String selectedtab = "appointments";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNavigationMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        getAllData();
        userData = ModelHelper.getInstance().getUser();
        setSupportActionBar(binding.appBarNavigationMain.toolbar);
        binding.appBarNavigationMain.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedtab == "mycars") {
                    startActivity(new Intent(NavigationMainActivity.this, AddCarActivity.class));
                } else if(selectedtab == "myclients") {
                    Intent obj = new Intent(NavigationMainActivity.this, SignupActivity.class);
                    startActivity(obj);
                } else {

                }
            }
        });

        binding.appBarNavigationMain.toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(NavigationMainActivity.this, "Something needs to be done here", Toast.LENGTH_SHORT).show();

            }
        });

        DrawerLayout drawer = binding.drawerLayout;
         navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder( R.id.id_nav_myappointments,
                R.id.id_nav_mycars, R.id.id_nav_myclients)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        customsActions();
    }


    void customsActions() {
        imageProfile = navigationView.getHeaderView(0).findViewById(R.id.id_drawer_userImage_imageView);
        editProfile = navigationView.getHeaderView(0).findViewById(R.id.id_drawer_editProfile_imageView);
        emailTextView = navigationView.getHeaderView(0).findViewById(R.id.id_drawer_email_textView);
        nameTextView = navigationView.getHeaderView(0).findViewById(R.id.id_drawer_username_textview);



        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newInt = new Intent(NavigationMainActivity.this, EditProfileActivity.class);
                startActivity(newInt);
            }
        });

        String displayName = ModelHelper.getInstance().getUser().getFirstName() + " " + ModelHelper.getInstance().getUser().getLastName();
        nameTextView.setText(displayName);
        emailTextView.setText(ModelHelper.getInstance().getUser().getEmail());

        if (!userData.getProfileURL().equals("")) {
            Glide.with(this).load(userData.getProfileURL()).into(imageProfile);
        }

        Menu menu = navigationView.getMenu();
        MenuItem carItem = menu.findItem(R.id.id_nav_mycars);
        MenuItem usersItem = menu.findItem(R.id.id_nav_myclients);
        MenuItem appointments = menu.findItem(R.id.id_nav_myappointments);

        carItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                selectedtab = "mycars";
                binding.appBarNavigationMain.fab.setVisibility(View.VISIBLE);
                return false;
            }
        });
        usersItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                selectedtab = "myclients";
                binding.appBarNavigationMain.fab.setVisibility(View.VISIBLE);
                return false;
            }
        });
        appointments.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                selectedtab = "appointments";
                binding.appBarNavigationMain.fab.setVisibility(View.GONE);
                return false;
            }
        });
        binding.appBarNavigationMain.fab.setVisibility(View.GONE);
        if(ModelHelper.getInstance().getUser().isServiceProvider()) {
            carItem.setVisible(false);
        } else {
            usersItem.setVisible(false);
        }
    }





    @Override
    protected void onRestart() {
        super.onRestart();
        if (!userData.getProfileURL().equals("")) {
            Glide.with(this).load(userData.getProfileURL()).into(imageProfile);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_main, menu);

        MenuItem item = menu.findItem(R.id.action_settings);
        item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(@NonNull MenuItem item) {
                SharedPreferences.Editor editor = DatabaseHelper.getInstance().getPref().edit();
                editor.clear();
                editor.apply();
                ModelHelper.getInstance().destroyInstance();
               finish();
                Intent newInt = new Intent(NavigationMainActivity.this, LoginActivity.class);
                startActivity(newInt);
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_navigation_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}