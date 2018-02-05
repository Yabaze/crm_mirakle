package com.thomas.mirakle.crm_mirakle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;
import java.util.Objects;

public class client extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
        FirebaseAuth mAuth;
        FirebaseAuth.AuthStateListener mAuthListener;
        ImageView profile;
        ImageButton bg;
        Drawable Right;
        TextView user_name,e_mail_,intro,feature;
        Context mContext;
        View home_lay,chat_lay,about_lay;
        Toolbar toolbar;
        EditText msg;
        Button msg_button;
        FloatingActionButton fab;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

    }
   /*public void profileimageclick(View v){
       mAuth=FirebaseAuth.getInstance();
       FirebaseUser user=mAuth.getCurrentUser();
       String user_=user.getDisplayName();
       String e_mail=user.getEmail();
       user_name=findViewById(R.id.username);
       user_name.setVisibility(View.VISIBLE);
       user_name.setText(user_);
       TextView e_mail_=findViewById(R.id.e_mail);
       e_mail_.setVisibility(View.VISIBLE);
       e_mail_.setText(e_mail);

       //profile.setImageURI(image_url);

   }*/
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);
        mAuth=FirebaseAuth.getInstance();
        final FirebaseUser user=mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myuser = database.getReference("Users");
        final DatabaseReference myRef = database.getReference("chat");
        Toolbar toolbar = (Toolbar) findViewById(R.id.chat_toolbar);
        setSupportActionBar(toolbar);


        //home page content
        intro=findViewById(R.id.intro);
        intro.setText(Html.fromHtml("<html><head></head><body><ol><li>\t<b>Dry Air Technologies </b>- India is an well established manufacturing organization of specially <b>Container Desiccants</b> since <u>2007</u>.</li>\n" +
                "<li>\t\tIt is well equipped and serving all types of exporters globally to prevent their Export goods from moisture and condensation issues by a well Technical experts. </li>"+
                "<li>\t\tWe offer wide range of container desiccants in various sizes and types which ever fits the customer needs.\n</li>" +
                "<li>\t\tWe shall provide an <b>error free service</b> and timely delivery at any where globally.\n</li>" +
                "<li>\t\t<b>Ontime delivery</b> and customer service is our main motto.</li></ol></body></html>"));
        feature=findViewById(R.id.feature);
        feature.setText(Html.fromHtml("<ul>\n" +
                "<li>\t\tPrevents Moisture, Condensation damages, Container Sweat, Container Rain   while Sea Transport </li>\n" +
                "<li>\t\tLow expenses than Silica Gel Per container </li>\n" +
                "<li>\t\tAbsorbtion more than 200% </li>\n" +
                "<li>\t\tNon - toxic and Eco friendly </li>\n" +
                "<li>\t\tLong Lasting until the complete voyage </li>\n" +
                "<li>\t\tNo DMF - No Biocides</li>\n" +
                "<li>\t\tLeak proof - dual layer packed </li>\n" +
                "<li>\t\tEasy Installation </li>\n" +
                "</ul>"));
        home_lay = findViewById(R.id.home_for_client);
        about_lay=findViewById(R.id.about_for_client);
        chat_lay=findViewById(R.id.nav_chat_client);
        toolbar=findViewById(R.id.chat_toolbar);


        String user_= user != null ? user.getDisplayName() : "";
        String e_mail= user != null ? user.getEmail() : "";
        user_name=findViewById(R.id.username);
        user_name.setVisibility(View.VISIBLE);
        user_name.setText(user_);
        e_mail_ = findViewById(R.id.e_mail);
        e_mail_.setVisibility(View.VISIBLE);
        e_mail_.setText(e_mail);


        msg_button=findViewById(R.id.msg_send);
        msg=findViewById(R.id.msg);
        msg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myRef.child(user.getDisplayName()).push().setValue(msg.getText().toString());
                if(user!=null) {
                    //myuser.child("user").push().setValue(user.getDisplayName());
                }
                //Toast.makeText(client.this, myuser.child("user").child(user.getDisplayName()) +"   "+msg.getText().toString(),Toast.LENGTH_SHORT).show();
                msg.setText("");
            }
        });


        Uri image_url= user != null ? user.getPhotoUrl() : null;
        //Toast.makeText(this, ""+image_url, Toast.LENGTH_SHORT).show();
        //Log.d("mirakle", "profileimageclick: "+image_url);
        //ImageView profile=findViewById(R.id.sample_try);

        ImageView cool=findViewById(R.id.sample_try);

        Glide.with(client.this)
                .load(image_url)
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(cool);
        //Picasso.with(client.this)
          //      .load(image_url)
            //    .into(cool);


        //String user_=user.getDisplayName();
        //final TextView user_name;
        //user_name = (TextView) rootView.findViewById(R.id.username);

        //user_name=



        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()==null){
                    Intent intent=new Intent(client.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };



        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        //user_name=findViewById(R.id.username);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.client, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            home_lay.setVisibility(View.VISIBLE);
            fab.setVisibility(View.VISIBLE);
            chat_lay.setVisibility(View.INVISIBLE);
            about_lay.setVisibility(View.INVISIBLE);
            //toolbar.setTitle("Home");
        } else if (id == R.id.nav_chat) {
            //home_lay.setVisibility(View.INVISIBLE);
            home_lay.setVisibility(View.INVISIBLE);
            chat_lay.setVisibility(View.VISIBLE);
            about_lay.setVisibility(View.INVISIBLE);
            fab.setVisibility(View.INVISIBLE);


        } else if (id == R.id.nav_about) {
            home_lay.setVisibility(View.INVISIBLE);
            fab.setVisibility(View.VISIBLE);
            chat_lay.setVisibility(View.INVISIBLE);
            about_lay.setVisibility(View.VISIBLE);
            //toolbar.setTitle("About");

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_help) {

        }
        else if (id==R.id.logout_btn){
            mAuth.signOut();
            FirebaseAuth.getInstance().signOut();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
