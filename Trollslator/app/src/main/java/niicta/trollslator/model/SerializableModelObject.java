package niicta.trollslator.model;

import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * Created by niict on 08.06.2017.
 */

public interface SerializableModelObject extends Serializable {
    void serialize(OutputStream fos);
    void deserialize(InputStream fis);
}
