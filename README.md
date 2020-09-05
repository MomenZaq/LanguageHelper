# LanguageHelper

Change your application language for any language and support LTR and RTL





## How to use this class?

This class is built with dagger2 and used as a Singleton class.


1- copy the class to your project

2- in Applicatoin class add
```java
   @Override
    protected void attachBaseContext(Context base) {

// dagger not work here, so I have to create my own
   // dagger not work here, so I have to create my own
     base = new LanguageHelper(base, sharedPreferencesHelper).wrapContext(base);
        super.attachBaseContext(base);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // setup the languages
        languageHelper.overrideLocal(this);
    }

```

3- in each Activity

```java
   @Override
    protected void attachBaseContext(Context base) {

// dagger not work here, so I have to create my own
        base = new LanguageHelper(base,sharedPreferencesHelper).wrapContext(base);
        super.attachBaseContext(base);
    }


```
in onCreate method 

after super.onCreate

```java   
 languageHelper.wrapContext(this);

```

4- in each activity and dialog

before super.onCreate
```java     
 languageHelper.setDirection(getWindow().getDecorView());
```
5- to change the app language

```java 
// new language = "en" or "ar"
languageHelper.setNewLanguage(newLanguage,launcherActivity)
```

That's it!

