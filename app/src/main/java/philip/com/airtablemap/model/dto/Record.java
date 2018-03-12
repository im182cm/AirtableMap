package philip.com.airtablemap.model.dto;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Map;

import philip.com.airtablemap.util.GsonUtil;

/**
 * Data retrieved from Airtable
 */
public class Record implements Parcelable {
    private String id;
    private Map<String, String> fields;
    private String createdTime;

    public String getId() {
        return id;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    protected Record(Parcel in) {
        id = in.readString();
        createdTime = in.readString();
        fields = GsonUtil.fromJsonHashMap(in.readString());
    }

    public static final Creator<Record> CREATOR = new Creator<Record>() {
        @Override
        public Record createFromParcel(Parcel in) {
            return new Record(in);
        }

        @Override
        public Record[] newArray(int size) {
            return new Record[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(createdTime);
        dest.writeString(GsonUtil.getInstance().toJson(fields));
    }
}
