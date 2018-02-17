package com.thomas.mirakle.crm_mirakle;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by thomas on 14/02/18.
 */

public class database_helper {
    // Write a message to the database
    FirebaseDatabase database;
    DatabaseReference myRef;

    database_helper(){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
    }

    public void checkmail(final String email, final Context c) {
        final String[] user_identity = new String[1];
        final HashMap<String, ArrayList>[] admin = new HashMap[]{new HashMap<String, ArrayList>()};
        DatabaseReference admin_ref = myRef.child("userlist");

        admin_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                admin[0] = (HashMap<String, ArrayList>) dataSnapshot.getValue();

                ArrayList a = get_admin_mail();
                Log.e("Array size admin", "" + a.size());
                 if(a.size()>0){
                    for (int i = 0; i < a.size(); i++) {
                        //Log.e("" + i, "" + a.get(i));
                        if (Objects.equals(a.get(i).toString(), email)) {
                            user_identity[0]="admin";
                        }
                    }
                }
                ArrayList b = get_superadmin_mail();
                Log.e("Array size superadmin", "" + b.size());
                if(b.size()>0){
                    for (int i = 0; i < b.size(); i++) {
                        //Log.e("" + i, "" + b.get(i));
                        if (Objects.equals(b.get(i).toString(), email)) {
                            user_identity[0]="superadmin";
                        }
                    }
                }
                ArrayList c = get_potential_mail();
                Log.e("Array size potential", "" + c.size());
                if(c.size()>0){
                    for (int i = 0; i < c.size(); i++) {
                        //Log.e("" + i, "" + c.get(i));
                        if (Objects.equals(c.get(i).toString(), email)) {
                            user_identity[0]="potential";
                            //Log.e("table helper",""+user_identity[0]);

                        }
                    }
                }
                ArrayList d = get_client_mail();
                Log.e("Array list", "" + d.size());
                if(d.size()>0){
                    for (int i = 0; i < c.size(); i++) {
                        //Log.e("" + i, "" + d.get(i));
                        if (Objects.equals(d.get(i).toString(), email)) {
                            user_identity[0]="client";
                            Log.e("table helper",""+user_identity[0]);

                        }
                    }
                }
                if (user_identity[0]!=null) {
                    switch (user_identity[0]) {
                        case "potential":
                            potential_lay();
                            break;
                        case "admin":
                            admin_lay();
                            break;
                        case "superadmin":
                            spradmin_lay();
                            break;
                        default:
                            client_lay();
                            break;
                    }
                }
            }

            private void client_lay() {
                Intent clientintent = new Intent(c, superadmin_layout.class);
                c.startActivity(clientintent);
            }

            private void spradmin_lay() {
                Intent sprintent = new Intent(c, superadmin_layout.class);
                c.startActivity(sprintent);
            }

            private void admin_lay() {
                Intent adminintent = new Intent(c, admin_layout.class);
                c.startActivity(adminintent);
            }

            private void potential_lay() {
                Intent intent = new Intent(c, potential_layout.class);
                c.startActivity(intent);
            }

            private ArrayList<String> get_client_mail() {
                return admin[0].get("client");
            }
            private ArrayList get_potential_mail() {
                return admin[0].get("potential");
            }
            private ArrayList get_superadmin_mail(){
                return admin[0].get("superadmin");
            }
            private ArrayList get_admin_mail() {
                return admin[0].get("admin");
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
