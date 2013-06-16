package org.sfsteam.easyscrum;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by warmount on 15.06.13.
 */
public class CardActivity extends Activity {
    LinearLayout root;
    TextView cardTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.root = (LinearLayout) this.getLayoutInflater().inflate(R.layout.card_layout, null);
        this.setContentView(root);

        Bundle b = getIntent().getExtras();
        String cardValue = b.getString("card");

        cardTv = (TextView) root.findViewById(R.id.card_num);
        cardTv.setText(cardValue);
        cardTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });


    }

    public void showCard(View v){
        LinearLayout startLay = (LinearLayout) root.findViewById(R.id.start);
        startLay.setVisibility(View.GONE);
        cardTv.setVisibility(View.VISIBLE);
    }
}
