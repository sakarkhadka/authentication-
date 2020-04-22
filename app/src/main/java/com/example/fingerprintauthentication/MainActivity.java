package com.example.fingerprintauthentication;

import android.Manifest;
import android.app.KeyguardManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyPermanentlyInvalidatedException;
import android.security.keystore.KeyProperties;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.json.JSONException;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;





/*
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

*/

public class MainActivity extends AppCompatActivity {

    private KeyStore keyStore;
    private static final String Key_Name = "1";
    private Cipher cipher;
    private TextView textView;
    private KeyGenerator keyGenerator;
    private FingerprintManager.CryptoObject cryptoObject;
    private FingerprintManager fingerprintManager;
    private KeyguardManager keyguardManager;


    String login_info = "";
    boolean status = false;
   // public Response response;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        KeyguardManager keyguardManager = (KeyguardManager)getSystemService(KEYGUARD_SERVICE);
        FingerprintManager fingerprintManager = (FingerprintManager)getSystemService(FINGERPRINT_SERVICE);
        textView = (TextView) findViewById(R.id.textView);

        Log.d("lati ---> ", "happyhour");
        //Check whether the device has a fingerprint sensor//
        if (!fingerprintManager.isHardwareDetected()) {
            // If a fingerprint sensor isn’t available, then inform the user that they’ll be unable to use your app’s fingerprint functionality//
            textView.setText("Your device doesn't support fingerprint authentication");
        }
        //Check whether the user has granted your app the USE_FINGERPRINT permission//
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            // If your app doesn't have this permission, then display the following text//
            textView.setText("Please enable the fingerprint permission");
        }

        //Check that the user has registered at least one fingerprint//
        if (!fingerprintManager.hasEnrolledFingerprints()) {
            // If the user hasn’t configured any fingerprints, then display the following message//
            textView.setText("No fingerprint configured. Please register at least one fingerprint in your device's Settings");
        }

        //Check that the lockscreen is secured//
        if (!keyguardManager.isKeyguardSecure()) {
            // If the user hasn’t secured their lockscreen with a PIN password or pattern, then display the following text//
            textView.setText("Please enable lockscreen security in your device's Settings");
        }
        else {
            try {
                generateKey();
            } catch (FingerprintException e) {
                e.printStackTrace();
            }

            if (initCipher()) {
                //If the cipher is initialized successfully, then create a CryptoObject instance//
                cryptoObject = new FingerprintManager.CryptoObject(cipher);

                // Here, I’m referencing the FingerprintHandler class that we’ll create in the next section. This class will be responsible
                // for starting the authentication process (via the startAuth method) and processing the authentication process events//
                FingerprintHandler helper = new FingerprintHandler(this);
                helper.startAuth(fingerprintManager, cryptoObject);
            }

        }

    }

//Create the generateKey method that we’ll use to gain access to the Android keystore and generate the encryption key//

    private void generateKey() throws FingerprintException {
        try {
            // Obtain a reference to the Keystore using the standard Android keystore container identifier (“AndroidKeystore”)//
            keyStore = KeyStore.getInstance("AndroidKeyStore");

            //Generate the key//
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

            //Initialize an empty KeyStore//
            keyStore.load(null);

            //Initialize the KeyGenerator//
            keyGenerator.init(new
                //Specify the operation(s) this key can be used for//
                KeyGenParameterSpec.Builder(Key_Name, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT).setBlockModes(KeyProperties.BLOCK_MODE_CBC)

                //Configure this key so that the user has to confirm their identity with a fingerprint each time they want to use it//
                .setUserAuthenticationRequired(true).setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7).build());

            //Generate the key//
            keyGenerator.generateKey();
        }
        catch (KeyStoreException | NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException | CertificateException | IOException exc) {
            exc.printStackTrace();
            throw new FingerprintException(exc);
        }

    }

    //Create a new method that we’ll use to initialize our cipher//
    public boolean initCipher() {
        try {
            //Obtain a cipher instance and configure it with the properties required for fingerprint authentication//
            cipher = Cipher.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES + "/"
                            + KeyProperties.BLOCK_MODE_CBC + "/"
                            + KeyProperties.ENCRYPTION_PADDING_PKCS7);
        }
        catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException("Failed to get Cipher", e);
        }

        try {
            keyStore.load(null);
            SecretKey key = (SecretKey) keyStore.getKey(Key_Name, null);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            //Return true if the cipher has been initialized successfully//
            return true;
        }
        catch (KeyPermanentlyInvalidatedException e) {
            //Return false if cipher initialization failed//
            return false;
        }
        catch (KeyStoreException | CertificateException | UnrecoverableKeyException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            throw new RuntimeException("Failed to init Cipher", e);
        }
    }

    private class FingerprintException extends Exception {
        public FingerprintException(Exception e) {
            super(e);
        }
    }


    //start here ---g7s
    public void login(View view) throws JSONException {



        EditText uName = (EditText)findViewById(R.id.userName);
        String username = uName.getText().toString();

        EditText pswrd = (EditText)findViewById(R.id.password);
        String password = pswrd.getText().toString();


        Log.d("ERROR", "Username " + "\"" + username + "\"" +" "+ "Password " + "\"" + password + "\"");


        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");

        // Instantiate the RequestQueue.
        //String url ="https://kix7tx694g.execute-api.us-east-1.amazonaws.com/dev/signIn/1001";
        String url ="https://kix7tx694g.execute-api.us-east-1.amazonaws.com/dev/signIn/2494100525";


        //
        //OkHttpClient client = new OkHttpClient();
//
//        RequestBody formBody = new requestBody.create()//FormBody.Builder()
//                .add("{\n\t\"username\":\"Dilan\",\n\t\"password\":\"Dilan1\" \n}",password)
//                .build();
//        Request request = new Request.Builder()
//                .url(url)
//                .post(formBody)
//                .build();
     //public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
        String json = "{\n\t\"username\": " + "\"" + username + "\"" + ",\n\t\"password\":"+ "\"" + password + "\"" + "\n}";

//        String lat = "12.00";
//        String lon = "1.00";
//
//        String json1 = "{\n" + "\"geolocation\": \""  + lat  + "," + lon + "\"\n}";
//
//        Log.d("json1", json1);

        Log.d("json", json.toString());
        RequestBody body = RequestBody.create(mediaType,json);
        Request request = new Request.Builder()
                .url(url)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();

       // try {
             //Response response = client.newCall(request).execute();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()){
                    final String myResponse = response.body().string();
                    try{
                        login_info = myResponse.toString();
                        Log.d("Login status", login_info);
                        //status = true;



                    }catch (Exception e){}



                }
            }


          //  Log.d("response", response.toString());

            // Do something with the response.
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        //


//        okhttp3.Request request = new Request.Builder()
//                .url(url)
//                .build();

//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                e.printStackTrace();
//            }
//
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.isSuccessful()){
//                    final String myResponse = response.body().string();
//                    try{
//                         login_info = myResponse.toString();
//                         Log.d("Login status", login_info);
//                         //status = true;
//                    }catch (Exception e){}
//                }
//            }
//        });

       //while (status == false) {}
       //status = false;




    });

        try {
            Thread.sleep(500);
        }catch(Exception e){

        }

        String stat = "{"+ "\"" + "status"+ "\"" +":" + "\"" + "login successful" +"\""+ "}";

        Toast.makeText(getApplicationContext(), login_info, Toast.LENGTH_SHORT).show();
        if(login_info.equals(stat)) {
            Intent intent = new Intent(this, homepage.class);
            startActivity(intent);
            Intent intentgeo = new Intent(this, MyService.class);
            startService(intentgeo);

        }
}}


