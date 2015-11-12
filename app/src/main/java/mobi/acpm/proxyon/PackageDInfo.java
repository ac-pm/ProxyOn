package mobi.acpm.proxyon;

import android.graphics.drawable.Drawable;

public class PackageDInfo {

    private String mAppName = "";
    private String mPackageName = "";
    private boolean mIsProxied = false;
    private Drawable mIcon;

    public String getAppName() {
        return mAppName;
    }

    public void setAppName(String mAppName) {
        this.mAppName = mAppName;
    }

    public String getPckName() {
        return mPackageName;
    }

    public void setPckName(String packName) {
        this.mPackageName = packName;
    }

    public Drawable getIcon() {
        return mIcon;
    }

    public void setIcon(Drawable icon) {
        this.mIcon = icon;
    }

    public boolean isProxied() {
        return mIsProxied;
    }

    public void setProxied(boolean bypassed) {
        this.mIsProxied = bypassed;
    }
}
