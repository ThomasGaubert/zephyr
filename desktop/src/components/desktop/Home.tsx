import React from 'react';
import { connect } from 'react-redux';
import { Container, Title } from '../primitives';
import ConnectionInfo from './ConnectionInfo';

class Home extends React.Component<any, any> {
  render() {
    return (
      <Container>
        <Title>Zephyr</Title>
        <ConnectionInfo/>
        {/* <DownloadLinks/> */}
      </Container>
    );
  }
}

export default connect()(Home);
