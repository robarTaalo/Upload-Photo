package abc.abc.upload_photo.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;


public class ExitFragment extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.exit(0);
    }


}