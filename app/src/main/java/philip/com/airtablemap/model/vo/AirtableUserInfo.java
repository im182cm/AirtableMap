package philip.com.airtablemap.model.vo;

import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

/**
 * Class for base data for loading Airtable API which received from MainFragment
 */
public class AirtableUserInfo implements Parcelable {
    private String apiKey;
    private String baseId;
    private String tableName;
    private String viewName;
    private String addressField;

    public AirtableUserInfo(String apiKey, String baseId, String tableName, String viewName, String addressField) {
        this.apiKey = apiKey;
        this.baseId = baseId;
        this.tableName = tableName;
        this.viewName = viewName;
        this.addressField = addressField;
    }

    protected AirtableUserInfo(Parcel in) {
        apiKey = in.readString();
        baseId = in.readString();
        tableName = in.readString();
        viewName = in.readString();
        addressField = in.readString();
    }

    public static final Creator<AirtableUserInfo> CREATOR = new Creator<AirtableUserInfo>() {
        @Override
        public AirtableUserInfo createFromParcel(Parcel in) {
            return new AirtableUserInfo(in);
        }

        @Override
        public AirtableUserInfo[] newArray(int size) {
            return new AirtableUserInfo[size];
        }
    };

    public String getApiKey() {
        return apiKey;
    }

    public String getBaseId() {
        return baseId;
    }

    public String getTableName() {
        return tableName;
    }

    public String getViewName() {
        return viewName;
    }

    public String getAddressField() {
        return addressField;
    }

    public boolean isNotAllEmpty() {
        if (!TextUtils.isEmpty(apiKey) && !TextUtils.isEmpty(baseId) && !TextUtils.isEmpty(tableName) && !TextUtils.isEmpty(viewName) && !TextUtils.isEmpty(addressField)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(apiKey);
        dest.writeString(baseId);
        dest.writeString(tableName);
        dest.writeString(viewName);
        dest.writeString(addressField);
    }
}
