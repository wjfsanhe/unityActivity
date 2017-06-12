package com.unity3d.player;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.net.Uri;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.MediaController;
import android.widget.MediaController.MediaPlayerControl;
import java.io.FileInputStream;
import java.io.IOException;

public final class m extends FrameLayout
  implements MediaPlayer.OnBufferingUpdateListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnVideoSizeChangedListener, SurfaceHolder.Callback, MediaController.MediaPlayerControl
{
  private static boolean a = false;
  private volatile int A = 0;
  private final UnityPlayer b;
  private final Context c;
  private final SurfaceView d;
  private final SurfaceHolder e;
  private final String f;
  private final int g;
  private final int h;
  private final boolean i;
  private final long j;
  private final long k;
  private final FrameLayout l;
  private final Display m;
  private int n;
  private int o;
  private int p;
  private int q;
  private MediaPlayer r;
  private MediaController s;
  private boolean t = false;
  private boolean u = false;
  private int v = 0;
  private boolean w = false;
  private int x = 0;
  private boolean y;
  private a z;

  protected m(UnityPlayer paramUnityPlayer, Context paramContext, String paramString, int paramInt1, int paramInt2, int paramInt3, boolean paramBoolean, long paramLong1, long paramLong2, a parama)
  {
    super(paramContext);
    this.z = parama;
    this.b = paramUnityPlayer;
    this.c = paramContext;
    this.l = this;
    this.d = new SurfaceView(paramContext);
    this.e = this.d.getHolder();
    this.e.addCallback(this);
    this.e.setType(3);
    this.l.setBackgroundColor(paramInt1);
    this.l.addView(this.d);
    this.m = ((WindowManager)this.c.getSystemService("window")).getDefaultDisplay();
    this.f = paramString;
    this.g = paramInt2;
    this.h = paramInt3;
    this.i = paramBoolean;
    this.j = paramLong1;
    this.k = paramLong2;
    if (a)
      a("fileName: " + this.f);
    if (a)
      a("backgroundColor: " + paramInt1);
    if (a)
      a("controlMode: " + this.g);
    if (a)
      a("scalingMode: " + this.h);
    if (a)
      a("isURL: " + this.i);
    if (a)
      a("videoOffset: " + this.j);
    if (a)
      a("videoLength: " + this.k);
    setFocusable(true);
    setFocusableInTouchMode(true);
    this.y = true;
  }

  private void a()
  {
    this.A = 0;
    if (this.z != null)
      this.z.a(this.A);
    doCleanUp();
    try
    {
      this.r = new MediaPlayer();
      if (this.i)
        this.r.setDataSource(this.c, Uri.parse(this.f));
      while (true)
      {
        this.r.setDisplay(this.e);
        this.r.setScreenOnWhilePlaying(true);
        this.r.setOnBufferingUpdateListener(this);
        this.r.setOnCompletionListener(this);
        this.r.setOnPreparedListener(this);
        this.r.setOnVideoSizeChangedListener(this);
        this.r.setAudioStreamType(3);
        this.r.prepare();
        if ((this.g != 0) && (this.g != 1))
          return;
        this.s = new MediaController(this.c);
        this.s.setMediaPlayer(this);
        this.s.setAnchorView(this);
        this.s.setEnabled(true);
        this.s.show();
        return;
        if (this.k == 0L)
          break;
        FileInputStream localFileInputStream1 = new FileInputStream(this.f);
        this.r.setDataSource(localFileInputStream1.getFD(), this.j, this.k);
        localFileInputStream1.close();
      }
    }
    catch (Exception localException)
    {
      while (true)
      {
        if (a)
          a("error: " + localException.getMessage() + localException);
        onDestroy();
        this.A = 2;
        if (this.z == null)
          break;
        this.z.a(this.A);
        return;
        Object localObject = getResources().getAssets();
        try
        {
          localObject = ((AssetManager)localObject).openFd(this.f);
          this.r.setDataSource(((AssetFileDescriptor)localObject).getFileDescriptor(), ((AssetFileDescriptor)localObject).getStartOffset(), ((AssetFileDescriptor)localObject).getLength());
          ((AssetFileDescriptor)localObject).close();
        }
        catch (IOException localIOException)
        {
          FileInputStream localFileInputStream2 = new FileInputStream(this.f);
          this.r.setDataSource(localFileInputStream2.getFD());
          localFileInputStream2.close();
        }
      }
    }
  }

  private static void a(String paramString)
  {
    Log.v("Video", "VideoPlayer: " + paramString);
  }

  private void b()
  {
    if (isPlaying());
    do
    {
      return;
      this.A = 1;
      if (this.z != null)
        this.z.a(this.A);
      if (a)
        a("startVideoPlayback");
      updateVideoLayout();
    }
    while (this.w);
    start();
  }

  public final boolean canPause()
  {
    return true;
  }

  public final boolean canSeekBackward()
  {
    return true;
  }

  public final boolean canSeekForward()
  {
    return true;
  }

  protected final void doCleanUp()
  {
    if (this.r != null)
    {
      this.r.release();
      this.r = null;
    }
    this.p = 0;
    this.q = 0;
    this.u = false;
    this.t = false;
  }

  public final int getBufferPercentage()
  {
    if (this.i)
      return this.v;
    return 100;
  }

  public final int getCurrentPosition()
  {
    if (this.r == null)
      return 0;
    return this.r.getCurrentPosition();
  }

  public final int getDuration()
  {
    if (this.r == null)
      return 0;
    return this.r.getDuration();
  }

  public final boolean isPlaying()
  {
    int i1;
    if ((this.u) && (this.t))
    {
      i1 = 1;
      if (this.r != null)
        break label36;
      if (i1 != 0)
        break label34;
    }
    label34: label36: 
    while ((this.r.isPlaying()) || (i1 == 0))
    {
      return true;
      i1 = 0;
      break;
      return false;
    }
    return false;
  }

  public final void onBufferingUpdate(MediaPlayer paramMediaPlayer, int paramInt)
  {
    if (a)
      a("onBufferingUpdate percent:" + paramInt);
    this.v = paramInt;
  }

  public final void onCompletion(MediaPlayer paramMediaPlayer)
  {
    if (a)
      a("onCompletion called");
    onDestroy();
  }

  public final void onControllerHide()
  {
  }

  protected final void onDestroy()
  {
    onPause();
    doCleanUp();
    UnityPlayer.a(new Runnable()
    {
      public final void run()
      {
        m.a(m.this).hideVideoPlayer();
      }
    });
  }

  public final boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    if ((paramInt == 4) || ((this.g == 2) && (paramInt != 0) && (!paramKeyEvent.isSystem())))
    {
      onDestroy();
      return true;
    }
    if (this.s != null)
      return this.s.onKeyDown(paramInt, paramKeyEvent);
    return super.onKeyDown(paramInt, paramKeyEvent);
  }

  protected final void onPause()
  {
    if (a)
      a("onPause called");
    if (!this.w)
    {
      pause();
      this.w = false;
    }
    if (this.r != null)
      this.x = this.r.getCurrentPosition();
    this.y = false;
  }

  public final void onPrepared(MediaPlayer paramMediaPlayer)
  {
    if (a)
      a("onPrepared called");
    this.u = true;
    if ((this.u) && (this.t))
      b();
  }

  protected final void onResume()
  {
    if (a)
      a("onResume called");
    if ((!this.y) && (!this.w))
      start();
    this.y = true;
  }

  public final boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    int i1 = paramMotionEvent.getAction();
    if ((this.g == 2) && ((i1 & 0xFF) == 0))
    {
      onDestroy();
      return true;
    }
    if (this.s != null)
      return this.s.onTouchEvent(paramMotionEvent);
    return super.onTouchEvent(paramMotionEvent);
  }

  public final void onVideoSizeChanged(MediaPlayer paramMediaPlayer, int paramInt1, int paramInt2)
  {
    if (a)
      a("onVideoSizeChanged called " + paramInt1 + "x" + paramInt2);
    if ((paramInt1 == 0) || (paramInt2 == 0))
      if (a)
        a("invalid video width(" + paramInt1 + ") or height(" + paramInt2 + ")");
    do
    {
      return;
      this.t = true;
      this.p = paramInt1;
      this.q = paramInt2;
    }
    while ((!this.u) || (!this.t));
    b();
  }

  public final void pause()
  {
    if (this.r == null)
      return;
    this.r.pause();
    this.w = true;
  }

  public final void seekTo(int paramInt)
  {
    if (this.r == null)
      return;
    this.r.seekTo(paramInt);
  }

  public final void start()
  {
    if (this.r == null)
      return;
    this.r.start();
    this.w = false;
  }

  public final void surfaceChanged(SurfaceHolder paramSurfaceHolder, int paramInt1, int paramInt2, int paramInt3)
  {
    if (a)
      a("surfaceChanged called " + paramInt1 + " " + paramInt2 + "x" + paramInt3);
    if ((this.n != paramInt2) || (this.o != paramInt3))
    {
      this.n = paramInt2;
      this.o = paramInt3;
      updateVideoLayout();
    }
  }

  public final void surfaceCreated(SurfaceHolder paramSurfaceHolder)
  {
    if (a)
      a("surfaceCreated called");
    a();
    seekTo(this.x);
  }

  public final void surfaceDestroyed(SurfaceHolder paramSurfaceHolder)
  {
    if (a)
      a("surfaceDestroyed called");
    doCleanUp();
  }

  protected final void updateVideoLayout()
  {
    if (a)
      a("updateVideoLayout");
    Object localObject;
    if ((this.n == 0) || (this.o == 0))
    {
      localObject = (WindowManager)this.c.getSystemService("window");
      this.n = ((WindowManager)localObject).getDefaultDisplay().getWidth();
      this.o = ((WindowManager)localObject).getDefaultDisplay().getHeight();
    }
    int i2 = this.n;
    int i1 = this.o;
    float f1 = this.p / this.q;
    float f2 = this.n / this.o;
    if (this.h == 1)
      if (f2 <= f1)
        i1 = (int)(this.n / f1);
    while (true)
    {
      if (a)
        a("frameWidth = " + i2 + "; frameHeight = " + i1);
      localObject = new FrameLayout.LayoutParams(i2, i1, 17);
      this.l.updateViewLayout(this.d, (ViewGroup.LayoutParams)localObject);
      return;
      i2 = (int)(this.o * f1);
      continue;
      if (this.h == 2)
      {
        if (f2 >= f1)
          i1 = (int)(this.n / f1);
        else
          i2 = (int)(this.o * f1);
      }
      else if (this.h == 0)
      {
        i2 = this.p;
        i1 = this.q;
      }
    }
  }

  public static abstract interface a
  {
    public abstract void a(int paramInt);
  }
}

/* Location:           /disk1/tools/decompile/dex2jar/jar_src/classes.jar
 * Qualified Name:     com.unity3d.player.m
 * JD-Core Version:    0.6.2
 */