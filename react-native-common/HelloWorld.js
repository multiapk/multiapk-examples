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

import TitleBar from "./TitleBar";
import Banner from "./Banner";
import SiftListViewNew from "./widget/menu/SiftListViewNew";
import SiftListControl from "./widget/menu/SiftListControl";

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: 'steelblue',
        justifyContent: 'center',
        flexDirection: 'column',

        //区分平台
        ...Platform.select({
            ios: {
                marginTop: 22,
            },
            android: {
                marginTop: 0,
            },
        })
    },
});

class Test extends React.Component {
    constructor(props) {
        super(props);
        const ds = new ListView.DataSource({rowHasChanged: (r1, r2) => r1 !== r2});
        this.state = {
            dataSource: ds.cloneWithRows([
                'John', 'Joel', 'James', 'Jimmy', 'Jackson', 'Jillian', 'Julie', 'Devin'
            ])
        };
    }

    render() {
        let pic = {
            uri: 'https://upload.wikimedia.org/wikipedia/commons/d/de/Bananavarieties.jpg'
        };
        return (
            <View style={{
                height:100,
                backgroundColor: 'white',
                flexDirection: 'row'
            }}>
                <View style={{
                    flex:3,
                    height:40,
                    backgroundColor: '#0000ff',
                    borderBottomColor: '#ff0000',
                    borderBottomWidth: 5
                }}>
                    <TextInput
                        style={{
                            height:35,
                            color:'darkgray'
                        }}
                        underlineColorAndroid='#ffffff'
                        multiline={false}
                        placeholder="please input here"
                        placeholderTextColor='gray'
                    />
                </View>
                <Text style={{
                    flex:1,
                    color: 'black',
                    fontSize: 20,
                    textAlign: 'left',
                    backgroundColor: 'gray',
                    padding: 10
                }}>
                    props.name={this.props.name}{'\n'}
                    props.mao={this.props.mao}
                </Text>
                <Image
                    style={{ flex:0,width: 100, height: 100}}
                    source={pic}
                />
            </View>
        )
    }
}

export default class HelloWorld extends React.Component {
    constructor(props) {
        super(props);
        const ds = new ListView.DataSource({rowHasChanged: (r1, r2) => r1 !== r2});
        this.state = {
            dataSource: ds.cloneWithRows([
                '第0列', '第1列', '第2列', '第3列', '第4列', '第5列', '第6列', '第7列', '第8列', '第9列',
                '第10列', '第11列', '第12列', '第13列', '第14列', '第15列', '第16列', '第17列', '第18列', '第19列'
            ])
        };
    }

