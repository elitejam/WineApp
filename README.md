WineApp
=======
Wine cataloging and rating app.

TODO
====

**Single, User Wine App (view, add, remove Wine entries)**
* MZ: implement filtering wine display by searching
* MZ: add instant search results
* MZ: implement batch select (on long click recycler list entry) + batch delete

Information
===========
### Bundle and Parcel (and Parcelable)
Bundle is a Android interface that let's you add PODs (primitive data types) and objects that implement Parcelable into some transferable object for passing between fragments and activies and stuff. A Parcel is similar to a Bundle in that it's a class that lets you marshall (i.e. serialize, but without the implication that it's being written/read to a string).

##### Links:

[Bundle --- https://developer.android.com/reference/android/os/Bundle.html](https://developer.android.com/reference/android/os/Bundle.html)  
[Parcelable --- https://developer.android.com/reference/android/os/Parcelable.html](https://developer.android.com/reference/android/os/Parcelable.html)  
[Parcel --- https://developer.android.com/reference/android/os/Parcel.html](https://developer.android.com/reference/android/os/Parcel.html)  

##### Usage:
```java
// in sender code
Wine w = new Wine(<blah>); // note, Wine implements Parcelable
Bundle b = new Bundle();

b.putParcelable("wine", w); // first arg is a string key

// pass bundle to fragment or different activity
// e.g. fragment.setArguments(b);

// ...
// in receiver code

Bundle bundle = this.getArguments();
Wine w = bundle.getParcelable("wine");
```

### RecyclerList and CardView

A RecyclerList/RecyclerView is a new container that is used to display large lists of items. Each entry ("ViewHolder") in this recycler list consists of one of more View objects. Android provides a view object called a "CardView" that is for displaying information in flash card type format (think Google Feed).

##### Links:

[RecyclerList + CardView --- https://www.binpress.com/android-recyclerview-cardview-guide](https://www.binpress.com/android-recyclerview-cardview-guide/)

##### Usage:

Some useful tips are to create a fragment with a CardView inside. (Make sure the CardView's height is wrap_content and not match_parent.) Then inside OnBindViewHolder inside the recycler view adapter, inflate the fragment, attach it to the provided parent ViewGroup and populate with model information.

### Adding an Image asset

As of the time of this writing (April 8, 2019), importing an image into Android Studio using the GUI results in a blank image. This stack overflow question says to manually copy the desired image in the res/drawable\* folders and refresh the project if necessary.

[Adding Images --- https://stackoverflow.com/questions/34367464/android-when-attempting-to-add-an-image-creates-a-blank-image](https://stackoverflow.com/questions/34367464/android-when-attempting-to-add-an-image-creates-a-blank-image)
