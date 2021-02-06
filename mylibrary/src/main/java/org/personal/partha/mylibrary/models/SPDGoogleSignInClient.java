package org.personal.partha.mylibrary.models;

import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.Task;

public class SPDGoogleSignInClient {
    GoogleSignInClient mGoogleSignInClient;
    Intent signInIntent;

    public SPDGoogleSignInClient(GoogleSignInClient mGoogleSignInClient) {
        this.mGoogleSignInClient = mGoogleSignInClient;
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
}
