package sdi.com.currencywizard.views;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewMontserratUltraLight extends TextView {


    Context mContext;
    static String textFont = "Montserrat-UltraLight.otf";

    public TextViewMontserratUltraLight(Context context) {
        super(context);
        this.mContext = context;
        inite();
    }

    public TextViewMontserratUltraLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        inite();
    }

    public TextViewMontserratUltraLight(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        inite();
    }

    private void inite() {
        if (!isInEditMode()) {
            if(textFont == "" ||textFont == null){

            }else {
                this.setTypeface(Typeface.createFromAsset(mContext.getAssets(), textFont));
            }
        }
    }
}


