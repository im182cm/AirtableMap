package philip.com.airtablemap.model;

import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

import io.reactivex.SingleObserver;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import philip.com.airtablemap.view.BaseView;

/**
 * CallbackWrapper for SingerObserver which used for Retrofit response.
 */

public abstract class CallbackWrapper<T> implements SingleObserver<T> {

    //BaseView is just a reference of a View in MVP
    private WeakReference<BaseView> weakReference;

    public CallbackWrapper(BaseView view) {
        this.weakReference = new WeakReference<>(view);
    }

    private String getErrorMessage(ResponseBody responseBody) {
        try {
            JSONObject jsonObject = new JSONObject(responseBody.string());
            return jsonObject.getString("message");
        } catch (Exception e) {
            return e.getMessage();
        }
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onError(Throwable e) {
        BaseView view = weakReference.get();
        view.onError(((HttpException) e).response().message());
    }
}
