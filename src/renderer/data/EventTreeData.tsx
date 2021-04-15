import allEventsJson from '../data/events.json'

interface Event {
  event: string
}

interface EventsForComponent {
  component: string
  events: Array<Event>
}

interface EventsForSubsystem {
  subsystem: string
  components: Array<EventsForComponent>
}

export class EventTreeData {
  static allEvents = allEventsJson as Array<EventsForSubsystem>
}
