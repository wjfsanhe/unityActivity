package com.unity3d.player;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

class WWW extends Thread
{
  private int a;
  private int b;
  private String c;
  private byte[] d;
  private Map e;

  WWW(int paramInt, String paramString, byte[] paramArrayOfByte, Map paramMap)
  {
    this.b = paramInt;
    this.c = paramString;
    this.d = paramArrayOfByte;
    this.e = paramMap;
    this.a = 0;
    start();
  }

  private static native void doneCallback(int paramInt);

  private static native void errorCallback(int paramInt, String paramString);

  private static native boolean headerCallback(int paramInt, String paramString);

  private static native void progressCallback(int paramInt1, float paramFloat1, float paramFloat2, double paramDouble, int paramInt2);

  private static native boolean readCallback(int paramInt1, byte[] paramArrayOfByte, int paramInt2);

  protected boolean headerCallback(String paramString1, String paramString2)
  {
    StringBuilder localStringBuilder = new StringBuilder();
    localStringBuilder.append(paramString1);
    localStringBuilder.append(": ");
    localStringBuilder.append(paramString2);
    localStringBuilder.append("\n\r");
    return headerCallback(this.b, localStringBuilder.toString());
  }

  protected boolean headerCallback(Map paramMap)
  {
    if ((paramMap == null) || (paramMap.size() == 0))
      return false;
    StringBuilder localStringBuilder = new StringBuilder();
    paramMap = paramMap.entrySet().iterator();
    while (paramMap.hasNext())
    {
      Object localObject1 = (Map.Entry)paramMap.next();
      Object localObject2 = ((List)((Map.Entry)localObject1).getValue()).iterator();
      while (((Iterator)localObject2).hasNext())
      {
        String str = (String)((Iterator)localObject2).next();
        localStringBuilder.append((String)((Map.Entry)localObject1).getKey());
        localStringBuilder.append(": ");
        localStringBuilder.append(str);
        localStringBuilder.append("\r\n");
      }
      if (((Map.Entry)localObject1).getKey() == null)
      {
        localObject1 = ((List)((Map.Entry)localObject1).getValue()).iterator();
        while (((Iterator)localObject1).hasNext())
        {
          localObject2 = (String)((Iterator)localObject1).next();
          localStringBuilder.append("Status: ");
          localStringBuilder.append((String)localObject2);
          localStringBuilder.append("\r\n");
        }
      }
    }
    return headerCallback(this.b, localStringBuilder.toString());
  }

  protected void progressCallback(int paramInt1, int paramInt2, int paramInt3, int paramInt4, long paramLong1, long paramLong2)
  {
    float f2;
    float f1;
    if (paramInt4 > 0)
    {
      f2 = paramInt3 / paramInt4;
      f1 = 1.0F;
      paramInt1 = Math.max(paramInt4 - paramInt3, 0);
      d1 = 1000.0D * paramInt3 / Math.max(paramLong1 - paramLong2, 0.1D);
      double d2 = paramInt1 / d1;
      if (!Double.isInfinite(d2))
      {
        d1 = d2;
        if (!Double.isNaN(d2))
          break label76;
      }
    }
    for (double d1 = 0.0D; ; d1 = 0.0D)
    {
      label76: progressCallback(this.b, f1, f2, d1, paramInt4);
      do
        return;
      while (paramInt2 <= 0);
      f2 = 0.0F;
      f1 = paramInt1 / paramInt2;
    }
  }

  protected boolean readCallback(byte[] paramArrayOfByte, int paramInt)
  {
    return readCallback(this.b, paramArrayOfByte, paramInt);
  }

  public void run()
  {
    try
    {
      runSafe();
      return;
    }
    catch (Throwable localThrowable)
    {
      errorCallback(this.b, "Error: " + localThrowable.toString());
    }
  }

