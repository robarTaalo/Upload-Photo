package abc.abc.upload_photo.fragment.myphotos.recyclerview;


import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import abc.abc.upload_photo.R;


public class ImageViewHolders extends RecyclerView.ViewHolder {

    private final ImageView imageView3;


    public ImageViewHolders(@NonNull View itemView) {
        super(itemView);
        imageView3 = itemView.findViewById(R.id.imageView3);
    }

    public ImageView getImageView3() {
        return imageView3;
    }
}
