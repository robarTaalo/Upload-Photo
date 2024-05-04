package abc.abc.upload_photo.uploadapis;


import android.content.Context;

import com.google.android.gms.tasks.Tasks;
import com.google.api.client.http.FileContent;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.util.Collections;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class GoogleDrive {

    private final Executor mExecutor = Executors.newSingleThreadExecutor();
    private final Drive mDriveService;

    private final String FOLDER_MIME_TYPE = "application/vnd.google-apps.folder";
    private final String FOLDER_NAME = "Upload_Photo_App";

    public GoogleDrive(Drive driveService) {
        mDriveService = driveService;
    }


    /**
     * Upload the file to the user's My Drive Folder.
     */
    public void uploadFileToGoogleDrive(String fileUid, Context appContext) {

        Tasks.call(mExecutor, () -> {
            java.io.File root = appContext.getDir("imageDir", appContext.MODE_PRIVATE);
            java.io.File imageFilePath = new java.io.File(root.toString(), fileUid + ".jpg");
            java.io.File root2 = appContext.getDir("dataFileDir", appContext.MODE_PRIVATE);
            java.io.File txtFilePath = new java.io.File(root2.toString(), fileUid + ".txt");
            String rootFolderId = "";
            // get rootFolderId
            FileList result = mDriveService.files().list().setQ("mimeType='application/vnd.google-apps.folder' and trashed=false").execute();
            for (File file : result.getFiles()) {
                if (file.getName().equals(FOLDER_NAME))
                    rootFolderId = file.getId();
            }
            if (rootFolderId.isEmpty()) {
                // create rootFolderId
                File metadata = new File()
                        .setParents(Collections.singletonList("root"))
                        .setMimeType(FOLDER_MIME_TYPE)
                        .setName(FOLDER_NAME);
                rootFolderId = mDriveService.files().create(metadata).execute().getId();
            }
            // create new Folder
            File metadata = new File()
                    .setParents(Collections.singletonList(rootFolderId))
                    .setMimeType(FOLDER_MIME_TYPE)
                    .setName(fileUid);

            String folderId = mDriveService.files().create(metadata).execute().getId();

           // upload image file
            File fileMetadata = new File();
            fileMetadata.setName(imageFilePath.getName());
            fileMetadata.setParents(Collections.singletonList(folderId));
            fileMetadata.setMimeType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            FileContent mediaContent = new FileContent("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", imageFilePath);
            File imageFile = mDriveService.files().create(fileMetadata, mediaContent)
                    .setFields("id")
                    .execute();

            // upload text file
            File fileMetadata2 = new File();
            fileMetadata2.setName(txtFilePath.getName());
            fileMetadata2.setParents(Collections.singletonList(folderId));
            fileMetadata2.setMimeType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

            FileContent mediaContent2 = new FileContent("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", txtFilePath);
            File txtFile = mDriveService.files().create(fileMetadata2, mediaContent2)
                    .setFields("id")
                    .execute();


            return false;
        });
    }


}
