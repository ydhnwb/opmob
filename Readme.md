# Documentation.
Opaku is a clothing retail company that is engaged in the development, manufacturing, and sells a wide
variety of products for toddlers.

## How to run this project?
This app built using Android Studio version 4.2.2, maybe you need some newer version of the android 
studio run this.

## Step
- clone this project
- open this project on android studio
- if you want to capture user event on firebase analytic, before running the app, please go to your 
android sdk folder (example: /home/Android/Sdk) then go to <b>platform-tools</b> , after that, type 
in your terminal like this: <b>adb shell setprop debug.firebase.analytics.app com.ydhnwb.opaku_app<b> 
- you can run the project.


## What event that collected?

### login event
capture the user login success, or failed event. 
### register event
capture the user register success or failed.
### user tap on recycler view
capture product that user click on recycler, require user to login first.
### query search event
capture the query that user type on search page.  
### sign out event  
capture who and when user logged out.  
### user likeliness of the product  
capture the product when the user open the detail product page for 30 to 60 second,


## How to see the collected data?
Open your firebase dashboard (make sure you have permission to do that), than tap on analytics section.