package abc.abc.upload_photo.fragment.myphotos;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import abc.abc.upload_photo.R;
import abc.abc.upload_photo.databinding.FragmentMyPhotosBinding;
import abc.abc.upload_photo.fragment.myphotos.recyclerview.ImagesAdaptor;

public class MyPhotosFragment extends Fragment {

    private FragmentMyPhotosBinding binding;
    private FirebaseAuth mAuth;
    private ImagesAdaptor imagesAdaptor;
    private Map<String, String> data = new HashMap<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMyPhotosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        RecyclerView imagesRecyclerView = root.findViewById(R.id.imagesRecyclerView);
        mAuth = FirebaseAuth.getInstance();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext(), LinearLayoutManager.VERTICAL, false);
        imagesRecyclerView.setLayoutManager(linearLayoutManager);
        imagesAdaptor = new ImagesAdaptor(data, requireActivity());
        imagesRecyclerView.setAdapter(imagesAdaptor);
        getImagesData();
        return root;
    }

    private void getImagesData() {
        String uid = mAuth.getUid();
        Log.i("", "getImagesData: " + uid);
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference listRef = storage.getReference().child("images/" + uid);
        List<String> images2 = new ArrayList<>();
        listRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(ListResult listResult) {
                Log.i("TAG", "onSuccess: getImagesData" );
                for (StorageReference prefix : listResult.getPrefixes()) {
                    // All the prefixes under listRef.
                    // You may call listAll() recursively on them.
                    prefix.listAll().addOnSuccessListener(listResult2 -> {
                        for (StorageReference item : listResult2.getItems()) {
                            item.getDownloadUrl().addOnSuccessListener(uri -> {
                                if (uri.toString().contains(".jpg")) {
                                    data.put(uri.toString(), uid + "/" + prefix.getName() + "/" + item.getName().replace("jpg", "txt"));
                                    imagesAdaptor.notifyDataSetChanged();
                                }
                            });
                        }
                    });
                }
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}