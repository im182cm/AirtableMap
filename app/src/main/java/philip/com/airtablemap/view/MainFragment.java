package philip.com.airtablemap.view;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import philip.com.airtablemap.R;
import philip.com.airtablemap.contract.MainContract;
import philip.com.airtablemap.model.dto.Record;
import philip.com.airtablemap.model.vo.AirtableUserInfo;
import philip.com.airtablemap.util.ConstValue;

public class MainFragment extends Fragment implements MainContract.View {
    private MainContract.Presenter mPresenter;
    private EditText mAPIKeyEd, mBaseIDEd, mTableNameEd, mViewNameEd, mAddressEd;
    private ProgressBar mProgressBar;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main, container, false);

        mAPIKeyEd = (EditText) root.findViewById(R.id.edit_apiKey);
        mBaseIDEd = (EditText) root.findViewById(R.id.edit_baseId);
        mTableNameEd = (EditText) root.findViewById(R.id.edit_table_name);
        mViewNameEd = (EditText) root.findViewById(R.id.edit_view_name);
        mAddressEd = (EditText) root.findViewById(R.id.edit_address);

        mProgressBar = (ProgressBar) root.findViewById(R.id.progressBar);

        Button startBt = (Button) root.findViewById(R.id.bt_start);
        Button sampleBt = (Button) root.findViewById(R.id.bt_sample);

        startBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.retrieveRecords(new AirtableUserInfo(
                        mAPIKeyEd.getText().toString(), mBaseIDEd.getText().toString(), mTableNameEd.getText().toString(), mViewNameEd.getText().toString(), mAddressEd.getText().toString()));
            }
        });

        sampleBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setSampleData();
            }
        });
        return root;
    }

    @Override
    public void setPresenter(MainContract.Presenter presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void onError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        showProgressBar(false);
    }

    @Override
    public void showProgressBar(boolean show) {
        mProgressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public String getStringFromInt(int string) {
        return getString(string);
    }

    @Override
    public void showToast(String message) {
        showProgressBar(false);
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void goToMapActivity(List<Record> records, @NonNull AirtableUserInfo airtableUserInfo) {
        Intent intent = new Intent(getContext(), MapActivity.class);
        if (records != null && records.size() > 0) {
            intent.putExtra(ConstValue.EXTRA_RECORDS, (ArrayList) records);
        }
        intent.putExtra(ConstValue.EXTRA_AIRTABLE_USER_INFORMATION, airtableUserInfo);
        startActivity(intent);
    }

    /**
     * Set sample data to EditTexts.
     */
    private void setSampleData() {
        // This is for sample data.
        mAPIKeyEd.setText("keynvxF7i8NewZ5jI");
        mBaseIDEd.setText("appxNFMYrLJ32SEtH");
        mTableNameEd.setText("Location");
        mViewNameEd.setText("LocationGrid");
        mAddressEd.setText("Location");
    }
}
