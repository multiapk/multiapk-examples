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
    listBar: {height: 32, flexDirection: 'row',}
});


class SiftListControl extends React.Component {
    static defaultProps = {items: [{title: '交易方向', tag: 0, icon: require('./images/btn_down.svg'), list: [],}]};

    constructor() {
        super();
        this.state = {};
    }

    _selectedIndex = (index, tag) => {
        const {callBack}=this.props;
        callBack && callBack(index, tag);
    };

    render() {
        const {items, subItemStyle}=this.props;
        return (
            <View style={[styles.listBar,this.props.style]}>
                {
                    items.map((item, i) => {
                        return (
                            <SiftListViewNew
                                style={{backgroundColor:'white',width:subItemStyle.width}}
                                item={item}
                                key={i}
                                selectedCallBack={this._selectedIndex}>
                            </SiftListViewNew>
                        )
                    })
                }
            </View>
        );
    }
}

export default class Menu extends React.Component {
    _renderButton = () => {
        const {item, textStyle, style}=this.props;
        const {showSiftList}=this.state;
        let icon = showSiftList ? require('./images/icon_up.svg') : require('./images/btn_down.svg');
        return (
            <TouchableOpacity onPress={this._onButtonPress}>
                <View style={[styles.button,style]}>
                    <Text
                        style={[styles.buttonText, textStyle]} numberOfLines={1}> {item.title}
                    </Text>
                    <SvgImage
                        style={{marginLeft:4}} height={6} source={icon}/>
                </View>
            </TouchableOpacity>
        );
    };
    _renderModal = () => {
        const {showSiftList, selectedIndex}=this.state;
        const {style, item}=this.props;
        if (showSiftList && this._buttonFrame) {
            let frameStyle = this._calculatePosition();
            return (
                <Modal
                    animationType='fade'
                    transparent={true}>

                    <TouchableWithoutFeedback
                        onPress={this._onModalPress}>
                        <View
                            style={[styles.modal]}>
                            <View
                                style={[frameStyle,styles.dropdown,{height:item.list?30*item.list.length:0},{width:style.width}]}>
                                { item.list ? item.list.map((sublist, i) => {
                                        return (
                                            <TouchableOpacity onPress={()=>this.select(i)} key={i}>
                                                <View
                                                    style={[styles.subItemStyle,{width:style.width-1},selectedIndex===i&&{backgroundColor:System_styles.hei_240}]}>
                                                    <Text
                                                        style={[styles.rowText,selectedIndex===i&&{color:System_styles.blue}]}>
                                                        {sublist}
                                                    </Text>
                                                </View>
                                            </TouchableOpacity>
                                        )
                                    }) : null}
                            </View>
                        </View>
                    </TouchableWithoutFeedback>
                </Modal>
            );
        }
    };
    show = () => {
        this._updatePosition(() => {
            this.setState({showSiftList: true,});
        });
    };
    hide = () => {
        this.setState({showSiftList: false,});
    };
    select = (index) => {
        const {item, selectedCallBack}=this.props;
        const {selectedIndex}=this.state;
        if (index == null || item.list == null || index >= item.list.length) {
            index = selectedIndex;
        }
        this.setState({selectedIndex: index,});
        selectedCallBack && selectedCallBack(index, item.tag);
        this.hide();
    };

    render() {
        return (
            <View style={{height:100}} {...this.props}>
                {this._renderButton()}
                {this._renderSiftList()}
            </View>
        )
    }
}