package com.xxbb.jcip.avoidlivenesshazard.unsafe;

import com.xxbb.jcip.annotation.GuardBy;
import com.xxbb.jcip.composingobjects.safe.Point;

import java.util.HashSet;
import java.util.Set;

public class CooperationgDeadlock {
    class Taxi {
        @GuardBy("this")
        private Point location, destination;
        private final Dispatcher dispatcher;

        public Taxi(Dispatcher dispatcher) {
            this.dispatcher = dispatcher;
        }

        public synchronized Point getLocation() {
            return location;
        }

        public synchronized void setLocation(Point location) {
            this.location = location;
            if (location.equals(destination)) {
                dispatcher.notifyAvailable(this);
            }
        }
    }

    class Dispatcher {
        private final Set<Taxi> taxis;
        private final Set<Taxi> availableTaxis;

        public Dispatcher(Set<Taxi> taxis, Set<Taxi> availableTaxis) {
            this.taxis = new HashSet<>();
            this.availableTaxis = new HashSet<>();
        }

        public synchronized void notifyAvailable(Taxi taxi) {
            availableTaxis.add(taxi);
        }

        public synchronized Image getImage() {
            Image image = new Image();
            for (Taxi t: taxis) {
                image.drawMarker(t.getLocation());
            }
            return image;
        }

    }

    class Image {
        public void drawMarker(Point p) {
        }
    }
}
