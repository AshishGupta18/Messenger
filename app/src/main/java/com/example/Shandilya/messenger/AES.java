package com.example.Shandilya.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.speech.RecognizerIntent;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.util.ArrayList;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

public class AES extends AppCompatActivity {

    EditText inputMessage;
    TextView outputText;
    Button enc, dec,clear,send;

    String outputString="";


    public  static String pwdtext="qwerty";
    String inptext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aes);
        Toast.makeText(AES.this,"AES Algorithm ",Toast.LENGTH_SHORT).show();
        inputMessage = (EditText) findViewById(R.id.inputMessage);
        outputText = (TextView) findViewById(R.id.outputText);
        enc = (Button) findViewById(R.id.encrypt);
        dec = (Button) findViewById(R.id.decrypt);
        send = (Button) findViewById(R.id.send);
        clear = (Button) findViewById(R.id.clear);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        enc.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                try{
                    inptext = inputMessage.getText().toString();
                    outputString = encrypt(inptext,pwdtext);
                    outputText.setText(outputString);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    inptext = inputMessage.getText().toString();
                    outputString = decrypt(inptext,pwdtext);
                    outputText.setText(outputString);
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    inputMessage.setText("");
                    outputText.setText("");
                    inputMessage.setHint("Enter Your Message here");
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(outputText.length()>0){
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT,outputString);
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }
                else{
                    Toast.makeText(AES.this,"phle kuch tho likhe le yrr",Toast.LENGTH_SHORT).show();
//                    System.out.println("hello"); -> Used for Debugging
                }
            }
        });

    }

    private String encrypt(String data,String passwordText) throws Exception{
        SecretKeySpec key = generateKey(passwordText);
        //Log.d("Rupesh", "encrypt key:" + key.toString()); used for Debugging
        Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");//creating object as an AES cipher with ECB model ECB Model  encryption block supports 128 bit only encryption.
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(data.getBytes("UTF-8"));
        String encryptedValue = Base64.encodeToString(encVal,Base64.DEFAULT);
        return encryptedValue;
    }

    private String decrypt(String data,String passwordText) throws Exception{
        SecretKeySpec key = generateKey(passwordText);
        //Log.d("Rupesh", "encrypt key:" + key.toString()); used for Debugging
        Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
        c.init(Cipher.DECRYPT_MODE,key);
        byte[] decodeText = Base64.decode(data,Base64.DEFAULT);
        byte[] decodeVal = c.doFinal(decodeText);
        String decryptedvalue = new String(decodeVal,"UTF-8");
        return decryptedvalue;
    }

    private SecretKeySpec generateKey(String password) throws Exception{
        final  MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes("UTF-8");
        digest.update(bytes, 0,bytes.length);
        byte[] key = digest.digest();
        SecretKeySpec secretKeySpec = new SecretKeySpec(key,"AES");
        return secretKeySpec;
    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        switch (requestCode){
            case 1001:
                if(resultCode==RESULT_OK&&data!=null){
                    ArrayList<String> res = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    inputMessage.setText(res.get(0));
                }
                break;
        }
    }
}