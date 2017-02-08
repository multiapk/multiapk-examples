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
    View
} from 'react-native';

import Swiper from 'react-native-swiper';

const styles = StyleSheet.create({
    slide1: {
        height: 100,
        backgroundColor: '#FF33AA',
        alignItems: "center",
        justifyContent: "center"
    },
    slide2: {
        height: 100,
        backgroundColor: '#00ff00',
        alignItems: "center",
        justifyContent: "center"
    },
    slide3: {
        height: 100,
        backgroundColor: '#92BBD9',
        alignItems: "center",
        justifyContent: "center"
    },
    text: {
        color: '#fff',
        fontSize: 30,
        fontWeight: 'bold',
    },
});

export default class Banner extends React.Component {
    render() {
        return (
            <View style={{height:100}}>
                <Swiper showsButtons={false}>
                    <View style={styles.slide1}>
                        <Text style={styles.text}>Hello Swiper</Text>
                    </View>
                    <View style={styles.slide2}>
                        <Text style={styles.text}>Beautiful</Text>
                    </View>
                    <View style={styles.slide3}>
                        <Text style={styles.text}>And simple</Text>
                    </View>
                </Swiper>
            </View>
        )
    }
}