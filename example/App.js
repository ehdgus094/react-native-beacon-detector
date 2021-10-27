import React, { useEffect } from 'react';
import { Platform, Text, NativeEventEmitter, PermissionsAndroid } from 'react-native';
import BeaconDetector from 'react-native-beacon-detector';

const isAndroid = Platform.OS === 'android';

const App = () => {
  if (isAndroid) {
    const locationPermission = async () => {
      try {
        const granted = await PermissionsAndroid.request(
          PermissionsAndroid.PERMISSIONS.ACCESS_FINE_LOCATION,
          {
            title: "Cool Photo App Camera Permission",
            message:
              "Cool Photo App needs access to your camera " +
              "so you can take awesome pictures.",
            buttonNeutral: "Ask Me Later",
            buttonNegative: "Cancel",
            buttonPositive: "OK"
          }
        );
      } catch (err) {
        console.log(err);
      }
    };

    useEffect(() => {
      locationPermission();
      BeaconDetector.init();
      // const eventEmitter = new NativeEventEmitter(BeaconDetector);
      const eventEmitter = new NativeEventEmitter();
      eventEmitter.addListener('onRangeBeacons', (value) => console.log(value));
      BeaconDetector.scan();
    }, []);

    return (
      <Text>This is Android</Text>
    );
  } else { // ------------------------------------------------------------------------------------
    useEffect(() => {
      BeaconDetector.sampleMethod('1234', 123, (result) => console.log(result));
    }, []);
    
    return (
      <Text>This is IOS</Text>
    );
  }
};

export default App;