package com.example.elab_yang.egometer;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Objects;

public class CustomDialog extends Dialog {

    private static String memo;
    EditText edit;
    SeekBar seekbar;
    TextView text;
    Button set_btn;
    static int num = 3;

    public CustomDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //타이틀 바 삭제
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.custom_dialog);

        text = (TextView) findViewById(R.id.text);
        text.setText("그냥저냥");

        // 0 : 넘 ㅜ쉬운데?
        // 1 : 이정도면 할만해
        // 2 : 그냥저냥
        // 3 : 좀 힘들어
        // 4 : 매우 힘들얼
        seekbar = (SeekBar) findViewById(R.id.seekbar);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                num = seekBar.getProgress();
                setNum(num);
                if(num == 0){
                    // 0
                    text.setText("넘 ㅜ쉬운데?");
                } else if(num == 1){
                    // 1
                    text.setText("이정도면 할만해");
                } else if(num == 2){
                    // 2
                    text.setText("그냥저냥");
                } else if(num == 3){
                    // 3
                    text.setText("좀 힘들어");
                } else {
                    // 4
                    text.setText("매우 힘들얼");
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // 움직임이 시작될때
                num = seekBar.getProgress();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 움직임이 멈췄을때
                num = seekBar.getProgress();
            }
        });

        set_btn = (Button) findViewById(R.id.set_btn);
        set_btn.setOnClickListener(v -> {
            setMemo(memo);
            dismiss();
        });
    }

    public static String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public static int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
