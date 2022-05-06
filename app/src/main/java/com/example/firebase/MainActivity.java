package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
TextView txtwelcome;
EditText txtchangename;
Button btnchangename;
    Button btnLogOut;
    FirebaseAuth mAuth;
FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnchangename=findViewById(R.id.btnchangename);
        txtchangename=findViewById(R.id.txtchangename);
        txtwelcome=findViewById(R.id.txtwelcome);
        btnLogOut = findViewById(R.id.btnLogout);
        mAuth = FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        btnchangename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        btnLogOut.setOnClickListener(view ->{
            mAuth.signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        });
btnchangename.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        String changename=txtchangename.getText().toString();
        if(TextUtils.isEmpty(changename))
        {
            txtchangename.setError("Name cannot be empty");
            txtchangename.requestFocus();
        }
        else
        {
            DocumentReference doc=db.collection("user").document(mAuth.getCurrentUser().getUid());
            doc.update("name",changename).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(MainActivity.this, "Change name successfully", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this,"Error"+e.toString(),Toast.LENGTH_SHORT).show();
                }
            });
            doc.get().addOnSuccessListener(MainActivity.this, new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    txtwelcome.setText("Hello, "+documentSnapshot.getString("name"));
                }
            });
        }
    }
});
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null){
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
        else
        {
            DocumentReference doc=db.collection("user").document(mAuth.getCurrentUser().getUid());
            doc.get().addOnSuccessListener(MainActivity.this, new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                  txtwelcome.setText("Hello, "+documentSnapshot.getString("name"));
                }
            });

        }
    }
}