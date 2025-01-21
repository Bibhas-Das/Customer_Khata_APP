package com.example.ashagrillhouse;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddOrder extends AppCompatActivity {

    private EditText customerNameEditText;
    private EditText customerPhoneEditText;
    private EditText customerAddressEditText;
    private EditText dueAmountEditText;
    private Button submitButton;

    private static final String FILE_NAME = "orders.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_order);

        customerNameEditText = findViewById(R.id.customer_name);
        customerPhoneEditText = findViewById(R.id.customer_phone);
        customerAddressEditText = findViewById(R.id.customer_address);
        dueAmountEditText = findViewById(R.id.due_amount);
        submitButton = findViewById(R.id.submit);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOrder();
            }
        });
    }

    private void saveOrder() {
        String customerName = customerNameEditText.getText().toString().trim();
        String customerPhone = customerPhoneEditText.getText().toString().trim();
        String customerAddress = customerAddressEditText.getText().toString().trim();
        String dueAmount = dueAmountEditText.getText().toString().trim();

        if (customerName.isEmpty() || dueAmount.isEmpty()) {
            Toast.makeText(this, "Please fill in required fields: Name and Due Amount.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            // Read existing data
            JSONArray ordersArray = readOrders();

            // Check if customer already exists
            for (int i = 0; i < ordersArray.length(); i++) {
                JSONObject order = ordersArray.getJSONObject(i);
                if (order.getString("customerName").equalsIgnoreCase(customerName) &&
                        order.getString("customerAddress").equalsIgnoreCase(customerAddress)) {
                    Toast.makeText(this, "Customer already exists.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            // Create new order
            JSONObject newOrder = new JSONObject();
            newOrder.put("customerName", customerName);
            newOrder.put("customerPhone", customerPhone);
            newOrder.put("customerAddress", customerAddress);
            newOrder.put("dueAmount", dueAmount);
            newOrder.put("date", new SimpleDateFormat("dd/MM/yy").format(new Date()));

            // Add to array and save
            ordersArray.put(newOrder);
            saveOrders(ordersArray);

            Toast.makeText(this, "Order saved successfully.", Toast.LENGTH_SHORT).show();
            resetForm();

        } catch (JSONException | IOException e) {
            Toast.makeText(this, "Error saving order: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private JSONArray readOrders() throws IOException, JSONException {
        File file = new File(getFilesDir(), FILE_NAME);
        if (!file.exists()) {
            return new JSONArray();
        }

        FileReader reader = new FileReader(file);
        StringBuilder builder = new StringBuilder();
        int ch;
        while ((ch = reader.read()) != -1) {
            builder.append((char) ch);
        }
        reader.close();

        return new JSONArray(builder.toString());
    }

    private void saveOrders(JSONArray orders) throws IOException {
        File file = new File(getFilesDir(), FILE_NAME);
        FileWriter writer = new FileWriter(file);
        writer.write(orders.toString());
        writer.close();
    }

    private void resetForm() {
        customerNameEditText.setText("");
        customerPhoneEditText.setText("");
        customerAddressEditText.setText("");
        dueAmountEditText.setText("");
    }
}
