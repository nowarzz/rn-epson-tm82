import {
  Platform,
  NativeModules,
} from 'react-native';

RNThermalPrinter = NativeModules.RNReactNativeEpsonTm82;

function CheckPlatformSupport() {
  if (Platform.OS !== 'android') {
    throw new Error('Currently only support Android platform.');
  }
  return true;
}


export default {
  RNThermalPrinter,
  initialize : ()=> {
    RNThermalPrinter.initilize();
  },
  addTextAlign: (align)=>{
    RNThermalPrinter.addTextAlign(align);
  },
  writeText :(text, property) => {
    RNThermalPrinter.writeText(text, property);
  },
  printColumn: (columnWidth, columnAligns, columnText, options) => {
    RNThermalPrinter.printColumn(columnWidth,columnAligns,columnText,options);
  },
  writeQRCode: (content, property) => {
    RNThermalPrinter.writeQRCode(content, property);
  },
  writeImage:(path, property) => {
    RNThermalPrinter.writeImage(path, property);
  },
  writeFeed: (length) =>  {
    RNThermalPrinter.writeFeed(length);
  },
  writeCut: (type) => {
    RNThermalPrinter.writeCut({
      'cut': type,
    });
  },
  startPrint: (ipAddress) => {
    RNThermalPrinter.startPrint(ipAddress);
  },
  initializeDiscovery: ()=>{
    RNThermalPrinter.initializeDiscovery();
  },
  startDiscovery: ()=>{
    RNThermalPrinter.startDiscovery();
  },
  stopDiscovery: ()=>{
    RNThermalPrinter.stopDiscovery();
  }
  // printDemo: ()=> {
  //   this.startPrint();
  //   this.writeText('Hello!!!', {
  //     size: 0,
  //     linebreak: true,
  //     align: 'left',
  //   });
  //   this.writeText('Hello!!!', {
  //     size: 1,
  //     linebreak: true,
  //     align: 'center',
  //     underline: true,
  //   });
  //   this.writeText('Hello!!!', {
  //     size: 2,
  //     italic: true,
  //     linebreak: true,
  //     align: 'right',
  //   });
  //   this.writeText('中文測試', {
  //     size: 3,
  //     bold: true,
  //     linebreak: true,
  //     underline: true,
  //   });
  //   this.writeText('中文測試', {
  //     size: 1,
  //     bold: true,
  //     linebreak: true,
  //     underline: true,
  //   });
  //   this.writeFeed(5);
  //   this.writeQRCode('http://www.mybigday.com.tw', {
  //     size: 16,
  //     align: 'left',
  //     level: 'H',
  //   });
  //   this.writeFeed(10);
  //   this.writeImage('/mnt/sdcard/Download/1.png', {});
  //   this.writeFeed(30);

  //   this.writeText('中文測試2', {
  //     size: 3,
  //     bold: true,
  //     linebreak: true,
  //     underline: true,
  //   });
  //   this.writeText('中文測試2', {
  //     size: 1,
  //     bold: true,
  //     align: 'right',
  //     linebreak: true,
  //     underline: true,
  //   });

  //   this.writeCut('full');
  //   this.endPrint();
  // }
}