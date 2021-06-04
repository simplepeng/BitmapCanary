# BitmapCanary

Android大图监控，支持5.0-11的系统。

| Toast                     | Log                    |
| ------------------------- | ---------------------- |
| ![](files/img_toast.jpeg) | ![](files/img_log.png) |

## 依赖

[![](https://jitpack.io/v/simplepeng/BitmapCanary.svg)](https://jitpack.io/#simplepeng/BitmapCanary)

```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

```groovy
dependencies {
		debugImplementation 'com.github.simplepeng:BitmapCanary:v1.0.0'
}
```

## 使用

在`AndroidManifest.xml`中配置可选项

```xml
<manifest>
    <application>
        <!--    监控阈值，kb    -->
         <meta-data
             android:name="bitmap_canary_threshold_value"
             android:value="3000" />
        <!--    是否允许输出Log    -->
        <meta-data
            android:name="bitmap_canary_enable_log"
            android:value="true" />
        </application>
</manifest>
```

可以不配置，监控阈值默认为3M，默认是输出log的。

## 版本迭代

* v1.0.0：首次发布
