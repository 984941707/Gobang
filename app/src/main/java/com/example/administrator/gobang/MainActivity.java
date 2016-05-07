package com.example.administrator.gobang;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements Toolbar.OnMenuItemClickListener {

    private static final String INDEX_WHITE = "com.example.administrator.gobang.index_white";
    private static final String INDEX_BLACK = "com.example.administrator.gobang.index_black";
    private static final String INDEX_ROTATED = "com.example.administrator.gobang.index_rotated";

    private TextView tvWhiteScore;
    private TextView tvBlackScore;
    private GobangPanel mGobangPanel;
    private Toolbar mToolBar;
    private int whiteScore = 0;
    private int blackScore = 0;

    private boolean have_rotated = false;//防止旋转后造成，分值改变

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        mGobangPanel = (GobangPanel) findViewById (R.id.gobang);
        mToolBar = (Toolbar) findViewById (R.id.toolbar);
        tvBlackScore = (TextView) findViewById (R.id.black_score);
        tvWhiteScore = (TextView) findViewById (R.id.white_score);

        mToolBar.inflateMenu(R.menu.menu_main);
        mToolBar.setOnMenuItemClickListener (this);

        mGobangPanel.setOnGameOverListener (new OnGameOverListener () {
            @Override
            public void onGameWin (boolean win) {
                showDia (win ? "白棋赢了" : "黑棋赢了");
            }

            @Override
            public void onGameTie () {
                showDia ("平手");
            }
        });

        if(savedInstanceState != null) {
            whiteScore = savedInstanceState.getInt (INDEX_WHITE,0);
            blackScore = savedInstanceState.getInt (INDEX_BLACK,0);
            have_rotated = savedInstanceState.getBoolean (INDEX_ROTATED,false);
        }
        tvWhiteScore.setText (whiteScore+"");
        tvBlackScore.setText (blackScore+"");
    }

    private void showDia (String text) {

        Dialog dialog = new AlertDialog.Builder (MainActivity.this)
            .setIcon (R.drawable.gobang)
            .setTitle (text)
            .setPositiveButton (R.string.again, new DialogInterface.OnClickListener () {
                @Override
                public void onClick (DialogInterface dialog, int which) {
                    mGobangPanel.restart ();
                }
            })
            .setNegativeButton (R.string.cancel, new DialogInterface.OnClickListener () {
                @Override
                public void onClick (DialogInterface dialog, int which) {

                }
            }).create ();
        dialog.show ();
        if(!have_rotated){
            score (text);
        }
    }

    public void score(String text) {
        switch (text) {
            case "白棋赢了":
                whiteScore++;
                break;
            case "黑棋赢了":
                blackScore++;
                break;
            case "平手":
                break;
        }
        tvWhiteScore.setText (whiteScore+"");
        tvBlackScore.setText (blackScore+"");
    }
    @Override
    public boolean onMenuItemClick (MenuItem item) {
        int id = item.getItemId ();
        switch (id) {
            case R.id.action_restart:
                mGobangPanel.restart ();
                break;
            case R.id.action_regret:
                mGobangPanel.regret ();
                break;
        }
        return false;
    }

    @Override
    protected void onSaveInstanceState (Bundle saveInstanceState) {
        super.onSaveInstanceState (saveInstanceState);
        saveInstanceState.putInt(INDEX_BLACK,blackScore);
        saveInstanceState.putInt(INDEX_WHITE,whiteScore);
        saveInstanceState.putBoolean (INDEX_ROTATED,!have_rotated);
    }

    //    @Override
//    public boolean onCreateOptionsMenu (Menu menu) {
//        //Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater ().inflate (R.menu.menu_main, menu);
//        return true;
//    }
//
//
//    @Override
//    public boolean onOptionsItemSelected (MenuItem item) {
//        //Handle action bar item clicks here. The action bar will
//        //automatically handle clicks on the Home/Up button, so long
//        //as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId ();
//
//        //noinspection SimplifiableIfStatement
//        if(id == R.id.action_restart) {
//            mGobangPanel.start ();
//            return true;
//        }
//        return super.onOptionsItemSelected (item);
//    }
}
