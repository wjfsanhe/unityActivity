package org.fmod;

import android.media.AudioRecord;
import android.util.Log;
import java.nio.ByteBuffer;

final class a
  implements Runnable
{
  private final FMODAudioDevice a;
  private final ByteBuffer b;
  private final int c;
  private final int d;
  private final int e;
  private volatile Thread f;
  private volatile boolean g;
  private AudioRecord h;
  private boolean i;

  a(FMODAudioDevice paramFMODAudioDevice, int paramInt1, int paramInt2)
  {
    this.a = paramFMODAudioDevice;
    this.c = paramInt1;
    this.d = paramInt2;
    this.e = 2;
    this.b = ByteBuffer.allocateDirect(AudioRecord.getMinBufferSize(paramInt1, paramInt2, 2));
  }

  private void d()
  {
    if (this.h != null)
    {
      if (this.h.getState() == 1)
        this.h.stop();
      this.h.release();
      this.h = null;
    }
    this.b.position(0);
    this.i = false;
  }

  public final int a()
  {
    return this.b.capacity();
  }

  public final void b()
  {
    if (this.f != null)
      c();
    this.g = true;
    this.f = new Thread(this);
    this.f.start();
  }

  public final void c()
  {
    while (this.f != null)
    {
      this.g = false;
      try
      {
        this.f.join();
        this.f = null;
      }
      catch (InterruptedException localInterruptedException)
      {
      }
    }
  }

  public final void run()
  {
    int j = 3;
    label72: label220: 
    while (true)
    {
      if (this.g)
      {
        int k = j;
        boolean bool;
        if (!this.i)
        {
          k = j;
          if (j > 0)
          {
            d();
            this.h = new AudioRecord(1, this.c, this.d, this.e, this.b.capacity());
            if (this.h.getState() == 1)
            {
              bool = true;
              this.i = bool;
              if (!this.i)
                break label169;
              this.b.position(0);
              this.h.startRecording();
            }
          }
        }
        for (j = 3; ; j = k)
        {
          if ((!this.i) || (this.h.getRecordingState() != 3))
            break label220;
          k = this.h.read(this.b, this.b.capacity());
          this.a.fmodProcessMicData(this.b, k);
          this.b.position(0);
          break;
          bool = false;
          break label72;
          Log.e("FMOD", "AudioRecord failed to initialize (status " + this.h.getState() + ")");
          k = j - 1;
          d();
        }
      }
      d();
      return;
    }
  }
}

/* Location:           /disk1/tools/decompile/dex2jar/jar_src/classes.jar
 * Qualified Name:     org.fmod.a
 * JD-Core Version:    0.6.2
 */