package niicta.trollslator.events;

/**
 * Created by niict on 06.06.2017.
 */

public interface Event {
    String getType();
    Object getEventArgs();
}
