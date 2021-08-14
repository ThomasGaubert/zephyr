import { createMuiTheme, MuiThemeProvider } from '@material-ui/core/styles';
import electron from 'electron';
import { forwardToMain, replayActionRenderer } from 'electron-redux';
import React, { Component } from 'react';
import { TitleBar, Window } from 'react-desktop/windows';
import { Provider } from 'react-redux';
import { applyMiddleware, compose, createStore } from 'redux';
import { ThemeProvider } from 'styled-components';
import RootReducer from '../../reducers/RootReducer';
import { GlobalStyle, ZephyrDark } from '../../styles/Global';
import ZephyrClient from '../common/ZephyrClient';
import ContentView from './ContentView';
import NavigationBar from './NavigationBar';
import Toaster from './Toaster';

class DesktopApp extends Component<Props> {
  static defaultProps: Props = {
    backgroundColor: '#0D253A',
    toolbarColor: '#091B2A',
    theme: 'dark'
  };

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

  composeEnhancers = (window as any)?.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ || compose;
  store = createStore(RootReducer, this.composeEnhancers(applyMiddleware(forwardToMain)));

  remote = electron.remote;
  minimize = (): void => this.remote.getCurrentWindow().minimize();
  close = (): void => this.remote.getCurrentWindow().close();

  render(): any {
    replayActionRenderer(this.store);
    return (
      <Provider store={this.store}>
        <ThemeProvider theme={ZephyrDark}>
          <MuiThemeProvider theme={DesktopApp.theme}>
            <Window
              color={this.props.backgroundColor}
              background={this.props.backgroundColor}
              theme={this.props.theme}
              style={{ height: '100%' }}
              chrome>
              <GlobalStyle/>
              <TitleBar
                title='Zephyr β'
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

interface Props {
  backgroundColor: string;
  theme: string;
  toolbarColor: string;
}

export default DesktopApp;
