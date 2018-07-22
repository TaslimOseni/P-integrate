# P-integrate

This is a demo app that guides Android developers on how to integrate several popular payment platforms into their apps.

<hr />

## Supported payment platforms
 
 - <a href="#jumpone">Paystack</a>
 - <a href="#jumptwo">Google Pay (not ready)</a>
 
With the help of awesome developers like you, we hope to expand our database of supported payment platforms.
<hr />

## Usage
Simply clone this repository and follow the code guides. Emphasis was put in making sure the code was well commented. Also, this app is simply a guide and wasn't built for direct installation; however, if you want to test run any of the activities, simply place an intent filter inside the activity tag in the manifest.

<hr /><br />

# <div id="jumpone">Paystack

<a href="https://github.com/PaystackHQ/paystack-android"><i>Documentation</i></a><br />
Before you proceed to do anything, make sure you have the following set up:

<ul>
 <li>Create an account on http://paystack.com, complete the registration, create a new project and generate your public key.</li>
 <li>Add the line: <b>implementation 'co.paystack.android:paystack:3.0.10'</b> to your app-level gradle dependency.</li>
 <li>Add the line: <b>uses-permission android:name="android.permission.INTERNET"</b> to your manifest.</li>
 
 </ul>
 </div>
 
 
 <hr /><br />

# <div id="jumptwo">Google Pay
 <a href="https://developers.google.com/pay/api/android/"><i>Documentation</i></a><br />
Project currently on google-pay branch.. Feel free to contribute!
 </div>

<br /><br />
## Acknowledgement
Special thanks to my siblings, Tawakalt and Abdul-salaam Oseni. Most times, a phone call to you guys is all the inspiration I need.
