import React from 'react';
import {
    StyleSheet,
    View,
    Text,
    TextInput,
    TouchableOpacity,
    ScrollView
} from 'react-native';

export default class SlidSegmentedControl extends React.Component {
    static defaultProps = {
        items: ['s1', 's2'],
        selectedClick: null,
        itemWidth: 100
    };

    constructor() {
        super();
        this.state = {
            selectedIndex: 0
        };
    }


    reset = () => {
        this.setState({
            selectedIndex: 0,
        });
    }

    _selected = (index) => {
        if (index === this.state.selectedIndex)
            return;

        const {selectedClick}= this.props
        selectedClick && selectedClick(index)
        this.setState({
            selectedIndex: index
        })
    }

    _renderItem = (item, i) => {
        const {selectedIndex} = this.state
        const {itemWidth, showLine}= this.props

        return (
            <TouchableOpacity
                activeOpacity={0.8}
                onPress={()=>this._selected(i)}
                style={{width:itemWidth}}
                key={i}
            >
                <View
                    style={[{width:itemWidth ,justifyContent:'center',alignItems:'center'}]}
                >
                    <Text style={[styles.textNormol,i===selectedIndex&&styles.textSelected]}>
                        {item}
                    </Text>
                    {showLine !== undefined ? (
                            <View
                                style={[{width:itemWidth,height:3,borderRadius:1.5},i===selectedIndex&&styles.lineSelect]}
                            />
                        ) : null}

                </View>
            </TouchableOpacity>
        )
    }

    render() {
        const {items} = this.props

        return (
            <View
                style={[styles.container,this.props.style]}
            >
                <ScrollView
                    showsHorizontalScrollIndicator={false}
                    horizontal={true}
                >
                    {items.map((item, i) => {
                        return this._renderItem(item, i)
                    })}
                </ScrollView>


            </View>
        )
    }
}

const styles = StyleSheet.create({
    container: {
        justifyContent: 'center',
        alignItems: 'center',
        flexDirection: 'row'
    },
    textNormol: {textAlign: 'center'},
    textSelected: {
        color: "blue"
    },
    itemCon: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center'
    },
    lineNor: {},
    lineSelect: {
        backgroundColor: "#0000ff"
    }
});