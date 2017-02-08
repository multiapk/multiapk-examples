'use strict';
import React from 'react';

//noinspection JSUnresolvedVariable
import {
    AppRegistry,
    StyleSheet,
    Platform,
    Text,
    TextInput,
    TouchableHighlight,
    ListView,
    Navigator,
    StatusBar,
    ScrollView,
    Image,
    View,
    Dimensions,
    PixelRatio
} from 'react-native';

export default class TitleBar extends React.Component {
    render() {
        return (
            <View >
                <View style={{height:48,backgroundColor:"#111",justifyContent:"center",alignItems:"center"}}>
                    <Text style={{fontSize:20,color:"white",fontWeight:"bold"}}>REACT NATIVE</Text>
                </View>
                <Text
                    style={{backgroundColor:"gray"}}>
                    DEVICE INFORMATION:{'\n'}
                    window.width={Dimensions.get('window').width }{'\n'}
                    window.height={Dimensions.get('window').height }{'\n'}
                    pxielRatio={PixelRatio.get()}
                </Text>
            </View>
        )
    }
}