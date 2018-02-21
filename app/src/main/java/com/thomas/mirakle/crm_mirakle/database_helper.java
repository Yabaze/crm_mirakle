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
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Created by thomas on 14/02/18.
 */

public class database_helper {
    // Write a message to the database
    FirebaseDatabase database;
    DatabaseReference myRef,myuser;
    database_helper(){
        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("Users");
    }

    public void checkmail(final String email, final Context c) {
        final String[] user_identity = new String[1];
        DatabaseReference admin_ref = myRef.child("userlist");
        admin_ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final HashMap<String, HashMap<String, ArrayList>>[] admin = new HashMap[1];//size=1
                admin[0] = (HashMap<String, HashMap<String, ArrayList>>) dataSnapshot.getValue();
                HashMap<String, ArrayList> aa = admin[0].get("admin");
                Collection<ArrayList> a = aa.values();
                HashMap<String, ArrayList> bb = admin[0].get("superadmin");
                Collection<ArrayList> b = bb.values();
                HashMap<String, ArrayList> cc = admin[0].get("potential");
                Collection<ArrayList> c = cc.values();
                HashMap<String, ArrayList> dd = admin[0].get("client");
                Collection<ArrayList> d = dd.values();
                 if(a.size()>0){
                    for (int i = 0; i < a.size(); i++) {
                        if (Objects.equals(a.toArray()[i], email)) {
                            user_identity[0]="admin";
                        }
                    }
                }
                if(b.size()>0){
                    for (int i = 0; i < b.size(); i++) {
                        if (Objects.equals(b.toArray()[i], email)) {
                            user_identity[0]="superadmin";
                        }
                    }
                }
                if(c.size()>0){
                    for (int i = 0; i < c.size(); i++) {
                        if (Objects.equals(c.toArray()[i], email)) {
                            user_identity[0]="potential";
                        }
                    }
                }
                if(d.size()>0){
                    for (int i = 0; i < d.size(); i++) {
                        if (Objects.equals(d.toArray()[i], email)) {
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
                Intent clientintent = new Intent(c, client.class);
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
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}