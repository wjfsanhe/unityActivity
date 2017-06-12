package com.unity3d.player;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class UnityPlayerProxyActivity extends Activity
{
  protected void onCreate(Bundle paramBundle)
  {
    Log.w("Unity", "UnityPlayerNativeActivity has been deprecated, please update your AndroidManifest to use UnityPlayerActivity instead");
    super.onCreate(paramBundle);
    paramBundle = new Intent(this, UnityPlayerActivity.class);
    paramBundle.addFlags(65536);
    Bundle localBundle = getIntent().getExtras();
    if (localBundle != null)
      paramBundle.putExtras(localBundle);
    startActivity(paramBundle);
  }
}

/* Location:           /disk1/tools/decompile/dex2jar/jar_src/classes.jar
 * Qualified Name:     com.unity3d.player.UnityPlayerProxyActivity
 * JD-Core Version:    0.6.2
 */