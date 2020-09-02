package ics.softwares.encryptdecryptapplication;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class MainActivity extends AppCompatActivity {
    EditText original_message, encrypted_message, decrypted_message;
    Button bEncrypt, bDecrypt;

    KeyPairGenerator kpg;
    KeyPair kp;
    static PublicKey publicKey;
    static PrivateKey privateKey;
    byte [] encryptedBytes,decryptedBytes;
    Cipher cipher,cipher1;
    String encrypted,decrypted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        original_message=(EditText)findViewById(R.id.orig_message);
        encrypted_message=(EditText)findViewById(R.id.encr_message);
        decrypted_message=(EditText)findViewById(R.id.decry_message);

        bEncrypt=(Button)findViewById(R.id.encrypt);
        bEncrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String m=original_message.getText().toString();
                try {encrypted_message.setText(Encrypt(m));}
                catch(Exception e){}
            }
        });

        bDecrypt=(Button)findViewById(R.id.decrypt);
        bDecrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String m=encrypted_message.getText().toString();
                try {decrypted_message.setText(Decrypt(m));}
                catch(Exception e){}
            }
        });
    }


    public String Encrypt (String plain) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        kpg = KeyPairGenerator.getInstance("RSA");
        kpg.initialize(1024);
        kp = kpg.genKeyPair();
        publicKey = kp.getPublic();
        privateKey = kp.getPrivate();
        cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        encryptedBytes = cipher.doFinal(plain.getBytes());
        encrypted = bytesToString(encryptedBytes);
        return encrypted;

    }

    public String Decrypt (String result) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException
    {
        cipher1=Cipher.getInstance("RSA");
        cipher1.init(Cipher.DECRYPT_MODE, privateKey);
        decryptedBytes = cipher1.doFinal(stringToBytes(result));
        decrypted = new String(decryptedBytes);
        return decrypted;
    }

    public  String bytesToString(byte[] b) {
        byte[] b2 = new byte[b.length + 1];
        b2[0] = 1;
        System.arraycopy(b, 0, b2, 1, b.length);
        return new BigInteger(b2).toString(36);
    }

    public  byte[] stringToBytes(String s) {
        byte[] b2 = new BigInteger(s, 36).toByteArray();
        return Arrays.copyOfRange(b2, 1, b2.length);
    }
}
