package abc.abc.upload_photo.fragment.singlephoto.recyclerview;


import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import abc.abc.upload_photo.R;


public class AttributeViewHolders2 extends RecyclerView.ViewHolder {

    private final TextView textViewKey;
    private final TextView textViewValue;


    public AttributeViewHolders2(@NonNull View itemView) {
        super(itemView);
        textViewKey = itemView.findViewById(R.id.textViewKey);
        textViewValue = itemView.findViewById(R.id.textViewValue);

    }

    public TextView getTextViewKey() {
        return textViewKey;
    }

    public TextView getTextViewValue() {
        return textViewValue;
    }
}
