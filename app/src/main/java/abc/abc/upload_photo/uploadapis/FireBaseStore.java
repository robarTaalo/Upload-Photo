package abc.abc.upload_photo.uploadapis;

import android.content.Context;
import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class FireBaseStore {


    public static UploadTask uploadImage(String userUid, String fileUid, Context appContext) {
        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Upload image to firebase storage
        File root = appContext.getDir("imageDir", appContext.MODE_PRIVATE);
        File file = new File(root.toString(), fileUid + ".jpg");
        Uri fileuri = Uri.fromFile(file);
        StorageReference storageReference = storage.getReference().child("images").child(userUid)
                .child(fileUid).child(fileuri.getLastPathSegment());
        storageReference.putFile(fileuri);

        // Upload text file to firebase storage
        File root2 = appContext.getDir("dataFileDir", appContext.MODE_PRIVATE);
        File file2 = new File(root2.toString(), fileUid + ".txt");
        Uri fileuri2 = Uri.fromFile(file2);
        StorageReference storageReference2 = storage.getReference().child("images").child(userUid)
                .child(fileUid).child(fileuri2.getLastPathSegment());
       return storageReference2.putFile(fileuri2);


    }


}
