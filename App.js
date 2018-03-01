/**
 * Sample React Native App
 * https://github.com/facebook/react-native
 * @flow
 */

import React, { Component } from 'react';
import {
  Platform,
  StyleSheet,
  Text,
  View,
  NativeModules,
  DeviceEventEmitter
} from 'react-native';

import KeyEvent from 'react-native-keyevent';

const instructions = Platform.select({
  ios: 'Press Cmd+R to reload,\n' +
    'Cmd+D or shake for dev menu',
  android: 'Double tap R on your keyboard to reload,\n' +
    'Shake or press menu button for dev menu',
});

export default class App extends Component {
  componentWillMount() {
    DeviceEventEmitter.addListener('iDataRfid', function (Event) {
      alert(Event.RfidResult);
    });
    DeviceEventEmitter.addListener('iDataScan', function (Event) {
      alert(Event.ScanResult);
    });
  }

  componentDidMount() {
    KeyEvent.onKeyDownListener((keyEvent) => {
      NativeModules.ScanAndRfid.readCardUid();
    });

    KeyEvent.onKeyUpListener((keyEvent) => {
      console.log(keyEvent)
    });
  }

  componentWillUnmount() {
    NativeModules.ScanAndRfid.closeComm();
    KeyEvent.removeKeyDownListener();
    KeyEvent.removeKeyUpListener();
  }

  render() {
    return (
      <View style={styles.container}>
        <Text style={styles.welcome}>
          Welcome to React Native!
        </Text>
        <Text style={styles.instructions}>
          To get started, edit App.js
        </Text>
        <Text style={styles.instructions}>
          {instructions}
        </Text>
      </View>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'center',
    alignItems: 'center',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  instructions: {
    textAlign: 'center',
    color: '#333333',
    marginBottom: 5,
  },
});
