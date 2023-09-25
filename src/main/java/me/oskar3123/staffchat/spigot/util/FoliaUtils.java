package me.oskar3123.staffchat.spigot.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class FoliaUtils {

  private static final String FOLIA_PROBE_CLASS =
      "io.papermc.paper.threadedregions.RegionizedServer";
  private static final boolean IS_FOLIA;

  static {
    boolean isFolia;
    try {
      Class.forName(FOLIA_PROBE_CLASS);
      isFolia = true;
    } catch (ClassNotFoundException e) {
      isFolia = false;
    }
    IS_FOLIA = isFolia;
  }

  private FoliaUtils() {}

  public static boolean isFolia() {
    return IS_FOLIA;
  }

  public static void execute(Plugin plugin, Runnable runnable) {
    if (isFolia()) {
      try {
        Method getGlobalRegionSchedulerMethod =
            Class.forName("org.bukkit.Bukkit").getDeclaredMethod("getGlobalRegionScheduler");
        Method executeMethod =
            Class.forName("io.papermc.paper.threadedregions.scheduler.GlobalRegionScheduler")
                .getDeclaredMethod("execute", Plugin.class, Runnable.class);
        Object globalRegionScheduler = getGlobalRegionSchedulerMethod.invoke(null);
        executeMethod.invoke(globalRegionScheduler, plugin, runnable);
      } catch (ClassNotFoundException
          | IllegalAccessException
          | NoSuchMethodException
          | InvocationTargetException e) {
        // Should not happen, rethrow as a runtime exception if it does
        throw new RuntimeException(e);
      }
    } else {
      Bukkit.getScheduler().runTask(plugin, runnable);
    }
  }
}
