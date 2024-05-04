package abc.abc.upload_photo.fragment.newphoto;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.Scope;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import abc.abc.upload_photo.R;
import abc.abc.upload_photo.databinding.FragmentNewPhotoBinding;
import abc.abc.upload_photo.fragment.newphoto.recyclerview.AttributesAdaptor;
import abc.abc.upload_photo.helper.RoundedTransformation;
import abc.abc.upload_photo.uploadapis.DropBox;
import abc.abc.upload_photo.uploadapis.FireBaseStore;
import abc.abc.upload_photo.uploadapis.GoogleDrive;

public class NewPhotoFragment extends Fragment {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private static final int REQUEST_CODE_SIGN_IN = 1;
    static GoogleDrive mDriveServiceHelper;
    GoogleSignInClient googleSignInClient;
    String fileUid;
    private FragmentNewPhotoBinding binding;
    private FirebaseAuth mAuth;
    private Bitmap imageBitmap = null;
    private ImageView image;
    private AttributesAdaptor attributesAdaptor;
    private View root;
    private int keyCounter = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNewPhotoBinding.inflate(inflater, container, false);
        root = binding.getRoot();
        image = root.findViewById(R.id.image);
        Button uploadBtn = root.findViewById(R.id.uploadBtn);
        FloatingActionButton addAttBtn = root.findViewById(R.id.addAttBtn);
        RecyclerView attributesRecyclerView = root.findViewById(R.id.attributesRecyclerView);

        mAuth = FirebaseAuth.getInstance();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(root.getContext(), LinearLayoutManager.VERTICAL, false);
        attributesRecyclerView.setLayoutManager(linearLayoutManager);
        Map<String, String> attrs = new HashMap<>();
        attrs.put(String.valueOf(keyCounter++), "");
        attrs.put(String.valueOf(keyCounter++), "");
        attrs.put(String.valueOf(keyCounter++), "");

        attributesAdaptor = new AttributesAdaptor(attrs, root.getContext());
        attributesRecyclerView.setAdapter(attributesAdaptor);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tackPhoto();
            }
        });


        addAttBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View v) {
                attrs.put(String.valueOf(keyCounter++), "");
                attributesAdaptor.notifyDataSetChanged();
            }
        });

        GoogleSignInOptions signInOptions =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestScopes(new Scope(DriveScopes.DRIVE_FILE))
                        .requestEmail()
                        .build();
        googleSignInClient = GoogleSignIn.getClient(requireContext(), signInOptions);
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  saveImage();
                Map<String, String> dataValues = attributesAdaptor.getDataValues();
                fileUid = String.valueOf(UUID.randomUUID());

                saveImage(fileUid);
                saveDataFile(fileUid, dataValues);

                DropBox.uploadImage(mAuth.getUid(), fileUid, v.getContext().getApplicationContext());

                // authenticate user google drive account and upload files
                startActivityForResult(googleSignInClient.getSignInIntent(), REQUEST_CODE_SIGN_IN);

                FireBaseStore.uploadImage(mAuth.getUid(), fileUid, v.getContext().getApplicationContext())
                        .addOnSuccessListener( ex->{
                            NavController navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);
                            navController.navigate(R.id.nav_my_photos);
                });
            }
        });
        return root;
    }

    private void tackPhoto() {

        if (root.getContext().checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);
        } else dispatchTakePictureIntent();
    }
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            // display error state to the user
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            image.setImageBitmap(imageBitmap);



        }
        if (requestCode == REQUEST_CODE_SIGN_IN && resultCode == RESULT_OK) {
            GoogleSignIn.getSignedInAccountFromIntent(data)
                    .addOnSuccessListener(googleAccount -> {
                        // Use the authenticated account to sign in to the Drive service.
                        GoogleAccountCredential credential =
                                GoogleAccountCredential.usingOAuth2(
                                        this.getContext(), Collections.singleton(DriveScopes.DRIVE_FILE));
                        credential.setSelectedAccount(googleAccount.getAccount());
                        Drive googleDriveService =
                                new Drive.Builder(
                                        AndroidHttp.newCompatibleTransport(),
                                        new GsonFactory(),
                                        credential)
                                        .setApplicationName("Drive API Migration")
                                        .build();

                        // The DriveServiceHelper encapsulates all REST API and SAF functionality.
                        // Its instantiation is required before handling any onClick actions.
                        mDriveServiceHelper = new GoogleDrive(googleDriveService);

                        //upload files to google drive
                        mDriveServiceHelper.uploadFileToGoogleDrive(fileUid, requireContext().getApplicationContext());
                    });
        }

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MY_CAMERA_REQUEST_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent();
            } else {
                Toast.makeText(root.getContext(), "camera permission denied", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void saveImage(String uid) {

        File file = null;
        try {
            File root2 = root.getContext().getApplicationContext().getDir("imageDir", root.getContext().getApplicationContext().MODE_PRIVATE);
            OutputStream fOut = null;
            file = new File(root2.toString(), uid + ".jpg"); // the File to save.
            fOut = new FileOutputStream(file);
            Bitmap pictureBitmap = imageBitmap; // obtaining the Bitmap
            pictureBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut); // saving the Bitmap to a file compressed as a JPEG
            fOut.flush();
            fOut.close(); // do not forget to close the stream
            MediaStore.Images.Media.insertImage(root.getContext().getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());

        } catch (Exception e) {
        }
    }

    private void saveDataFile(String uid, Map<String, String> dataValues) {

        File file = null;
        try {
            File root2 = root.getContext().getApplicationContext().getDir("dataFileDir", root.getContext().getApplicationContext().MODE_PRIVATE);
            file = new File(root2.toString(), uid + ".txt"); // the File to save.
            BufferedWriter bf = null;
            bf = new BufferedWriter(new FileWriter(file));
            // iterate map entries
            for (Map.Entry<String, String> entry :
                    dataValues.entrySet()) {
                // put key and value separated by a colon
                bf.write(entry.getKey() + ":"
                        + entry.getValue());
                // new line
                bf.newLine();
            }
            bf.flush();
            // always close the writer
            bf.close();

        } catch (Exception e) {
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}