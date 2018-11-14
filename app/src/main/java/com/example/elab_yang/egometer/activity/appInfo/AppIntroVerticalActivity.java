package com.example.elab_yang.egometer.activity.appInfo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import com.dreamwalker.verticalintrov2.VerticalIntro;
import com.dreamwalker.verticalintrov2.VerticalIntroItem;
import com.example.elab_yang.egometer.R;
import com.example.elab_yang.egometer.activity.MainActivity;

public class AppIntroVerticalActivity extends VerticalIntro {
    private static final String TAG = "IntroVertical";
    SharedPreferences pref;

    @Override
    protected void onStart() {
        super.onStart();
        // 처음이 아니네?
//        check_first();
    }

    @Override
    protected void init() {
        // 상태바 제거
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        // 1페이지
        addIntroItem(new VerticalIntroItem.Builder()
                .backgroundColor(R.color.gray)
                .image(R.mipmap.intro1)
                .title("이모든솔루션 - 유산소 운동 관리 프로그램")
                .titleSize(30)
                .titleColor(R.color.black)
                .text("강원대학교 전자공학전공")
                .textSize(20)
                .textColor(R.color.black)
                .build());

        // 2페이지
        addIntroItem(new VerticalIntroItem.Builder()
                .backgroundColor(R.color.colorPrimary)
                .image(R.mipmap.intro2)
                .title("운동 데이터 자동 저장")
                .titleSize(30)
                .titleColor(R.color.white)
                .text("자동저장해주요")
                .textSize(20)
                .textColor(R.color.white)
                .build());

        // 3페이지
        addIntroItem(new VerticalIntroItem.Builder()
                .backgroundColor(R.color.lime)
                .image(R.mipmap.intro3)
                .title("개개인에 맞게 운동 프로그램 작성! 코칭!")
                .titleSize(30)
                .titleColor(R.color.black)
                .text("우와ㅏ아앙")
                .textSize(20)
                .textColor(R.color.black)
                .build());

        setDoneText("시작해볼까요?");
        setTheme(android.R.style.Theme_Holo_Dialog_NoActionBar);
        setSkipEnabled(true);
        setSkipText("스-킵");
        setSkipColor(R.color.black);
        setVibrateEnabled(false);
        setCustomTypeFace(Typeface.createFromAsset(getAssets(), "bmhanna_11yrs_ttf.ttf"));
    }

    // 3페이지 버튼(최종 버튼)
    @Override
    protected Integer setLastItemBottomViewColor() {
        return R.color.colorAccent;
    }

    // 스맵버튼
    @Override
    protected void onSkipPressed(View view) {
        Log.d(TAG, "onSkipPressed: 스키이이입입입입!!");
    }

    // 페이지 넘어갈 때
    @Override
    protected void onFragmentChanged(int position) {
        Log.d(TAG, "onFragmentChanged: 변경 : position" + position);
    }

    // 미지막 버튼 클릭시
    @Override
    protected void onDonePressed() {
        // first_or_second = true;
//        check_exec();
        // 전환
        startActivity(new Intent(this, MainActivity.class));
        Log.d(TAG, "onDonePressed: AppIntroVerticalActivity -> MainActivity");
        finish();
    }

    // 처음 사용자인가?
//    private void check_first() {
//        pref = getSharedPreferences("ActivityPREF", Context.MODE_PRIVATE);
//        if (pref.getBoolean("first_or_second", false)) {
//            Log.d(TAG, "check_first: 응 아니야");
////            startActivity(new Intent(this, MainActivity.class));
//            startActivity(new Intent(this, MainActivity.class));
//            finish();
//        }
//    }

    // 실행
//    private void check_exec() {
//        SharedPreferences.Editor editor = pref.edit();
//        editor.putBoolean("first_or_second", true);
//        editor.apply();
//    }
}

