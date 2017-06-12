package com.unity3d.player;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Window;

public class UnityPlayerActivity extends Activity
{
  protected UnityPlayer mUnityPlayer;

  public boolean dispatchKeyEvent(KeyEvent paramKeyEvent)
  {
    if (paramKeyEvent.getAction() == 2)
      return this.mUnityPlayer.injectEvent(paramKeyEvent);
    return super.dispatchKeyEvent(paramKeyEvent);
  }

  public void onConfigurationChanged(Configuration paramConfiguration)
  {
    super.onConfigurationChanged(paramConfiguration);
    this.mUnityPlayer.configurationChanged(paramConfiguration);
  }

  protected void onCreate(Bundle paramBundle)
  {
    requestWindowFeature(1);
    super.onCreate(paramBundle);
    getWindow().setFormat(2);
    this.mUnityPlayer = new UnityPlayer(this);
    setContentView(this.mUnityPlayer);
    this.mUnityPlayer.requestFocus();
  }

  protected void onDestroy()
  {
    this.mUnityPlayer.quit();
    super.onDestroy();
  }

  public boolean onGenericMotionEvent(MotionEvent paramMotionEvent)
  {
    return this.mUnityPlayer.injectEvent(paramMotionEvent);
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    return this.mUnityPlayer.injectEvent(paramKeyEvent);
  }

  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent)
  {
    return this.mUnityPlayer.injectEvent(paramKeyEvent);
  }

  protected void onNewIntent(Intent paramIntent)
  {
    setIntent(paramIntent);
  }

  protected void onPause()
  {
    super.onPause();
    this.mUnityPlayer.pause();
  }

  protected void onResume()
  {
    super.onResume();
    this.mUnityPlayer.resume();
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    return this.mUnityPlayer.injectEvent(paramMotionEvent);
  }

  public void onWindowFocusChanged(boolean paramBoolean)
  {
    super.onWindowFocusChanged(paramBoolean);
    this.mUnityPlayer.windowFocusChanged(paramBoolean);
  }
}

/* Location:           /disk1/tools/decompile/dex2jar/jar_src/classes.jar
 * Qualified Name:     com.unity3d.player.UnityPlayerActivity
 * JD-Core Version:    0.6.2
 */