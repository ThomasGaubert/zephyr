import React from 'react';
import { connect } from 'react-redux';
import { Container, Title } from '../primitives';

class Unknown extends React.Component<any, any> {
  render(): any {
    return (
      <Container>
        <Title>Unknown Page</Title>
      </Container>
    );
  }
}

export default connect()(Unknown);
