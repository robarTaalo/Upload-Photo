package abc.abc.upload_photo.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

import abc.abc.upload_photo.MainActivity;
import abc.abc.upload_photo.R;

public class UserPhoneSignup extends AppCompatActivity {

    Spinner countryCodeSpinner;
    String verifyCode;
    String TAG = "UserPhoneSignup";
    EditText editTextPhoneNo, editTextCode;
    Button registerButton;
    String phoneNo;
    private FirebaseAuth mAuth;

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verifyCode = s;
            Log.d(TAG, " onCodeSent: " + s);
            Log.i(TAG, " onCodeSent22: " + s);
            registerButton.setText(R.string.register);
            editTextCode.setVisibility(View.VISIBLE);
        }


        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                login(code);
            }
        }


        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {

            Toast.makeText(getApplicationContext(), "bad phonenumber", Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_phone_signup);
        countryCodeSpinner = findViewById(R.id.spinner);
        editTextPhoneNo = findViewById(R.id.editTextPhoneNo);
        editTextCode = findViewById(R.id.editTextCode);
        registerButton = findViewById(R.id.registerbtn);
        countryCodeSpinner.setAdapter(
                new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_dropdown_item,
                        CountryData.countryNames));

        countryCodeSpinner.setSelection(66);
        mAuth = FirebaseAuth.getInstance();

    }


    public void RegisterByPhone(View view) {
        Button b = (Button) view;
        String buttonText = b.getText().toString();
        if (buttonText.equals(getResources().getString(R.string.sendcode))) {
            String p1 = editTextPhoneNo.getText().toString().trim();

            String cod = CountryData.countryAreaCodes[countryCodeSpinner.getSelectedItemPosition()];
            phoneNo = "+" + cod + p1;
            Toast.makeText(getApplicationContext(), phoneNo, Toast.LENGTH_LONG).show();
            if (!p1.isEmpty()) {
                PhoneAuthOptions options =
                        PhoneAuthOptions.newBuilder(mAuth)
                                .setPhoneNumber(phoneNo)       // Phone number to verify
                                .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                                .setActivity(this)                 // Activity (for callback binding)
                                .setCallbacks(mCallbacks)          // OnVerificationStateChangedCallbacks
                                .build();
                PhoneAuthProvider.verifyPhoneNumber(options);
            }
        }
        if (buttonText.equals(getResources().getString(R.string.register))) {
            String code = editTextCode.getText().toString();
            if (code.length() == 6) {
                login(code);
            }
        }
    }

    private void login(String code) {

        PhoneAuthCredential pc = PhoneAuthProvider.getCredential(verifyCode, code);
        mAuth.signInWithCredential(pc).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            // دالة اكمال العملية
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String userid = task.getResult().getUser().getUid();
                    User myUser = new User();
                    myUser.setUuid(userid);
                    myUser.setProvider("Phone");
                    myUser.setEnabled(true);
                    myUser.setPhoto(phoneNo);
                    DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("users").child(userid);
                    dref.setValue(myUser);

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                } else {

                    Toast.makeText(getApplicationContext(), "Wrong code", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void change(View view) {
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();

    }
}