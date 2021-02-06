package org.personal.partha.mylibrary.models;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;

public class SPDGoogleSignInClient {
    GoogleSignInClient mGoogleSignInClient;
    Intent signInIntent;

    private GoogleSignInOptions getGoogleSignInOptions() {
        return new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
    }

    public SPDGoogleSignInClient(Context mContext) {
        this.mGoogleSignInClient = GoogleSignIn.getClient(mContext, getGoogleSignInOptions());;
        this.signInIntent = mGoogleSignInClient.getSignInIntent();
    }

    public Intent getSignInIntent() {
        return signInIntent;
    }

    public void setSignInIntent(Intent signInIntent) {
        this.signInIntent = signInIntent;
    }

    public Task<Void> revokeAccess() {
        return mGoogleSignInClient.revokeAccess();
    }

    public Task<Void> signOut() {
        return mGoogleSignInClient.signOut();
    }

    public Task<GoogleSignInAccount> silentSignIn() {
        return mGoogleSignInClient.silentSignIn();
    }

    public GoogleSignInClient getGoogleSignInClient() {
        return mGoogleSignInClient;
    }

    public void setGoogleSignInClient(GoogleSignInClient mGoogleSignInClient) {
        this.mGoogleSignInClient = mGoogleSignInClient;
    }
}
