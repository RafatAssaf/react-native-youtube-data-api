

import React, {Component} from 'react';
import {
  StyleSheet, 
  Text, 
  View, 
  NativeModules, 
  TextInput,
  TouchableOpacity,
  ScrollView,
  StatusBar,
  Image
} from 'react-native';
const {YoutubeAPIModule} = NativeModules;

export default class App extends Component {
  
  constructor(props) {
    super(props)
    this.state = {
      query: 'Ed Sheeran',
      videos: []
    }
  }
  
  render() {
    return (
      <ScrollView>
        <StatusBar backgroundColor='steelblue' barStyle='light-content'/>
        <View style={styles.container}>
          <TextInput 
            value={this.state.query}
            onChangeText={(newMsg) => this.setState({ query: newMsg})}
            style={styles.msgInput}
          />
          <View style={styles.buttonsContainer}>
            <TouchableOpacity
              style={styles.button}
              onPress={() => {
                YoutubeAPIModule.toastAlert(this.state.query, YoutubeAPIModule.SHORT)
              }}
            >
              <Text style={styles.buttonText}>Display Msg</Text>
            </TouchableOpacity>
            <TouchableOpacity
              style={styles.button}
              onPress={() => {
                YoutubeAPIModule.setupYoutubeClient('AIzaSyBikULY-0gK5xbVt315i3Qt9QD5QGRrAnA');
              }}
            >
              <Text style={styles.buttonText}>Setup Client</Text>
            </TouchableOpacity>
            <TouchableOpacity
              style={styles.button}
              onPress={() => {
                YoutubeAPIModule.search(
                  this.state.query,
                  (res) => {
                    let response = JSON.parse(res);
                    console.log('searched successfully!', response)
                    this.setState({ videos: response.items })
                  }, 
                  (err) => {
                    console.log(err)
                  }
                )
              }}
            >
              <Text style={styles.buttonText}>Search query</Text>
            </TouchableOpacity>
          </View>

          {this.state.videos.map(vid => 
          <View
            key={vid.id.videoId}
            style={styles.videoContainer}
          >
            <Image source={{uri: vid.snippet.thumbnails.high.url}} resizeMethod='resize' resizeMode='cover' style={styles.videoThumb}/>
            <View style={styles.videoInfoContainer}>
              <Text>{vid.snippet.title}</Text>
            </View>
          </View>)}

        </View>
      </ScrollView>
    );
  }
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    justifyContent: 'flex-start',
    backgroundColor: '#F5FCFF',
  },
  welcome: {
    fontSize: 20,
    textAlign: 'center',
    margin: 10,
  },
  msgInput: {
    width: '100%', 
    height: 56,
    backgroundColor: 'steelblue',
    color: 'white',
    borderBottomRightRadius: 25,
    borderBottomLeftRadius: 25,
    paddingHorizontal: 15,
    marginBottom: 15
  },
  button: {
    minWidth: 150,
    height: 40,
    alignItems: 'center',
    justifyContent: 'center',
    borderRadius: 10,
    paddingHorizontal: 15,
    backgroundColor: 'forestgreen',
    elevation: 2,
    marginBottom: 15
  },
  buttonText: {
    color: 'white',
    fontSize: 20
  },
  buttonsContainer: {
    flexDirection: 'row',
    width: '100%',
    justifyContent: 'space-around',
    alignItems: 'center',
    flexWrap: 'wrap'
  },
  videoContainer: {
    width: '100%',
    // height: 100,
    padding: 10,
    marginBottom: 10,
    flexDirection: 'row',
    elevation: 1,
    backgroundColor: '#F5FCFF'
  },
  videoThumb: {
    height: 100,
    width: 100,
  },
  videoInfoContainer: {
    padding: 15,
  }
});
