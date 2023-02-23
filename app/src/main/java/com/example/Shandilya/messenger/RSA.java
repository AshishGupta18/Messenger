package com.example.Shandilya.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;

import javax.crypto.Cipher;

public class RSA extends AppCompatActivity {

    String temp;
    TextView Output;
    EditText inputMessage;
    Button enc,dec,send,clear;

    String toSend = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rsa);

        Toast.makeText(RSA.this,"RSA Algorithm",Toast.LENGTH_SHORT).show();
        Output = (TextView) findViewById(R.id.outputText);
        inputMessage = (EditText) findViewById(R.id.inputMessage);
        enc = (Button) findViewById(R.id.encrypt);
        dec = (Button) findViewById(R.id.decrypt);
        send = (Button) findViewById(R.id.send);
        clear = (Button) findViewById(R.id.clear);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        KeyPair kp = getKeyPair();

        PublicKey publicKey = kp.getPublic();

        final byte[] publicKeyBytes = publicKey.getEncoded();
        final String publicKeyBytesBase64 = new String(android.util.Base64.encode(publicKeyBytes, Base64.DEFAULT));

        PrivateKey privateKey = kp.getPrivate();

        final byte[] privateBytes = privateKey.getEncoded();
        final String privateKeyBytesBase64 = new String(Base64.encode(privateBytes, Base64.DEFAULT));

        enc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp=inputMessage.getText().toString();
                String encrypted = encryptRSA(temp, publicKeyBytesBase64);
                //  Log.d("NIKHIL", "encrypt key:" +encrypted);
                Output.setText(encrypted);
                toSend=encrypted;
            }
        });

        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                temp = inputMessage.getText().toString();
                String decrypt = decryptRSA(temp,privateKeyBytesBase64);
                Output.setText(decrypt);
                toSend=decrypt;
            }
        });


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    public static KeyPair getKeyPair() {
        KeyPair kp = null;
        try {
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048);
            kp = kpg.generateKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return kp;
    }


    public static String encryptRSA(String Message, String publicKey) {
        String encryptedBase64 = "";
        try {
            KeyFactory keyFac = KeyFactory.getInstance("RSA");
            KeySpec keySpec = new X509EncodedKeySpec(Base64.decode(publicKey.trim().getBytes(), Base64.DEFAULT));
            Key key = keyFac.generatePublic(keySpec);

            // get an RSA cipher object and print the provider
            final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");
            // encrypt the plain text using the public key
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] encryptedBytes = cipher.doFinal(Message.getBytes("UTF-8"));
            encryptedBase64 = new String(Base64.encode(encryptedBytes, Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return encryptedBase64.replaceAll("(\\r|\\n)", "");//new line or carriage return replace kar and send kr
    }

    public static String decryptRSA(String encryptedData,String privateKey){
        String decryptedData = "";
        try{
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            KeySpec keySpec = new PKCS8EncodedKeySpec(Base64.decode(privateKey.trim().getBytes(),Base64.DEFAULT));
            Key key = keyFactory.generatePrivate(keySpec);

            final Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING");

            byte[] encryptedBytes = Base64.decode(encryptedData,Base64.DEFAULT);
            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);
            decryptedData = new String(decryptedBytes);
        } catch (Exception e){
            e.printStackTrace();
        }
        return decryptedData;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);

        switch(requestCode){
            case 1001:
                if(resultCode==RESULT_OK && data!=null){
                    ArrayList<String> res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    inputMessage.setText(res.get(0));
                }
                break;
        }
    }
}