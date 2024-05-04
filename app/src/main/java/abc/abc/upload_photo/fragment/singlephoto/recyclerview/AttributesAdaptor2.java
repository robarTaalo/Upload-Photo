package abc.abc.upload_photo.fragment.singlephoto.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import abc.abc.upload_photo.R;
import abc.abc.upload_photo.fragment.newphoto.recyclerview.Attribute;
import abc.abc.upload_photo.fragment.newphoto.recyclerview.AttributeViewHolders;


public class AttributesAdaptor2 extends RecyclerView.Adapter<AttributeViewHolders2> {
    private final Map<String, String> data;
    private final Context context;
    private List<Attribute> attributes;
    private List<AttributeViewHolders> attributeViewHoldersList = new ArrayList<>();

    public AttributesAdaptor2(Map<String, String> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public AttributeViewHolders2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View attribute_item = LayoutInflater.from(parent.getContext()).inflate(R.layout.attribute_item2, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        attribute_item.setLayoutParams(lp);
        return new AttributeViewHolders2(attribute_item);
    }


    @Override
    public void onBindViewHolder(@NonNull AttributeViewHolders2 attribute_item2, int position) {
        String key = data.keySet().toArray()[position].toString();
        attribute_item2.getTextViewKey().setText(key);
        attribute_item2.getTextViewValue().setText(data.get(key));

    }


    @Override
    public int getItemCount() {
        return data.size();
    }
}