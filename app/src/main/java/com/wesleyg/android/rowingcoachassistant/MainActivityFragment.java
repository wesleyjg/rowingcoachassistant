package com.wesleyg.android.rowingcoachassistant;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    boolean spmTimerActive = false;
    long timeOfLastStroke;
    boolean ratioTimerActive = false;
    long catchTime;
    long finishTime;
    long driveDuration;
    long recoveryDuration;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        final TextView spmText = (TextView) rootView.findViewById(R.id.SpmText);

        final Button strokeButton = (Button) rootView.findViewById(R.id.strokeButton);
        strokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long timeOfThisStroke = System.currentTimeMillis();

                if (spmTimerActive) {
                    long timeDiff = timeOfThisStroke - timeOfLastStroke;
                    double strokesPerMin = (double) (60.0/(timeDiff/1000.0));
                    spmText.setText(String.format( "%.2f spm", strokesPerMin));
                    //android.util.Log.i("This Stroke", String.valueOf(timeOfThisStroke));
                    //android.util.Log.i("Last Stroke", String.valueOf(timeOfLastStroke));
                    //android.util.Log.i("Difference", String.valueOf(timeDiff));
                    //android.util.Log.i("SPM", String.valueOf(strokesPerMin));

                } else {
                    spmTimerActive = true;
                    spmText.setText("-- spm");
                }
                timeOfLastStroke = timeOfThisStroke;
            }
        });

        final TextView ratioText = (TextView) rootView.findViewById(R.id.RatioText);

        final Button ratioButton = (Button) rootView.findViewById(R.id.ratioButton);
        ratioButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        //android.util.Log.i("Ratio Button", "Pressed");

                        long thisCatchTime = System.currentTimeMillis();
                        if (ratioTimerActive)
                        {
                            driveDuration = finishTime - catchTime;
                            recoveryDuration = thisCatchTime - finishTime;
                            double ratio = (double)recoveryDuration/(double)driveDuration;
                            //android.util.Log.i("Ratio", String.format("%.2f",ratio));
                            ratioText.setText(String.format("%.2f : 1",ratio));

                            long timeDiff = thisCatchTime - catchTime;
                            double strokesPerMin = (double) (60.0/(timeDiff/1000.0));
                            spmText.setText(String.format( "%.2f spm", strokesPerMin));
                        } else {
                            ratioTimerActive = true;
                            ratioText.setText("-- : 1");
                            spmText.setText("-- spm");
                        }

                        catchTime = thisCatchTime;
                        break;
                    case MotionEvent.ACTION_UP:
                        //android.util.Log.i("Ratio Button", "Released");

                        finishTime = System.currentTimeMillis();
                        break;

                }

                return false;
            }
        });


        return rootView;
    }
}
