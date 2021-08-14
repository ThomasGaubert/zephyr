import React from 'react';
import { connect } from 'react-redux';
import { Container, Title } from '../primitives';
import ConnectionInfo from './ConnectionInfo';

class Home extends React.Component<any, any> {
  render(): any {
    return (
      <Container>
        <Title>Zephyr β</Title>
        <ConnectionInfo/>
        {/* <DownloadLinks/> */}
      </Container>
    );
  }
}

export default connect()(Home);
