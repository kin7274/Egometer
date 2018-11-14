package com.example.elab_yang.egometer;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.Objects;

import static android.support.constraint.Constraints.TAG;

public class CustomDialog extends Dialog {

    private static String memo;
    EditText edit;
    SeekBar seekbar;
    TextView text;
    Button set_btn;
    static int num;

    public CustomDialog(Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);  //타이틀 바 삭제
        Objects.requireNonNull(getWindow()).setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setContentView(R.layout.custom_dialog);

        edit = (EditText) findViewById(R.id.edit);

        text = (TextView) findViewById(R.id.text);
        text.setText("6 : 너무 가볍다");

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
                Log.d(TAG, "onProgressChanged: mi, " + num);
                setnNum(num);

                switch (num) {
                    case 0:
                        text.setText("6 : 너무 가볍다");
                        break;
                    case 1:
                        text.setText("7 : 가볍다");
                        break;
                    case 2:
                        text.setText("8 : 가볍다");
                        break;
                    case 3:
                        text.setText("9 : 가볍다");
                        break;
                    case 4:
                        text.setText("10 : 가볍다");
                        break;
                    case 5:
                        text.setText("11 : 가볍다");
                        break;
                    case 6:
                        text.setText("12 : 조금 힘들다");
                        break;
                    case 7:
                        text.setText("13 : 조금 힘들다");
                        break;
                    case 8:
                        text.setText("14 : 힘들다");
                        break;
                    case 9:
                        text.setText("15 : 힘들다");
                        break;
                    case 10:
                        text.setText("16 : 매우 힘들다");
                        break;
                    case 11:
                        text.setText("17 : 매우 힘들다");
                        break;
                    case 12:
                        text.setText("18 : 최고 힘들다");
                        break;
                    case 13:
                        text.setText("19 : 최고 힘들다");
                        break;
                    case 14:
                        text.setText("20 : 최고 힘들다");
                        break;
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
//                setnNum(num);
            }
        });

        set_btn = (Button) findViewById(R.id.set_btn);
        set_btn.setOnClickListener(v -> {
            setnNum(num);
            memo = edit.getText().toString();
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

    public static int getnNum() {
        return num;
    }

    public void setnNum(int num) {
        this.num = num;
    }
}
