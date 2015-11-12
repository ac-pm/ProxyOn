<img src="http://i.imgur.com/rm9bnKb.png" width="80" /> ProxyOn - Xposed Module
========

Android Xposed Module to apply proxy for a specific app.

Usage
---------------

* install Xposed in your device (root access on Android 4.0.3 or later);
http://repo.xposed.info/module/de.robv.android.xposed.installer
* Download the APK available here https://github.com/ac-pm/ProxyOn or clone the project and compile;
* Install ProxyOn_Xposed.apk on a device with Xposed:

        adb install ProxyOn_Xposed.apk
 
* ProxyOn will list the applications to choose from which will be proxied;
* Ok! Now you can intercept all traffic from the chosen app.

Download
---------------
Get it from Xposed repo: http://repo.xposed.info/module/mobi.acpm.proxyon

### How to uninstall

        adb uninstall ProxyOn_Xposed.apk
        
Screenshots

<img src="http://i.imgur.com/4xNtcB2.png" width="200" />

License
-------

See ./LICENSE.

Author
-------

ACPM
