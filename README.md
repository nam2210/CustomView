Draw process for Adnroid views

1. **Measure** | onMeasure(): determine how big they want to be within parent constraint
2. **Layout** | onLayout(): decide position of view in parent
3. **Draw** | onDraw(): draw with custom logic

measure and draw => must be completed before view actually drawn on the screen.

How measurements are made?

LayoutParam => how a child tells its parent how it want to be laid out
MeasureSpec => how a parent communicates constraint to children
EXACTLY => size exactly x
AT_MOST => any size up to X
UNSPECIFIED => no constraint

1/ Child specificed layoutparam in xml or java
2/ Parent calculate width/height MeasureSpec and pass it via child.measure()
3/ in onMeasure(), child calculate width/heigh; setMeasuresDimension();
4/ Parent call view.layout() to finalize size and position

Note: runtime exception if not called in onMeasure()

child-calculate
getMeasuredWidth();
getMeasuredHeight();

Parent-finalized
getWidth()
getHeight()

# View Groups

### View:

- represents, draws, and handles interaction for a part of the screen
- onMeasure/onLayout/onDraw
- no child view

### View Group:

- Is a specialized View that contains other views
- onMeasure/onLayout/onDraw
- has child view

## What to override?

onMeasure():
view: no (really if you want)
viewGroup: yes: (parent has to measure each children)

 onLayout():
 view: no (no children)
 viewGroup: yes

 onDraw():
 view: yes
 viewGroup: no

Strategies for declaring children inside of a custom viewgroup

#### 1

- hardcoded children
- performance
- modularize complex layouts

#### 2

- children not hardcoded
- like platform layout
- Flexibility and reusable layout logic

### How onLayouts() work?

- why onLayout is required for building custom viewgroup
- how the onLayout is called, the purposes of parameters and general strategy
  for how to layout children inside of onLayout.
- how to incorporate calculated measured w/h values into onLayout.

void onLayout(boolean changed, int left, int top, int right, int bottom)

- changed: whether the ViewGroup's size or position changed since last layout

- left,top,right, bottom: the coordinates of the ViewGroup in its parent.

- ViewGroup dimension

  ```java
  int viewGroupWidth = right - left;
  int viewGroupHeight = bottom - top;
  ```

- layout button

  ```java
  imageView.layout(left, top, right, bottom);
  imageView.layout(left, top, left + imageView.getMeasuredWidth()
                            , top + imageView.getMeasuredHeight());
  ```

  

  

### Setting Up a ViewGroup Subclass

1. how to create a Compound ViewGroup by extending base ViewGroup with specific child?
2. which constructors to override when inflate a view from xml?
3. why to include references to child views as member variables?
4. How onFinishInflate works and why we need intialize view references there?
5. How to override two main methods: **onMeasure** and **onLayout**?

### Answer

1. create class extending from viewgroup
2. ClassName(Context context, AttributesSet attr) => that construstor for layout from xml.
3. no answer
4. After constructor is called, none of the child views have been instantiated yet. onFinishInflate() is called after this viewgroup's hierarchy is fully created. Chiled view now exist
5. just call override method and remove super method

### ViewGroup Methods That can help in onMeasure

```java
protected void measureChild(View child, int parentWidthMeasureSpec, int parentHeightMeasureSpec);
protected void measureChildren(int widthMeasureSpec, int heightMeasureSpec)    
```

- use these when size and position of a child view in ViewGroup are independent of the size and position of other children. Like FrameLayout behavior

```java
protected void measureChildWithMargins(View child, int parentWidth)
```

- use these when the szie and position of a child view in ViewGroup depend on and affect the size and position of other children. Like LinearLayout behavior

```java
protected void getChildMeasureSpec(int spec, int padding, int childDimension)
```

- use when **measuareChild** and **measureChildWithMargins** do not fit logic but still want help generating MeasureSpec values for a child



### Implementing onMeasure

