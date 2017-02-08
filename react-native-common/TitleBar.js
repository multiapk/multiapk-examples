'use strict';
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

import  React, {Component, PropTypes} from 'react';

export default class TitleBar extends React.Component {
    static propTypes = {
        title: PropTypes.string.isRequired,
        onForward: PropTypes.func.isRequired,
        onBack: PropTypes.func.isRequired,
    }

    render() {
        return (
            <View>
                <View
                    style={{flexDirection:"row",height:48,backgroundColor:"#111",justifyContent:"space-between",alignItems:"center"}}>
                    <TouchableHighlight onPress={this.props.onBack}>
                        <Image
                            style={{width: 36, height: 36,marginLeft:5}}
                            source={require("./images/ic_menu_back.png")}
                        />
                    </TouchableHighlight>

                    <Text style={{fontSize:20,color:"white",fontWeight:"bold"}}>{this.props.title}</Text>

                    <TouchableHighlight onPress={this.props.onForward}>
                        <Image
                            onPress={this.props.onForward}
                            style={{width: 36, height: 36, resizeMode: 'cover',marginRight:5}}
                            source={require("./images/ic_menu_forward.png")}
                        />
                    </TouchableHighlight>
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