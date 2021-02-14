import { createMuiTheme, MuiThemeProvider } from '@material-ui/core/styles';
import { forwardToMain, replayActionRenderer } from 'electron-redux';
import React, { Component } from 'react';
import { Provider } from 'react-redux';
import { applyMiddleware, compose, createStore } from 'redux';
import { ThemeProvider } from 'styled-components';
import RootReducer from '../../reducers/RootReducer';
import { GlobalStyle, ZephyrDark } from '../../styles/Global';
import ZephyrClient from '../common/ZephyrClient';
import BigHistory from './BigHistory';

class OverlayApp extends Component<any, any> {
  static defaultProps = {
    backgroundColor: '#0D253A',
    toolbarColor: '#091B2A',
    theme: 'dark'
  };

  static composeEnhancers = (window as any)?.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;
  static store = createStore(RootReducer, OverlayApp.composeEnhancers(applyMiddleware(forwardToMain)));

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
    replayActionRenderer(OverlayApp.store);
    return (
      <Provider store={OverlayApp.store}>
        <ThemeProvider theme={ZephyrDark}>
          <MuiThemeProvider theme={OverlayApp.theme}>
            <GlobalStyle/>
            <BigHistory/>
            <ZephyrClient/>
          </MuiThemeProvider>
        </ThemeProvider>
      </Provider>
    );
  }
}

export default OverlayApp;
