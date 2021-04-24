package com.xxbb.jcip.composingobjects.safe;

import com.xxbb.jcip.annotation.ThreadSafe;
import com.xxbb.jcip.composingobjects.unsafe.MutablePoint;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@ThreadSafe
public class MonitorVehicleTracker {
    private final Map<String, MutablePoint> locations;

    public MonitorVehicleTracker(Map<String, MutablePoint> locations) {
        this.locations = deepCopy(locations);
    }

    public synchronized Map<String, MutablePoint> getLocations() {
        return deepCopy(locations);
    }

    public synchronized MutablePoint getLocation(String id) {
        MutablePoint loc = locations.get(id);
        return loc == null ? null : new MutablePoint(loc);
    }

    public synchronized void setLocation(String id, int x, int y) {
        MutablePoint mp = locations.get(id);
        if (mp == null) {
            throw new IllegalArgumentException("No such id " + id);
        }
        mp.x = x;
        mp.y = y;
    }

    private Map<String, MutablePoint> deepCopy(Map<String, MutablePoint> m) {
        Map<String, MutablePoint> output = new HashMap<>();
        for (String id :
                m.keySet()) {
            output.put(id, m.get(id));
        }
        return Collections.unmodifiableMap(output);
    }
}
