import amber from '@material-ui/core/colors/amber';
import green from '@material-ui/core/colors/green';
import Fade from '@material-ui/core/Fade';
import IconButton from '@material-ui/core/IconButton';
import Snackbar from '@material-ui/core/Snackbar';
import SnackbarContent from '@material-ui/core/SnackbarContent';
import { withStyles } from '@material-ui/core/styles';
import CheckCircleIcon from '@material-ui/icons/CheckCircle';
import CloseIcon from '@material-ui/icons/Close';
import ErrorIcon from '@material-ui/icons/Error';
import InfoIcon from '@material-ui/icons/Info';
import WarningIcon from '@material-ui/icons/Warning';
import classNames from 'classnames';
import React from 'react';
import { connect } from 'react-redux';
import ActionTypeKeys from '../../actions/ActionTypeKeys';
import IStoreState from '../../store/IStoreState';

const variantIcon = {
  success: CheckCircleIcon,
  warning: WarningIcon,
  error: ErrorIcon,
  info: InfoIcon
};

const toastContentStyles = theme => ({
  success: {
    backgroundColor: green[600]
  },
  error: {
    backgroundColor: theme.palette.error.dark
  },
  info: {
    backgroundColor: theme.palette.primary.dark,
    color: '#FFFFFF'
  },
  warning: {
    backgroundColor: amber[700]
  },
  icon: {
    fontSize: 20
  },
  iconVariant: {
    opacity: 0.9,
    marginRight: theme.spacing.unit
  },
  message: {
    display: 'flex',
    alignItems: 'center'
  }
});

function ToastContent(props) {
  const { classes, className, message, onClose, variant, dismissable, ...other } = props;

  const Icon = variantIcon[variant];

  return (
    <SnackbarContent
      className={classNames(classes[variant], className)}
      aria-describedby='client-snackbar'
      message={
        <span id='client-snackbar' className={classes.message}>
          <Icon className={classNames(classes.icon, classes.iconVariant)} />
          {message}
        </span>
      }
      action={dismissable ? [
        <IconButton
          key='close'
          aria-label='Close'
          color='inherit'
          className={classes.close}
          onClick={onClose}>
          <CloseIcon className={classes.icon} />
        </IconButton>
      ] : null}
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
    duration: 0,
    dismissable: false
  };

  handleClose = (_, reason) => {
    if (reason === 'clickaway') {
      return;
    }

    this.props.dispatch({ type: ActionTypeKeys.TOAST_HIDE });
  }

  render() {
    return (
      <div>
        <Snackbar
          anchorOrigin={{
            vertical: 'bottom',
            horizontal: 'center'
          }}
          open={this.props.open}
          autoHideDuration={this.props.duration}
          onClose={this.handleClose}
          TransitionComponent={Fade}
          key={this.props.content}>
          <MySnackbarContentWrapper
            onClose={this.handleClose}
            variant={this.props.variant}
            message={this.props.content}
            dismissable={this.props.dismissable}
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
      duration: state.toast.duration,
      dismissable: state.toast.dismissable
    };
  } else {
    return {
      open: false,
      variant: 'info'
    };
  }
}

export default connect(mapStatesToProps)(Toast);
