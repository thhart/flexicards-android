package com.ksmirenko.flexicards.app;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import com.ksmirenko.flexicards.app.adapters.CardsPagerAdapter;

import java.util.ArrayList;

/**
 * Activity for card viewing, i.e. main purpose of the app.
 *
 * @author Kirill Smirenko
 */
public class CardViewActivity extends AppCompatActivity
        implements CardContainerFragment.Callbacks {
    /**
     * The argument representing ID of the currently viewed module.
     */
    public static final String ARG_MODULE_ID = "module_id";

    private ViewPager cardContainerPager;
    private Cursor cardCursor;

    private int cardsTotalCount;
    private ArrayList<Long> cardsUnansweredIds;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards_view);

        long moduleId = getIntent().getLongExtra(ARG_MODULE_ID, 0);
        cardCursor = DatabaseManager.INSTANCE.getModuleCards(moduleId);
//        CardsPagerAdapter pagerAdapter = new CardsPagerAdapter(getSupportFragmentManager(), cardCursor);
        CardsPagerAdapter pagerAdapter = new CardsPagerAdapter(getFragmentManager(), cardCursor);
        cardContainerPager = (ViewPager) findViewById(R.id.viewpager_card_container);
        cardContainerPager.setAdapter(pagerAdapter);

        // initializing counters
        cardsTotalCount = cardCursor.getCount();
        cardsUnansweredIds = new ArrayList<Long>();
    }

    // callback for cards' buttons
    @Override
    public void onCardButtonClicked(boolean knowIt) {
        int position = cardContainerPager.getCurrentItem();
        if (position + 1 >= cardsTotalCount) {
            // setting module passage result and closing the activity
            Intent intent = new Intent();
            intent.putExtra(CategoryFragment.ARG_CARDS_UNANSWERED, Utils.INSTANCE.listToString(cardsUnansweredIds));
            intent.putExtra(CategoryFragment.ARG_CARDS_UNANSWERED_CNT, cardsUnansweredIds.size());
            intent.putExtra(CategoryFragment.ARG_CARDS_TOTAL_CNT, cardsTotalCount);
            setResult(RESULT_OK, intent);
            finish();
        } else {
            if (!knowIt) {
                cardCursor.moveToPosition(position);
                // should I just write "0" as column index in the next line?
                cardsUnansweredIds.add(cardCursor.getLong(DatabaseManager.CardQuery.Companion.getCOLUMN_INDEX_ID()));
            }
            cardContainerPager.setCurrentItem(position + 1, true);
        }
    }
}
