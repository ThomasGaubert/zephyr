import { createMuiTheme, MuiThemeProvider } from '@material-ui/core/styles';
import React, { Component } from 'react';
import { Provider } from 'react-redux';
import { createStore } from 'redux';
import { ThemeProvider } from 'styled-components';
import RootReducer from '../../reducers/RootReducer';
import { ZephyrDark } from '../../styles/Global';
import ZephyrClient from '../common/ZephyrClient';
import BigHistory from './BigHistory';

class OverlayApp extends Component<any, any> {
  static defaultProps = {
    backgroundColor: '#0D253A',
    toolbarColor: '#091B2A',
    theme: 'dark'
  };

  static enhancer = window['devToolsExtension'] ? window['devToolsExtension']()(createStore) : createStore;
  static store = OverlayApp.enhancer(RootReducer);

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
      <Provider store={OverlayApp.store}>
        <ThemeProvider theme={ZephyrDark}>
          <MuiThemeProvider theme={OverlayApp.theme}>
            <BigHistory/>
            <ZephyrClient/>
          </MuiThemeProvider>
        </ThemeProvider>
      </Provider>
    );
  }
}

export default OverlayApp;