    render() {
        const {items, subItemStyle} = {
            items: [{
                title: '交易方向1',
                tag: 0,
                icon: require('./images/btn_down.svg'),
                list: [],
            }]
        };
        return (
            <View style={styles.container}>
                <Navigator
                    style={{
                        flex: 0
                    }}
                    initialRoute={{statusBarHidden: true}}
                    renderScene={(route, navigator) =>
                        <View>
                            <StatusBar
                                animated={true}
                                hidden={false}
                                backgroundColor={'blue'}
                                translucent={true}
                                barStyle={'default'}
                                showHideTransition={'fade'}
                                networkActivityIndicatorVisible={true}
                            />
                        </View>
                    }
                />
                <TitleBar/>

                <SiftListControl/>

                <View style={{
                    flex: 1,
                    backgroundColor: 'steelblue',
                    justifyContent: 'center',
                    flexDirection: 'row'
                    }}>
                    <ScrollView style={{flex: 1}}>
                        <Text style={{
                            color: 'black',
                            backgroundColor: 'powderblue',
                            fontSize: 20,
                            textAlign: 'center',
                            padding: 10
                        }}>
                            《念奴娇·赤壁怀古》
                        </Text>

                        <Text style={{
                            color: 'darkorange',
                            backgroundColor: 'powderblue',
                            fontFamily: 'Georgia',
                            fontStyle: 'italic',
                            fontSize: 14,
                            textAlign: 'left',
                            paddingLeft: 10,
                            paddingRight: 10,
                            paddingBottom: 10
                        }}>
                            大江东去，浪淘尽，千古风流人物。故垒西边，人道是，三国周郎赤壁。乱石穿空，惊涛拍岸，卷起千堆雪。江山如画，一时多少豪杰。遥想公瑾当年，小乔初嫁了(liǎo)，雄姿英(yīng)发。羽扇纶(guān)巾，谈笑间，樯橹灰飞烟灭。故国神游，多情应笑我，早生华(huā)发。人生如梦，一尊还酹(lèi)江月。
                        </Text>

                        <Text style={{
                            color: 'gray',
                            backgroundColor: 'powderblue',
                            fontFamily: 'Georgia',
                            fontStyle: 'italic',
                            fontSize: 12,
                            textAlign: 'left',
                            paddingLeft: 10,
                            paddingRight: 10,
                            paddingBottom: 10
                        }}>
                            赏析{'\n'}
                            此词怀古抒情，写自己消磨壮心殆尽，转而以旷达之心关注历史和人生。上阕以描写赤壁矶风起浪涌的自然风景为主，意境开阔博大，感慨隐约深沉。起笔凌云健举，包举有力。将浩荡江流与千古人事并收笔下。
                            千古风流人物既被大浪淘尽，则一己之微岂不可悲？然而苏轼却另有心得：既然千古风流人物也难免如此，那么一己之荣辱穷达复何足悲叹！人类既如此殊途而同归，则汲汲于一时功名，不免过于迂腐了。接下两句切入怀古主题，专说三国赤壁之事。"人道是"三字下得极有分寸。赤壁之战的故地，争议很大。一说在今湖北蒲圻县境内，已改为赤壁市。但今湖北省内有四处地名同称赤壁者，另三处在黄冈、武昌、汉阳附近。苏轼所游是黄冈赤壁，他似乎也不敢肯定，所以用"人道是"三字引出以下议论。
                            "乱石"以下五句是写江水腾涌的壮观景象。其中"穿"、"拍"、"卷"等动词用得形象生动。"江山如画"是写景的总括之句。"一时多少豪杰"则又由景物过渡到人事。
                            苏轼重点要写的是"三国周郎"，故下阕便全从周郎引发。换头五句写赤壁战争。与周瑜的谈笑论战相似，作者描写这么一场轰轰烈烈的战争也是举重若轻，闲笔纷出。从起句的"千古风流人物"到"一时多少豪杰"再到"遥想公瑾当年"，视线不断收束，最后聚焦定格在周瑜身上。然而写周瑜却不写其大智大勇，只写其儒雅风流的气度。
                            不留意的人容易把"羽扇纶巾"看作是诸葛亮的代称，因为诸葛亮的装束素以羽扇纶巾著名。但在三国之时，这是儒将通常的装束。宋人也多以"羽扇"代指周瑜，如戴复古《赤壁》诗云："千载周公瑾，如其在目前。英风挥羽扇，烈火破楼船。"
                            苏轼在这里极言周瑜之儒雅淡定，但感情是复杂的。"故国"两句便由周郎转到自己。周瑜破曹之时年方三十四岁，而苏轼写作此词时年已四十七岁。孔子曾说："四十五十而无闻焉，斯亦不足畏也已。"苏轼从周瑜的年轻有为，联想到自己坎坷不遇，故有"多情应笑我"之句，语似轻淡，意却沉郁。但苏轼毕竟是苏轼，他不是一介悲悲戚戚的寒儒，而是参破世间宠辱的智者。所以他在察觉到自己的悲哀后，不是像南唐李煜那样的沉溺苦海，自伤心志，而是把周瑜和自己都放在整个江山历史之中进行观照。在苏轼看来，当年潇洒从容、声名盖世的周瑜现今又如何呢?不是也被大浪淘尽了吗。这样一比，苏轼便从悲哀中超脱了。"人生到处知何似，应似飞鸿踏雪泥。泥上偶然留指爪，鸿飞哪复计东西"(《和子由渑池怀旧》)。所以苏轼在与周瑜作了一番比较后，虽然也看到了自己的政治功业无法与周瑜媲美，但上升到整个人类的发展规律和普遍命运，双方其实也没有什么大的差别。有了这样深沉的思索，遂引出结句"人间如梦，一樽还酹江月"的感慨。正如他在《西江月》词中所说的那样："世事一场大梦，人生几度秋凉。"消极悲观不是人生的真谛，超脱飞扬才是生命的壮歌。既然人间世事恍如一梦，何妨将樽酒洒在江心明月的倒影之中，脱却苦闷，从有限中玩味无限，让精神获得自由。其同期所作的《赤壁赋》于此说得更为清晰明断："惟江上之清风，与山间之明月，耳得之而为声，目遇之而成色。取之无禁，用之不竭，是造物者之无尽藏也，而吾与子之所共适也。"这种超然远想的文字，宛然是《庄子?齐物论》思想的翻版。但庄子以此回避现实，苏轼则以此超越现实。
                            黄州数年是苏轼思想发生转折的时期，也是他不断走向成熟和睿智的时期，他以此保全自己的岸然人格，也以此养护自己淳至的精神。这首《念奴娇》词及其作于同一时期的数篇诗文，都为我们透示了其中的端倪。
                            此词自问世后，经历了两种截然不同的命运，誉之者如胡仔《苕溪渔隐丛话》称其"语意高妙，真古今绝唱"。贬之者如俞文豹《吹剑续录》所云："东坡在玉堂，有幕士善讴。因问：'我词比柳七何如?'对曰：'柳郎中词，只好合十七八女孩儿，执红牙板，歌'杨柳岸晓风残月'。学士词，须关西大汉，执铁板，唱'大江东去'。公为之绝倒。"幕士的言论表面上是从演唱风格上区分了柳、苏二家词风的不同，但暗含有对苏词悖离传统词风的揶揄。清代更有人认为此词"平仄句调都不合格"(丁绍仪《听秋声馆词话》)，朱彝尊《词综》并详加辩证，亦可谓吹毛求疵者。
                            《念奴娇》是苏轼贬官黄州后的作品。苏轼21岁中进士，30岁以前绝大部分时间过着书房生活，仕途坎坷，随着北宋政治风浪，几上几下。43岁(元丰二年)时因作诗讽刺新法，被捕下狱，出狱后贬官为黄州团练副使。这是个闲职，他在旧城营地辟畦耕种，游历访古，政治上失意，滋长了他逃避现实和怀才不遇的思想情绪，但由于他豁达的胸怀，在祖国雄伟的江山和历史风云人物的激发下，借景抒情，写下了一系列脍炙人口的名篇，此词为其代表。
                            《念奴娇》词分上下两阙。上阙咏赤壁，下阙怀周瑜，并怀古伤己，以自身感慨作结。
                            作者吊古伤怀，想古代豪杰，借古传颂之英雄业绩，思自己历遭之挫折。不能建功立业，壮志难酬，词作抒发了他内心忧愤的情怀。
                            上阙咏赤壁，着重写景，为描写人物作烘托。前三句不仅写出了大江的气势，而且把千古英雄人物都概括进来，表达了对英雄的向往之情。假借“人道是”以引出所咏的人物。“乱”“穿”“惊”“拍”“卷”等词语的运用，精妙独到地勾画了古战场的险要形势，写出了它的雄奇壮丽景象，从而为下片所追怀的赤壁大战中的英雄人物渲染了环境气氛。
                            下阙着重写人，借对周瑜的仰慕，抒发自己功业无成的感慨。写“小乔”在于烘托周瑜才华横溢、意气风发，突出人物的风姿，中间描写周瑜的战功意在反衬自己的年老无为。“多情”后几句虽表达了伤感之情，但这种感情其实正是词人不甘沉沦，积极进取，奋发向上的表现，仍不失英雄豪迈本色。
                            用豪壮的情调书写胸中块垒。
                            诗人是个旷达之人，尽管政治上失意，却从未对生活失去信心。这首词就是他这种复杂心情的集中反映，词中虽然书写失意，然而格调是豪壮的，跟失意文人的同主题作品显然不同。词作中的豪壮情调首先表现在对赤壁景物的描写上。长江的非凡气象，古战场的险要形势都给人以豪壮之感。周瑜的英姿与功业无不让人艳羡。
                        </Text>
                    </ScrollView>
                    <ListView
                        style={{flex: 1,backgroundColor: 'green'}}
                        dataSource={this.state.dataSource}
                        renderRow={(rowData) =>
                        <TouchableHighlight underlayColor='#99d9f4' onPress={()=>{console.log("clicked")}}>
                            <Text
                                style={{color: 'lightyellow',padding:10,justifyContent: 'center',alignItems: 'center'}}>
                                {rowData}
                            </Text>
                        </TouchableHighlight>
                        }

                    />
                </View>
                <View style={{
                    flex: 0,
                    backgroundColor: 'black',
                    }}>
                    <ScrollView
                        bouncesZoom={false}
                        alwaysBounceHorizontal={true}
                        style={{flex:0,backgroundColor:"#ff0000"}}
                        horizontal={true}>

                        <Image
                            style={{width: 100, height: 100, resizeMode: 'cover' ,backgroundColor:'gray'}}
                            source={{uri: 'https://facebook.github.io/react/img/logo_og.png'}}
                        />
                        <Image
                            style={{width: 100, height: 100, resizeMode: 'cover' ,backgroundColor:'gray'}}
                            source={{uri: 'https://odw6aoxik.bkt.clouddn.com/avatar_cartoon_1.jpg'}}
                        />
                        <Image
                            style={{width: 100, height: 100, resizeMode: 'cover' ,backgroundColor:'gray'}}
                            source={{uri: 'https://facebook.github.io/react/img/logo_og.png'}}
                        />
                        <Image
                            style={{width: 100, height: 100, resizeMode: 'cover' ,backgroundColor:'gray'}}
                            source={{uri: 'https://odw6aoxik.bkt.clouddn.com/avatar_cartoon_1.jpg'}}
                        />
                        <Image
                            style={{width: 100, height: 100, resizeMode: 'cover' ,backgroundColor:'gray'}}
                            source={{uri: 'https://facebook.github.io/react/img/logo_og.png'}}
                        />
                        <Image
                            style={{width: 100, height: 100, resizeMode: 'cover' ,backgroundColor:'gray'}}
                            source={{uri: 'https://odw6aoxik.bkt.clouddn.com/avatar_cartoon_1.jpg'}}
                        />
                        <Image
                            style={{width: 100, height: 100, resizeMode: 'cover' ,backgroundColor:'gray'}}
                            source={{uri: 'https://facebook.github.io/react/img/logo_og.png'}}
                        />
                        <Image
                            style={{width: 100, height: 100, resizeMode: 'cover' ,backgroundColor:'gray'}}
                            source={{uri: 'https://odw6aoxik.bkt.clouddn.com/avatar_cartoon_1.jpg'}}
                        />
                    </ScrollView>
                </View>
                <Test style={{flex:0
                }} name="test" mao="kangren"/>

                <Banner style={{flex:0}}/>

                <Text
                    style={{borderRadius:10,borderWidth:1,borderColor:"red",position:'absolute',marginTop:300,marginLeft:155,width:100,height:100,color:'red',backgroundColor:'#00000077',justifyContent:"center",alignItems:"center"}}>100x100</Text>
            </View>
        )
    }
}