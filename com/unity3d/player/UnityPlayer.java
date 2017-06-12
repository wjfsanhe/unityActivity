package com.unity3d.player;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Size;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.view.InputEvent;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ProgressBar;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public class UnityPlayer extends FrameLayout
  implements a.a
{
  public static Activity currentActivity = null;
  private static boolean m = loadLibraryStatic("main");
  b a = new b((byte)0);
  j b = null;
  private boolean c = false;
  private boolean d = true;
  private boolean e = false;
  private boolean f = false;
  private boolean g = false;
  private l h = new l();
  private final ConcurrentLinkedQueue i = new ConcurrentLinkedQueue();
  private BroadcastReceiver j = null;
  private ContextWrapper k;
  private SurfaceView l;
  private boolean n;
  private Bundle o = new Bundle();
  private List p = new ArrayList();
  private m q;
  private ProgressBar r = null;
  private Runnable s = new Runnable()
  {
    public final void run()
    {
      int i = UnityPlayer.k(UnityPlayer.this);
      if (i >= 0)
      {
        if (UnityPlayer.l(UnityPlayer.this) == null)
        {
          UnityPlayer.a(UnityPlayer.this, new ProgressBar(UnityPlayer.m(UnityPlayer.this), null, new int[] { 16842874, 16843401, 16842873, 16843400 }[i]));
          UnityPlayer.l(UnityPlayer.this).setIndeterminate(true);
          UnityPlayer.l(UnityPlayer.this).setLayoutParams(new FrameLayout.LayoutParams(-2, -2, 51));
          UnityPlayer.this.addView(UnityPlayer.l(UnityPlayer.this));
        }
        UnityPlayer.l(UnityPlayer.this).setVisibility(0);
        UnityPlayer.this.bringChildToFront(UnityPlayer.l(UnityPlayer.this));
      }
    }
  };
  private Runnable t = new Runnable()
  {
    public final void run()
    {
      if (UnityPlayer.l(UnityPlayer.this) != null)
      {
        UnityPlayer.l(UnityPlayer.this).setVisibility(8);
        UnityPlayer.this.removeView(UnityPlayer.l(UnityPlayer.this));
        UnityPlayer.a(UnityPlayer.this, null);
      }
    }
  };
  private FrameLayout u = null;
  private SurfaceView v = null;
  private boolean w = false;
  private AtomicBoolean x = new AtomicBoolean(false);

  static
  {
    new k().a();
    m = false;
  }

  public UnityPlayer(ContextWrapper paramContextWrapper)
  {
    super(paramContextWrapper);
    if ((paramContextWrapper instanceof Activity))
      currentActivity = (Activity)paramContextWrapper;
    this.k = paramContextWrapper;
    b();
    if (i.e)
    {
      if (currentActivity == null)
        break label292;
      i.h.a(currentActivity, new Runnable()
      {
        public final void run()
        {
          UnityPlayer.this.b(new Runnable()
          {
            public final void run()
            {
              UnityPlayer.c(UnityPlayer.this).d();
              UnityPlayer.d(UnityPlayer.this);
            }
          });
        }
      });
    }
    while (true)
    {
      if (i.a)
        i.f.a(this);
      setFullscreen(true);
      a(this.k.getApplicationInfo());
      if (l.c())
        break;
      paramContextWrapper = new AlertDialog.Builder(this.k).setTitle("Failure to initialize!").setPositiveButton("OK", new DialogInterface.OnClickListener()
      {
        public final void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt)
        {
          UnityPlayer.b(UnityPlayer.this);
        }
      }).setMessage("Your hardware does not support this application, sorry!").create();
      paramContextWrapper.setCancelable(false);
      paramContextWrapper.show();
      return;
      label292: this.h.d();
    }
    initJni(paramContextWrapper);
    nativeFile(this.k.getPackageCodePath());
    m();
    this.l = a();
    addView(this.l);
    this.n = false;
    if ((currentActivity != null) && (currentActivity.getIntent().getBooleanExtra("android.intent.extra.VR_LAUNCH", false)))
    {
      this.e = true;
      this.f = true;
      nativeSetHardwareVolumeOverride(true);
    }
    nativeInitWWW(WWW.class);
    nativeInitWebRequest(UnityWebRequest.class);
    if (i.b)
      i.g.a(this, this.k);
    n();
    this.a.start();
  }

  public static void UnitySendMessage(String paramString1, String paramString2, String paramString3)
  {
    if (!l.c())
    {
      g.Log(5, "Native libraries not loaded - dropping message for " + paramString1 + "." + paramString2);
      return;
    }
    nativeUnitySendMessage(paramString1, paramString2, paramString3);
  }

  private SurfaceView a()
  {
    SurfaceView localSurfaceView = new SurfaceView(this.k);
    localSurfaceView.getHolder().setFormat(2);
    localSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback()
    {
      public final void surfaceChanged(SurfaceHolder paramAnonymousSurfaceHolder, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3)
      {
        UnityPlayer.a(UnityPlayer.this, paramAnonymousSurfaceHolder.getSurface());
      }

      public final void surfaceCreated(SurfaceHolder paramAnonymousSurfaceHolder)
      {
        UnityPlayer.a(UnityPlayer.this, paramAnonymousSurfaceHolder.getSurface());
      }

      public final void surfaceDestroyed(SurfaceHolder paramAnonymousSurfaceHolder)
      {
        UnityPlayer.a(UnityPlayer.this, null);
      }
    });
    localSurfaceView.setFocusable(true);
    localSurfaceView.setFocusableInTouchMode(true);
    return localSurfaceView;
  }

  private void a(int paramInt, Surface paramSurface)
  {
    if (this.c)
      return;
    b(0, paramSurface);
  }

  private static void a(ApplicationInfo paramApplicationInfo)
  {
    if ((m) && (NativeLoader.load(paramApplicationInfo.nativeLibraryDir)))
      l.a();
  }

  private void a(c paramc)
  {
    if (isFinishing())
      return;
    c(paramc);
  }

  static void a(Runnable paramRunnable)
  {
    new Thread(paramRunnable).start();
  }

  private void a(String paramString)
  {
    if (this.u != null);
    try
    {
      UnityPlayer.class.getClassLoader().loadClass("com.google.vr.ndk.base.GvrLayout").getMethod(paramString, new Class[0]).invoke(this.u, new Object[0]);
      return;
    }
    catch (Exception paramString)
    {
      g.Log(6, "Unable to pause GvrLayout.");
    }
  }

  private static String[] a(Context paramContext)
  {
    String str1 = paramContext.getPackageName();
    Vector localVector = new Vector();
    try
    {
      int i1 = paramContext.getPackageManager().getPackageInfo(str1, 0).versionCode;
      if (Environment.getExternalStorageState().equals("mounted"))
      {
        paramContext = Environment.getExternalStorageDirectory();
        paramContext = new File(paramContext.toString() + "/Android/obb/" + str1);
        if (paramContext.exists())
        {
          if (i1 > 0)
          {
            String str2 = paramContext + File.separator + "main." + i1 + "." + str1 + ".obb";
            if (new File(str2).isFile())
              localVector.add(str2);
          }
          if (i1 > 0)
          {
            paramContext = paramContext + File.separator + "patch." + i1 + "." + str1 + ".obb";
            if (new File(paramContext).isFile())
              localVector.add(paramContext);
          }
        }
      }
      paramContext = new String[localVector.size()];
      localVector.toArray(paramContext);
      return paramContext;
    }
    catch (PackageManager.NameNotFoundException paramContext)
    {
    }
    return new String[0];
  }

  private static String b(String paramString)
  {
    int i2 = 0;
    Object localObject;
    try
    {
      localObject = MessageDigest.getInstance("MD5");
      FileInputStream localFileInputStream = new FileInputStream(paramString);
      long l1 = new File(paramString).length();
      localFileInputStream.skip(l1 - Math.min(l1, 65558L));
      paramString = new byte[1024];
      for (i1 = 0; i1 != -1; i1 = localFileInputStream.read(paramString))
        ((MessageDigest)localObject).update(paramString, 0, i1);
      paramString = ((MessageDigest)localObject).digest();
      if (paramString == null)
        return null;
    }
    catch (FileNotFoundException paramString)
    {
      while (true)
        paramString = null;
    }
    catch (IOException paramString)
    {
      while (true)
        paramString = null;
    }
    catch (NoSuchAlgorithmException paramString)
    {
      while (true)
        paramString = null;
      localObject = new StringBuffer();
      int i1 = i2;
      while (i1 < paramString.length)
      {
        ((StringBuffer)localObject).append(Integer.toString((paramString[i1] & 0xFF) + 256, 16).substring(1));
        i1 += 1;
      }
    }
    return ((StringBuffer)localObject).toString();
  }

  private void b()
  {
    while (true)
    {
      XmlPullParser localXmlPullParser;
      int i1;
      Object localObject4;
      try
      {
        Object localObject1 = new File(this.k.getPackageCodePath(), "assets/bin/Data/settings.xml");
        if (((File)localObject1).exists())
        {
          localObject1 = new FileInputStream((File)localObject1);
          localObject2 = XmlPullParserFactory.newInstance();
          ((XmlPullParserFactory)localObject2).setNamespaceAware(true);
          localXmlPullParser = ((XmlPullParserFactory)localObject2).newPullParser();
          localXmlPullParser.setInput((InputStream)localObject1, null);
          i1 = localXmlPullParser.getEventType();
          localObject1 = null;
          localObject4 = null;
          if (i1 != 1)
          {
            if (i1 != 2)
              break label355;
            localObject3 = localXmlPullParser.getName();
            i1 = 0;
            if (i1 >= localXmlPullParser.getAttributeCount())
              break label350;
            if (!localXmlPullParser.getAttributeName(i1).equalsIgnoreCase("name"))
              break label343;
            localObject1 = localXmlPullParser.getAttributeValue(i1);
            break label343;
          }
        }
        else
        {
          localObject1 = this.k.getAssets().open("bin/Data/settings.xml");
          continue;
          i1 = localXmlPullParser.next();
          localObject1 = localObject2;
          localObject4 = localObject3;
          continue;
          localObject2 = localObject1;
          localObject3 = localObject4;
          if (i1 != 4)
            continue;
          localObject2 = localObject1;
          localObject3 = localObject4;
          if (localObject1 == null)
            continue;
          if (localObject4.equalsIgnoreCase("integer"))
            this.o.putInt((String)localObject1, Integer.parseInt(localXmlPullParser.getText()));
          else if (localObject4.equalsIgnoreCase("string"))
            this.o.putString((String)localObject1, localXmlPullParser.getText());
        }
      }
      catch (Exception localException)
      {
        g.Log(6, "Unable to locate player settings. " + localException.getLocalizedMessage());
        c();
      }
      return;
      if (localObject4.equalsIgnoreCase("bool"))
      {
        this.o.putBoolean(localException, Boolean.parseBoolean(localXmlPullParser.getText()));
      }
      else if (localObject4.equalsIgnoreCase("float"))
      {
        this.o.putFloat(localException, Float.parseFloat(localXmlPullParser.getText()));
        break label368;
        label343: i1 += 1;
        continue;
        label350: localObject2 = localException;
        continue;
        label355: if (i1 != 3)
          continue;
        localObject3 = null;
        localObject2 = localException;
        continue;
      }
      label368: Object localObject2 = null;
      Object localObject3 = localObject4;
    }
  }

  private boolean b(int paramInt, Surface paramSurface)
  {
    if (!l.c())
      return false;
    nativeRecreateGfxState(paramInt, paramSurface);
    return true;
  }

  private void c()
  {
    if (this.u != null)
      nativeShutdownGoogleVR();
    if (((this.k instanceof Activity)) && (!((Activity)this.k).isFinishing()))
      ((Activity)this.k).finish();
  }

  private void c(Runnable paramRunnable)
  {
    if (!l.c())
      return;
    if (Thread.currentThread() == this.a)
    {
      paramRunnable.run();
      return;
    }
    this.i.add(paramRunnable);
  }

  private void d()
  {
    Iterator localIterator = this.p.iterator();
    while (localIterator.hasNext())
      ((a)localIterator.next()).c();
  }

  private void e()
  {
    Iterator localIterator = this.p.iterator();
    while (localIterator.hasNext())
    {
      a locala = (a)localIterator.next();
      try
      {
        locala.a(this);
      }
      catch (Exception localException)
      {
        g.Log(6, "Unable to initialize camera: " + localException.getMessage());
        locala.c();
      }
    }
  }

  private void f()
  {
    reportSoftInputStr(null, 1, true);
    if (!this.h.g())
      return;
    final Semaphore localSemaphore;
    if (l.c())
    {
      localSemaphore = new Semaphore(0);
      if (!isFinishing())
        break label112;
      c(new Runnable()
      {
        public final void run()
        {
          UnityPlayer.g(UnityPlayer.this);
          localSemaphore.release();
        }
      });
    }
    try
    {
      while (true)
      {
        if (!localSemaphore.tryAcquire(4L, TimeUnit.SECONDS))
          g.Log(5, "Timeout while trying to pause the Unity Engine.");
        if (localSemaphore.drainPermits() > 0)
          quit();
        this.h.c(false);
        this.h.b(true);
        d();
        this.a.c();
        return;
        label112: c(new Runnable()
        {
          public final void run()
          {
            if (UnityPlayer.h(UnityPlayer.this))
            {
              UnityPlayer.i(UnityPlayer.this);
              UnityPlayer.g(UnityPlayer.this);
              localSemaphore.release(2);
              return;
            }
            localSemaphore.release();
          }
        });
      }
    }
    catch (InterruptedException localInterruptedException)
    {
      while (true)
        g.Log(5, "UI thread got interrupted while trying to pause the Unity Engine.");
    }
  }

  private void g()
  {
    nativeDone();
  }

  private void h()
  {
    if (!this.h.f())
      return;
    if (this.q != null)
    {
      this.q.onResume();
      return;
    }
    this.h.c(true);
    e();
    if (l.c())
      m();
    c(new Runnable()
    {
      public final void run()
      {
        UnityPlayer.j(UnityPlayer.this);
      }
    });
    this.a.b();
  }

  private static void i()
  {
    if (!l.c())
      return;
    if (!NativeLoader.unload())
      throw new UnsatisfiedLinkError("Unable to unload libraries from libmain.so");
    l.b();
  }

  private final native void initJni(Context paramContext);

  private void j()
  {
    a("onPause");
  }

  private void k()
  {
    a("onResume");
  }

  private boolean l()
  {
    if (!i.a);
    while ((!this.k.getPackageManager().hasSystemFeature("android.hardware.camera")) && (!this.k.getPackageManager().hasSystemFeature("android.hardware.camera.front")))
      return false;
    return true;
  }

  protected static boolean loadLibraryStatic(String paramString)
  {
    try
    {
      System.loadLibrary(paramString);
      return true;
    }
    catch (UnsatisfiedLinkError localUnsatisfiedLinkError)
    {
      g.Log(6, "Unable to find " + paramString);
      return false;
    }
    catch (Exception paramString)
    {
      g.Log(6, "Unknown error " + paramString);
    }
    return false;
  }

  private void m()
  {
    if (!this.o.getBoolean("useObb"));
    while (true)
    {
      return;
      String[] arrayOfString = a(this.k);
      int i2 = arrayOfString.length;
      int i1 = 0;
      while (i1 < i2)
      {
        String str1 = arrayOfString[i1];
        String str2 = b(str1);
        if (this.o.getBoolean(str2))
          nativeFile(str1);
        this.o.remove(str2);
        i1 += 1;
      }
    }
  }

  private void n()
  {
    if ((this.k instanceof Activity))
      ((Activity)this.k).getWindow().setFlags(1024, 1024);
  }

  private final native int nativeActivityIndicatorStyle();

  private final native void nativeDone();

  private final native void nativeFile(String paramString);

  private final native void nativeFocusChanged(boolean paramBoolean);

  private final native void nativeInitWWW(Class paramClass);

  private final native void nativeInitWebRequest(Class paramClass);

  private final native boolean nativeInjectEvent(InputEvent paramInputEvent);

  private final native boolean nativePause();

  private final native void nativeRecreateGfxState(int paramInt, Surface paramSurface);

  private final native boolean nativeRender();

  private final native void nativeResume();

  private final native void nativeSetHardwareVolumeOverride(boolean paramBoolean);

  private final native void nativeSetInputCanceled(boolean paramBoolean);

  private final native void nativeSetInputString(String paramString);

  private final native void nativeShutdownGoogleVR();

  private final native void nativeSoftInputClosed();

  private static native void nativeUnitySendMessage(String paramString1, String paramString2, String paramString3);

  private final native void nativeVideoFrameCallback(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3);

  final void b(Runnable paramRunnable)
  {
    if ((this.k instanceof Activity))
    {
      ((Activity)this.k).runOnUiThread(paramRunnable);
      return;
    }
    g.Log(5, "Not running Unity from an Activity; ignored...");
  }

  protected void closeCamera(int paramInt)
  {
    Iterator localIterator = this.p.iterator();
    while (localIterator.hasNext())
    {
      a locala = (a)localIterator.next();
      if (locala.a() == paramInt)
      {
        locala.c();
        this.p.remove(locala);
      }
    }
  }

  public void configurationChanged(Configuration paramConfiguration)
  {
    if ((this.l instanceof SurfaceView))
      this.l.getHolder().setSizeFromLayout();
    if (this.q != null)
      this.q.updateVideoLayout();
  }

  protected void daydreamQuitToOSLauncher()
  {
    if ((this.f) && (!this.g) && (currentActivity != null))
    {
      this.g = true;
      Intent localIntent = new Intent("android.intent.action.MAIN");
      localIntent.addCategory("android.intent.category.HOME");
      localIntent.setFlags(268435456);
      currentActivity.startActivity(localIntent);
    }
  }

  protected void disableLogger()
  {
    g.a = true;
  }

  public boolean displayChanged(int paramInt, Surface paramSurface)
  {
    if (paramInt == 0)
      if (paramSurface == null)
        break label34;
    label34: for (boolean bool = true; ; bool = false)
    {
      this.c = bool;
      b(new Runnable()
      {
        public final void run()
        {
          if (UnityPlayer.e(UnityPlayer.this))
          {
            UnityPlayer.this.removeView(UnityPlayer.f(UnityPlayer.this));
            return;
          }
          UnityPlayer.this.addView(UnityPlayer.f(UnityPlayer.this));
        }
      });
      return b(paramInt, paramSurface);
    }
  }

  protected void executeGLThreadJobs()
  {
    while (true)
    {
      Runnable localRunnable = (Runnable)this.i.poll();
      if (localRunnable == null)
        break;
      localRunnable.run();
    }
  }

  protected int getCameraOrientation(int paramInt)
  {
    Camera.CameraInfo localCameraInfo = new Camera.CameraInfo();
    Camera.getCameraInfo(paramInt, localCameraInfo);
    return localCameraInfo.orientation;
  }

  protected int getNumCameras()
  {
    if (!l())
      return 0;
    return Camera.getNumberOfCameras();
  }

  public Bundle getSettings()
  {
    return this.o;
  }

  protected int getSplashMode()
  {
    return this.o.getInt("splash_mode");
  }

  public View getView()
  {
    return this;
  }

  protected void hideSoftInput()
  {
    // Byte code:
    //   0: new 56	com/unity3d/player/UnityPlayer$7
    //   3: dup
    //   4: aload_0
    //   5: invokespecial 993	com/unity3d/player/UnityPlayer$7:<init>	(Lcom/unity3d/player/UnityPlayer;)V
    //   8: astore_1
    //   9: getstatic 994	com/unity3d/player/i:d	Z
    //   12: ifeq +17 -> 29
    //   15: aload_0
    //   16: new 58	com/unity3d/player/UnityPlayer$8
    //   19: dup
    //   20: aload_0
    //   21: aload_1
    //   22: invokespecial 997	com/unity3d/player/UnityPlayer$8:<init>	(Lcom/unity3d/player/UnityPlayer;Ljava/lang/Runnable;)V
    //   25: invokespecial 999	com/unity3d/player/UnityPlayer:a	(Lcom/unity3d/player/UnityPlayer$c;)V
    //   28: return
    //   29: aload_0
    //   30: aload_1
    //   31: invokevirtual 957	com/unity3d/player/UnityPlayer:b	(Ljava/lang/Runnable;)V
    //   34: return
  }

  protected void hideVideoPlayer()
  {
    b(new Runnable()
    {
      public final void run()
      {
        if (UnityPlayer.u(UnityPlayer.this) == null)
          return;
        if (UnityPlayer.f(UnityPlayer.this).getParent() == null)
          UnityPlayer.this.addView(UnityPlayer.f(UnityPlayer.this));
        UnityPlayer.this.removeView(UnityPlayer.u(UnityPlayer.this));
        UnityPlayer.a(UnityPlayer.this, null);
        UnityPlayer.this.resume();
      }
    });
  }

  public void init(int paramInt, boolean paramBoolean)
  {
  }

  protected int[] initCamera(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    a locala = new a(paramInt1, paramInt2, paramInt3, paramInt4);
    try
    {
      locala.a(this);
      this.p.add(locala);
      Camera.Size localSize = locala.b();
      paramInt1 = localSize.width;
      paramInt2 = localSize.height;
      return new int[] { paramInt1, paramInt2 };
    }
    catch (Exception localException)
    {
      g.Log(6, "Unable to initialize camera: " + localException.getMessage());
      locala.c();
    }
    return null;
  }

  public boolean injectEvent(InputEvent paramInputEvent)
  {
    return nativeInjectEvent(paramInputEvent);
  }

  protected boolean installPresentationDisplay(int paramInt)
  {
    if (i.b)
      return i.g.a(this, this.k, paramInt);
    return false;
  }

  protected boolean isCameraFrontFacing(int paramInt)
  {
    Camera.CameraInfo localCameraInfo = new Camera.CameraInfo();
    Camera.getCameraInfo(paramInt, localCameraInfo);
    return localCameraInfo.facing == 1;
  }

  protected boolean isFinishing()
  {
    boolean bool2 = false;
    if (!this.n)
      if ((!(this.k instanceof Activity)) || (!((Activity)this.k).isFinishing()))
        break label47;
    label47: for (boolean bool1 = true; ; bool1 = false)
    {
      this.n = bool1;
      if (bool1)
        bool2 = true;
      return bool2;
    }
  }

  protected void kill()
  {
    daydreamQuitToOSLauncher();
    Process.killProcess(Process.myPid());
  }

  protected long loadGoogleVR(final boolean paramBoolean1, final boolean paramBoolean2, final boolean paramBoolean3)
  {
    final Semaphore localSemaphore = new Semaphore(0);
    final AtomicLong localAtomicLong = new AtomicLong(0L);
    final AtomicBoolean localAtomicBoolean1 = new AtomicBoolean(false);
    final AtomicBoolean localAtomicBoolean2 = new AtomicBoolean(true);
    b(new Runnable()
    {
      public final void run()
      {
        try
        {
          if (!UnityPlayer.p(UnityPlayer.this).get())
          {
            boolean bool = UnityPlayer.this.loadLibrary("gvr");
            UnityPlayer.p(UnityPlayer.this).set(bool);
          }
          if ((UnityPlayer.p(UnityPlayer.this).get()) && (UnityPlayer.q(UnityPlayer.this) == null))
          {
            Object localObject1 = UnityPlayer.class.getClassLoader();
            UnityPlayer.a(UnityPlayer.this, UnityPlayer.r(UnityPlayer.this));
            Object localObject2 = ((ClassLoader)localObject1).loadClass("com.google.vr.ndk.base.GvrLayout");
            Object localObject3 = ((Class)localObject2).getConstructor(new Class[] { Context.class });
            UnityPlayer.a(UnityPlayer.this, (FrameLayout)((Constructor)localObject3).newInstance(new Object[] { UnityPlayer.m(UnityPlayer.this) }));
            ((Class)localObject2).getMethod("setPresentationView", new Class[] { View.class }).invoke(UnityPlayer.q(UnityPlayer.this), new Object[] { UnityPlayer.s(UnityPlayer.this) });
            if (paramBoolean1)
            {
              g.Log(4, "Enabling Asynchronous Projection Mode.");
              localObject3 = (Boolean)((Class)localObject2).getMethod("setAsyncReprojectionEnabled", new Class[] { Boolean.TYPE }).invoke(UnityPlayer.q(UnityPlayer.this), new Object[] { Boolean.valueOf(true) });
              g.Log(4, "Asynchronous Projection enabled: " + localObject3);
              localAtomicBoolean2.set(((Boolean)localObject3).booleanValue());
              if ((localAtomicBoolean2.get()) && (paramBoolean2))
              {
                g.Log(4, "Enabling sustained performance");
                ((ClassLoader)localObject1).loadClass("com.google.vr.ndk.base.AndroidCompat").getMethod("setSustainedPerformanceMode", new Class[] { Activity.class, Boolean.TYPE }).invoke(null, new Object[] { UnityPlayer.m(UnityPlayer.this), Boolean.valueOf(true) });
                localAtomicBoolean1.set(true);
              }
            }
            if (localAtomicBoolean2.get())
            {
              localObject3 = ((ClassLoader)localObject1).loadClass("com.google.vr.ndk.base.GvrUiLayout");
              Object localObject4 = ((Class)localObject2).getMethod("getUiLayout", new Class[0]).invoke(UnityPlayer.q(UnityPlayer.this), new Object[0]);
              ((Class)localObject3).getMethod("setCloseButtonListener", new Class[] { Runnable.class }).invoke(localObject4, new Object[] { new Runnable()
              {
                public final void run()
                {
                  UnityPlayer.this.injectEvent(new KeyEvent(0, 4));
                  UnityPlayer.this.injectEvent(new KeyEvent(1, 4));
                }
              }
               });
              if (!UnityPlayer.t(UnityPlayer.this))
                ((Class)localObject3).getMethod("setTransitionViewEnabled", new Class[] { Boolean.TYPE }).invoke(localObject4, new Object[] { Boolean.valueOf(paramBoolean3) });
              localObject2 = ((Class)localObject2).getMethod("getGvrApi", new Class[0]).invoke(UnityPlayer.q(UnityPlayer.this), new Object[0]);
              localObject1 = (Long)((ClassLoader)localObject1).loadClass("com.google.vr.ndk.base.GvrApi").getMethod("getNativeGvrContext", new Class[0]).invoke(localObject2, new Object[0]);
              localAtomicLong.set(((Long)localObject1).longValue());
              if ((UnityPlayer.m(UnityPlayer.this) instanceof Activity))
                ((Activity)UnityPlayer.m(UnityPlayer.this)).getWindow().addFlags(128);
            }
          }
          localSemaphore.release();
          return;
        }
        catch (Exception localException)
        {
          while (true)
            localException.printStackTrace();
        }
      }
    });
    try
    {
      if (!localSemaphore.tryAcquire(4L, TimeUnit.SECONDS))
        g.Log(5, "Timeout waiting for GoogleVR to initialize");
      label84: if (localAtomicBoolean2.get())
      {
        g.Log(4, "GoogleVR Initialization Succeeded.");
        this.w = localAtomicBoolean1.get();
      }
      while (true)
      {
        return localAtomicLong.longValue();
        g.Log(5, "GoogleVR Initialization Failed.");
        this.w = false;
        this.u = null;
        this.v = null;
      }
    }
    catch (InterruptedException localInterruptedException)
    {
      break label84;
    }
  }

  protected boolean loadLibrary(String paramString)
  {
    return loadLibraryStatic(paramString);
  }

  public void onCameraFrame(final a parama, final byte[] paramArrayOfByte)
  {
    a(new c(parama.a(), paramArrayOfByte)
    {
      public final void a()
      {
        UnityPlayer.a(UnityPlayer.this, this.a, paramArrayOfByte, this.c.width, this.c.height);
        parama.a(paramArrayOfByte);
      }
    });
  }

  public boolean onGenericMotionEvent(MotionEvent paramMotionEvent)
  {
    g.Log(4, "Handling onGenericMotionEvent");
    return injectEvent(paramMotionEvent);
  }

  public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent)
  {
    g.Log(4, "Handling onKeyDown");
    return injectEvent(paramKeyEvent);
  }

  public boolean onKeyLongPress(int paramInt, KeyEvent paramKeyEvent)
  {
    g.Log(4, "Handling onKeyLongPress");
    return injectEvent(paramKeyEvent);
  }

  public boolean onKeyMultiple(int paramInt1, int paramInt2, KeyEvent paramKeyEvent)
  {
    g.Log(4, "Handling onKeyMultiple");
    return injectEvent(paramKeyEvent);
  }

  public boolean onKeyUp(int paramInt, KeyEvent paramKeyEvent)
  {
    g.Log(4, "Handling onKeyUp");
    return injectEvent(paramKeyEvent);
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    g.Log(4, "Handling onTouchEvent");
    return injectEvent(paramMotionEvent);
  }

  public void pause()
  {
    if (this.q != null)
    {
      this.q.onPause();
      return;
    }
    j();
    f();
  }

  public void quit()
  {
    daydreamQuitToOSLauncher();
    this.n = true;
    if (!this.h.e())
      pause();
    this.a.a();
    try
    {
      this.a.join(4000L);
      if (this.j != null)
        this.k.unregisterReceiver(this.j);
      this.j = null;
      if (l.c())
        removeAllViews();
      if (i.b)
        i.g.a(this.k);
      kill();
      i();
      return;
    }
    catch (InterruptedException localInterruptedException)
    {
      while (true)
        this.a.interrupt();
    }
  }

  protected void reportSoftInputStr(final String paramString, final int paramInt, final boolean paramBoolean)
  {
    if (paramInt == 1)
      hideSoftInput();
    a(new c(paramBoolean, paramString)
    {
      public final void a()
      {
        if (paramBoolean)
          UnityPlayer.n(UnityPlayer.this);
        while (true)
        {
          if (paramInt == 1)
            UnityPlayer.o(UnityPlayer.this);
          return;
          if (paramString != null)
            UnityPlayer.a(UnityPlayer.this, paramString);
        }
      }
    });
  }

  public void resume()
  {
    if (i.a)
      i.f.b(this);
    this.h.b(false);
    h();
    k();
  }

  protected void setFullscreen(final boolean paramBoolean)
  {
    this.d = paramBoolean;
    if (i.a)
      b(new Runnable()
      {
        public final void run()
        {
          i.f.a(UnityPlayer.this, paramBoolean);
        }
      });
  }

  protected boolean setGoogleVREnabled(final boolean paramBoolean)
  {
    boolean bool2 = false;
    final Semaphore localSemaphore = new Semaphore(0);
    b(new Runnable()
    {
      public final void run()
      {
        try
        {
          Object localObject2 = UnityPlayer.class.getClassLoader();
          Class localClass = ((ClassLoader)localObject2).loadClass("com.google.vr.ndk.base.GvrLayout");
          localObject2 = ((ClassLoader)localObject2).loadClass("com.google.vr.ndk.base.AndroidCompat").getMethod("setVrModeEnabled", new Class[] { Activity.class, Boolean.TYPE });
          if (paramBoolean)
            if (UnityPlayer.q(UnityPlayer.this).getParent() == null)
            {
              UnityPlayer.this.addView(UnityPlayer.q(UnityPlayer.this));
              UnityPlayer.this.removeView(UnityPlayer.f(UnityPlayer.this));
              localClass.getMethod("onResume", new Class[0]).invoke(UnityPlayer.q(UnityPlayer.this), new Object[0]);
              ((Method)localObject2).invoke(null, new Object[] { UnityPlayer.m(UnityPlayer.this), Boolean.valueOf(true) });
              UnityPlayer.b(UnityPlayer.this, true);
              UnityPlayer.c(UnityPlayer.this, true);
            }
          while (true)
          {
            return;
            UnityPlayer.b(UnityPlayer.this, false);
            UnityPlayer.c(UnityPlayer.this, false);
            if ((UnityPlayer.q(UnityPlayer.this) != null) && (UnityPlayer.q(UnityPlayer.this).getParent() != null))
            {
              ((Method)localObject2).invoke(null, new Object[] { UnityPlayer.m(UnityPlayer.this), Boolean.valueOf(false) });
              if (UnityPlayer.f(UnityPlayer.this).getParent() == null)
              {
                UnityPlayer.this.addView(UnityPlayer.f(UnityPlayer.this));
                UnityPlayer.this.removeView(UnityPlayer.q(UnityPlayer.this));
              }
            }
          }
        }
        catch (Exception localException)
        {
          localException.printStackTrace();
          UnityPlayer.b(UnityPlayer.this, false);
          return;
        }
        finally
        {
          localSemaphore.release();
        }
      }
    });
    try
    {
      if (!localSemaphore.tryAcquire(4L, TimeUnit.SECONDS))
        g.Log(5, "Timeout waiting for video");
      label48: boolean bool1 = bool2;
      if (this.w)
      {
        bool1 = bool2;
        if (paramBoolean)
          bool1 = true;
      }
      return bool1;
    }
    catch (InterruptedException localInterruptedException)
    {
      break label48;
    }
  }

  protected void setSoftInputStr(final String paramString)
  {
    b(new Runnable()
    {
      public final void run()
      {
        if ((UnityPlayer.this.b != null) && (paramString != null))
          UnityPlayer.this.b.a(paramString);
      }
    });
  }

  protected void showSoftInput(final String paramString1, final int paramInt, final boolean paramBoolean1, final boolean paramBoolean2, final boolean paramBoolean3, final boolean paramBoolean4, final String paramString2)
  {
    b(new Runnable()
    {
      public final void run()
      {
        UnityPlayer.this.b = new j(UnityPlayer.m(UnityPlayer.this), jdField_this, paramString1, paramInt, paramBoolean1, paramBoolean2, paramBoolean3, paramString2);
        UnityPlayer.this.b.show();
      }
    });
  }

  protected boolean showVideoPlayer(final String paramString, final int paramInt1, final int paramInt2, final int paramInt3, final boolean paramBoolean, final int paramInt4, final int paramInt5)
  {
    final Semaphore localSemaphore = new Semaphore(0);
    final AtomicInteger localAtomicInteger = new AtomicInteger(-1);
    b(new Runnable()
    {
      public final void run()
      {
        if (UnityPlayer.u(UnityPlayer.this) != null)
        {
          g.Log(5, "Video already playing");
          localAtomicInteger.set(2);
          localSemaphore.release();
          return;
        }
        UnityPlayer.a(UnityPlayer.this, new m(UnityPlayer.this, UnityPlayer.m(UnityPlayer.this), paramString, paramInt1, paramInt2, paramInt3, paramBoolean, paramInt4, paramInt5, new m.a()
        {
          public final void a(int paramAnonymous2Int)
          {
            UnityPlayer.16.this.a.set(paramAnonymous2Int);
            if (paramAnonymous2Int != 0)
              UnityPlayer.16.this.b.release();
          }
        }));
        UnityPlayer.this.addView(UnityPlayer.u(UnityPlayer.this));
      }
    });
    paramBoolean = false;
    try
    {
      if (!localSemaphore.tryAcquire(4L, TimeUnit.SECONDS))
        g.Log(5, "Timeout waiting for video");
      while (true)
      {
        label71: if (paramBoolean)
          b(new Runnable()
          {
            public final void run()
            {
              UnityPlayer.v(UnityPlayer.this);
              UnityPlayer.u(UnityPlayer.this).requestFocus();
              UnityPlayer.this.removeView(UnityPlayer.f(UnityPlayer.this));
            }
          });
        return paramBoolean;
        paramInt1 = localAtomicInteger.get();
        if (paramInt1 != 2)
          paramBoolean = true;
        else
          paramBoolean = false;
      }
    }
    catch (InterruptedException paramString)
    {
      break label71;
    }
  }

  protected void startActivityIndicator()
  {
    b(this.s);
  }

  protected void stopActivityIndicator()
  {
    b(this.t);
  }

  protected void unloadGoogleVR()
  {
    final Semaphore localSemaphore = new Semaphore(0);
    b(new Runnable()
    {
      public final void run()
      {
        try
        {
          if (UnityPlayer.q(UnityPlayer.this) != null)
          {
            Class localClass = UnityPlayer.class.getClassLoader().loadClass("com.google.vr.ndk.base.GvrLayout");
            if (UnityPlayer.f(UnityPlayer.this).getParent() == null)
              UnityPlayer.this.removeView(UnityPlayer.q(UnityPlayer.this));
            localClass.getMethod("shutdown", new Class[0]).invoke(UnityPlayer.q(UnityPlayer.this), new Object[0]);
            UnityPlayer.a(UnityPlayer.this, null);
            if (UnityPlayer.f(UnityPlayer.this).getParent() == null)
              UnityPlayer.this.addView(UnityPlayer.f(UnityPlayer.this));
          }
          return;
        }
        catch (Exception localException)
        {
          localException.printStackTrace();
          return;
        }
        finally
        {
          localSemaphore.release();
        }
      }
    });
    try
    {
      if (!localSemaphore.tryAcquire(4L, TimeUnit.SECONDS))
        g.Log(5, "Timeout waiting for GoogleVR to unload");
      return;
    }
    catch (InterruptedException localInterruptedException)
    {
    }
  }

  public void windowFocusChanged(final boolean paramBoolean)
  {
    this.h.a(paramBoolean);
    if ((paramBoolean) && (this.b != null))
      reportSoftInputStr(null, 1, false);
    if ((i.a) && (paramBoolean))
      i.f.b(this);
    c(new Runnable()
    {
      public final void run()
      {
        UnityPlayer.a(UnityPlayer.this, paramBoolean);
      }
    });
    this.a.a(paramBoolean);
    h();
  }

  static enum a
  {
  }

  private final class b extends Thread
  {
    Handler a;
    boolean b = false;

    private b()
    {
    }

    private void a(UnityPlayer.a parama)
    {
      Message.obtain(this.a, 2269, parama).sendToTarget();
    }

    public final void a()
    {
      a(UnityPlayer.a.c);
    }

    public final void a(boolean paramBoolean)
    {
      if (paramBoolean);
      for (UnityPlayer.a locala = UnityPlayer.a.d; ; locala = UnityPlayer.a.e)
      {
        a(locala);
        return;
      }
    }

    public final void b()
    {
      a(UnityPlayer.a.b);
    }

    public final void c()
    {
      a(UnityPlayer.a.a);
    }

    public final void run()
    {
      setName("UnityMain");
      Looper.prepare();
      this.a = new Handler(new Handler.Callback()
      {
        public final boolean handleMessage(Message paramAnonymousMessage)
        {
          if (paramAnonymousMessage.what != 2269)
            return false;
          paramAnonymousMessage = (UnityPlayer.a)paramAnonymousMessage.obj;
          if (paramAnonymousMessage == UnityPlayer.a.c)
            Looper.myLooper().quit();
          while (true)
          {
            if (UnityPlayer.b.this.b)
              Message.obtain(UnityPlayer.b.this.a, 2269, UnityPlayer.a.f).sendToTarget();
            return true;
            if (paramAnonymousMessage == UnityPlayer.a.b)
            {
              UnityPlayer.b.this.b = true;
            }
            else if (paramAnonymousMessage == UnityPlayer.a.a)
            {
              UnityPlayer.b.this.b = false;
              UnityPlayer.this.executeGLThreadJobs();
            }
            else if (paramAnonymousMessage == UnityPlayer.a.e)
            {
              if (!UnityPlayer.b.this.b)
                UnityPlayer.this.executeGLThreadJobs();
            }
            else if (paramAnonymousMessage == UnityPlayer.a.f)
            {
              UnityPlayer.this.executeGLThreadJobs();
              if ((!UnityPlayer.this.isFinishing()) && (!UnityPlayer.a(UnityPlayer.this)))
                UnityPlayer.b(UnityPlayer.this);
            }
          }
        }
      });
      Looper.loop();
    }
  }

  private abstract class c
    implements Runnable
  {
    private c()
    {
    }

    public abstract void a();

    public final void run()
    {
      if (!UnityPlayer.this.isFinishing())
        a();
    }
  }
}

/* Location:           /disk1/tools/decompile/dex2jar/jar_src/classes.jar
 * Qualified Name:     com.unity3d.player.UnityPlayer
 * JD-Core Version:    0.6.2
 */