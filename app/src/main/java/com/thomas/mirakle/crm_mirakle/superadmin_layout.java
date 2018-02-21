package com.thomas.mirakle.crm_mirakle;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;


public class superadmin_layout extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference myRef,myuser,contact_ref,userlist_ref,lead_ref;
    ImageView photo;
    TextView name,Mail;
    FirebaseAuth.AuthStateListener mAuthListener;
    ExpandableListView event_list_sa;

    ExpandableListAdapter event_listAdapter;
    List<String> listDataHeader;
    HashMap<String, List<String>> listDataChild;
    ListView contact_list_sa,lead_list_sa;
    ListAdapter contact_listAdapter,lead_listAdapter;
    ArrayList contact_list;
    database_helper db;
    String user_,e_mail;

    //final HashMap<String, ArrayList>[] admin = new HashMap[]{new HashMap<String,ArrayList>()};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_superadmin_layout);
        mAuth=FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myRef = database.getReference("chat");
        myuser = database.getReference("Users");
        contact_ref = myuser.child("contact");
        userlist_ref=myuser.child("userlist");
        lead_ref=myuser.child("lead");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

         user_= user != null ? user.getDisplayName() : "";
         e_mail= user != null ? user.getEmail() : "";
        name=findViewById(R.id.name_superadmin);
        Mail=findViewById(R.id.mail_superadmin);
        Log.e("e_mail yabaze"+e_mail,"Mail"+user_);
        try{
            name.setText(user_);
            Mail.setText(e_mail);
            Uri image_url= user != null ? user.getPhotoUrl() : null;
            photo=findViewById(R.id.photo_superadmin);

            Glide.with(superadmin_layout.this)
                    .load(image_url)
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(photo);
        }
        catch (NullPointerException e)
        {
            Log.e("Mail does not ","get any detail");
        }



        /* starts before 1 month from now */
        Calendar startDate = Calendar.getInstance();
        startDate.add(Calendar.MONTH, -1);

        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);


        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .build();

        /*
        //unselected date config
        horizontalCalendar.getDefaultStyle()
                .setColorTopText(Color.parseColor("#FF0000"))
        .setColorMiddleText(Color.parseColor("#FFFF35"))
        .setColorBottomText(Color.parseColor("#0000FF"));

        //date underline config
        horizontalCalendar.getConfig()
                .setSelectorColor(Color.BLACK)
                .setSizeTopText(23);
        */

        /*Disable some dates
        HorizontalCalendar.Builder builder = new HorizontalCalendar.Builder();
        builder.disableDates(new HorizontalCalendarPredicate() {
            @Override
            public boolean test(Calendar date) {
                return false;    // return true if this date should be disabled, false otherwise.
            }

            @Override
            public CalendarItemStyle style() {
                return null;     // create and return a new Style for disabled dates, or null if no styling needed.
            }
        });
        */

        //date_sample=findViewById(R.id.date_sample);
        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSelected(Calendar date, int position) {
                //do something
                //date_sample.setText(date.get(Calendar.DATE)+"-"+date.get(Calendar.MONTH)+"-"+date.get(Calendar.YEAR));
            }
        });




        event_list_sa=findViewById(R.id.event_list_sa);
        prepareListData();
        event_listAdapter = new com.thomas.mirakle.crm_mirakle.ExpandableListAdapter(this, listDataHeader, listDataChild);
        event_list_sa.setAdapter(event_listAdapter);

        contact_list_sa=findViewById(R.id.contact_list_sa);
        lead_list_sa=findViewById(R.id.lead_list_sa);
        contact_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String,ArrayList> admin[] = new HashMap[10000];
                admin[0] = (HashMap<String, ArrayList>) dataSnapshot.getValue();
                Collection<ArrayList> values = admin[0].values();
                ArrayList listOfValues = new ArrayList<>(values);
                contact_listAdapter=new ArrayAdapter<>(superadmin_layout.this,android.R.layout.simple_list_item_1,listOfValues);
                contact_list_sa.setAdapter(contact_listAdapter);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        lead_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final HashMap<String,String> lead_[] = new HashMap[1];
                final HashMap<String,String> lea_[] = new HashMap[1];
                lead_[0] = (HashMap<String, String>) dataSnapshot.getValue();
                final Collection<String> update_lead = lead_[0].values();

                Set<String> le = lead_[0].keySet();
                final ArrayList<String> le_array=new ArrayList<>(le);
                Log.e("lead_details",""+update_lead);

                lead_listAdapter=new ArrayAdapter<>(superadmin_layout.this,android.R.layout.simple_list_item_1,le_array);
                lead_list_sa.setAdapter(lead_listAdapter);
                lead_list_sa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //Log.e("position"+position,"id"+id);
                        LayoutInflater inflater = getLayoutInflater();
                        View alertLayout = inflater.inflate(R.layout.new_lead, null);
                        final EditText lead_owner = alertLayout.findViewById(R.id.lead_owner);
                        final EditText lead_username = alertLayout.findViewById(R.id.lead_username);
                        final EditText lead_title = alertLayout.findViewById(R.id.lead_title);
                        final EditText lead_phone = alertLayout.findViewById(R.id.lead_phone);
                        final EditText lead_address = alertLayout.findViewById(R.id.lead_address);
                        final EditText lead_desc = alertLayout.findViewById(R.id.lead_desc);
                        final Spinner lead_source=alertLayout.findViewById(R.id.lead_source);
                        final Spinner lead_status=alertLayout.findViewById(R.id.lead_status);
                        lead_title.setEnabled(false);
                        lead_owner.setEnabled(false);
                        lead_username.setEnabled(false);

                        final String selected_string = lead_list_sa.getItemAtPosition(position).toString();
                        //HashMap<String,String> hash_elements=get_hash(selected_string);




                        final HashMap<String, String>[] ret = new HashMap[1];
                        DatabaseReference sel = lead_ref.child(selected_string);
                        sel.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                ret[0] = (HashMap<String, String>) dataSnapshot.getValue();
                                lead_owner.setText(ret[0].get("Name"));
                                lead_address.setText(ret[0].get("Address"));
                                lead_desc.setText(ret[0].get("Description"));
                                lead_phone.setText(ret[0].get("Contact"));
                                lead_title.setText(selected_string);
                                lead_username.setText(ret[0].get("Mail"));
                                lead_source.setSelection(Integer.parseInt(ret[0].get("So_pos")));
                                lead_status.setSelection(Integer.parseInt(ret[0].get("St_pos")));
                                //Log.e("my name is Khan",""+ret[0]);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });




                        AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                        alert.setTitle("Update Lead");
                        // this is set the view from XML inside AlertDialog
                        alert.setView(alertLayout);
                        // disallow cancel of AlertDialog on click of back button and outside touch
                        alert.setCancelable(false);
                        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //Toast.makeText(getBaseContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
                            }
                        });

                        alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String lead_own,lead_un,lead_ti,lead_ph,lead_sour,lead_sta,lead_add,lead_des;
                                lead_add=lead_address.getText().toString();
                                lead_des=lead_desc.getText().toString();
                                lead_own=lead_owner.getText().toString();
                                lead_ph=lead_phone.getText().toString();
                                lead_sour=lead_source.getSelectedItem().toString();
                                lead_sta=lead_status.getSelectedItem().toString();
                                lead_ti=lead_title.getText().toString();
                                lead_un=lead_username.getText().toString();
                                int so_pos,st_pos;
                                so_pos=lead_source.getSelectedItemPosition();
                                st_pos=lead_status.getSelectedItemPosition();
                                create_new_lead(lead_own,lead_un,lead_ti,lead_ph,lead_sour,lead_sta,lead_add,lead_des,so_pos,st_pos);
                            }
                        });
                        AlertDialog dialog = alert.create();
                        dialog.show();


                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });




        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent intent = new Intent(superadmin_layout.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
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

    private HashMap<String,String> get_hash(String selected_string) {
        final HashMap<String, String>[] ret = new HashMap[1];
        DatabaseReference sel = lead_ref.child(selected_string);
        sel.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ret[0] = (HashMap<String, String>) dataSnapshot.getValue();
                Log.e("cool_baby Name",""+ret[0].get("Name"));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //if(ret[0]==null){
         //   get_hash(selected_string);
        //}
        //else {
        //    return ret[0];
       // }
        return ret[0];
    }


    public void new_user(View view) {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.new_contact, null);
        final EditText etUsername = alertLayout.findViewById(R.id.new_username);
        final EditText etEmail = alertLayout.findViewById(R.id.new_password);
        final CheckBox cbToggle = alertLayout.findViewById(R.id.show_password);
        final Spinner table_name=alertLayout.findViewById(R.id.selected_un);

        cbToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // to encode password in dots
                    etEmail.setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else {
                    // to display the password in normal text
                    etEmail.setTransformationMethod(null);
                }
            }
        });

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("New Contact");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getBaseContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
            }
        });

        alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String user = etUsername.getText().toString();
                String pass = etEmail.getText().toString();
                String table=table_name.getSelectedItem().toString();
                create_new_user(user,pass,table);
                //TastyToast.makeText(getBaseContext(),"User Created Sucessfully",TastyToast.LENGTH_LONG,TastyToast.SUCCESS)
                //Toast.makeText(getBaseContext(), "Username: " + user + " Email: " + pass + " Spinner: "+table, Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    private void create_new_user(final String name_new, String pass_new, final String cool) {
        mAuth.createUserWithEmailAndPassword(name_new, pass_new)
                .addOnCompleteListener(superadmin_layout.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Sucess", "createUserWithEmail:success");
                            TastyToast.makeText(superadmin_layout.this,"User Created Successfully",TastyToast.LENGTH_LONG,TastyToast.SUCCESS).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            //contact_ref.child("contact").push().setValue(name_new);
                            contact_ref.push().setValue(name_new);
                            userlist_ref.child(cool.toLowerCase()).push().setValue(name_new);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Failed", "createUserWithEmail:failure", task.getException());
                            TastyToast.makeText(superadmin_layout.this,"Failed... Try Again",TastyToast.LENGTH_LONG,TastyToast.ERROR).show();

                        }
                    }
                });
    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Top 250");
        listDataHeader.add("Now Showing");
        listDataHeader.add("Coming Soon..");

        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
    }


    public void new_lead(View v)
    {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.new_lead, null);
        final EditText lead_owner = alertLayout.findViewById(R.id.lead_owner);
        lead_owner.setText(user_);
        final EditText lead_username = alertLayout.findViewById(R.id.lead_username);
        lead_username.setText(e_mail);
        final EditText lead_title = alertLayout.findViewById(R.id.lead_title);
        final EditText lead_phone = alertLayout.findViewById(R.id.lead_phone);
        final EditText lead_address = alertLayout.findViewById(R.id.lead_address);
        final EditText lead_desc = alertLayout.findViewById(R.id.lead_desc);
        final Spinner lead_source=alertLayout.findViewById(R.id.lead_source);
        final Spinner lead_status=alertLayout.findViewById(R.id.lead_status);


        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("New Lead");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Toast.makeText(getBaseContext(), "Cancel clicked", Toast.LENGTH_SHORT).show();
            }
        });

        alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String lead_own,lead_un,lead_ti,lead_ph,lead_sour,lead_sta,lead_add,lead_des;
                lead_add=lead_address.getText().toString();
                lead_des=lead_desc.getText().toString();
                lead_own=lead_owner.getText().toString();
                lead_ph=lead_phone.getText().toString();
                lead_sour=lead_source.getSelectedItem().toString();
                lead_sta=lead_status.getSelectedItem().toString();
                lead_ti=lead_title.getText().toString();
                lead_un=lead_username.getText().toString();
                int so_pos,st_pos;
                so_pos=lead_source.getSelectedItemPosition();
                st_pos=lead_status.getSelectedItemPosition();
                create_new_lead(lead_own,lead_un,lead_ti,lead_ph,lead_sour,lead_sta,lead_add,lead_des,so_pos,st_pos);
            }
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }
    private void create_new_lead(String lead_own, String lead_un, String lead_ti, String lead_ph, String lead_sour, String lead_sta, String lead_add, String lead_des,int so_pos,int st_pos) {
        //lead_ref.push().setValue(lead_ti);
        HashMap<String,String> lead=new HashMap<>();
        lead.put("Name",lead_own);
        lead.put("Mail",lead_un);
        lead.put("Contact",lead_ph);
        lead.put("Lead Source",lead_sour);
        lead.put("Lead Status",lead_sta);
        lead.put("Address",lead_add);
        lead.put("Description",lead_des);
        lead.put("So_pos",""+so_pos);
        lead.put("St_pos",""+st_pos);
        lead_ref.child(lead_ti).setValue(lead);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.superadmin_layout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id==R.id.logout){
            mAuth.signOut();
            FirebaseAuth.getInstance().signOut();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }else if (id==R.id.logout_btn){
            mAuth.signOut();
            FirebaseAuth.getInstance().signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}


