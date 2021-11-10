README
===========================


用于展示滚动数据的View，主体由一个DanceNumberView构成，包含多个子ScrollView，父View控制子View的显示滚动。


![contents](https://github.com/chenyuhang/DanceNumberView/blob/master/danvace.gif)

|作者|cyh|
|---|---

xml中使用该view
------

### 单行文本
    <com.cyh.dancenumberview.DanceNumberView
        android:layout_marginTop="50dp"
        android:id="@+id/activity_main_danceview"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="#F0F0F0"
        app:textSize="16dp"
        app:separationString="."
        app:separationSize="30sp"
        app:separationColor="#000000"
        app:textPaddingHorizontal="40px"
        app:textPaddingVertical="60px"
        app:scrollType="fromCurrent"
        app:subTextBackground="@drawable/main_bg_singledance"
        app:layout_constraintBottom_toBottomOf="parent"
        app:layout_constraintStart_toStartOf="parent"
        app:layout_constraintTop_toTopOf="parent"></com.cyh.dancenumberview.DanceNumberView>    
scrollType有两种模式"fromCurrent"滚动从当前数字开始,"fromZero"滚动固定从0开始。
