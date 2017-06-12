package com.unity3d.player;

import android.os.Build;

final class k
  implements Thread.UncaughtExceptionHandler
{
  private volatile Thread.UncaughtExceptionHandler a;

  final boolean a()
  {
    try
    {
      Thread.UncaughtExceptionHandler localUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
      if (localUncaughtExceptionHandler == this);
      for (boolean bool = false; ; bool = true)
      {
        return bool;
        this.a = localUncaughtExceptionHandler;
        Thread.setDefaultUncaughtExceptionHandler(this);
      }
    }
    finally
    {
    }
  }

  public final void uncaughtException(Thread paramThread, Throwable paramThrowable)
  {
    try
    {
      Error localError = new Error(String.format("FATAL EXCEPTION [%s]\n", new Object[] { paramThread.getName() }) + String.format("Unity version     : %s\n", new Object[] { "5.4.2f2-GVR13" }) + String.format("Device model      : %s %s\n", new Object[] { Build.MANUFACTURER, Build.MODEL }) + String.format("Device fingerprint: %s\n", new Object[] { Build.FINGERPRINT }));
      localError.setStackTrace(new StackTraceElement[0]);
      localError.initCause(paramThrowable);
      this.a.uncaughtException(paramThread, localError);
      return;
    }
    catch (Throwable localThrowable)
    {
      while (true)
        this.a.uncaughtException(paramThread, paramThrowable);
    }
    finally
    {
    }
    throw paramThread;
  }
}

/* Location:           /disk1/tools/decompile/dex2jar/jar_src/classes.jar
 * Qualified Name:     com.unity3d.player.k
 * JD-Core Version:    0.6.2
 */