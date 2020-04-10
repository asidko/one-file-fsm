package com.company;

import static java.util.Arrays.asList;

import com.company.OneFileFsm.Example.Event;
import com.company.OneFileFsm.Example.State;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class OneFileFsm {

    // It's very handy to use method "name()" here because Enums also have it
    interface IState { String name(); }

    interface IAction { void run(IEvent event); }

    interface IEvent { String name(); }

    static class DataEvent implements IEvent {
        private final String name;
        private final Object data;

        private DataEvent(String name, Object data) { this.name = name; this.data = data; }

        public static DataEvent of(IEvent event, Object data) { return new DataEvent(event.name(), data); }

        public String name() { return name; }
        public Object getData() { return data; }
    }

    static class Transition {
        private final IState sourceState;
        private final IState targetState;
        private final IEvent transitionEvent;
        private final IAction transitionAction;

        Transition(IState sourceState, IState targetState, IEvent transitionEvent, IAction transitionAction) {
            this.sourceState = sourceState;
            this.targetState = targetState;
            this.transitionEvent = transitionEvent;
            this.transitionAction = transitionAction;
        }

        public IState getSourceState() { return sourceState; }
        public IState getTargetState() { return targetState; }
        public IEvent getTransitionEvent() { return transitionEvent; }
        public IAction getTransitionAction() { return transitionAction; }
    }

    static class FSM {
        private final Set<Transition> transitions;
        private IState currentState;

        public FSM(Set<Transition> transitions, IState initialState) {
            this.transitions = transitions;
            this.currentState = initialState;
        }

        public void fireTransitionEvent(IEvent event) {
            for (Transition transition : transitions) {
                if(
                    transition.getSourceState().name().equals(currentState.name()) &&
                    transition.getTransitionEvent().name().equals(event.name())
                ) {
                    System.out.println(String.format("FSM is changing state: [%s]->[%s].", this.currentState, transition.getTargetState()));
                    transition.getTransitionAction().run(event);
                    this.currentState = transition.getTargetState();
                    break;
                }
            }
        }
    }

    static class Example {
        enum State implements IState {
            MUSIC_PLAYING, MUSIC_PAUSED, MUSIC_STOPPED
        }

        enum Event implements IEvent {
            PRESSED_PLAY_BUTTON, PRESSED_PAUSE_BUTTON, PRESSED_STOP_BUTTON
        }

        static IAction playAction  = (event) -> { System.out.println("The music is playing now " + ((event instanceof DataEvent) ? ((DataEvent)event).getData() : "")); };
        static IAction pauseAction = (event) -> { System.out.println("The music is paused now"); };
        static IAction stopAction  = (event) -> { System.out.println("The music is stopped now"); };
    }

    public static void main(String[] args) {
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
    }
}
