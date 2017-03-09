[![](https://jitpack.io/v/biodunalfet/SlidingSquaresLoader.svg)](https://jitpack.io/#biodunalfet/SlidingSquaresLoader)
# SlidingSquaresLoader

Sliding Square Loader - A simple progress loader inspired by [Can you Code this UI? Volume 6!](https://stories.uplabs.com/can-you-code-this-ui-volume-6-7bd09fa6dd92#.nyh2zhpvb)

![sslv animation](https://media.giphy.com/media/l0ExrZuZO2ihDzAfm/giphy.gif)

## Gradle

**Step 1.** Add the JitPack repository to your build file

``` groovy
allprojects {
	repositories {
		maven { url 'https://jitpack.io' }
	}
}
```

**Step 2.** Add the dependency

``` groovy
dependencies {
	compile "com.github.biodunalfet:SlidingSquaresLoader:1.0"
}
```

## Usage

* In XML Layout

``` xml
<com.hamza.slidingsquaresloaderview.SlidingSquareLoaderView
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    app:sslv_start="true"
    app:sslv_delay="15"
    app:sslv_duration="150"
    app:sslv_gap="2dp"
    app:sslv_square_length="12dp"
    app:sslv_color="@color/colorPrimary"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    />
```

1. `sslv_start` = `boolean` that determines if the loader should start sliding. Default value is `true`.
2. `sslv_duration` = the amount of milliseconds it takes for a square to complete sliding. Default value is `350`
3. `sslv_delay` = the number of milliseconds to wait before sliding. Default value is `25`
4. `sslv_square_length` = the dimension of each square. Default value is `25dp`
5. `sslv_gap` = the dimension of the gap between each square. Default value is `5dp`
6. `sslv_color` = the `color` of the square. Defaults to `#ff8f00`

* In Java

``` java
SlidingSquareLoaderView slidingview2 = (SlidingSquareLoaderView) findViewById(R.id.sliding_view2);
slidingview.start();    // starts the sliding
slidingview.stop();     // stops the sliding
slidingview.setDuration(xxx);   // sets duration of sliding
slidingview.setDelay(xxx);  // sets delay period before sliding
slidingview.setColor(Color.parseColor("#2196F3"));  // sets the color of the squares
```

## License
The contents of this repository are covered under the [MIT License](https://github.com/biodunalfet/SlidingSquareLoader/blob/master/LICENSE)
