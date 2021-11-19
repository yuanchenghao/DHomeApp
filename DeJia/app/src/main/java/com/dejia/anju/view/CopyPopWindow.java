package com.dejia.anju.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.dejia.anju.R;
//私信页面复制pop
public class CopyPopWindow extends PopupWindow {
    public CopyPopWindow(final Context context) {
        super(context);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View contentView = LayoutInflater.from(context).inflate(R.layout.chat_copy_pop, null, false);
        setContentView(contentView);
        TextView textView = contentView.findViewById(R.id.copy_pop_text);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mOnTextClickListener!= null){
                    mOnTextClickListener.onTextClick();
                }
            }
        });
    }


    private OnTextClickListener mOnTextClickListener;

    public void setOnTextClickListener(OnTextClickListener onTextClickListener) {
        mOnTextClickListener = onTextClickListener;
    }

    public interface OnTextClickListener{
        void onTextClick();
    }
}
