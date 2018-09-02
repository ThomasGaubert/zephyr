import React from 'react';
import { connect } from 'react-redux';
import classNames from 'classnames';
import CheckCircleIcon from '@material-ui/icons/CheckCircle';
import ErrorIcon from '@material-ui/icons/Error';
import InfoIcon from '@material-ui/icons/Info';
import CloseIcon from '@material-ui/icons/Close';
import green from '@material-ui/core/colors/green';
import amber from '@material-ui/core/colors/amber';
import IconButton from '@material-ui/core/IconButton';
import Snackbar from '@material-ui/core/Snackbar';
import SnackbarContent from '@material-ui/core/SnackbarContent';
import WarningIcon from '@material-ui/icons/Warning';
import { withStyles } from '@material-ui/core/styles';
import Fade from '@material-ui/core/Fade';
import IStoreState from '../../store/IStoreState';
import ActionTypeKeys from '../../actions/ActionTypeKeys';

const variantIcon = {
  success: CheckCircleIcon,
  warning: WarningIcon,
  error: ErrorIcon,
  info: InfoIcon
};

const toastContentStyles = theme => ({
  success: {
    backgroundColor: green[600],
  },
  error: {
    backgroundColor: theme.palette.error.dark,
  },
  info: {
    backgroundColor: theme.palette.primary.dark,
    color: '#FFFFFF'
  },
  warning: {
    backgroundColor: amber[700],
  },
  icon: {
    fontSize: 20,
  },
  iconVariant: {
    opacity: 0.9,
    marginRight: theme.spacing.unit,
  },
  message: {
    display: 'flex',
    alignItems: 'center',
  },
});

function ToastContent(props) {
  const { classes, className, message, onClose, variant, ...other } = props;

  const Icon = variantIcon[variant];

  return (
    <SnackbarContent
      className={classNames(classes[variant], className)}
      aria-describedby="client-snackbar"
      message={
        <span id="client-snackbar" className={classes.message}>
          <Icon className={classNames(classes.icon, classes.iconVariant)} />
          {message}
        </span>
      }
      action={[
        <IconButton
          key="close"
          aria-label="Close"
          color="inherit"
          className={classes.close}
          onClick={onClose}
        >
          <CloseIcon className={classes.icon} />
        </IconButton>,
      ]}
      {...other}
    />
  );
}

const MySnackbarContentWrapper = withStyles(toastContentStyles)(ToastContent);

class Toast extends React.Component<any, any> {
  state = {
    open: false,
    variant: 'info',
    content: '',
    duration: 0
  };

  handleClose = (_, reason) => {
    if (reason === 'clickaway') {
      return;
    }

    this.props.dispatch({type: ActionTypeKeys.TOAST_HIDE});
  };

  render() {
    return (
      <div>
        <Snackbar
          anchorOrigin={{
            vertical: 'bottom',
            horizontal: 'center',
          }}
          open={this.props.open}
          autoHideDuration={this.props.duration}
          onClose={this.handleClose}
          TransitionComponent={Fade}
          key={this.props.content}
        >
          <MySnackbarContentWrapper
            onClose={this.handleClose}
            variant={this.props.variant}
            message={this.props.content}
          />
        </Snackbar>
      </div>
    );
  }
}

function mapStatesToProps (state: IStoreState) {
  if (state.toast !== null) {
    return {
      open: true,
      content: state.toast.message,
      variant: state.toast.type ? state.toast.type : 'info',
      duration: state.toast.duration
    };
  } else {
    return {
      open: false,
      variant: 'info'
    };
  }
}

export default connect(mapStatesToProps)(Toast);
