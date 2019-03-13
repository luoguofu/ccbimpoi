package com.weqia.wq.component.activity.assist;

import android.view.KeyEvent;

public interface TalkBarInterface {
    
    public void sendText(String text);
    
    public void sendVoice(String path, int second);
    
    public void scrollToSend();
    
    public void artPeople();
    
    public void delChar(KeyEvent keyevent);
    
    public void onSendVoice();
}
