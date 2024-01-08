package com.naylalabs.scorely.ui.main.home.moreFragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.example.rushi.R
import com.example.rushi.common.BaseFragment
import com.example.rushi.databinding.FragmentAuthBinding
import com.example.rushi.utils.toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class AuthFragment : BaseFragment() {

    companion object {
        private val RC_SIGN_IN = 9001
    }

    private lateinit var binding: FragmentAuthBinding
    private val viewModel: AuthFragmentViewModel by activityViewModels()
    private lateinit var mFirebaseAuth: FirebaseAuth

    private var mGoogleSignInClient: GoogleSignInClient? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(inflater, container, false)

        mFirebaseAuth = FirebaseAuth.getInstance()
        listeners()
        updateUI()
        return binding.root
    }

    private fun listeners() {
        binding.googleButton.setOnClickListener {
            googleLogin()
        }

        binding.logoutButton.setOnClickListener {
            signOut()
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                val idToken: String? = account.idToken
                if (idToken != null) {
                    context?.toast("Google Login Success${account.displayName}")
                    firebaseAuthWithGoogle(idToken)
                }

            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Timber.e("Google sign in failed $e")
                context?.toast("Google Sign in failed $e")

            }
        }
    }

    private fun googleLogin() {
        //1
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))//you can also use R.string.default_web_client_id
            .requestEmail()
            .build()
        //2
        mGoogleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        //3
        googleSignInIntent()
    }

    private fun googleSignInIntent() {
        val intent = mGoogleSignInClient?.signInIntent
        startActivityForResult(intent, RC_SIGN_IN)

    }

    private fun signOut() {
        // Google sign out
        mFirebaseAuth.signOut()
        mGoogleSignInClient?.signOut()
        updateUI()
    }

    private fun firebaseAuthWithGoogle(token:String) {
        val credential = GoogleAuthProvider.getCredential(token, null)
        mFirebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener(requireActivity()) {
                context?.toast("Firebase auth  success. ")
                updateUI()
            }
            .addOnFailureListener(requireActivity()) { e ->
                context?.toast("Authentication failed. $e")
            }
    }

    private fun updateUI(){
        val user : FirebaseUser? = mFirebaseAuth.currentUser
        if(user != null){
            binding.googleButton.visibility = View.GONE
            binding.logoutButton.visibility = View.VISIBLE
            binding.userNameTv.visibility = View.VISIBLE
            binding.userNameTv.text  = "Welcome, ${user.displayName}"
        }else{
            binding.userNameTv.visibility = View.GONE
            binding.googleButton.visibility = View.VISIBLE
            binding.logoutButton.visibility = View.GONE
    }
    }


}
