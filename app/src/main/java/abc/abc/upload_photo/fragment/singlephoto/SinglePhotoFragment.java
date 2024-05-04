package abc.abc.upload_photo.fragment.singlephoto;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

import abc.abc.upload_photo.R;
import abc.abc.upload_photo.databinding.FragmentSinglePhotoBinding;
import abc.abc.upload_photo.fragment.singlephoto.recyclerview.AttributesAdaptor2;
import abc.abc.upload_photo.helper.RoundedTransformation;

public class SinglePhotoFragment extends Fragment {


    Map<String, String> data = new HashMap<>();
    private FragmentSinglePhotoBinding binding;
    private FirebaseAuth mAuth;
    private ImageView image2;
    private RecyclerView attributesRecyclerView2;
    private AttributesAdaptor2 attributesAdaptor2;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSinglePhotoBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        image2 = root.findViewById(R.id.image2);
        attributesRecyclerView2 = root.findViewById(R.id.attributesRecyclerView2);

        mAuth = FirebaseAuth.getInstance();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext(), LinearLayoutManager.VERTICAL, false);
        attributesRecyclerView2.setLayoutManager(linearLayoutManager);


        attributesAdaptor2 = new AttributesAdaptor2(data, root.getContext());
        attributesRecyclerView2.setAdapter(attributesAdaptor2);

        String imageURL = this.getArguments().getString("imageURL");
        String filePath = this.getArguments().getString("filePath");
        Log.i("TAG", "onCreateView: " + imageURL);
        Log.i("TAG", "onCreateView: " + filePath);

        Picasso.get()
                .load(imageURL)
                .resize(600, 800)
                .transform(new RoundedTransformation(75, 0))
                .into(image2);
        readData(filePath);

        return root;
    }

    @SuppressLint("NotifyDataSetChanged")
    private void readData(String filePath) {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference fileRef = storage.getReference().child("images").child(filePath);

        final long ONE_MEGABYTE = 1024 * 1024;
        fileRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(bytes -> {
            String data2 = new String(bytes);
            String[] attributes = data2.split("\n");
            for (String s : attributes)
                data.put(s.split(":")[0], s.split(":")[1]);
            attributesAdaptor2.notifyDataSetChanged();

        });


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}