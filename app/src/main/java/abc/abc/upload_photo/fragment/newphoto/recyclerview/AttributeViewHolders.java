package abc.abc.upload_photo.fragment.newphoto.recyclerview;


import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import abc.abc.upload_photo.R;


public class AttributeViewHolders extends RecyclerView.ViewHolder {

    private final EditText editTextKey;
    private final EditText editTextValue;


    public AttributeViewHolders(@NonNull View itemView) {
        super(itemView);
        editTextKey = itemView.findViewById(R.id.editTextKey);
        editTextValue = itemView.findViewById(R.id.editTextValue);

    }


    public EditText getEditTextKey() {
        return editTextKey;
    }

    public EditText getEditTextValue() {
        return editTextValue;
    }
}
