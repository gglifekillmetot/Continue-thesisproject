package io.classify.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.TextView
import io.classify.MainActivity
import io.classify.R
import io.classify.data.remote.service.UserService
import io.classify.ui.BasicActivity
import io.classify.ui.home.HomeActivity
import retrofit2.Retrofit
import javax.inject.Inject

class LoginActivity : BasicActivity(), LoginView, View.OnClickListener {

    @Inject
    lateinit var retrofit: Retrofit

    override fun onClick(view: View?) {
        presenter?.validateCredentials(username?.text.toString(), password?.text.toString());
    }

    private var message: TextView? = null
    private var progressBar: ProgressBar? = null
    private var username: EditText? = null
    private var password: EditText? = null

    private var presenter: LoginPresenter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        appComponent.inject(this)

        progressBar = findViewById<ProgressBar>(R.id.progress)
        username = findViewById<EditText>(R.id.username)
        password = findViewById<EditText>(R.id.password)
        message = findViewById<TextView>(R.id.message)

        (findViewById<Button>(R.id.button)).setOnClickListener(this)

        val userService: UserService = retrofit.create(UserService::class.java)

        presenter = LoginPresenterImpl(this, LoginInteractImpl(userService))
    }

    override fun showProgress() {
        progressBar?.setVisibility(View.VISIBLE);
        message?.setVisibility(View.GONE);
    }

    override fun hideProgress() {
        progressBar?.setVisibility(View.GONE);
    }

    override fun setUsernameError() {
        username?.setError(getString(R.string.username_error));
    }

    override fun setPasswordError() {
        password?.setError(getString(R.string.password_error));
    }

    override fun setLoginError() {
        message?.setVisibility(View.VISIBLE)
    }

    override fun navigateToHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }
}
