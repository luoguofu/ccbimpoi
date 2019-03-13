package com.weqia.wq.component.talk;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

public class SoftKeyboardUtil {
            public static void observeSoftKeyboard(final Activity activity, final boolean fullScreen, final OnSoftKeyboardChangeListener listener) {
                final View decorView = activity.getWindow().getDecorView();
                decorView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    int previousKeyboardHeight = -1;
                    @Override
                    public void onGlobalLayout() {
                        int statusBarHeight = 0;
                        if (fullScreen) {
                            statusBarHeight = 0;
                        } else {
                            Rect frame = new Rect();
                            activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
                            statusBarHeight = frame.top;
                        }

                        Rect rect = new Rect();
                        decorView.getWindowVisibleDisplayFrame(rect);

                        int displayHeight = rect.bottom - rect.top + statusBarHeight;
                        int height = decorView.getHeight();
                        int keyboardHeight = height - displayHeight;
                        if (previousKeyboardHeight != keyboardHeight) {
                            boolean hide = (double) displayHeight / height > 0.8;
                            listener.onSoftKeyBoardChange(keyboardHeight, !hide);
                        }

                        previousKeyboardHeight = height;

                    }
                });
            }

            public interface OnSoftKeyboardChangeListener {
                void onSoftKeyBoardChange(int softKeybardHeight, boolean visible);
            }
        }
        