package com.sadeny.alphaim;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.json.JSONException;
import org.w3c.dom.Text;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;

public class Register extends AppCompatActivity {

    //global variables
    EditText username,password;
    Button registerButton;
    String user,pass;
    DataClass dataClass;
    TextView login;
    final String url="https://alphaim-d0e00.firebaseio.com/users.json";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        registerButton =(Button)findViewById(R.id.registerButton);
        login = (TextView)findViewById(R.id.login);

        Firebase.setAndroidContext(this);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this,Login.class));
                finish();
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = username.getText().toString();
                pass=password.getText().toString();
                if(user.equals(" "))
                {
                    username.setError("can't be blank");
                }else if(pass.equals(" "))
                {
                    password.setError("can't be blank");
                }else if(!user.matches("[A-Za-z0-9]+"))
                {
                    username.setError("only alphabet or number allowed");
                }else if(user.length()<5)
                {
                    username.setError("at least 5 charchaters long");
                }else if(pass.length()<6)
                {
                    password.setError("at least 6 characters long");
                }else
                {
                    final ProgressDialog pd =new ProgressDialog(Register.this);
                    pd.setMessage("loading...");
                    pd.show();



                    StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String s) {
                            Firebase reference = new Firebase(url);
                            if (s.equals("null")) {
                                reference.child(user).child("password").setValue(pass);
                                Toast.makeText(Register.this, "registration Successful", Toast.LENGTH_LONG).show();
                                UserDetails.username=user;
                                UserDetails.password=pass;
                                dataClass.setName(user);
                                dataClass.setPass(pass);
                                startActivity(new Intent(Register.this,Users.class));
                                finish();

                            } else {
                                try {
                                    JSONObject obj = new JSONObject(s);
                                    if (!obj.has(user)) {
                                        reference.child(user).child("password").setValue(pass);
                                        Toast.makeText(Register.this, "registration Successful", Toast.LENGTH_LONG).show();
                                        UserDetails.username = user;
                                        UserDetails.password = pass;
                                        dataClass.setName(user);
                                        dataClass.setPass(pass);
                                        startActivity(new Intent(Register.this,Users.class));
                                        finish();
                                    } else {
                                        Toast.makeText(Register.this, "Username already exists", Toast.LENGTH_LONG).show();

                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                            pd.dismiss();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.d("Error on volley: ",error.toString());
                            pd.dismiss();
                        }
                    });
                    RequestQueue rQueue = Volley.newRequestQueue(Register.this);
                    rQueue.add(request);
                }
            }
        });
    }
}
