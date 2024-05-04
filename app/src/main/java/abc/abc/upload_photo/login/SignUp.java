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
        mAuth = FirebaseAuth.getInstance();// FirebaseAuthتعريف
    }

    // دالة تنفذ عند الضغط علي زر تسجيل
    public void Regeister(View view) {

        smail = editTextMail.getText().toString();// الحصول علي الميل الذي تم ادخاله
        spass = editTextPassword.getText().toString();// الحصول علي الباسورد
        String sconfirm = editTextConfirmPassword.getText().toString();// الحصول علي تاكيد الباسورد
        String fullname = editTextFullname.getText().toString();// الحصول علي
        String address = editTextAddress.getText().toString();// الحصول علي
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

        if (spass.equals(sconfirm)) {// التاكد من ان الباسورد والتكيد متطابقان
            // انشاء مستخدم جديد بالميل والباسورد والتصنت علي اتمام العملية
            mAuth.createUserWithEmailAndPassword(smail, spass)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {// التاكد من نجاح العملية
                            userid = task.getResult().getUser().getUid();// الحصول علي id المستخدم المسجل في firebase

                            verify();// تفعيل دالة ارسال ايميل اثبات الملكية
                        } else { // في حالة عدم نجاح العملية ارسال رسالة خطأ
                            Toast.makeText(this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        } else {// في حالة عدم تطابق الباسورد مع التاكيد يظهر رسالة خطأ
            Toast.makeText(this, getResources().getString(R.string.checkpassconfirm), Toast.LENGTH_LONG).show();
        }
    }

    // دالة ارسال ميل اثبات ملكية
    private void verify() {
// ارسال ميل اثبات ملكية والتصنت علي العملية
        mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(this, task -> {

            if (task.isSuccessful()) {// في حالة نجاح العملية
                // الاتصال بقاعدة بيانات ال firebase لتسجيل المستخدم والسماح له بالعمل
                DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("users").child(userid);
                dref.child("providers").setValue("mail");
                dref.child("enabled").setValue(true);
                dref.child("mail").setValue(smail);
                dref.child("fullname").setValue(editTextFullname.getText().toString());
                dref.child("address").setValue(editTextAddress.getText().toString());
                gosignin(null);

            } else {// في حالة فشل العملية حذف التسجيل في firebase Auth واظهار رسالة خطأ
                mAuth.getCurrentUser().delete();
                Toast.makeText(this, getResources().getString(R.string.checkmail), Toast.LENGTH_LONG).show();
            }
        });
    }

    public void gosignin(View view) {
// انشاء intent للانتقال الي شاشة تسجيل الدخول
        Intent intent = new Intent(this, Login.class);
        intent.putExtra("mail", smail);//  اضافة الميل الي intent لارساله الي شاشة تسجيل الدخول
        intent.putExtra("pass", spass);// اضافة الباسورد الي intent
        startActivity(intent);// الذهاب الي شاشة تسجيل الدخول
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        finish();
    }

}