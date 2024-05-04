package abc.abc.upload_photo.fragment.myphotos.recyclerview;


import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.Map;

import abc.abc.upload_photo.R;
import abc.abc.upload_photo.helper.RoundedTransformation;

public class ImagesAdaptor extends RecyclerView.Adapter<ImageViewHolders> {
    private final Activity activity;
    private Map<String, String> data;

    public ImagesAdaptor(Map<String, String> data, Activity activity) {
        this.data = data;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ImageViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View attribute_item = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        attribute_item.setLayoutParams(lp);
        return new ImageViewHolders(attribute_item);
    }


    @Override
    public void onBindViewHolder(@NonNull ImageViewHolders item, int position) {
        String imageURL = data.keySet().toArray()[position].toString();
        Picasso.get()
                .load(imageURL)
                .resize(500, 750)
                .transform(new RoundedTransformation(50, 0))
                .into(item.getImageView3());


        item.getImageView3().setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(activity, R.id.nav_host_fragment_content_main);
            Bundle bundle = new Bundle();
            bundle.putString("imageURL", imageURL);
            bundle.putString("filePath", data.get(imageURL));
            navController.navigate(R.id.nav_single_photo, bundle);

        });
    }


    @Override
    public int getItemCount() {
        return data.size();
    }
}
