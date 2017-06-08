package niicta.trollslator.view.creators;

import android.content.Context;
import android.view.View;

/**
 * Created by niict on 08.06.2017.
 */

public interface ViewCreator<T> {
    //почувствовал себя богом, когда додумался до дженериков, никогда так не делал, не знаю, правильно ли
    void createView(T source);
}
