package com.example.Shandilya.messenger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(MainActivity.this, "Make your Data Safe",Toast.LENGTH_SHORT).show();
    }

    public void aes(View view)
    {
        Intent intentAes = new Intent( MainActivity.this, AES.class  );
        startActivity(intentAes);

    }

    public void rsa(View view)
    {
        Intent intentRSA = new Intent(MainActivity.this, RSA.class );
        startActivity(intentRSA);
    }
}