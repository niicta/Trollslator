package niicta.trollslator.events;

/**
 * Created by niict on 06.06.2017.
 */

public interface Event {
    int getType();
    Object getEventArgs();
}
