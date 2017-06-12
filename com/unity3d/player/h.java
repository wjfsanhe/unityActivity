package com.unity3d.player;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageItemInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PermissionInfo;
import android.os.Bundle;
import java.util.LinkedList;
import java.util.List;

public final class h
  implements e
{
  private static boolean a(PackageItemInfo paramPackageItemInfo)
  {
    try
    {
      boolean bool = paramPackageItemInfo.metaData.getBoolean("unityplayer.SkipPermissionsDialog");
      return bool;
    }
    catch (Exception paramPackageItemInfo)
    {
    }
    return false;
  }

  public final void a(final Activity paramActivity, final Runnable paramRunnable)
  {
    int i = 0;
    if (paramActivity == null)
      return;
    PackageManager localPackageManager = paramActivity.getPackageManager();
    Object localObject;
    try
    {
      ActivityInfo localActivityInfo = localPackageManager.getActivityInfo(paramActivity.getComponentName(), 128);
      localObject = localPackageManager.getApplicationInfo(paramActivity.getPackageName(), 128);
      if ((a(localActivityInfo)) || (a((PackageItemInfo)localObject)))
      {
        paramRunnable.run();
        return;
      }
    }
    catch (Exception localException)
    {
    }
    while (true)
    {
      final LinkedList localLinkedList;
      try
      {
        localObject = localPackageManager.getPackageInfo(paramActivity.getPackageName(), 4096);
        if (((PackageInfo)localObject).requestedPermissions == null)
          ((PackageInfo)localObject).requestedPermissions = new String[0];
        localLinkedList = new LinkedList();
        localObject = ((PackageInfo)localObject).requestedPermissions;
        int j = localObject.length;
        if (i < j)
        {
          String str = localObject[i];
          try
          {
            if ((localPackageManager.getPermissionInfo(str, 128).protectionLevel != 1) || (paramActivity.checkCallingOrSelfPermission(str) == 0))
              break label308;
            localLinkedList.add(str);
          }
          catch (PackageManager.NameNotFoundException localNameNotFoundException)
          {
            g.Log(5, "Failed to get permission info for " + str + ", manifest likely missing custom permission declaration");
            g.Log(5, "Permission " + str + " ignored");
          }
        }
      }
      catch (Exception paramActivity)
      {
        g.Log(6, "Unable to query for permission: " + paramActivity.getMessage());
        return;
      }
      if (localLinkedList.isEmpty())
      {
        paramRunnable.run();
        return;
      }
      paramActivity = paramActivity.getFragmentManager();
      paramRunnable = new Fragment()
      {
        public final void onRequestPermissionsResult(int paramAnonymousInt, String[] paramAnonymousArrayOfString, int[] paramAnonymousArrayOfInt)
        {
          if (paramAnonymousInt != 15881)
            return;
          paramAnonymousInt = 0;
          if ((paramAnonymousInt < paramAnonymousArrayOfString.length) && (paramAnonymousInt < paramAnonymousArrayOfInt.length))
          {
            StringBuilder localStringBuilder = new StringBuilder().append(paramAnonymousArrayOfString[paramAnonymousInt]);
            if (paramAnonymousArrayOfInt[paramAnonymousInt] == 0);
            for (String str = " granted"; ; str = " denied")
            {
              g.Log(4, str);
              paramAnonymousInt += 1;
              break;
            }
          }
          paramAnonymousArrayOfString = paramActivity.beginTransaction();
          paramAnonymousArrayOfString.remove(this);
          paramAnonymousArrayOfString.commit();
          paramRunnable.run();
        }

        public final void onStart()
        {
          super.onStart();
          requestPermissions((String[])localLinkedList.toArray(new String[0]), 15881);
        }
      };
      paramActivity = paramActivity.beginTransaction();
      paramActivity.add(0, paramRunnable);
      paramActivity.commit();
      return;
      label308: i += 1;
    }
  }
}

/* Location:           /disk1/tools/decompile/dex2jar/jar_src/classes.jar
 * Qualified Name:     com.unity3d.player.h
 * JD-Core Version:    0.6.2
 */