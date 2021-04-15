import {Event, EventKey, EventName, Prefix, Subscription, Subsystem} from "@tmtsoftware/esw-ts";

export interface EventSubscription {
  subscription: Subscription
  subsystem: string
  component: string
  event: string
}
