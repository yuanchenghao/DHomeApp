package com.dejia.anju.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import com.dejia.anju.model.Emoji;
import com.dejia.anju.view.HttpTextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by Administrator on 2017/12/22.
 */

public class Expression {
    private static ArrayList<Emoji> emojiList;

    public static ArrayList<Emoji> getEmojiList() {
        if (emojiList == null) {
            emojiList = generateEmojis();
        }
        return emojiList;
    }

    private static ArrayList<Emoji> generateEmojis() {
        ArrayList<Emoji> list = new ArrayList<>();
        for (int i = 0; i < EmojiResArray.length; i++) {
            Emoji emoji = new Emoji();
            emoji.setImageUri(EmojiResArray[i]);
            emoji.setContent(EmojiTextArray[i]);
            list.add(emoji);
        }
        return list;
    }

    public static int[] EmojiResArray = new int[]{
//            R.drawable.f000, R.drawable.f001, R.drawable.f002, R.drawable.f003, R.drawable.f004, R.drawable.f005, R.drawable.f006, R.drawable.f007, R.drawable.f008, R.drawable.f009, R.drawable.f010,
//            R.drawable.f011, R.drawable.f012, R.drawable.f013, R.drawable.f014, R.drawable.f015, R.drawable.f016, R.drawable.f017, R.drawable.f018, R.drawable.f019, R.drawable.f020,
//            R.drawable.f021, R.drawable.f022, R.drawable.f023, R.drawable.f024, R.drawable.f025, R.drawable.f026, R.drawable.f0000, R.drawable.f027, R.drawable.f028, R.drawable.f029, R.drawable.f030,
//            R.drawable.f031, R.drawable.f032, R.drawable.f033, R.drawable.f034, R.drawable.f035, R.drawable.f036, R.drawable.f037, R.drawable.f038, R.drawable.f039, R.drawable.f040,
//            R.drawable.f041, R.drawable.f042, R.drawable.f044, R.drawable.f045, R.drawable.f046, R.drawable.f047, R.drawable.f048, R.drawable.f049, R.drawable.f050,
//            R.drawable.f051, R.drawable.f052, R.drawable.f053, R.drawable.f054 ,R.drawable.f0000,R.drawable.f055, R.drawable.f056, R.drawable.f057, R.drawable.f058, R.drawable.f059,
//            R.drawable.f060, R.drawable.f061, R.drawable.f062, R.drawable.f063, R.drawable.f064, R.drawable.f065, R.drawable.f066, R.drawable.f067, R.drawable.f068, R.drawable.f069,R.drawable.small_ask
            };
    public static String[] EmojiTextArray = new String[]{"[微笑]", "[羞涩]", "[惊呆]", "[可爱]", "[大爱]", "[龇牙]", "[大笑]", "[坏笑]", "[腼腆]", "[抓狂]",
            "[黑脸]", "[得意]", "[亲亲]", "[鼓掌]", "[挖鼻]", "[忿忿]", "[泪]", "[笑]", "[飞吻]", "[汪星人]",
            "[喵星人]", "[汗]", "[疑惑]", "[思考]", "[俏皮]", "[财迷]", "[可怜]", "[删除]", "[馋]", "[泪汪汪]",
            "[黑线]", "[烦躁]", "[鄙视]", "[楞神]", "[再见]", "[晕]", "[哈欠]", "[礼物]", "[春风]", "[雨天]",
            "[灰机]", "[鲜花]", "[太阳]", "[月亮]", "[音符]", "[爱心]", "[心碎]", "[棒棒哒]", "[胜利]", "[握手]",
            "[拱手]", "[差劲]", "[没问题]", "[蛋糕]", "[蜡烛]", "[删除]","[菜刀]","[红唇]","[口红]","[礼盒]","[闪电]",
            "[闪闪]","[闪心]","[水滴]","[五星]","[向日葵]","[心]","[星环]","[星星]","[叶子]","[钻戒]","[-sw-]"};

    static {
        emojiList = generateEmojis();
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId, int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }


    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    public static void handlerEmojiText(HttpTextView comment, String content, Context context) {

        SpannableStringBuilder sb = new SpannableStringBuilder(content);
        String regex = "\\[(\\S+?)\\]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        Iterator<Emoji> iterator;
        Emoji emoji = null;
        while (m.find()) {
            iterator = emojiList.iterator();
            String tempText = m.group();
            while (iterator.hasNext()) {
                emoji = iterator.next();
                if (tempText.equals(emoji.getContent())) {
                    //转换为Span并设置Span的大小
                    sb.setSpan(new ImageSpan(context, decodeSampledBitmapFromResource(context.getResources(), emoji.getImageUri(), dip2px(context, 12), dip2px(context, 12))), m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
                }
            }
        }
        comment.setUrlText(sb);
    }

    public static SpannableStringBuilder handlerEmojiText1(String content, Context context, int resId) {
        SpannableStringBuilder sb = new SpannableStringBuilder(content);
        String regex = "\\[(\\S+?)\\]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        Iterator<Emoji> iterator;
        Emoji emoji = null;
        while (m.find()) {
            iterator = emojiList.iterator();
            String tempText = m.group();
            while (iterator.hasNext()) {
                emoji = iterator.next();
                if (tempText.equals(emoji.getContent())) {
                    //转换为Span并设置Span的大小
                    sb.setSpan(new ImageSpan(context, decodeSampledBitmapFromResource(context.getResources(), emoji.getImageUri(), resId, resId)), m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
                }
            }
        }
        return sb;
    }

    public static SpannableStringBuilder handlerEmojiText2(SpannableStringBuilder content, Context context, int resId) {
        String regex = "\\[(\\S+?)\\]";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(content);
        Iterator<Emoji> iterator;
        Emoji emoji = null;
        while (m.find()) {
            iterator = emojiList.iterator();
            String tempText = m.group();
            while (iterator.hasNext()) {
                emoji = iterator.next();
                if (tempText.equals(emoji.getContent())) {
                    //转换为Span并设置Span的大小
                    content.setSpan(new CenterAlignImageSpan(context, decodeSampledBitmapFromResource(context.getResources(), emoji.getImageUri(), resId, resId)), m.start(), m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    break;
                }
            }
        }
        return content;
    }
}
