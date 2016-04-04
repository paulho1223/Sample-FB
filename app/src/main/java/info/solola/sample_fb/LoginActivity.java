package info.solola.sample_fb;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


public class LoginActivity extends Activity {
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private AccessToken accessToken;
    private TextView fbId, fbName;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login);

        FacebookInitialize();

        // Create global configuration and initialize ImageLoader with this config
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)

                .build();

        ImageLoader.getInstance().init(config);

        fbId = (TextView) findViewById(R.id.fbId);
        fbName = (TextView) findViewById(R.id.fbName);

        imageView = (ImageView) findViewById(R.id.imageView);

    }

    private void FacebookInitialize() {


        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
                accessToken = newToken;
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                Log.e("Profile", "profileTracker");
                displayMessage();
            }
        };

        accessTokenTracker.startTracking();
        profileTracker.startTracking();

        accessToken = AccessToken.getCurrentAccessToken();

        loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.setReadPermissions("public_profile");

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException e) {
            }
        });
    }

    private void displayMessage() {
        if (accessToken != null) {

            Profile profile = Profile.getCurrentProfile();
            if (profile != null) {

                fbId.setText(profile.getId());
                fbName.setText(profile.getName());

                String imageUri = profile.getProfilePictureUri(100, 100).toString();

                ImageLoader imageLoader = ImageLoader.getInstance(); // Get singleton instance

                // Load image, decode it to Bitmap and display Bitmap in ImageView (or any other view
                //  which implements ImageAware interface)
                imageLoader.displayImage(imageUri, imageView);

                // TODO: Implement this method to send any registration to your app's servers.

            }
        } else {

            //Initialize NULL value
            fbId.setText("");
            fbName.setText("");
            imageView.setImageResource(android.R.color.transparent);
        }

    }


    @Override
    public void onStop() {
        super.onStop();
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    public void onResume() {
        super.onResume();

        displayMessage();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


}
