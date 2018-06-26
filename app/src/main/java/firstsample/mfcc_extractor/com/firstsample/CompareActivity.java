package firstsample.mfcc_extractor.com.firstsample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import firstsample.mfcc_extractor.com.firstsample.Comparator.DTW;
import firstsample.mfcc_extractor.com.firstsample.Support.FileHelper;

public class CompareActivity extends AppCompatActivity {
    private TextView mtvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compare);

        mtvResult = findViewById(R.id.result);

        List<double[]> result = FileHelper.ReadFile("data.txt", 39);

        List<double[]> des = FileHelper.ReadFile("mfcc.txt", 39);

        DTW dtw = DTW.getInstance();
        double distance = dtw.process(result, des).getDistance();
        mtvResult.setText(String.valueOf(distance));
    }
}
