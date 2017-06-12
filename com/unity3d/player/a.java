package com.unity3d.player;

import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.Size;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

final class a
{
  Camera a;
  Camera.Parameters b;
  Camera.Size c;
  int d;
  int[] e;
  private final Object[] f = new Object[0];
  private final int g;
  private final int h;
  private final int i;
  private final int j;

  public a(int paramInt1, int paramInt2, int paramInt3, int paramInt4)
  {
    this.g = paramInt1;
    this.h = a(paramInt2, 640);
    this.i = a(paramInt3, 480);
    this.j = a(paramInt4, 24);
  }

  private static final int a(int paramInt1, int paramInt2)
  {
    if (paramInt1 != 0)
      return paramInt1;
    return paramInt2;
  }

  private static void a(Camera.Parameters paramParameters)
  {
    if (paramParameters.getSupportedColorEffects() != null)
      paramParameters.setColorEffect("none");
    if (paramParameters.getSupportedFocusModes().contains("continuous-video"))
      paramParameters.setFocusMode("continuous-video");
  }

  private void b(final a parama)
  {
    synchronized (this.f)
    {
      this.a = Camera.open(this.g);
      this.b = this.a.getParameters();
      this.c = f();
      this.e = e();
      this.d = d();
      a(this.b);
      this.b.setPreviewSize(this.c.width, this.c.height);
      this.b.setPreviewFpsRange(this.e[0], this.e[1]);
      this.a.setParameters(this.b);
      parama = new Camera.PreviewCallback()
      {
        long a = 0L;

        public final void onPreviewFrame(byte[] paramAnonymousArrayOfByte, Camera paramAnonymousCamera)
        {
          if (a.this.a != paramAnonymousCamera)
            return;
          parama.onCameraFrame(a.this, paramAnonymousArrayOfByte);
        }
      };
      int k = this.c.width * this.c.height * this.d / 8 + 4096;
      this.a.addCallbackBuffer(new byte[k]);
      this.a.addCallbackBuffer(new byte[k]);
      this.a.setPreviewCallbackWithBuffer(parama);
      return;
    }
  }

  private final int d()
  {
    this.b.setPreviewFormat(17);
    return ImageFormat.getBitsPerPixel(17);
  }

  private final int[] e()
  {
    double d3 = this.j * 1000;
    Object localObject2 = this.b.getSupportedPreviewFpsRange();
    Object localObject1 = localObject2;
    if (localObject2 == null)
      localObject1 = new ArrayList();
    localObject2 = new int[2];
    localObject2[0] = (this.j * 1000);
    localObject2[1] = (this.j * 1000);
    double d1 = 1.7976931348623157E+308D;
    Iterator localIterator = ((List)localObject1).iterator();
    localObject1 = localObject2;
    if (localIterator.hasNext())
    {
      localObject2 = (int[])localIterator.next();
      double d2 = Math.abs(Math.log(d3 / localObject2[0])) + Math.abs(Math.log(d3 / localObject2[1]));
      if (d2 >= d1)
        break label154;
      localObject1 = localObject2;
      d1 = d2;
    }
    label154: 
    while (true)
    {
      break;
      return localObject1;
    }
  }

  private final Camera.Size f()
  {
    double d3 = this.h;
    double d4 = this.i;
    Object localObject2 = this.b.getSupportedPreviewSizes();
    Object localObject1 = null;
    double d1 = 1.7976931348623157E+308D;
    Iterator localIterator = ((List)localObject2).iterator();
    if (localIterator.hasNext())
    {
      localObject2 = (Camera.Size)localIterator.next();
      double d2 = Math.abs(Math.log(d3 / ((Camera.Size)localObject2).width)) + Math.abs(Math.log(d4 / ((Camera.Size)localObject2).height));
      if (d2 >= d1)
        break label111;
      localObject1 = localObject2;
      d1 = d2;
    }
    label111: 
    while (true)
    {
      break;
      return localObject1;
    }
  }

  public final int a()
  {
    return this.g;
  }

  public final void a(a parama)
  {
    synchronized (this.f)
    {
      if (this.a == null)
        b(parama);
      if ((i.a) && (i.f.a(this.a)))
      {
        this.a.startPreview();
        return;
      }
      return;
    }
  }

  public final void a(byte[] paramArrayOfByte)
  {
    synchronized (this.f)
    {
      if (this.a != null)
        this.a.addCallbackBuffer(paramArrayOfByte);
      return;
    }
  }

  public final Camera.Size b()
  {
    return this.c;
  }

  public final void c()
  {
    synchronized (this.f)
    {
      if (this.a != null)
      {
        this.a.setPreviewCallbackWithBuffer(null);
        this.a.stopPreview();
        this.a.release();
        this.a = null;
      }
      return;
    }
  }

  static abstract interface a
  {
    public abstract void onCameraFrame(a parama, byte[] paramArrayOfByte);
  }
}

/* Location:           /disk1/tools/decompile/dex2jar/jar_src/classes.jar
 * Qualified Name:     com.unity3d.player.a
 * JD-Core Version:    0.6.2
 */