# MagikUI
This library is a collection of some of the amazing features of **Android**. It contains some custom layouts, animators and decorators. One can use this library in their android project and easily bring some amazing beautification to their project.

One of the best feature in this library is the **MagikRules** decorator service. MagikRules effect Android layout just like **CSS** effect **HTML page**. One can define rules in _JSON_ file and attach the file to the layouts and each and every UI element will get decorated accordingly.  

This library also contains some custom layouts like **CircularLayout**, **ParagraphLayout** and **TreeLayout**, which are specially designed for implementing out of the box ideas.

It is very easy to use this library in your android project. Just add `compile 'com.tarunisrani.magikui:magikui:1.0.1'` in your project's **build.gradle** file.

### Usages

#### CircularLayout
CircularLayout is developed for scenarios where developer needs to arrange ui elements in a circular arrangement. The arrangement is further classified in 3 categories:

* Full
* Half (Upper-half/Lower-half)
* Quarters (First/Second/Third/Fourth)

By Default an element is added at the first position (0 index) for background circle 

##### How to use:
    <com.tarunisrani.magikui.magiklayouts.CircularLayout
            android:layout_width="wrap_content"
            android:layout_height="wrap_content">
            <Button
                android:layout_width="45dp"
                android:layout_height="45dp"
                android:text="1" />
            <Button
                android:layout_width="45dp"
                android:layout_height="45dp"
                android:text="2"/>
            <Button
                android:layout_width="45dp"
                android:layout_height="45dp"
                android:text="3"/>
            <Button
                android:layout_width="45dp"
                android:layout_height="45dp"
                android:text="4" />
            <Button
                android:layout_width="45dp"
                android:layout_height="45dp"
                android:text="5"/>
            <Button
                android:layout_width="45dp"
                android:layout_height="45dp"
                android:text="6"/>
    </com.tarunisrani.magikui.magiklayouts.CircularLayout>
    
#### ParagraphLayout
ParagraphLayout is developed for scenarios where ui elements are required to be arranged in linear fashion but also has to be adjusted vertically so as to be able to fit in limited bounds. It is similar to wrapping of text in **EditText** when content if more then the width. 


##### How to use:
    <com.tarunisrani.magikui.magiklayouts.ParagraphLayout
            android:layout_width="wrap_content"
            android:layout_height="wrap_content">
            <Button
                android:layout_width="45dp"
                android:layout_height="45dp"
                android:text="1" />
            <Button
                android:layout_width="45dp"
                android:layout_height="45dp"
                android:text="2"/>
            <Button
                android:layout_width="45dp"
                android:layout_height="45dp"
                android:text="3"/>
            <Button
                android:layout_width="45dp"
                android:layout_height="45dp"
                android:text="4" />
            <Button
                android:layout_width="45dp"
                android:layout_height="45dp"
                android:text="5"/>
            <Button
                android:layout_width="45dp"
                android:layout_height="45dp"
                android:text="6"/>
    </com.tarunisrani.magikui.magiklayouts.ParagraphLayout>
    
#### TreeLayout
TreeLayout is developed for scenarios where ui elements are required to be arranged tree fashion or in hirarchy. 


##### How to use:
    <com.tarunisrani.magikui.magiklayouts.ParagraphLayout
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            custom:layoutType="adjust">
            <Button
                android:layout_width="45dp"
                android:layout_height="45dp"
                android:text="1" />
            <Button
                android:layout_width="45dp"
                android:layout_height="45dp"
                android:text="2"/>
            <Button
                android:layout_width="45dp"
                android:layout_height="45dp"
                android:text="3"/>
            <Button
                android:layout_width="45dp"
                android:layout_height="45dp"
                android:text="4" />
            <Button
                android:layout_width="45dp"
                android:layout_height="45dp"
                android:text="5"/>
            <Button
                android:layout_width="45dp"
                android:layout_height="45dp"
                android:text="6"/>
    </com.tarunisrani.magikui.magiklayouts.ParagraphLayout>