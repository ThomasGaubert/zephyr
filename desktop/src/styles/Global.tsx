import { createGlobalStyle } from 'styled-components';

export const GlobalStyle = createGlobalStyle`
  @font-face {
    font-family: Righteous;
    src: url('./assets/fonts/Righteous-Regular.ttf');
    font-weight: normal;
    font-style: normal;
  }
  @font-face {
    font-family: Roboto;
    src: url('./assets/fonts/Roboto-Regular.ttf');
    font-weight: normal;
    font-style: normal;
  }
  @font-face {
    font-family: Roboto;
    src: url('./assets/fonts/Roboto-Italic.ttf');
    font-weight: normal;
    font-style: italic;
  }
  @font-face {
    font-family: Roboto;
    src: url('./assets/fonts/Roboto-Bold.ttf');
    font-weight: bold;
    font-style: normal;
  }
  * {
    font-family: Roboto, sans-serif;
  }
  body {
    padding: 0;
    margin: 0;
    color: #ffffff;
  }
  html {
      overflow: scroll;
      overflow-x: hidden;
  }
  ::-webkit-scrollbar {
      width: 0px;
      background: transparent;
  }
  a[title="Maximize"] {
    display: none !important;
  }
`;

// injectGlobal`
//   @font-face {
//     font-family: Righteous;
//     src: url('./assets/fonts/Righteous-Regular.ttf');
//     font-weight: normal;
//     font-style: normal;
//   }
//   @font-face {
//     font-family: Roboto;
//     src: url('./assets/fonts/Roboto-Regular.ttf');
//     font-weight: normal;
//     font-style: normal;
//   }
//   @font-face {
//     font-family: Roboto;
//     src: url('./assets/fonts/Roboto-Italic.ttf');
//     font-weight: normal;
//     font-style: italic;
//   }
//   @font-face {
//     font-family: Roboto;
//     src: url('./assets/fonts/Roboto-Bold.ttf');
//     font-weight: bold;
//     font-style: normal;
//   }
//   * {
//     font-family: Roboto, sans-serif;
//   }
//   body {
//     padding: 0;
//     margin: 0;
//     color: #ffffff;
//   }
//   html {
//       overflow: scroll;
//       overflow-x: hidden;
//   }
//   ::-webkit-scrollbar {
//       width: 0px;
//       background: transparent;
//   }
//   a[title="Maximize"] {
//     display: none !important;
//   }
// `;

export const ZephyrDark = {
  primaryColor: '#0D253A',
  secondaryColor: '#091B2A',
  textColor: '#FFFFFF'
};
