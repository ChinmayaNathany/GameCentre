package fall2018.csc2017.gamecentre;

/*
Adapted from:
https://github.com/DaveNOTDavid/sample-puzzle/blob/master/app/src/main/java/com/davenotdavid/samplepuzzle/GestureDetectGridView.java

This extension of GridView contains built in logic for handling swipes between buttons
 */

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.GridView;

import fall2018.csc2017.gamecentre.cardmatching.MatchingBoardManager;
import fall2018.csc2017.gamecentre.simon.SimonBoardManager;
import fall2018.csc2017.gamecentre.cardmatching.MatchingMainActivity;
import fall2018.csc2017.gamecentre.slidingtiles.BoardManager;

public class GestureDetectGridView extends GridView {
    public static final int SWIPE_MIN_DISTANCE = 100;
    public static final int SWIPE_MAX_OFF_PATH = 100;
    public static final int SWIPE_THRESHOLD_VELOCITY = 100;
    private GestureDetector gDetector;
    private MovementController mController;
    private boolean mFlingConfirmed = false;
    private float mTouchX;
    private float mTouchY;
    private BoardManager boardManager;
    private MatchingBoardManager matchingBoardManager;
    private SimonBoardManager simonBoardManager;

    public GestureDetectGridView(Context context) {
        super(context);
        init(context);
    }

    public GestureDetectGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GestureDetectGridView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP) // API 21
    public GestureDetectGridView(Context context, AttributeSet attrs, int defStyleAttr,
                                 int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(final Context context) {
        mController = new MovementController();
        gDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {

            @Override
            public boolean onSingleTapConfirmed(MotionEvent event) {
                int position = GestureDetectGridView.this.pointToPosition
                        (Math.round(event.getX()), Math.round(event.getY()));

                switch (context.getClass().getSimpleName()) {
                    case "MatchingMainActivity":
                        mController.processMatchingTapMovement(context, position, true);
                        break;
                    case "SimonGameActivity":
                        mController.processSimonTapMovement(context, position, true);
                        break;
                    case "GameActivity":
                        mController.processTapMovement(context, position, true);
                        break;
                }
                return true;
            }

            @Override
            public boolean onDown(MotionEvent event) {
                return true;
            }

        });
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        int action = ev.getActionMasked();
        gDetector.onTouchEvent(ev);

        if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
            mFlingConfirmed = false;
        } else if (action == MotionEvent.ACTION_DOWN) {
            mTouchX = ev.getX();
            mTouchY = ev.getY();
        } else {

            if (mFlingConfirmed) {
                return true;
            }

            float dX = (Math.abs(ev.getX() - mTouchX));
            float dY = (Math.abs(ev.getY() - mTouchY));
            if ((dX > SWIPE_MIN_DISTANCE) || (dY > SWIPE_MIN_DISTANCE)) {
                mFlingConfirmed = true;
                return true;
            }
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return gDetector.onTouchEvent(ev);
    }

    public void setBoardManager(BoardManager boardManager) {
        this.boardManager = boardManager;
        mController.setBoardManager(boardManager);
    }

    public void setMatchingBoardManager(MatchingBoardManager manager) {
        this.matchingBoardManager = manager;
        mController.setMatchingBoardManager(manager);
    }

    public void setSimonBoardManager(SimonBoardManager manager) {
        this.simonBoardManager = manager;
        mController.setSimonBoardManager(manager);
    }
}
