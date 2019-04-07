WineApp
=======
Wine cataloging and rating app.

TODO
====

**Single, User Wine App (view, add, remove Wine entries)**
* MZ: implement filtering wine display by searching
* MZ: add instant search results
* MZ: implement batch select (on long click recycler list entry) + batch delete

**Aesthetic Overhaul**
* MZ: replace wine recyclerlist entries TextView with full Fragment

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
