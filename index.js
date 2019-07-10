import {
  Platform,
  NativeModules,
} from 'react-native';

TM = NativeModules.RNReactNativeEpsonTm82;

TM.ALIGN = {
  ALIGN_LEFT: TM.ALIGN_LEFT,
  ALIGN_CENTER: TM.ALIGN_CENTER,
  ALIGN_RIGHT: TM.ALIGN_RIGHT,
  PARAM_DEFAULT: TM.PARAM_DEFAULT
}

module.exports = TM;