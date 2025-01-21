/*
    Programmer : Bibhas Das
    Date : 22 july 2024
    Page : Login page
 */

package com.example.ashagrillhouse;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import java.security.MessageDigest;
import java.util.List;


public class LoginPage extends AppCompatActivity {

    Button login_button;
    EditText login_password;
    TextView login_status;

    int NO_ATTEMPT_LOGIN=6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login_page);


        //Database connection and set default password
        try {
            MyDBHelper mdb = new MyDBHelper(this);

            // Database connection and set default password
            List<String[]> dataList = mdb.selectTable("SELECT * from password where user='Biggyan'");
            if (dataList.isEmpty())
            {
                mdb.runQuery("INSERT INTO password (user, password, attempt) VALUES ('Biggyan', '"+SHA256("9932")+"', 6)");
                //Toast.makeText(this, "Password is set in password table", Toast.LENGTH_LONG).show();
            }
            else
            {
                String[] firstRow = dataList.get(0); // Get the first row
                if (firstRow.length > 1)
                {
                    NO_ATTEMPT_LOGIN = Integer.parseInt(firstRow[3]);
                  //  Toast.makeText(this, "Get Attempt status from database", Toast.LENGTH_LONG).show();
                }
            }
            // Close the database after you're done with it
            mdb.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }



        login_button = findViewById(R.id.login_button);
        login_password = findViewById(R.id.login_password);
        login_status = findViewById(R.id.login_status);



        if(NO_ATTEMPT_LOGIN<=0)
            Toast.makeText(this,"You can't Login. Because you have exceed you all attempts",Toast.LENGTH_LONG).show();

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if(NO_ATTEMPT_LOGIN > 0)
                {
                    String password = login_password.getText().toString();
                    if(password.length()<4)
                    {
                        Toast.makeText(LoginPage.this, "Password length should be grater than or equal to 4", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (checkPassword(password))
                    {

                        NO_ATTEMPT_LOGIN=6;
                        Intent inner = new Intent(LoginPage.this, MainActivity.class);
                        login_status.setTextColor(ContextCompat.getColor(LoginPage.this, R.color.white));
                        startActivity(inner);
                        finish();
                    }
                    else
                    {
                        NO_ATTEMPT_LOGIN--;
                        login_status.setText("Wrong Password! " + NO_ATTEMPT_LOGIN + " attempt(s) left");
                        login_password.setText("");
                        login_status.setTextColor(ContextCompat.getColor(LoginPage.this, R.color.red));
                        Toast.makeText(LoginPage.this,"Wrong Password",Toast.LENGTH_SHORT).show();
                    }
                }
                if(NO_ATTEMPT_LOGIN == 0)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginPage.this);
                    builder.setTitle("Login attempt exceed");
                    builder.setMessage(R.string.login_attempt_exceed);
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do something when the positive button is clicked
                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Do something when the negative button is clicked
                        }
                    });
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }

                //Update Login attempt count on database
                try {
                    MyDBHelper mdb = new MyDBHelper(LoginPage.this);
                    mdb.runQuery("UPDATE password SET attempt = "+NO_ATTEMPT_LOGIN+" WHERE user = 'Biggyan'");
                }
                catch(Exception e)
                {
                    Toast.makeText(LoginPage.this,"Login attempt update failed",Toast.LENGTH_LONG).show();
                }
            }
        });


        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }


    //Use for check pasword is correct or not?
    public boolean checkPassword(String passwd)
    {
        boolean status = false;
        try {
            MyDBHelper mdb = new MyDBHelper(LoginPage.this);
            List<String[]> dataList = mdb.selectTable("SELECT * from password where user='Biggyan'");
            if (!dataList.isEmpty())
            {
                String[] firstRow = dataList.get(0); // Get the first row
                if (firstRow.length > 1) {
                    String pass = firstRow[2];
                    //Toast.makeText(LoginPage.this,"Password hash : "+pass,Toast.LENGTH_LONG).show();
                    if(SHA256(passwd).equals(pass))
                        status=true;
                }
            }
        }
        catch(Exception e)
        {
            Toast.makeText(LoginPage.this,"Can't fetch password from database",Toast.LENGTH_LONG).show();
        }

        return status;
    }



    //Hashing convertion
    public static String SHA256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) hexString.append(String.format("%02x", b));
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}