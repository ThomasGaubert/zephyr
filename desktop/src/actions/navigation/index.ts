import keys from "../ActionTypeKeys";

export interface INavigateHome {
  readonly type: keys.NAVIGATE_HOME;
}

export interface INavigateHistory {
  readonly type: keys.NAVIGATE_HISTORY;
}

export interface INavigateSettings {
  readonly type: keys.NAVIGATE_SETTINGS;
}
