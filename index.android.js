'use strict';

import React from 'react';
import {
  AppRegistry,
  StyleSheet,
  Text,
  TextInput,
  ScrollView,
  Image,
  View
} from 'react-native';

class Test extends React.Component {
constructor(props) {
    super(props);
    this.state = {text: ''};
  }
  render() {
    let pic = {
        uri: 'https://upload.wikimedia.org/wikipedia/commons/d/de/Bananavarieties.jpg'
     };
    return (
      <View style={styles.container2}>
      <TextInput
                style={{height: 40}}
                placeholder="Type here to translate!"
                onChangeText={(text) => this.setState({text})}
              />
      <Text>
        props.name={this.props.name}
        props.mao={this.props.mao}
        {'\n'}
        {this.state.text.split(' ').map((word) => word && ' ').join(' ')}
      </Text>
      <Image
              style={{width: 50, height: 50}}
              source={pic}
            />
      </View>
    )
  }
}
class HelloWorld extends React.Component {
  render() {
    return (
      <View style={styles.container}>
      <ScrollView style={{flex: 1}}>
      <Text style={styles.textStyle}>hi , my name is michael mao mao !{'\n'} nice to meet you now !</Text>
      <Text style={styles.textStyle}>hi , my name is michael mao mao !{'\n'} nice to meet you now !</Text>
      <Text style={styles.textStyle}>hi , my name is michael mao mao !{'\n'} nice to meet you now !</Text>
      <Text style={styles.textStyle}>hi , my name is michael mao mao !{'\n'} nice to meet you now !</Text>
      <Text style={styles.textStyle}>hi , my name is michael mao mao !{'\n'} nice to meet you now !</Text>
      <Text style={styles.textStyle}>hi , my name is michael mao mao !{'\n'} nice to meet you now !</Text>
      <Text style={styles.textStyle}>hi , my name is michael mao mao !{'\n'} nice to meet you now !</Text>
      <Text style={styles.textStyle}>hi , my name is michael mao mao !{'\n'} nice to meet you now !</Text>
      <Text style={styles.textStyle}>hi , my name is michael mao mao !{'\n'} nice to meet you now !</Text>
      </ScrollView>

         <View style={{flex: 1, flexDirection: 'row',  justifyContent: 'center', alignItems: 'center'}}>
         <Image
            style={{width: 50, height: 50}}
            source={{uri: 'https://facebook.github.io/react/img/logo_og.png'}}
          />
        <Image
            style={{ width: 100, height: 100}}
            source={{uri: 'http://odw6aoxik.bkt.clouddn.com/avatar_cartoon_1.jpg'}}
            />
      </View>
            <Test name="test" mao="kangren"/>
      </View>
    )
  }
}
var styles = StyleSheet.create({
  container: {
    flex: 1,
    backgroundColor: 'steelblue',
    justifyContent: 'center'//,
  },
    container2: {
      flex: 1,
      backgroundColor: 'skyblue',
      justifyContent: 'center'//,
    },
  textStyle: {
    backgroundColor: 'powderblue',
    fontSize: 20,
    textAlign: 'center',
    padding: 10,
  },
});






AppRegistry.registerComponent('HelloWorld', () => HelloWorld);