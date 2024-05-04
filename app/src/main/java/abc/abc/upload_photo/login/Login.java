package abc.abc.upload_photo.login;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.appset.AppSet;
import com.google.android.gms.appset.AppSetIdClient;
import com.google.android.gms.appset.AppSetIdInfo;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import abc.abc.upload_photo.MainActivity;
import abc.abc.upload_photo.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class Login extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;
    private final String TAG = "Signin";
    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    private EditText editTextMail, editTextPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        CircleImageView googleImageButton = findViewById(R.id.googleimageButton);
        editTextMail = findViewById(R.id.editTextMail);
        editTextPassword = findViewById(R.id.editTextPassword);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        mAuth = FirebaseAuth.getInstance();// FirebaseAuthتعريف
        FirebaseAuth.getInstance().signOut();
        googleImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInWithGoogle();
            }
        });
        Context context = getApplicationContext();
        AppSetIdClient client = AppSet.getClient(context);
        Task<AppSetIdInfo> task = client.getAppSetIdInfo();

        task.addOnSuccessListener(new OnSuccessListener<AppSetIdInfo>() {
            @Override
            public void onSuccess(AppSetIdInfo info) {
                // Determine current scope of app set ID.
                int scope = info.getScope();

                // Read app set ID value, which uses version 4 of the
                // universally unique identifier (UUID) format.
                String id = info.getId();
                Log.i(TAG, "onSuccess: " + id);
            }
        });


    }


    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        }

    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            User myUser = new User();
                            myUser.setUuid(user.getUid());
                            myUser.setProvider("Google");
                            myUser.setEnabled(true);
                            myUser.setEmail(user.getEmail());
                            myUser.setName(user.getDisplayName());
                            myUser.setPhone(user.getPhoneNumber());
                            myUser.setPhoto(user.getPhotoUrl().toString());
                            DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("users").child(user.getUid());
                            dref.setValue(myUser);
                            updateUI();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                        }
                    }
                });
    }

    public void signInWithEmailAndPassword(View view) {
        String smail = editTextMail.getText().toString();// الحصول علي الميل من edittext الخاص بالميل
        String spass = editTextPassword.getText().toString();// الحصول علي الباسورد
        final ValueEventListener[] vel = {null};
// تسجيل الدخول بالباسورد والميل وعمل تصنت علي العملية

        mAuth.signInWithEmailAndPassword(smail, spass).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // التاكد من نجاح العملية
                if (task.isSuccessful()) {// اذا تم نجاح العملية
                    if (mAuth.getCurrentUser().isEmailVerified()) {// التاكد من اثبات ملكية الميل
                        updateUI();
                    } else {
                        // اظهار رسالة تطلب اثبات ملكية الميل
                        Toast.makeText(getApplicationContext(), " please verify your E-Mail", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // اظهار رسالة تفيد ان خطأ في الميل او الباسورد
                    Toast.makeText(getApplicationContext(), " Invalid E-Mail or password", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private void updateUI() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    public void changeActivity(View view) {
        String x = "abc.abc.upload_photo:id/";
        String s = view.getResources().getResourceName(view.getId());
        if (s.contains(x)) s = s.replace(x, "");
        Intent intent = null;

        switch (s) {
            case "phoneimageButton" -> intent = new Intent(this, UserPhoneSignup.class);
            case "usersignup" -> intent = new Intent(this, SignUp.class);
        }
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();

    }


}