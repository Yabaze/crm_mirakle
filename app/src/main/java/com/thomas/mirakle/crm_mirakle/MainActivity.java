package com.thomas.mirakle.crm_mirakle;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;


import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class MainActivity extends AppCompatActivity {
    Drawable Right,Right1;
    AutoCompleteTextView username;
    TextInputLayout username_layout,password_layout;
    EditText password;
    ProgressBar progressBar;

    Button login_btn,twitter,forgot,signInButton;
    TextView log;
    private static int RC_SIGN_IN=2;
    String email,password_email;
    private static final String EMAIL = "email";
    boolean loggedIn;
    private FirebaseAuth mAuth;
    CallbackManager callbackManager;
    GoogleSignInClient mGoogleSignInClient;
    FirebaseAuth.AuthStateListener mAuthListener;
    LoginButton fbloginButton;
    FirebaseUser user;
    //Firebase usersRef;
    DatabaseReference myuser;
    database_helper db;
    String type_user;
    Button sample_yab;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        mAuth = FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();
        type_user=new String();
       // if (user != null) {
        //    type_user=db.checkmail(user.getEmail());
        //}
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        myuser = database.getReference("Users");
        callbackManager = CallbackManager.Factory.create();
        resource();
        fbloginButton = (LoginButton) findViewById(R.id.fb_login_button);
        //facebook = (Button) findViewById(R.id.facebook);

        //fbloginButton.setReadPermissions(Arrays.asList(EMAIL));
        fbloginButton.setReadPermissions(EMAIL,"public_profile");

        signInButton = findViewById(R.id.g_signin_button);

        db=new database_helper();


        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
              .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
          .build();
        mGoogleSignInClient = GoogleSignIn.getClient(MainActivity.this, gso);
        mAuthListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null){
                    Log.e("firbasemail",""+firebaseAuth.getCurrentUser().getEmail());
                    //type_user=
                    db.checkmail(firebaseAuth.getCurrentUser().getEmail(),getApplicationContext());
                }
            }
        };
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email=username.getText().toString();
                password_email=password.getText().toString();
                signIn();
            }
        });


        /*
        myuser = myuser.child("Users");
        Map<String, String> userData = new HashMap<String, String>();

        userData.put("Nombre", "Yabaze");
        userData.put("Password", "Password");
        userData.put("Confirmed", "FALSE");
        userData.put("Email", "cool_baby");

        myuser.setValue("yabaze");
        myuser = myuser.child("Users").child("yabaze");
        myuser.setValue(userData);
        */


        //trial
        sample_yab=findViewById(R.id.sample_yab);
        sample_yab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sample_cal=new Intent(MainActivity.this,add_event_activity.class);
                startActivity(sample_cal);
            }
        });




        fbloginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d("sucess", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());

            }

            @Override
            public void onCancel() {
                Log.d("Cancel", "facebook:onCancel");

                // App code
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d("Error", "facebook:onError", exception);

                // App code
            }
        });

        LoginManager.getInstance().registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // App code
                        Log.d("TAG", "Success");
                    }

                    @Override
                    public void onCancel() {
                        // App code
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        // App code
                    }
                });
        loggedIn = AccessToken.getCurrentAccessToken() == null;
        //LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile"));

        twitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });





        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,forgot_password.class);
                startActivity(intent);
            }
        });






    }

    private void handleFacebookAccessToken(AccessToken token) {
        Log.d("TAG", "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            TastyToast.makeText(getApplicationContext(), "Login Success", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                            FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            //Toast.makeText(MainActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            TastyToast.makeText(getApplicationContext(), "Authentication failed.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                            //updateUI(null);
                        }
                        if(user != null) {
                            //myuser.child("user").push().setValue(user.getDisplayName());
                        }

                        // ...
                    }
                });
    }


    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w("Failed", "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
       // Log.d("Firebase Authentication processed", "firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Success", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            db.checkmail(user.getEmail(),getApplicationContext());
                        } else {
                            Log.w("Failed", "signInWithCredential:failure", task.getException());
                            //Toast.makeText(MainActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                            TastyToast.makeText(getApplicationContext(), "Authentication failed.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                        }
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        //FirebaseUser currentUser = mAuth.getCurrentUser();
        //GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //mAuth.addAuthStateListener(mAuthListener);
    }
    public void showDialog() throws Resources.NotFoundException {
        new AlertDialog.Builder(this)
                .setTitle("Hello")
                .setMessage("Hello")
                .setIcon(
                        getResources().getDrawable(
                                android.R.drawable.ic_dialog_alert))
                .setPositiveButton(
                        "how are you?",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                //Do Something Here
                                //Intent intent=new Intent(MainActivity.this,admin_page.class);
                                //Toast.makeText(MainActivity.this,"I'm always Fine",Toast.LENGTH_LONG).show();
                                TastyToast.makeText(getApplicationContext(), "I'm always Fine", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                            }
                        })
                .setNegativeButton(
                        "i'm fine",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                //Do Something Here
                            }
                        }).show();
    }
    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.u_name:
                    validateusername();
                    break;
                case  R.id.pass:
                    validatePassword();
                    break;
            }
        }

    }
    private boolean validateusername() {
        String username1 = username.getText().toString().trim();

        if (username1.isEmpty()) {
            username_layout.setError("Enter the correct username");
            requestFocus(username);
            return false;
        }
        else
        {
            username_layout.setErrorEnabled(false);
        }
        return true;
    }
    private boolean validatePassword() {
        String pass=password.getText().toString().trim();
        if (pass.isEmpty()) {
            password.setError("Enter the password");
            requestFocus(password);
            return false;
        } else {
            password_layout.setErrorEnabled(false);
        }

        return true;
    }
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }




    private void resource() {
        Right = getResources().getDrawable( R.mipmap.usericon);
        Right1 = getResources().getDrawable( R.mipmap.pass);
        Right.setBounds( 0, 0, 100, 90 );
        Right1.setBounds( 0, -10, 90, 80 );
        username=findViewById(R.id.u_name);
        username_layout=findViewById(R.id.username_layout);
        password=findViewById(R.id.pass);
        password_layout=findViewById(R.id.password_layout);
        login_btn=findViewById(R.id.login_btn);
        twitter=findViewById(R.id.twitter);
        forgot=findViewById(R.id.forgot);
        log=findViewById(R.id.title);
        username.addTextChangedListener(new MyTextWatcher(username));
        password.addTextChangedListener(new MyTextWatcher(password));
        username.setCompoundDrawables( Right, null, null, null );
        password.setCompoundDrawables( Right1, null, null, null );
        final Typeface typeface = Typeface.createFromAsset(getAssets(),"fonts/AppleMyungjo.ttf");
        Typeface typeface1 = Typeface.createFromAsset(getAssets(),"fonts/cool.ttf");

        forgot.setText(Html.fromHtml("<STRONG><color=\"BLACK\"><u>Forgot Password..?</u><color></STRONG>"));
        forgot.setTypeface(typeface);
        //log.setTypeface(typeface1);
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = username.getText().toString();
                password_email = password.getText().toString();

                if (!email.isEmpty()&&!password_email.isEmpty()){

                    mAuth.signInWithEmailAndPassword(email, password_email)
                            .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.e("cool", "signInWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        //Toast.makeText(MainActivity.this, "Wait Please" + user.getEmail(), Toast.LENGTH_LONG).show();
                                        TastyToast.makeText(getApplicationContext(), "Welcome "+user.getEmail(), TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                                        Log.e("firbasemail",""+user.getEmail());
                                        //type_user=
                                                db.checkmail(user.getEmail(),getApplicationContext());

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.w("coolest", "signInWithEmail:failure", task.getException());
                                        //Toast.makeText(MainActivity.this, "You Entered Wrong Userame Password", Toast.LENGTH_SHORT).show();
                                        TastyToast.makeText(getApplicationContext(), "You Entered Wrong Username and Password.", TastyToast.LENGTH_LONG, TastyToast.ERROR);

                                    }
                                }
                            });
                    }
                    else{
                    validatePassword();
                    validateusername();
                }
            }
        });
    }


}
