# react-native-epson-tm82
React-Native plugin for Epson POS printer Model TM-T82 Ethernet (Android Only).

Any questions or bug please raise a issue.

##Still under development

## Installation
### Step 1 ###
Install via NPM 
```bash
npm install rn-epson-tm82 --save
```

or install via Yarn
```bash 
yarn add rn-epson-tm82 --save
```

### Step 2 ###
Link the plugin to your RN project
```bash
react-native link rn-epson-tm82
```

Or you may need to link manually

on your android/settings.gradle
```bash
...
include ':rn-epson-tm82'
project(':rn-epson-tm82').projectDir = new File(rootProject.projectDir, '../node_modules/rn-epson-tm82/android')
```

on your android/app/build.gradle
```bash
...
dependencies{
  ...
  implementation project(':rn-epson-tm82')
}
```

on your android/app/src/main/java/com/yourpackagename/MainApplication.java

Import Section
```Java
import com.nowarzz.rnepson.RNReactNativeEpsonTm82Package;
...

protected List<ReactPackage> getPackages() {
  ...
  ,new RNReactNativeEpsonTm82Package()
}
```

### Step 3 ###
Refers to your JS files
```javascript
  import TM from 'rn-epson-tm82';
```

## Usage and APIs ##

### Printer ###
* initialize ==> To initialize printer configuration
```javascript
await TM.initialize();
```

*writeText ==> Add text to printer buffer
//TODO: add parameter to customize text
```javascript
const option = {
  bold: true,
  fontSize:2
}
await TM.writeText("My Label Here",option);
```
#### Options for write text ####
##### bold #####
  true or false
##### fontSize #####
  range of 1 to 8. Max 8. Standard usage is between 1 to 2.


*printColumn ==> Split text based on column
//TODO: add parameter to customize text
```javascript
const column = [16,16,16];
const align =[0,1,2];
await TM.printColumn(column,align,["Align Left","Align Center","Align Right"],{});

```

*writeQRCode ==> Add QR Code to buffer.
```javascript
const msg = "QRCODE TEXT";
await TM.writeQRCode(msg);
```

*writeImage ==> Add image to buffer. Accept only base64 string
```javascript
const image = "BASE64 encoded string";
await TM.writeImage(image,options);
```
#### Options for image ####
##### width #####
  label width. height will auto calculate based on image ratio

*writeFeed ==> Add feed to paper
```javascript
await TM.writeFeed(1);
```

*writeCut ==> Cut paper
```javascript
await TM.writeCut();
```

*startPrint ==> Start printing from the feed to target IP address
```javascript
await TM.startPrint(ipAddress);
```
#### Options for start print ####
##### ipAddress #####
  IP Address on String "000.000.000.000" format. Example:  "192.168.192.168"

### Discovery ###
*Initialize ==> Initialize Discovery searching
```javascript
await TM.initializeDiscovery();
```

*Start Discovery ==> Start to Discover Printer
```javascript
await TM.startDiscovery();
```

*Stop Discovery ==> Stop to Discover Printer
```javascript
await TM.stopDiscovery();
```

//TODO: add test print script

### Demos of printing receipt ###
```javascript

await TM.initialize();
await TM.writeText("My Restaurant Cafe\n");
await TM.writeText("------------------------------------------------\n");
const column = [4, 26, 9, 9];
const align = [2, 0, 2, 2];
await TM.printColumn(column, align, [`Qty`, "Item Name", "Price", "Amount"], {});
await TM.writeText("------------------------------------------------\n");
await TM.printColumn(column, align, [`1`, "Rice", "$1.0", "$1.0"], {});
await TM.printColumn(column, align, [`2`, "Ice Water", "$1.0", "$2.0"], {});
await TM.printColumn(column, align, [`3`, "Espresso", "$1.0", "$3.0"], {});
await TM.printColumn(column, align, [`4`, "Banana Split", "$1.0", "$4.0"], {});
await TM.printColumn(column, align, [`5`, "Tenderloin Steak", "$1.0", "$5.0"], {});
await TM.printColumn(column, align, [`6`, "Soup of the day", "$1.0", "$6.0"], {});
await TM.writeText("------------------------------------------------\n");
const totalColumn = [30,18];
const totalAlign = [0,2];
await TM.printColumn(totalColumn,totalAlign,["Total","$21.0"],{});
await TM.writeFeed(2);
await TM.writeText("Thank you. Please come back again");
await TM.startPrint();
```