package philip.com.airtablemap.contract;

import java.util.List;

import philip.com.airtablemap.model.vo.AirtableUserInfo;
import philip.com.airtablemap.model.dto.Record;
import philip.com.airtablemap.view.BaseView;

/**
 * Contract in MainActivity both with Presenter and View
 */
public interface MainContract {
    interface View extends BaseView<Presenter> {

        /**
         * Show progress bar or not.
         */
        void showProgressBar(boolean show);

        /**
         * To use getString().
         */
        String getStringFromInt(int string);

        /**
         * Show Toast message.
         */
        void showToast(String message);

        /**
         * Start Map activity.
         */
        void goToMapActivity(List<Record> records, AirtableUserInfo airtableUserInfo);
    }

    interface Presenter {
        /**
         * Retrieve records from Airtable
         */
        void retrieveRecords(AirtableUserInfo airtableUserInfo);
    }
}
