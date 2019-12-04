export default class SocketChannels {
  /* Actions */
  public static readonly ACTION_POST_NOTIFICATION = 'post-notification';
  public static readonly ACTION_DISMISS_NOTIFICATION = 'dismiss-notification';
  public static readonly ACTION_DISCONNECT = 'disconnect';

  /* Events */
  public static readonly EVENT_NOTIFICATION_POSTED = 'event-post-notification';
  public static readonly EVENT_NOTIFICATION_DISMISSED = 'event-dismiss-notification';
}
