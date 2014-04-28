package org.sfsteam.easyscrum.v1;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by warmount on 15.06.13.
 */
public class CardActivity extends Activity {
    LinearLayout root;
    TextView cardTv;
    int cardState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.root = (LinearLayout) this.getLayoutInflater().inflate(R.layout.card_layout, null);
        this.setContentView(root);
        setCardSystemView();
        Bundle b = getIntent().getExtras();
        String cardValue = b.getString("card");

        cardTv = (TextView) root.findViewById(R.id.card_num);
        if (cardValue.length()<5){
            cardTv.setTextSize(200);
        }
        cardTv.setText(cardValue);
        cardTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setCardSystemView() {
        if (Build.VERSION.SDK_INT > 18) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                    | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                    | View.SYSTEM_UI_FLAG_IMMERSIVE);
        } else if (Build.VERSION.SDK_INT > 15) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                    | View.SYSTEM_UI_FLAG_FULLSCREEN);
        } else {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    public void showCard(View v){
        LinearLayout startLay = (LinearLayout) root.findViewById(R.id.start);
        startLay.setVisibility(View.GONE);
        cardTv.setVisibility(View.VISIBLE);
        cardState = 1;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        if (cardState == 1){
            outState.putBoolean("open",true);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState.getBoolean("open")) {
            LinearLayout startLay = (LinearLayout) root.findViewById(R.id.start);
            startLay.setVisibility(View.GONE);
            TextView cardTv = (TextView) root.findViewById(R.id.card_num);
            cardTv.setVisibility(View.VISIBLE);
            cardState = 1;
        }
    }
}
