package com.example.tradingjournal;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.TypefaceCompat;

import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.tradingjournal.BaseActivity;
import com.example.tradingjournal.R;

import org.w3c.dom.Text;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class OddEnhancersActivity extends BaseActivity {

    TableLayout tlOddEnhancers;
    TextView tvOddEnhancerScore;
    int childTableRowsCount;
    int oddEnhancersScore;
    int textViewCounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_odd_enhancers);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.setTitle("Odd Enhancers");

        oddEnhancersScore = 0;
        tlOddEnhancers = findViewById(R.id.tlOddEnhancers);
        tvOddEnhancerScore = findViewById(R.id.totalScore);
        tvOddEnhancerScore.setText(String.valueOf(oddEnhancersScore));
        childTableRowsCount = tlOddEnhancers.getChildCount();

        TableRow[] tableRows = new TableRow[childTableRowsCount];
        for (int i = 0; i < tableRows.length; i++) {
            TableRow tr = (TableRow) tlOddEnhancers.getChildAt(i);
            tableRows[i] = tr;
        }

        Map<TextView, Boolean> oddEnhancers = new HashMap<TextView, Boolean>();


        for (int i = 0; i < tableRows.length; i++) {
            textViewCounts = tableRows[i].getChildCount();
            for (int j = 0; j < textViewCounts; j++) {
                TextView et = (TextView) tableRows[i].getChildAt(j);
                oddEnhancers.put(et, false);
            }
        }


        for (int i = 0; i < tableRows.length; i++) {
            TableRow tr = (TableRow) tlOddEnhancers.getChildAt(i);
            tableRows[i] = tr;
        }

        for (Map.Entry<TextView, Boolean> entry : oddEnhancers.entrySet()) {
            TextView textView = entry.getKey();
            String textViewName = this.getResources().getResourceEntryName(textView.getId());
            textView.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onClick(View v) {
                    oddEnhancers.replace(textView, !entry.getValue());
                    boolean isRelevant = textViewName.contains("0") || textViewName.contains("1") || textViewName.contains("2");
                    if (entry.getValue()) {
                        if (isRelevant) {
                            textView.setTypeface(null, Typeface.BOLD);
                            if (textViewName.contains("1")) oddEnhancersScore += 1;
                            if (textViewName.contains("2")) oddEnhancersScore += 2;
                        }
                    } else {
                        if (isRelevant) {
                            textView.setTypeface(null, Typeface.NORMAL);
                            if (textViewName.contains("1")) oddEnhancersScore -= 1;
                            if (textViewName.contains("2")) oddEnhancersScore -= 2;
                        }
                    }
                    tvOddEnhancerScore.setText(String.valueOf(oddEnhancersScore));
                }
            });
        }
    }
}