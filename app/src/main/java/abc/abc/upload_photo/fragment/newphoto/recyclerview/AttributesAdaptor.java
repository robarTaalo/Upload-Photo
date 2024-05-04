package abc.abc.upload_photo.fragment.newphoto.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import abc.abc.upload_photo.R;


public class AttributesAdaptor extends RecyclerView.Adapter<AttributeViewHolders> {
    private final Map<String, String> attrs;
    private final Context context;
    private List<Attribute> attributes;
    private List<AttributeViewHolders> attributeViewHoldersList = new ArrayList<>();

    public AttributesAdaptor(Map<String, String> attrs, Context context) {
        this.attrs = attrs;
        this.context = context;
    }

    @NonNull
    @Override
    public AttributeViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View attribute_item = LayoutInflater.from(parent.getContext()).inflate(R.layout.attribute_item, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        attribute_item.setLayoutParams(lp);
        return new AttributeViewHolders(attribute_item);
    }


    @Override
    public void onBindViewHolder(@NonNull AttributeViewHolders attribute_item, int position) {
        attributeViewHoldersList.add(attribute_item);
    }

    public Map<String, String> getDataValues() {
        Map<String, String> data = new HashMap<>();
        for (AttributeViewHolders attribute_item : attributeViewHoldersList)
            if (!attribute_item.getEditTextKey().getText().toString().trim().isEmpty() &&
                    !attribute_item.getEditTextValue().getText().toString().trim().isEmpty())
                data.put(attribute_item.getEditTextKey().getText().toString(),
                        attribute_item.getEditTextValue().getText().toString());

        return data;
    }

    @Override
    public int getItemCount() {
        return attrs.size();
    }
}