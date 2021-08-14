import { EventEmitter } from 'events';

export default class EventUtils {

  private static instance: EventUtils;
  private readonly eventEmitter: EventEmitter;

  private constructor() {
    this.eventEmitter = new EventEmitter();
  }

  static getInstance(): EventUtils {
    if (!EventUtils.instance) {
      EventUtils.instance = new EventUtils();
    }
    return EventUtils.instance;
  }

  emit (event: string, ...args: any[]): void {
    this.eventEmitter.emit(event, args);
  }

  on (event: string, listener: (...args: any[]) => void): void {
    this.eventEmitter.on(event, listener);
  }
}
