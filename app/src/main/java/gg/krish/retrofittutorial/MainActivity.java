package gg.krish.retrofittutorial;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Activity in which the click of the button will result in a call to GitHub's API to get a list of
 * the followers of a user. Once this list has been retrieved, one of the follower's usernames,
 * chosen randomly, will be displayed in the TextView
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Starts Retrofit
        final GitHubApi gitHubApi = GitHubApi.retrofit.create(GitHubApi.class);

        //Sets up Button and TextView for use in this class
        final TextView textView = (TextView) findViewById(R.id.text_view);
        Button requestButton = (Button) findViewById(R.id.button);

        //Behavior once button is clicked
        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Assigns temporary text to the TextView
                textView.setText("Loading random follower...");

                //Sets up up the API call
                Call<List<UserModel>> call = gitHubApi.loadFollowers("krishmasand");

                //Runs the call on a different thread
                call.enqueue(new Callback<List<UserModel>>() {
                    @Override
                    //Once the call has finished
                    public void onResponse(Call<List<UserModel>> call, Response<List<UserModel>> response) {
                        //Gets the list of followers
                        List<UserModel> followers = response.body();

                        //Loads a random follower and assigns it to the TextView
                        int index = (int) (Math.random() * followers.size());
                        String text = "Random follower - " + followers.get(index).getLogin();
                        textView.setText(text);
                    }

                    @Override
                    //If the call failed
                    public void onFailure(Call<List<UserModel>> call, Throwable t) {
                        textView.setText("Request Failed");
                        Log.e("RequestCall", "Request failed");
                    }
                });
            }
        });
    }


}
