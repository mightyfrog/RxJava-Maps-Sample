package org.mightyfrog.android.rxjavamaps

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import io.reactivex.Observable
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * @author Shigehiro Soejima
 */
class MainActivity : AppCompatActivity() {

    private val sample = listOf("a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m")

    private val observer = object : SingleObserver<List<String>> {
        override fun onSuccess(t: List<String>) {
            textView.text = t.toString()
        }

        override fun onSubscribe(d: Disposable) {
            // add to disposables
        }

        override fun onError(e: Throwable) {
            android.util.Log.e("RxJava Maps Sample", e.message)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.action_map -> map()
            R.id.action_flatmap -> flatMap()
            R.id.action_concatmap -> concatMap()
            R.id.action_switchmap -> switchMap()
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun map(): Boolean {
        Observable.fromIterable(sample)
                .map { it.toUpperCase() }
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)

        return true
    }

    /**
     * http://reactivex.io/documentation/operators/flatmap.html
     */
    private fun flatMap(): Boolean {
        Observable.fromIterable(sample)
                .flatMap { s -> toUpperCaseWithRandomDelay(s) }
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)

        return true
    }

    /**
     * http://reactivex.io/documentation/operators/concat.html
     */
    private fun concatMap(): Boolean {
        Observable.fromIterable(sample)
                .concatMap { s -> toUpperCaseWithRandomDelay(s) }
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)

        return true
    }

    /**
     * http://reactivex.io/documentation/operators/switch.html
     */
    private fun switchMap(): Boolean {
        Observable.fromIterable(sample)
                .switchMap { s -> toUpperCaseWithRandomDelay(s) }
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)

        return true
    }

    private val rnd = Random()

    private fun toUpperCaseWithRandomDelay(s: String): Observable<String> {
        return Observable.just(s.toUpperCase())
                .delay(rnd.nextInt(25).toLong(), TimeUnit.MILLISECONDS, Schedulers.computation())
    }
}
