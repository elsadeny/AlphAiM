package com.sadeny.alphaim;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;

public class Users extends AppCompatActivity {

    ListView userList;
    TextView noUsersText;
    ArrayList<String> al = new ArrayList<>();
    int totalUsers=0;
    ProgressDialog pd;
    DataClass dataClass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        userList = (ListView)findViewById(R.id.usersList);
        noUsersText = (TextView)findViewById(R.id.noUsersText);

        dataClass = new DataClass(this);

        boolean ch=checkName();
            FetchUsers();
            if(ch==true)
            {
                FetchUsers();
            }

    }

    public void FetchUsers()
    {
        pd = new ProgressDialog(Users.this);
        pd.setMessage("loading...");
        pd.show();

        String url = "https://alphaim-d0e00.firebaseio.com/users.json";
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                doOnSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Error in Users activity",error.toString());
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(Users.this);
        requestQueue.add(request);

        userList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserDetails.chatWith = al.get(position);
                Intent intent = new Intent(Users.this,MainActivity.class);
                intent.putExtra("CHAT_WITH",al.get(position));
                startActivity(intent);
            }
        });
    }

    public void doOnSuccess(String s)
    {
        try
        {
            JSONObject obj = new JSONObject(s);
            Iterator i =obj.keys();
            String key="";
            while(i.hasNext())
            {
                key = i.next().toString();
                if(!key.equals(UserDetails.username))
                {
                    al.add(key);
                }
                totalUsers++;
            }
        }catch (JSONException e)
        {
            e.printStackTrace();
        }
        if(totalUsers<=1)
        {
            noUsersText.setVisibility(View.VISIBLE);
            userList.setVisibility(View.GONE);
        }
        else
        {
            noUsersText.setVisibility(View.GONE);
            userList.setVisibility(View.VISIBLE);
            userList.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,al));

        }
        pd.dismiss();
    }

    private boolean checkName()
    {
        if(dataClass.getName()==null || dataClass.getPass()==null){ startActivityForResult(new Intent(this, Login.class),5);
        return false;
        }
        else
        {
            UserDetails.username=dataClass.getName();
            UserDetails.password=dataClass.getPass();
            return true;
        }

    }

    @Override
    protected  void onActivityResult(int req,int res,Intent data)
    {
        if(req==5 && res==RESULT_OK)
        {
            UserDetails.username=dataClass.getName();
            UserDetails.password=dataClass.getPass();
            FetchUsers();
        }
        else if(req==5 && res==RESULT_CANCELED){
            Toast.makeText(this,"You have to set name", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater= getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id =item.getItemId();
        if(id==R.id.logout)
        {
            UserDetails.username=null;
            UserDetails.password=null;
            startActivityForResult(new Intent(this,Login.class),5);
        }
        else
        {
            Toast.makeText(this,"This service will be provided soon",Toast.LENGTH_LONG).show();
        }
        return true;
    }
}
