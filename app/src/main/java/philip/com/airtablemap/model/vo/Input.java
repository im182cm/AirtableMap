package philip.com.airtablemap.model.vo;

import android.widget.AutoCompleteTextView;
import android.widget.EditText;

/**
 * Class for input in marker window info.
 */
public class Input {
    private EditText fieldName;
    private AutoCompleteTextView fieldValue;

    private String name;
    private String value;

    public Input(EditText fieldName, AutoCompleteTextView fieldValue) {
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public Input(Input input) {
        this.name = input.fieldName.getText().toString();
        this.value = input.fieldValue.getText().toString();
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }
}
