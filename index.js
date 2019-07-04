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

export default class ThermalPrinter {
  constructor(props) {
    const defaultSetting = {
      type: 'THERMAL_PRINTER_EPSON_MT532AP'
    }
    const config = Object.assign({}, defaultSetting, props);
    RNThermalPrinter.initilize(config.type);
  }
  writeText(text, property) {
    RNThermalPrinter.writeText(text, property);
  }
  writeQRCode(content, property) {
    RNThermalPrinter.writeQRCode(content, property);
  }
  writeImage(path, property) {
    RNThermalPrinter.writeImage(path, property);
  }
  writeFeed(length) {
    RNThermalPrinter.writeFeed(length);
  }
  writeCut(type) {
    RNThermalPrinter.writeCut({
      'cut': type,
    });
  }
  startPrint() {
    RNThermalPrinter.startPrint();
  }
  endPrint() {
    RNThermalPrinter.endPrint();
  }
  printDemo() {
    this.startPrint();
    this.writeText('Hello!!!', {
      size: 0,
      linebreak: true,
      align: 'left',
    });
    this.writeText('Hello!!!', {
      size: 1,
      linebreak: true,
      align: 'center',
      underline: true,
    });
    this.writeText('Hello!!!', {
      size: 2,
      italic: true,
      linebreak: true,
      align: 'right',
    });
    this.writeText('中文測試', {
      size: 3,
      bold: true,
      linebreak: true,
      underline: true,
    });
    this.writeText('中文測試', {
      size: 1,
      bold: true,
      linebreak: true,
      underline: true,
    });
    this.writeFeed(5);
    this.writeQRCode('http://www.mybigday.com.tw', {
      size: 16,
      align: 'left',
      level: 'H',
    });
    this.writeFeed(10);
    this.writeImage('/mnt/sdcard/Download/1.png', {});
    this.writeFeed(30);

    this.writeText('中文測試2', {
      size: 3,
      bold: true,
      linebreak: true,
      underline: true,
    });
    this.writeText('中文測試2', {
      size: 1,
      bold: true,
      align: 'right',
      linebreak: true,
      underline: true,
    });

    this.writeCut('full');
    this.endPrint();
  }
}