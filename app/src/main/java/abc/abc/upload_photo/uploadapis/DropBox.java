package abc.abc.upload_photo.uploadapis;


import android.content.Context;
import android.util.Log;

import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class DropBox {

    public static void uploadImage(String userUid, String fileUid, Context appContext) {

        new Thread(new Runnable() {
            @Override
            public void run() {
                String responseData="ddd";
                try {
                    String ACCESS_TOKEN = "";
                    OkHttpClient httpClient = new OkHttpClient();
                    RequestBody formBody = new FormBody.Builder()
                            .add("is_xhr", "true")
                            .add("t", "XZVOn3nQOqNMugsgLwpwPGOf")
                            .add("app_id", "2615891")
                            .build();
                    Request request = new Request.Builder()
                            .url("https://www.dropbox.com/developers/apps/generate_access_token")
                            .post(formBody)
                            .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:125.0) Gecko/20100101 Firefox/125.0")
                            .header("Accept", "*/*")
                            .header("Accept-Language", "de,en-US;q=0.7,en;q=0.3")
                           // .header("Accept-Encoding", "gzip, deflate, br")
                            .header("Referer", "https://www.dropbox.com/developers/apps/info/z2d57thxw35wfqf")
                            .header("content-type", "application/x-www-form-urlencoded; charset=UTF-8")
                            .header("x-dropbox-client-yaps-attribution", "atlasservlet.api_developers-live:control-pdx")
                            .header("Origin", "https://www.dropbox.com")
                            .header("Connection", "keep-alive")
                            .header("Cookie", "gvc=MTUxMjY3Njc4MzI2NDAxMTA2MzQxNDUwNjA5MjMwOTY2MjE5NTg4; t=XZVOn3nQOqNMugsgLwpwPGOf; __Host-js_csrf=XZVOn3nQOqNMugsgLwpwPGOf; __Host-ss=sLqq-xnMKQ; locale=de; billing_cycle=yearly; __Secure-dbx_consent={\"consentType\":1,\"consentDate\":\"2024-04-24T18:19:18.198Z\",\"expireDate\":\"2024-10-24T18:19:18.198Z\",\"consentMonths\":6,\"categories\":{\"strictly necessary\":true,\"general marketing and advertising\":true,\"analytics\":true,\"performance and functionality\":true,\"social media advertising\":true},\"userInteracted\":true,\"numDots\":1}; utag_main=v_id:018f11559c4b0002cd75fd93677905050003200d00978$_sn:5$_se:27$_ss:0$_st:1714238420124$vapi_domain:dropbox.com$ses_id:1714236596269%3Bexp-session$_pn:3%3Bexp-session; AMCV_B2AAF3C959275C660A495E7B%40AdobeOrg=-1124106680%7CMCIDTS%7C19841%7CMCMID%7C05720627231707615074335246423141038802%7CMCAAMLH-1714841397%7C6%7CMCAAMB-1714841397%7C6G1ynYcLPuiQxYZrsz_pkqfLG9yMXBpb2zX5dvJdYQJzPXImdj0y%7CMCOPTOUT-1714243797s%7CNONE%7CMCAID%7CNONE%7CvVersion%7C5.2.0%7CMCSYNCSOP%7C411-19848; _gcl_au=1.1.154382946.1713982775; _ga=GA1.2.1595370367.1713982780; _fbp=fb.1.1713982780215.45718506; _iidt=0PBE4mN+s7kRZD7LsehMNfygjbAVDxVmHBc2p7TrPmfyi5dRwDKQiISWT10/pTzGewAB70ESlf9wENSYgEcVeqn/B+oUX0g6aoVA1DI=; g_state={\"i_l\":0}; _vid_t=uWa4VYRBpIjrPODNFj2g9Dntexyu4lb09M04JdXQl17Tp0z4F/7zvdP6qbSoKpdljig4HBH12RkQjfmZqcp62fLTLhhS9CttBkkdFVg=; jar=W3sidWlkIjogMTIzNDc0OTYxOSwgIm5zIjogMjU4OTQwMDk3OSwgInJlbWVtYmVyIjogdHJ1ZSwgImV4cGlyZXMiOiAxODA4NTkwOTg3LCAiaCI6ICIifV0%3D; lid=AAAzGBbDVbe8fCgsBUm2DyrJ8vJG79S6lGkvkG1TSrXhtQ; bjar=W3sidWlkIjogMTIzNDc0OTYxOSwgInNlc3NfaWQiOiAyMTAxMTA4MDkzNDYxMDI5MTQ1NDg0MDg4MTQyNjU0OTIyNDk0ODQsICJyb2xlIjogInBlcnNvbmFsIiwgInRlYW1faWQiOiAiIiwgImV4cGlyZXMiOiAxODA4NTkwOTg3LCAidXNlcl9naWQiOiAiQUJlekdNUU9XUEpvbWJFNzNmdFJHZ2pxIn1d; blid=AADnZ2zDPyfsJ0EEhGylvoc-o60V4hyXO_pfkmylRbigKg; db-help-center-uid=ZXlKMllXeDFaU0k2SUhzaWRXbGtJam9nTVRJek5EYzBPVFl4T1gwc0lDSnphV2R1WVhSMWNtVWlPaUFpUVVGRFNrVmhNek5wTm5ScExUZFlURzV4V1Y5UWVEQTRMVUp4TUZkTVpVeHROVGw0UWt4cmQwUXdZbkI0UVNKOQ%3D%3D; last_active_role=personal; last_active_linked_user_id=412272cd; _gid=GA1.2.239928332.1714232070; AMCVS_B2AAF3C959275C660A495E7B%40AdobeOrg=1")
                            .header("Sec-Fetch-Dest", "empty")
                            .header("Sec-Fetch-Mode", "cors")
                            .header("Sec-Fetch-Site", "same-origin")
                            .header("TE", "trailers")
                            .build();

                    Response response = httpClient.newCall(request).execute();
                     responseData = response.body().string();
                    JSONObject jsonObject = new JSONObject(responseData);
                    ACCESS_TOKEN = jsonObject.getString("token");
                    DbxRequestConfig config = DbxRequestConfig.newBuilder("dropbox/informatik_projekt").build();
                    DbxClientV2 client = new DbxClientV2(config, ACCESS_TOKEN);

                    // Upload image to Dropbox
                    File root = appContext.getDir("imageDir", appContext.MODE_PRIVATE);
                    File file = new File(root.toString(), fileUid + ".jpg");
                    InputStream in = new FileInputStream(file.getAbsolutePath());
                    FileMetadata metadata = client.files().uploadBuilder("/images/" + userUid
                            + "/" + fileUid + "/" + file.getName()).uploadAndFinish(in);

                    // Upload text file to Dropbox
                    File root2 = appContext.getDir("dataFileDir", appContext.MODE_PRIVATE);
                    File file2 = new File(root2.toString(), fileUid + ".txt");
                    InputStream in2 = new FileInputStream(file2.getAbsolutePath());
                    FileMetadata metadata2 = client.files().uploadBuilder("/images/" + userUid
                            + "/" + fileUid + "/" + file2.getName()).uploadAndFinish(in2);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }


}