  public void runSafe()
  {
    int i = this.a + 1;
    this.a = i;
    if (i > 5)
    {
      errorCallback(this.b, "Too many redirects");
      return;
    }
    URLConnection localURLConnection;
    try
    {
      URL localURL = new URL(this.c);
      localURLConnection = localURL.openConnection();
      if ((localURL.getProtocol().equalsIgnoreCase("file")) && (localURL.getHost() != null) && (localURL.getHost().length() != 0))
      {
        errorCallback(this.b, localURL.getHost() + localURL.getFile() + " is not an absolute path!");
        return;
      }
    }
    catch (MalformedURLException localMalformedURLException)
    {
      errorCallback(this.b, localMalformedURLException.toString());
      return;
    }
    catch (IOException localIOException1)
    {
      errorCallback(this.b, localIOException1.toString());
      return;
    }
    Object localObject3;
    if (this.e != null)
    {
      localObject2 = this.e.entrySet().iterator();
      while (((Iterator)localObject2).hasNext())
      {
        localObject3 = (Map.Entry)((Iterator)localObject2).next();
        localURLConnection.addRequestProperty((String)((Map.Entry)localObject3).getKey(), (String)((Map.Entry)localObject3).getValue());
      }
    }
    int j;
    if (this.d != null)
    {
      localURLConnection.setDoOutput(true);
      try
      {
        localObject2 = localURLConnection.getOutputStream();
        i = 0;
        while (i < this.d.length)
        {
          j = Math.min(1428, this.d.length - i);
          ((OutputStream)localObject2).write(this.d, i, j);
          i += j;
          progressCallback(i, this.d.length, 0, 0, 0L, 0L);
        }
      }
      catch (Exception localException1)
      {
        errorCallback(this.b, localException1.toString());
        return;
      }
    }
    if ((localURLConnection instanceof HttpURLConnection))
    {
      localObject2 = (HttpURLConnection)localURLConnection;
      try
      {
        i = ((HttpURLConnection)localObject2).getResponseCode();
        localObject3 = ((HttpURLConnection)localObject2).getHeaderFields();
        if ((localObject3 != null) && ((i == 301) || (i == 302)))
        {
          localObject3 = (List)((Map)localObject3).get("Location");
          if ((localObject3 != null) && (!((List)localObject3).isEmpty()))
          {
            ((HttpURLConnection)localObject2).disconnect();
            this.c = ((String)((List)localObject3).get(0));
            run();
            return;
          }
        }
      }
      catch (IOException localIOException2)
      {
        errorCallback(this.b, localIOException2.toString());
        return;
      }
    }
    Object localObject2 = localURLConnection.getHeaderFields();
    boolean bool2 = headerCallback((Map)localObject2);
    boolean bool1;
    if (localObject2 != null)
    {
      bool1 = bool2;
      if (((Map)localObject2).containsKey("content-length"));
    }
    else
    {
      bool1 = bool2;
      if (localURLConnection.getContentLength() != -1)
      {
        if ((!bool2) && (!headerCallback("content-length", String.valueOf(localURLConnection.getContentLength()))))
          break label594;
        bool1 = true;
      }
    }
    if (localObject2 != null)
    {
      bool2 = bool1;
      if (((Map)localObject2).containsKey("content-type"));
    }
    else
    {
      bool2 = bool1;
      if (localURLConnection.getContentType() != null)
        if ((!bool1) && (!headerCallback("content-type", localURLConnection.getContentType())))
          break label600;
    }
    label594: label600: for (bool2 = true; ; bool2 = false)
    {
      if (!bool2)
        break label606;
      errorCallback(this.b, this.c + " aborted");
      return;
      bool1 = false;
      break;
    }
    label606: int n;
    if (localURLConnection.getContentLength() > 0)
    {
      j = localURLConnection.getContentLength();
      if ((!localIOException2.getProtocol().equalsIgnoreCase("file")) && (!localIOException2.getProtocol().equalsIgnoreCase("jar")))
        break label909;
      if (j != 0)
        break label806;
      i = 32768;
      label655: n = 0;
    }
    while (true)
    {
      long l;
      try
      {
        l = System.currentTimeMillis();
        localObject3 = new byte[i];
        if (!(localURLConnection instanceof HttpURLConnection))
          break label898;
        localObject2 = (HttpURLConnection)localURLConnection;
        InputStream localInputStream = ((HttpURLConnection)localObject2).getErrorStream();
        localObject2 = ((HttpURLConnection)localObject2).getResponseCode() + ": " + ((HttpURLConnection)localObject2).getResponseMessage();
        if (localInputStream != null)
          break label893;
        localInputStream = localURLConnection.getInputStream();
        i = 0;
        break label916;
        if (k == -1)
          break label860;
        if (!readCallback((byte[])localObject3, k))
          break label817;
        errorCallback(this.b, this.c + " aborted");
        return;
      }
      catch (Exception localException2)
      {
        errorCallback(this.b, localException2.toString());
        return;
      }
      j = 0;
      break;
      label806: i = Math.min(j, 32768);
      break label655;
      label817: int m = n;
      if (i == 0)
      {
        m = n + k;
        progressCallback(0, 0, m, j, System.currentTimeMillis(), l);
      }
      int k = localException2.read((byte[])localObject3);
      n = m;
      continue;
      label860: if (i != 0)
        errorCallback(this.b, (String)localObject2);
      progressCallback(0, 0, n, n, 0L, 0L);
      doneCallback(this.b);
      return;
      label893: i = 1;
      break label916;
      label898: localObject2 = "";
      Object localObject1 = null;
      continue;
      label909: i = 1428;
      break label655;
      label916: k = 0;
    }
  }
}

/* Location:           /disk1/tools/decompile/dex2jar/jar_src/classes.jar
 * Qualified Name:     com.unity3d.player.WWW
 * JD-Core Version:    0.6.2
 */