package philip.com.airtablemap.model.dto;

import java.util.List;
import java.util.Map;

/**
 * Airtable base response for all API
 */
public class AirtableResponse {
    private List<Record> records;
    private String id;
    private Map<String, String> fields;
    private String createdTime;
    private boolean deleted;

    public List<Record> getRecords() {
        return records;
    }

    public String getId() {
        return id;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public boolean isDeleted() {
        return deleted;
    }
}
