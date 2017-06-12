package com.unity3d.player;

import android.os.Bundle;
import android.util.Log;

public class UnityPlayerNativeActivity extends UnityPlayerActivity
{
  protected void onCreate(Bundle paramBundle)
  {
    Log.w("Unity", "UnityPlayerNativeActivity has been deprecated, please update your AndroidManifest to use UnityPlayerActivity instead");
    super.onCreate(paramBundle);
  }
}

/* Location:           /disk1/tools/decompile/dex2jar/jar_src/classes.jar
 * Qualified Name:     com.unity3d.player.UnityPlayerNativeActivity
 * JD-Core Version:    0.6.2
 */