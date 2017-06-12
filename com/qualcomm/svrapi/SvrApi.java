package com.qualcomm.svrapi;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Point;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.Choreographer;
import android.view.Choreographer.FrameCallback;
import android.view.Display;
import android.view.WindowManager;

public class SvrApi
  implements Choreographer.FrameCallback
{
  public static final String TAG = "SvrApi";
  public static Choreographer choreographerInstance;
  public static SvrApi handler = new SvrApi();

  public static void NotifyNoVr(Activity paramActivity)
  {
    paramActivity.runOnUiThread(new Runnable()
    {
      public void run()
      {
        try
        {
          AlertDialog.Builder localBuilder = new AlertDialog.Builder(this.val$act);
          localBuilder.setMessage("SnapdragonVR not supported on this device!");
          localBuilder.setCancelable(true);
          localBuilder.setPositiveButton("Close", new DialogInterface.OnClickListener()
          {
            public void onClick(DialogInterface paramAnonymous2DialogInterface, int paramAnonymous2Int)
            {
              paramAnonymous2DialogInterface.cancel();
            }
          });
          localBuilder.create().show();
          return;
        }
        catch (Exception localException)
        {
          Log.e("SvrApi", "Exception displaying dialog box!");
          Log.e("SvrApi", localException.getMessage());
        }
      }
    });
  }

  public static int getAndroidOsVersion()
  {
    return Build.VERSION.SDK_INT;
  }

  public static int getDisplayHeight(Activity paramActivity)
  {
    paramActivity = paramActivity.getWindowManager().getDefaultDisplay();
    Point localPoint = new Point();
    paramActivity.getRealSize(localPoint);
    return localPoint.y;
  }

  public static int getDisplayOrientation(Activity paramActivity)
  {
    int i = paramActivity.getWindowManager().getDefaultDisplay().getRotation();
    if (i == 0)
      return 0;
    if (i == 1)
      return 90;
    if (i == 2)
      return 180;
    if (i == 3)
      return 270;
    return -1;
  }

  public static int getDisplayWidth(Activity paramActivity)
  {
    paramActivity = paramActivity.getWindowManager().getDefaultDisplay();
    Point localPoint = new Point();
    paramActivity.getRealSize(localPoint);
    return localPoint.x;
  }

  public static float getRefreshRate(Activity paramActivity)
  {
    return paramActivity.getWindowManager().getDefaultDisplay().getRefreshRate();
  }

  public static long getVsyncOffsetNanos(Activity paramActivity)
  {
    return paramActivity.getWindowManager().getDefaultDisplay().getAppVsyncOffsetNanos();
  }

  public static native void nativeVsync(long paramLong);

  public static void startVsync(Activity paramActivity)
  {
    paramActivity.runOnUiThread(new Thread()
    {
      public void run()
      {
        SvrApi.choreographerInstance = Choreographer.getInstance();
        SvrApi.choreographerInstance.removeFrameCallback(SvrApi.handler);
        SvrApi.choreographerInstance.postFrameCallback(SvrApi.handler);
      }
    });
  }

  public static void stopVsync(Activity paramActivity)
  {
    paramActivity.runOnUiThread(new Thread()
    {
      public void run()
      {
        SvrApi.choreographerInstance.removeFrameCallback(SvrApi.handler);
      }
    });
  }

  public void doFrame(long paramLong)
  {
    nativeVsync(paramLong);
    choreographerInstance.postFrameCallback(this);
  }
}

/* Location:           /disk1/tools/decompile/dex2jar/jar_src/classes.jar
 * Qualified Name:     com.qualcomm.svrapi.SvrApi
 * JD-Core Version:    0.6.2
 */