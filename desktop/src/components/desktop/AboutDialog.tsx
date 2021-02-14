import AppBar from '@material-ui/core/AppBar';
import Dialog from '@material-ui/core/Dialog';
import IconButton from '@material-ui/core/IconButton';
import ListItem from '@material-ui/core/ListItem';
import ListItemIcon from '@material-ui/core/ListItemIcon';
import ListItemText from '@material-ui/core/ListItemText';
import Slide, { SlideProps } from '@material-ui/core/Slide';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';
import CloseIcon from '@material-ui/icons/Close';
import InfoIcon from '@material-ui/icons/Info';
import React from 'react';
import ConfigUtils from '../../utils/ConfigUtils';
import NetworkUtils from '../../utils/NetworkUtils';
import { Container, Title } from '../primitives';

const Transition = React.forwardRef<unknown, SlideProps>((props, ref) => (
  <Slide direction='up' {...props} ref={ref} />
));

class AboutDialog extends React.Component<any, any> {
  state = {
    open: false
  };

  handleClickOpen = () => {
    this.setState({ open: true });
  }

  handleClose = () => {
    this.setState({ open: false });
  }

  render() {
    if (!NetworkUtils.foundIpAddress()) {
      return null;
    } else {
      return (
        <div>
          <ListItem button onClick={this.handleClickOpen}>
            <ListItemIcon>
              <InfoIcon />
            </ListItemIcon>
            <ListItemText primary='About' />
          </ListItem>
          <Dialog
            fullScreen
            open={this.state.open}
            onClose={this.handleClose}
            TransitionComponent={Transition}
            style={{ top: 30 }}
            hideBackdrop={true}>
            <AppBar style={{ position: 'relative' }}>
              <Toolbar>
                <IconButton color='inherit' onClick={this.handleClose} aria-label='Close'>
                  <CloseIcon />
                </IconButton>
                <Typography variant='h6' color='inherit' style={{ flex: 1 }}>
                  About
                </Typography>
              </Toolbar>
            </AppBar>
            <Container>
              <Title>Zephyr β</Title>
              <p>Version {ConfigUtils.getAppVersion()} ({ConfigUtils.getBuildType()})</p>
              <p>&copy; Thomas Gaubert</p>
            </Container>
          </Dialog>
        </div>
      );
    }
  }
}

export default AboutDialog;
