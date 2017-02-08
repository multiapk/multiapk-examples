'use strict'

import React, {Component, PropTypes} from 'react';

import {
    NativeModules,
    StyleSheet,
    Dimensions,
    View,
    Text,
    ListView,
    TouchableWithoutFeedback,
    TouchableWithNativeFeedback,
    TouchableOpacity,
    TouchableHighlight,
    Modal,
    ActivityIndicator,
} from 'react-native';

import SiftListViewNew from "./SiftListViewNew"

export default class SiftListControl extends React.Component {
    static defaultProps = {
        items: [
            {
                title: '水果',
                tag: 0,
                icon: require('../../images/btn_down.svg'),
                list: ["苹果", "香蕉", "火龙果", "红蛇果"],
            },
            {
                title: '手机',
                tag: 1,
                icon: require('../../images/btn_down.svg'),
                list: ["华为", "小米", "苹果", "魅族"],
            },
            {
                title: '家电',
                tag: 2,
                icon: require('../../images/btn_down.svg'),
                list: ["洗衣机", "冰箱", "空调", "电饭煲"],
            }
            ,
            {
                title: '电脑',
                tag: 3,
                icon: require('../../images/btn_down.svg'),
                list: ["DELL", "MAC", "MAC BOOK", "LENOVO"],
            }
        ]
    };

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
                                style={{backgroundColor:'white',width:100}}
                                item={item}
                                key={i}
                                selectedCallBack={this._selectedIndex}>
                            </SiftListViewNew> )
                    })
                }
            </View>
        );
    }
}
const styles = StyleSheet.create({listBar: {height: 32, flexDirection: 'row',}});