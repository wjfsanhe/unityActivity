package com.unity3d.player;

import android.app.Presentation;
import android.content.Context;
import android.hardware.display.DisplayManager;
import android.hardware.display.DisplayManager.DisplayListener;
import android.os.Bundle;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public final class f
  implements d
{
  private Object a = new Object[0];
  private Presentation b;
  private DisplayManager.DisplayListener c;

  public final void a(Context paramContext)
  {
    if (this.c == null);
    do
    {
      return;
      paramContext = (DisplayManager)paramContext.getSystemService("display");
    }
    while (paramContext == null);
    paramContext.unregisterDisplayListener(this.c);
  }

  public final void a(final UnityPlayer paramUnityPlayer, Context paramContext)
  {
    paramContext = (DisplayManager)paramContext.getSystemService("display");
    if (paramContext == null)
      return;
    paramContext.registerDisplayListener(new DisplayManager.DisplayListener()
    {
      public final void onDisplayAdded(int paramAnonymousInt)
      {
        paramUnityPlayer.displayChanged(-1, null);
      }

      public final void onDisplayChanged(int paramAnonymousInt)
      {
        paramUnityPlayer.displayChanged(-1, null);
      }

      public final void onDisplayRemoved(int paramAnonymousInt)
      {
        paramUnityPlayer.displayChanged(-1, null);
      }
    }
    , null);
  }

  public final boolean a(final UnityPlayer paramUnityPlayer, final Context paramContext, int paramInt)
  {
    synchronized (this.a)
    {
      if ((this.b != null) && (this.b.isShowing()))
      {
        localObject2 = this.b.getDisplay();
        if ((localObject2 != null) && (((Display)localObject2).getDisplayId() == paramInt))
          return true;
      }
      Object localObject2 = (DisplayManager)paramContext.getSystemService("display");
      if (localObject2 == null)
        return false;
      localObject2 = ((DisplayManager)localObject2).getDisplay(paramInt);
      if (localObject2 == null)
        return false;
      paramUnityPlayer.b(new Runnable()
      {
        public final void run()
        {
          synchronized (f.a(f.this))
          {
            if (f.b(f.this) != null)
              f.b(f.this).dismiss();
            f.a(f.this, new Presentation(paramContext, this.b)
            {
              protected final void onCreate(Bundle paramAnonymous2Bundle)
              {
                paramAnonymous2Bundle = new SurfaceView(f.2.this.a);
                paramAnonymous2Bundle.getHolder().addCallback(new SurfaceHolder.Callback()
                {
                  public final void surfaceChanged(SurfaceHolder paramAnonymous3SurfaceHolder, int paramAnonymous3Int1, int paramAnonymous3Int2, int paramAnonymous3Int3)
                  {
                    f.2.this.c.displayChanged(1, paramAnonymous3SurfaceHolder.getSurface());
                  }

                  public final void surfaceCreated(SurfaceHolder paramAnonymous3SurfaceHolder)
                  {
                    f.2.this.c.displayChanged(1, paramAnonymous3SurfaceHolder.getSurface());
                  }

                  public final void surfaceDestroyed(SurfaceHolder paramAnonymous3SurfaceHolder)
                  {
                    f.2.this.c.displayChanged(1, null);
                  }
                });
                setContentView(paramAnonymous2Bundle);
              }

              public final void onDisplayRemoved()
              {
                dismiss();
                synchronized (f.a(f.this))
                {
                  f.a(f.this, null);
                  return;
                }
              }
            });
            f.b(f.this).show();
            return;
          }
        }
      });
      return true;
    }
  }
}

/* Location:           /disk1/tools/decompile/dex2jar/jar_src/classes.jar
 * Qualified Name:     com.unity3d.player.f
 * JD-Core Version:    0.6.2
 */