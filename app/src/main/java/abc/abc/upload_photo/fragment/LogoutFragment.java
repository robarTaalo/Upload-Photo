package abc.abc.upload_photo.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import abc.abc.upload_photo.login.Login;


public class LogoutFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseAuth.getInstance().signOut();
        Intent intent=new Intent(getContext(), Login.class);
        startActivity(intent);
        requireActivity().finish();

    }


}