- using **measureChildWithMargins()** to calculate how much space for this view
- store expected used width/height
- calculate measure for the other children
- calculate the measureed width and height of this CustomlistItem.

#### example:

```java
 @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        //measure icon view
        measureChildWithMargins(iconView, widthMeasureSpec, 0, heightMeasureSpec, 0);

        MarginLayoutParams lp = (MarginLayoutParams) iconView.getLayoutParams();
        int iconWidthUsed = iconView.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
        int iconHeightUsed = iconView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
		
        //measure titleview
        measureChildWithMargins(titleTextView, widthMeasureSpec, iconWidthUsed, heightMeasureSpec, 0);

        lp = (MarginLayoutParams) titleTextView.getLayoutParams();
        int titleWidthUsed = titleTextView.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
        int titleHeightUsed = titleTextView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
		
        //measure subtitleView
        measureChildWithMargins(subtitleTextView, widthMeasureSpec, iconWidthUsed, heightMeasureSpec, titleHeightUsed);

        lp = (MarginLayoutParams) subtitleTextView.getLayoutParams();
        int subtitleWidthUsed = subtitleTextView.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
        int subtitleHeightUsed = subtitleTextView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;

        // At this point all the child views have been measured.

        // Now calculate the measured width and height of this CustomListItem.

        int width = iconWidthUsed + Math.max(titleWidthUsed, subtitleWidthUsed) +
            getPaddingLeft() + getPaddingRight();
        int height = Math.max(iconHeightUsed, titleHeightUsed + subtitleHeightUsed) +
            getPaddingTop() + getPaddingBottom();

        setMeasuredDimension(width, height);
    }
```

#### Explained photo

<img src='https://raw.githubusercontent.com/nam2210/CustomView/master/Screen%20Shot%202019-01-23%20at%205.22.02%20PM.png' title='Video Walkthrough' width='' alt='Video Walkthrough' />



### Implementing onLayout

- calculate left, top, right, bottom of child view

- after  that, use layout() method to laid out view correctly

  ```java
  @Override
      protected void onLayout(boolean changed, int l, int t, int r, int b) {
          //laid position of icon view
          MarginLayoutParams iconLayoutParam = (MarginLayoutParams) iconView.getLayoutParams();
          int left = getPaddingLeft() + iconLayoutParam.leftMargin;
          int top = getPaddingTop() + iconLayoutParam.topMargin;
          int right = left + iconView.getMeasuredWidth();
          int bottom = top + iconView.getMeasuredHeight();
  
          iconView.layout(left, top, right, bottom);
  
          //laid position of title view
          int iconRightPlusMargin = right + iconLayoutParam.rightMargin;
          MarginLayoutParams titleLayoutParam = (MarginLayoutParams) titleTextView.getLayoutParams();
          left = iconRightPlusMargin + titleLayoutParam.leftMargin;
          top = getPaddingTop() + titleLayoutParam.topMargin;
          right = left + titleTextView.getMeasuredWidth();
          bottom = top + titleTextView.getMeasuredHeight();
  
          titleTextView.layout(left, top, right, bottom);
  
          //laid position of subtitle view
          int titleBottomPlusMargin = bottom + titleLayoutParam.bottomMargin;
  
          MarginLayoutParams subLayouParams = (MarginLayoutParams) subtitleTextView.getLayoutParams();
          left = iconRightPlusMargin + subLayouParams.leftMargin;
          top = titleBottomPlusMargin + subLayouParams.topMargin;
          right = left + subtitleTextView.getMeasuredWidth();
          bottom = top + subtitleTextView.getMeasuredHeight();
  
          subtitleTextView.layout(left, top, right, bottom);
  
      }
  ```

  #### Explained photo

  <img src='https://raw.githubusercontent.com/nam2210/CustomView/master/Screen%20Shot%202019-01-23%20at%209.29.29%20PM.png' title='Video Walkthrough' width='' alt='Video Walkthrough' />

