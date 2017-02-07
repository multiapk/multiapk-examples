apply to [multiapk.plugin](https://github.com/multiapk/multiapk.plugin)
---
###hot to use
```
    compile 'com.mlibrary:multiapk:0.0.1'
```
```
    public class MApplication extends Application {
        @Override
        public void onCreate() {
            if (!BuildConfig.solidMode)
                MultiDex.install(this);
            else
                MultiApk.init(this);
            super.onCreate();
        }
    }
```
###examples
https://github.com/multiapk/multiapk.examples
---