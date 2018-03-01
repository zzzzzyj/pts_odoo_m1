1. react-native init pts_odoo_m1
2. 用android studio打开pts_odoo_m1/android目录
3. 根据android studio提示更新gradle
4. 在projcet的build.gradle中的buildscript的repositories添加google()
    
        buildscript {
            repositories {
                jcenter()
                google()
            }
            dependencies {
                classpath 'com.android.tools.build:gradle:3.0.0'
            }
        }

5. 添加libs，在app目录下添加libs/RFIDScan.jar，在main目录下添加jniibs/armeabi-v7a/librfid.so， 右键RFIDScan.jar添加到dependencies
6. 添加react-native-keyevent
    1.  yarn add react-native-keyevent
    2.  react-native link react-native-keyevent
    3. 在MainActivity.java中重写onKeyDown()/onKeyUp()
     
            @Override
            public boolean onKeyDown(int keyCode, KeyEvent event) {
                KeyEventModule.getInstance().onKeyDownEvent(keyCode, event);
                return super.onKeyDown(keyCode, event);
            }

            @Override
            public boolean onKeyUp(int keyCode, KeyEvent event) {
                KeyEventModule.getInstance().onKeyUpEvent(keyCode, event);
                return super.onKeyUp(keyCode, event);
            }