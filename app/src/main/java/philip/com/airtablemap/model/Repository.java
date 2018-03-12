package philip.com.airtablemap.model;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Repository for Database and Network. However I just used this for only network.
 */

public class Repository {
    private static final String LOG_TAG = Repository.class.getSimpleName();
    private static Repository INSTANCE = null;

    public Repository() {
    }

    public static Repository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Repository();
        }

        return INSTANCE;
    }

    public <T> void start(Single<T> single, SingleObserver singleObserver) {
        single.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(singleObserver);
    }
}
