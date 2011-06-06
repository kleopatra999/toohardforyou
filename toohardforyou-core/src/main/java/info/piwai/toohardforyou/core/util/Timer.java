/**
 * Copyright (C) 2011 Pierre-Yves Ricau (py.ricau at gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed To in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package info.piwai.toohardforyou.core.util;

import static forplay.core.ForPlay.currentTime;

import java.util.ArrayList;

import forplay.core.Game;

/**
 * A simplified timer class. This class serves the same purpose as
 * java.util.Timer, but is simplified because of the single-threaded
 * environment.
 * 
 * To schedule a timer, simply create a subclass of it (overriding {@link #run})
 * and call {@link #schedule} or {@link #scheduleRepeating}.
 * 
 * You MUST call Timer.update() from the {@link Game#update(float)} method.
 */
public abstract class Timer {

    private static final ArrayList<Timer> timers = new ArrayList<Timer>();

    public static void update() {
        double currentTime = currentTime();

        for (Timer timer : new ArrayList<Timer>(timers)) {
            if (currentTime > timer.nextExecution) {
                if (timer.periodMillis > 0) {
                    timer.nextExecution = currentTime() + timer.periodMillis;
                } else {
                    timer.cancel();
                }
                timer.run();
            }
        }
    }

    private double nextExecution;

    private int periodMillis;

    /**
     * Cancels this timer.
     */
    public void cancel() {
        timers.remove(this);
    }

    /**
     * This method will be called when a timer fires. Override it to implement
     * the timer's logic.
     */
    public abstract void run();

    /**
     * Schedules a timer to elapse in the future.
     * 
     * @param delayMillis
     *            how long to wait before the timer elapses, in milliseconds
     */
    public void schedule(int delayMillis) {
        checkPositive(delayMillis);
        periodMillis = 0;
        scheduleInternal(delayMillis);
    }

    private void checkPositive(int param) {
        if (param <= 0) {
            throw new IllegalArgumentException("must be positive");
        }
    }
    
    private void scheduleInternal(double stepMillis) {
        cancel();
        nextExecution = currentTime() + stepMillis;
        timers.add(this);
    }

    /**
     * Schedules a timer that elapses repeatedly.
     * 
     * @param periodMillis
     *            how long to wait before the timer elapses, in milliseconds,
     *            between each repetition
     */
    public void scheduleRepeating(int periodMillis) {
        checkPositive(periodMillis);
        this.periodMillis = periodMillis;
        scheduleInternal(periodMillis);
    }

}
