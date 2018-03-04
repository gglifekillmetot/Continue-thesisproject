package io.classify.ui.login

import android.text.TextUtils
import io.classify.data.remote.service.UserService
import io.classify.ui.login.LoginInteract.OnLoginFinishedListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class LoginInteractImpl(val userService: UserService) : LoginInteract {

    override fun login(username: String, password: String, listener: OnLoginFinishedListener) {
        if (TextUtils.isEmpty(username)) {
            listener.onUsernameError()
            return
        }
        if (TextUtils.isEmpty(password)) {
            listener.onPasswordError()
            return
        }
        userService.getByUsername(username)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->
                            if (result?.password?.equals(password)!!) {
                                listener.onSuccess()
                            } else {
                                listener.onFailed()
                            }
                        },
                        {
                            listener.onFailed()
                        })
    }
}