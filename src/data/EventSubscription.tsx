import { Subscription } from '@tmtsoftware/esw-ts'

export interface EventSubscription {
  subscription: Subscription
  subsystem: string
  component: string
  event: string
}
