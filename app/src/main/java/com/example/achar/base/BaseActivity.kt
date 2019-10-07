package com.example.achar.base

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.example.achar.di.IRxSchedulers
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import okhttp3.ResponseBody
import org.kodein.di.Copy
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.android.retainedKodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Response
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper
import java.net.UnknownHostException


abstract class BaseActivity(open val layout: Int) : AppCompatActivity(), KodeinAware {

    private val _parentKodein by kodein()
    private var compositeDisposable: CompositeDisposable? = null
    private val schedulers: IRxSchedulers by instance()

    protected val loading: MutableLiveData<Boolean> = MutableLiveData()

    override val kodein: Kodein by retainedKodein {
        extend(_parentKodein, copy = Copy.All)
        bind<AppCompatActivity>("ActivityContext") with singleton { this@BaseActivity }
    }

    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout)
        viewIsReady(savedInstanceState)
    }

    abstract fun viewIsReady(savedInstanceState: Bundle?)


    override fun finish() {
        super.finish()
        compositeDisposable?.let {
            it.dispose()
            it.clear()
            compositeDisposable = null
        }
    }

    private fun addDisposable(disposable: Disposable) {
        if (compositeDisposable == null) {
            compositeDisposable = CompositeDisposable()
        }
        compositeDisposable?.add(disposable)
    }

    protected fun <T> callService(
        singleApi: Single<Response<T>>,
        onSuccess: (T?) -> Unit,
        onFinish: (() -> Unit)? = null,
        onError: ((ResponseBody) -> Unit)? = null
    ) {
        loading.postValue(true)
        addDisposable(
            singleApi
                .subscribeOn(schedulers.io())
                .observeOn(schedulers.main())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally {
                    loading.postValue(false)
                    onFinish?.let { onFinish() }
                }
                .subscribe(
                    {

                        if (it.isSuccessful) {
                            onSuccess(it.body())
                        } else {
                            it.errorBody()?.let { error ->
                                onError?.invoke(error)
                            }
                        }

                    },
                    { exception ->

                        // handling exception
                        when (exception) {
                            // when trigger internet not respond
                            is UnknownHostException -> offlineDialog(
                                singleApi,
                                onSuccess,
                                onFinish,
                                onError
                            )
                            // when trigger else
                        }
                    })
        )
    }

    private fun <T> offlineDialog(
        singleApi: Single<Response<T>>,
        onSuccess: (T?) -> Unit,
        onFinish: (() -> Unit)? = null,
        onError: ((ResponseBody) -> Unit)? = null
    ) {
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false).setMessage("Check you internet")
            .setNegativeButton("retry") { _, _ ->
                callService(
                    singleApi,
                    onSuccess,
                    onFinish,
                    onError
                )
            }
    }

}