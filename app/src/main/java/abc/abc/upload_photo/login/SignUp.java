package abc.abc.upload_photo.login;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import abc.abc.upload_photo.R;

public class SignUp extends AppCompatActivity {

    EditText editTextFullname;
    EditText editTextAddress;
    EditText editTextMail;
    EditText editTextPassword;
    EditText editTextConfirmPassword;
    String userid;
    String smail = "";
    String spass = "";
    private FirebaseAuth mAuth;

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        editTextFullname = findViewById(R.id.editTextfullname);
        editTextAddress = findViewById(R.id.editTextAddress);
        editTextMail = findViewById(R.id.editTextMail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        mAuth = FirebaseAuth.getInstance();
    }


    public void Regeister(View view) {

        smail = editTextMail.getText().toString();
        spass = editTextPassword.getText().toString();
        String sconfirm = editTextConfirmPassword.getText().toString();
        String fullname = editTextFullname.getText().toString();
        String address = editTextAddress.getText().toString();
        if (fullname.length() < 6) {
            Toast.makeText(this, getResources().getString(R.string.checkfullname), Toast.LENGTH_LONG).show();
            return;
        }
        if (address.length() < 6) {
            Toast.makeText(this, getResources().getString(R.string.checkaddress), Toast.LENGTH_LONG).show();
            return;
        }
        if (!isValidEmail(smail)) {
            Toast.makeText(this, getResources().getString(R.string.checkmail), Toast.LENGTH_LONG).show();
            return;
        }
        if (spass.length() < 6) {
            Toast.makeText(this, getResources().getString(R.string.checkpasslength), Toast.LENGTH_LONG).show();
            return;
        }

        if (spass.equals(sconfirm)) {

            mAuth.createUserWithEmailAndPassword(smail, spass)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            userid = task.getResult().getUser().getUid();

                            verify();
                        } else {
                            Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } else {
            Toast.makeText(this, getResources().getString(R.string.checkpassconfirm), Toast.LENGTH_LONG).show();
        }
    }


    private void verify() {

        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(this, task -> {

            if (task.isSuccessful()) {

                DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("users").child(userid);
                dref.child("providers").setValue("mail");
                dref.child("enabled").setValue(true);
                dref.child("mail").setValue(smail);
                dref.child("fullname").setValue(editTextFullname.getText().toString());
                dref.child("address").setValue(editTextAddress.getText().toString());
                gosignin(null);

            } else {
                mAuth.getCurrentUser().delete();
                Toast.makeText(this, getResources().getString(R.string.checkmail), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void gosignin(View view) {

        Intent intent = new Intent(this, Login.class);
        intent.putExtra("mail", smail);
        intent.putExtra("pass", spass);
        startActivity(intent);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

}