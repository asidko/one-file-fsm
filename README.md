# Pretty easy and understandable Final State Machine in Java

This is one of the simplest and good looking FSM written in Java.  
All implementation contained in the same file which takes only ~100 lines of code (with examples).

 ## Example
 
This example shows the transitions within the media player at the touch of a button.

Each transition contains a simple demo action that prints a text to the console.

```java
    Set<Transition> musicTransitions = new HashSet<>(asList(
        new Transition(State.MUSIC_STOPPED /* ⏩ */, State.MUSIC_PLAYING,/* 🔥 */ Event.PRESSED_PLAY_BUTTON, /* 🚀 */ Example.playAction),
        new Transition(State.MUSIC_PLAYING /* ⏩ */, State.MUSIC_PAUSED, /* 🔥 */ Event.PRESSED_PAUSE_BUTTON,/* 🚀 */ Example.pauseAction),
        new Transition(State.MUSIC_PLAYING /* ⏩ */, State.MUSIC_STOPPED,/* 🔥 */ Event.PRESSED_STOP_BUTTON, /* 🚀 */ Example.stopAction),
        new Transition(State.MUSIC_PAUSED  /* ⏩ */, State.MUSIC_STOPPED,/* 🔥 */ Event.PRESSED_STOP_BUTTON, /* 🚀 */ Example.stopAction),
        new Transition(State.MUSIC_PAUSED  /* ⏩ */, State.MUSIC_PLAYING,/* 🔥 */ Event.PRESSED_PLAY_BUTTON, /* 🚀 */ Example.playAction)
    ));
    IState initialState = State.MUSIC_STOPPED;
    
    FSM musicFSM = new FSM(musicTransitions, initialState);
    
    // Let's create some music events and then run them
    List<IEvent> musicPlayerAutoScript = asList(
        Event.PRESSED_PLAY_BUTTON,
        Event.PRESSED_PAUSE_BUTTON,
        // Example of Event with data
        DataEvent.of(Event.PRESSED_PLAY_BUTTON, "(Bob Marley - Bad Boys)"),
        // Next event will be ignored. There is no such transition PRESSED_PLAY_BUTTON -> PRESSED_PLAY_BUTTON
        Event.PRESSED_PLAY_BUTTON,
        Event.PRESSED_STOP_BUTTON,
        // Next event will be ignored. There is no such transition PRESSED_STOP_BUTTON -> PRESSED_STOP_BUTTON
        Event.PRESSED_STOP_BUTTON
    );
    
    for (IEvent playerEvent : musicPlayerAutoScript)
        musicFSM.fireTransitionEvent(playerEvent);
```

The result would be the following:

![image](https://user-images.githubusercontent.com/22843881/78972281-116bdd00-7b16-11ea-8f25-92a61d944157.png)


## Real usage

This FSM could be used in real projects, but most likely you will need some additional improvements, for example:

- Add null checks (at least in `fireTransitionEvent()` method of the FSM class).
- Save not only the `currentState` of FSM but also a whole current transition (because transition holds the information about the next state, that could be useful) 
- Add more events for `Transition` (for example, in addition to `transitionAction` you can also add `afterTransitionAction`).
- Pass `fsm` as the second argument to `run()` method in `IAction`. This feature will allow you, for example, to call `fsm.fireTransitionEvent()` in `afterTransitionAction` 
and your FSM will be able to switch to the next state automatically.
