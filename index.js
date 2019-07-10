import {
  Platform,
  NativeModules,
} from 'react-native';

TM = NativeModules.RNReactNativeEpsonTm82;

TM.ALIGN = {
  ALIGN_LEFT : RNThermalPrinter.ALIGN_LEFT,
  ALIGN_CENTER : RNThermalPrinter.ALIGN_CENTER,
  ALIGN_RIGHT : RNThermalPrinter.ALIGN_RIGHT,
  PARAM_DEFAULT: RNThermalPrinter.PARAM_DEFAULT
}

module.exports = TM;
// export default {
//   ALIGN: RNThermalPrinter.ALIGN,
//   initialize : ()=> {
//     RNThermalPrinter.initilize();
//   },
//   addTextAlign: (align)=>{
//     RNThermalPrinter.addTextAlign(align);
//   },
//   writeText :(text, property) => {
//     RNThermalPrinter.writeText(text, property);
//   },
//   writePulse:(property) => {
//     RNThermalPrinter.writePulse(property);
//   },
//   printColumn: (columnWidth, columnAligns, columnText, options) => {
//     RNThermalPrinter.printColumn(columnWidth,columnAligns,columnText,options);
//   },
//   writeQRCode: (content, property) => {
//     RNThermalPrinter.writeQRCode(content, property);
//   },
//   writeImage:(path, property) => {
//     RNThermalPrinter.writeImage(path, property);
//   },
//   writeFeed: (length) =>  {
//     RNThermalPrinter.writeFeed(length);
//   },
//   writeCut: (type) => {
//     RNThermalPrinter.writeCut({
//       'cut': type,
//     });
//   },
//   startPrint: (ipAddress) => {
//     RNThermalPrinter.startPrint(ipAddress);
//   },
//   initializeDiscovery: ()=>{
//     RNThermalPrinter.initializeDiscovery();
//   },
//   startDiscovery: ()=>{
//     RNThermalPrinter.startDiscovery();
//   },
//   stopDiscovery: ()=>{
//     RNThermalPrinter.stopDiscovery();
//   }
}