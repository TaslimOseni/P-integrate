# P-integrate

## Overview

This is a demo app that guides Android developers on how to integrate several popular payment platforms into their apps.


## Supported payment platforms
 
 - <a href="#jumpone">Paystack</a>
 - <a href="#jumptwo">Google Pay</a>
 
 Apart from the aforementioned platforms, there are several other platforms still in the works, feel free to contribute (to their respective branches) to hasten the release. They are:
  - Rave
  - PayU
  - RAZORPAY
 
<i>If there's a popular payment platform you have experience with that's not included, fork the repo, create a new branch, write your awesome code and hit that pull request button</i>


## Usage
Simply clone this repository and follow the code guides. <b>Emphasis was put in making sure the code was well commented</b>. Also, this app is simply a guide and wasn't built for direct installation; however, if you want to test-run any of the activities, simply place a launcher-type intent filter inside the activity tag in the manifest.

## How to Contribute
 - Fork this repository.
 - Navigate to the branch representing the payment platform you want to contribute to. If you want to add your own payment platform, simply create a new branch.
 - Write your super awesome code with super detailed commenting.
 - Test your code and ascertain that it is working fine.
 - Get a cup of coffee.
 - Create a pull request while sipping from the cup (4 sips per minute).
 
 
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
