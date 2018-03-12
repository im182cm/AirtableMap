package philip.com.airtablemap.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import philip.com.airtablemap.R;
import philip.com.airtablemap.contract.MainContract;
import philip.com.airtablemap.model.CallbackWrapper;
import philip.com.airtablemap.model.Repository;
import philip.com.airtablemap.model.RetrofitManager;
import philip.com.airtablemap.model.dto.AirtableResponse;
import philip.com.airtablemap.model.vo.AirtableUserInfo;

/**
 * Persenter for MainFragment.
 */

public class MainPresenter implements MainContract.Presenter {
    private static final String LOG_TAG = MainPresenter.class.getSimpleName();
    private final MainContract.View mView;
    private final Repository mRepository;

    public MainPresenter(MainContract.View view, Repository repository) {
        this.mView = view;
        this.mRepository = repository;

        mView.setPresenter(this);
    }

    @Override
    public void retrieveRecords(@NonNull final AirtableUserInfo aui) {
        Log.d(LOG_TAG, "retrieveRecords()");
        // All fields should be filled.
        if (aui.isNotAllEmpty()) {
            mView.showProgressBar(true);
            mRepository.start(RetrofitManager.getAirtable().retrieveRecords(aui.getBaseId(), aui.getTableName(), aui.getApiKey(), aui.getViewName()), new CallbackWrapper<AirtableResponse>(mView) {
                @Override
                public void onSuccess(AirtableResponse response) {
                    mView.showProgressBar(false);
                    mView.goToMapActivity(response.getRecords(), aui);
                }
            });
        } else {
            mView.showToast(mView.getStringFromInt(R.string.toast_fill_all_values));
        }

    }
}
