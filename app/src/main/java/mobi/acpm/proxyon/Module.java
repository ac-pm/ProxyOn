package mobi.acpm.proxyon;

import java.net.URI;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

public class Module implements IXposedHookLoadPackage {

    public static final String PREFS = "ProxyOnSettings";
    private static XSharedPreferences sPrefs;

    public static void loadPrefs() {
        sPrefs = new XSharedPreferences(Module.class.getPackage().getName(), PREFS);
        sPrefs.makeWorldReadable();
    }

    @Override
    public void handleLoadPackage(final LoadPackageParam loadPackageParam) throws Throwable {

        loadPrefs();

        if (!loadPackageParam.packageName.equals(sPrefs.getString("package", "")))
            return;

        findAndHookMethod("java.net.ProxySelectorImpl", loadPackageParam.classLoader, "select", URI.class, new XC_MethodHook() {

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                if(sPrefs.getBoolean("switch", false)) {

                    System.setProperty("proxyHost", sPrefs.getString("host", null));
                    System.setProperty("proxyPort", sPrefs.getString("port", null));

                    System.setProperty("http.proxyHost", sPrefs.getString("host", null));
                    System.setProperty("http.proxyPort", sPrefs.getString("port", null));

                    System.setProperty("https.proxyHost", sPrefs.getString("host", null));
                    System.setProperty("https.proxyPort", sPrefs.getString("port", null));

                    System.setProperty("socksProxyHost", sPrefs.getString("host", null));
                    System.setProperty("socksProxyPort", sPrefs.getString("port", null));
                }
            }
        });

        findAndHookMethod("java.net.ProxySelectorImpl", loadPackageParam.classLoader, "isNonProxyHost", String.class, String.class, new XC_MethodHook() {

            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {

                if(sPrefs.getBoolean("switch", false)) {
                    param.args[1] = "";
                }
            }
        });
    }
}
