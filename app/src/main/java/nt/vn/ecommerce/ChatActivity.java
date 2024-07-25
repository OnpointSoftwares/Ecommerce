package nt.vn.ecommerce;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ChatActivity extends AppCompatActivity {

    private TextView chatbotMessage1, chatbotMessage2;
    private Button theatreButton, movielistButton;
    private ImageButton sendButton;
    private EditText chatInput;
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_activity);

        chatbotMessage1 = findViewById(R.id.chatbotMessage1);
        chatbotMessage2 = findViewById(R.id.chatbotMessage2);
        sendButton = findViewById(R.id.sendButton);
        chatInput = findViewById(R.id.chatInput);

        client = new OkHttpClient();


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userMessage = chatInput.getText().toString();
                chatbotMessage1.setText(userMessage);
                try {
                    sendMessage(userMessage);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                Toast.makeText(ChatActivity.this,userMessage,Toast.LENGTH_LONG).show();
            }
        });
    }


    private void sendMessage(String message) throws JSONException {
        // The URL for your server endpoint
        String url = "https://eminently-rare-pegasus.ngrok-free.app/popup-chatbot/"; // Replace with your actual endpoint

        // Create a JSON media type
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject jsonMessage = new JSONObject();
        jsonMessage.put("text",message);
        // Create the request body with the provided message
        RequestBody body = RequestBody.create(jsonMessage.toString(),JSON);

        // Build the request
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        // Execute the request asynchronously
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Log the error in case of request failure
                e.printStackTrace();
                // Optionally, notify the user of the failure on the UI thread
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(ChatActivity.this, "Failed to send message", Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Check if the response is successful
                if (response.isSuccessful()) {
                    // Read the response data
                    final String responseData = response.body().string();

                    // Update the UI on the main thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Clear the input field
                            chatInput.setText("");
                            // Set the chatbot's message to the response data
                            chatbotMessage2.setText(responseData);
                            // Display a toast with the response data
                            Toast.makeText(ChatActivity.this, responseData, Toast.LENGTH_LONG).show();
                        }
                    });
                } else {
                    // Handle unsuccessful response
                    final String errorMessage = "Error: " + response.message();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Show an error message to the user
                            Toast.makeText(ChatActivity.this, errorMessage, Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
}
