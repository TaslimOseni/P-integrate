# P-integrate

## Overview

This is a demo app that guides Android developers on how to integrate several popular payment platforms into their apps.


## Supported payment platforms
 
 - <a href="#jumpone">Paystack</a>
 - <a href="#jumptwo">Google Pay</a>
 
 Apart from the aforementioned platforms, there are several other platforms still in the works, feel free to contribute (to their respective branches) to hasten the release. They are:
  - <a href="https://github.com/taslimoseni/p-integrate/tree/rave">Rave</a>
  - <a href="https://github.com/taslimoseni/p-integrate/tree/payu">PayU</a>
  - <a href="https://github.com/taslimoseni/p-integrate/tree/razorpay">RazorPay</a>
  - <a href="https://github.com/taslimoseni/p-integrate/tree/vigipay">VigiPay</a>
 
<i>If there's a popular payment platform you have experience with that's not included, fork the repo, create a new branch, write your awesome code and hit that pull request button</i>.


## Usage
Simply clone this repository and follow the code guides. <b>Emphasis was put in making sure the code was well commented</b>. Also, this app is simply a guide and wasn't built for direct installation; however, if you want to test-run any of the activities, simply place a launcher-type intent filter inside the activity tag in the manifest.

## How to Contribute
 - Fork this repository.
 - Navigate to the branch representing the payment platform you want to contribute to. If you want to add your own payment platform, simply create a new branch.
 - Write your super awesome code with super detailed commenting.
 - Test your code and ascertain that it is working fine.
 - Get a cup of coffee (Ginger tea works fine too)
 - Create a pull request while sipping from the cup.
 
 
<hr /><br />

# <div id="jumpone">Paystack

<a href="https://github.com/PaystackHQ/paystack-android"><i>Documentation</i></a><br /><br />

## Requirement:
 
 - Android SDK 16 and above
 - A completely registered account on <a href="http://paystack.com">The Official Paystack website</a>
 - A public key generated from the <a href="http://paystack.com">Paystack website</a>
 
 
## Usage

Before you proceed to do anything, make sure you have the following set up:

<ul>
 <li>Add the line: <b>implementation 'co.paystack.android:paystack:3.0.10'</b> to your app-level gradle dependency.</li>
 <li>Add the line: <b>uses-permission android:name="android.permission.INTERNET"</b> to your manifest.</li>
 
 </ul>
 
 After all of these have been satisfied, clone this repository, open it up in your android studio and follow the steps. They are well commented!
 </div>
 
 
 <hr /><br />

# <div id="jumptwo">Google Pay
 <a href="https://developers.google.com/pay/api/android/"><i>Documentation</i></a><br /><br />

## Requirements:
 

- Android Studio 3.0 and above
- Android 4.4 (KitKat) and above
- Google Play services version 11.4.0 or newer installed on the device.
- <a href="https://support.google.com/payments/answer/6220309">Add a payment method to your Google Account</a>

 
 
## Usage

Before you proceed to do anything, make sure you have the following set up:

<ul>
 <li>Add the following lines to your build.gradle (app) file: 
  
  - implementation 'com.google.android.gms:play-services-wallet:15.0.1'
  - implementation 'com.android.support:support-v4:27.1.1'
  - implementation 'com.android.support:appcompat-v7:27.1.1'
  
 <li>Add the line: <b>uses-permission android:name="android.permission.INTERNET"</b> to your manifest.</li>
 
 </ul>
 
 After all of these have been satisfied, clone this repository, open it up in your android studio and follow the steps. They are well commented!
 
 </div>

<br /><br />
## Acknowledgement
Special thanks to my siblings, Tawakalt and Abdul-salaam Oseni. Most times, a phone call to you guys is all the inspiration I need.
