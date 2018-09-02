import React, { Component } from 'react';
import { Window, TitleBar } from 'react-desktop/windows';
import { Provider } from 'react-redux'
import { createStore } from 'redux'
import { ThemeProvider } from 'styled-components';
import RootReducer from '../../reducers/RootReducer'
import { MuiThemeProvider, createMuiTheme } from '@material-ui/core/styles';
import NavigationBar from './NavigationBar';
import ContentView from './ContentView';
import { ZephyrDark } from '../../styles/Global';
import Toaster from './Toaster';
import ZephyrClient from '../common/ZephyrClient';

class DesktopApp extends Component<any, any> {
  static defaultProps = {
    backgroundColor: '#0D253A',
    toolbarColor: '#091B2A',
    theme: 'dark'
  };

  static enhancer = window['devToolsExtension'] ? window['devToolsExtension']()(createStore) : createStore;
  static store = DesktopApp.enhancer(RootReducer);

  static theme = createMuiTheme({
    palette: {
      type: 'dark',
      primary: {
        main: '#0D253A'
      },
      secondary: {
        main: '#091B2A'
      }
    },
    overrides: {
      MuiBottomNavigation: {
        root: {
          backgroundColor: '#091B2A'
        }
      },
      MuiBottomNavigationAction: {
        root: {
          '&$selected': {
            color: '#FFFFFF'
          }
        }
      },
      MuiSnackbar: {
        root: {
          marginBottom: 56
        }
      },
      MuiSnackbarContent: {
        root: {
          paddingTop: '0px',
          paddingBottom: '0px'
        }
      }
    }
  });

  remote = require('electron').remote;
  minimize = () => this.remote.getCurrentWindow().minimize();
  close = () => this.remote.getCurrentWindow().close();

  render() {
    return (
      <Provider store={DesktopApp.store}>
        <ThemeProvider theme={ZephyrDark}>
          <MuiThemeProvider theme={DesktopApp.theme}>
            <Window
              color={this.props.backgroundColor}
              background={this.props.backgroundColor}
              theme={this.props.theme}
              style={{height: '100%'}}
              chrome>
              <TitleBar
                title="Zephyr"
                background={this.props.toolbarColor}
                onMinimizeClick={this.minimize}
                onCloseClick={this.close}
                style={{ zIndex: 999999 }}
                controls/>
              <ContentView/>
              <Toaster/>
              <ZephyrClient/>
              <NavigationBar/>
            </Window>
          </MuiThemeProvider>
        </ThemeProvider>
      </Provider>
    );
  }
}

export default DesktopApp;
