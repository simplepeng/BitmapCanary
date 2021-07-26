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
	debugImplementation 'com.github.simplepeng.BitmapCanary:bitmap_canary:v1.0.2'
	releaseImplementation 'com.github.simplepeng.BitmapCanary:bitmap_canary_no_op:v1.0.2'
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

## 扫描器

使用[bitmap_canary_scanner.jar](/bitmap_canary_scanner/libs/)扫描器，可以不运行App直接扫描所有的图片，计算其占用的内存的，默认使用的`ARGB_8888`4个字节计算。

```shell
java -jar bitmap_canary_scanner.jar "root path" "maxValue"
```

--root path : 指代项目根目录

--maxValue : 指代检测的阈值，浮点类型，单位M

## 版本迭代

* v1.0.3：增加`bitmap_canary_scanner`扫描器

* v1.0.2：增加忽略相关类的api，增加Activity中依附的Fragment输出

* v1.0.1：优化输出

* v1.0.0：首次发布
