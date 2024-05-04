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

    Spinner countryCodeSpinner;// عمل spinner لاختيار الدولة
    String verifyCode;
    String TAG = "UserPhoneSignup";
    EditText editTextPhoneNo, editTextCode;
    Button registerButton;
    String phoneNo;
    private FirebaseAuth mAuth;
    // انشاء متغير التنصت علي الحدث والذي يعطيني اوتوماتك ثلاثة دوال للعمل
    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        // الدالة الاولي عند ارسال الكود
        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verifyCode = s;// الحصول علي الكود المرسل من الفيربيز
            Log.d(TAG, " onCodeSent: " + s);
            Log.i(TAG, " onCodeSent22: " + s);
            registerButton.setText(R.string.register);
            editTextCode.setVisibility(View.VISIBLE);
        }

        // الدالة الثانية عند استقبال الكود علي الجهاز الذي يعمل به تطبيقنا هذا
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();// الحصول علي الكود الذي تم استقباله علي الجهاز
            if (code != null) {
                login(code);// ارسال الكود للدالة ok للتاكد من صحته وتسجيل دخول المستخدم
            }
        }

        // عند فشل عملية تاكيد الكود
        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            // اظهار رسالة تفيد ان رقم الهاتف خطأ
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
        // اختيار المانيا كقيمة افتراضية
        countryCodeSpinner.setSelection(66);
        mAuth = FirebaseAuth.getInstance();// FirebaseAuthتعريف

    }

    // عند الضغط علي زر تسجيل
    public void RegisterByPhone(View view) {
        Button b = (Button) view;
        String buttonText = b.getText().toString();
        if (buttonText.equals(getResources().getString(R.string.sendcode))) {
            String p1 = editTextPhoneNo.getText().toString().trim();// الحصول علي رقم الهاتف من edittext
            // الحصول علي كود الدولة من كلاس country عن طريق معرفة اختيار المستخدم لاسم الدولة
            String cod = CountryData.countryAreaCodes[countryCodeSpinner.getSelectedItemPosition()];
            phoneNo = "+" + cod + p1;// تكوين رقم الهاتف بالكامل كود الدولة +رقم الهاتف
            Toast.makeText(getApplicationContext(), phoneNo, Toast.LENGTH_LONG).show();
            if (!p1.isEmpty()) {// التاكد ان رقم الهاتف ليس فارغ
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
            String code = editTextCode.getText().toString();// الحصول علي الكود من edittext
            if (code.length() == 6) {// التاكد ان طول النص يساوي 6 لان الكود هو 6 ارقام
                login(code);// ارسال الكود للدالة ok للتاكد من صحته وتسجيل دخول المستخدم
            }
        }
    }

    // دالة للتاكد من ان صحة الكود الذي تم استقباله سواء اوتوماتيكيا من خلال دالةonVerificationCompleted او من خلال المستخدم
    private void login(String code) {
        //
        PhoneAuthCredential pc = PhoneAuthProvider.getCredential(verifyCode, code);
        // تسجيل دخول المستخدم عن طريق  PhoneAuthCredential والتصنت علي اتمام العملية
        mAuth.signInWithCredential(pc).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            // دالة اكمال العملية
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {// شرط التحقق من نجاح العملية
                    String userid = task.getResult().getUser().getUid();// الحصول علي id المستخدم المسجل في firebase
                    User myUser = new User();
                    myUser.setUuid(userid);
                    myUser.setProvider("Phone");
                    myUser.setEnabled(true);
                    myUser.setPhoto(phoneNo);
                    DatabaseReference dref = FirebaseDatabase.getInstance().getReference().child("users").child(userid);
                    dref.setValue(myUser);
                    // الانتقال الي شاشة   عند نجاح العملية
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);

                } else {
                    // اظهار رسالة تفيد ان الكود  خطأوفشل عملية التسجيل
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