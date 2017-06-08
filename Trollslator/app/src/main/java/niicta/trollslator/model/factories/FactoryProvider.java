package niicta.trollslator.model.factories;

import niicta.trollslator.R;
import niicta.trollslator.model.factories.impl.StandardFactory;

/**
 * Created by niict on 07.06.2017.
 */

public class FactoryProvider {
    public static final int STANDARD_FACTORY = 1;

    private static FactoryProvider factoryProvider = new FactoryProvider();

    private FactoryProvider(){};

    public Factory provideFactory(int type){
        if (type == STANDARD_FACTORY)
            return new StandardFactory();
        else
            throw new RuntimeException("Unsupported Factory type " + type);
    }

    public Factory provideFactory() {
        return provideFactory(STANDARD_FACTORY);
    }

    public static FactoryProvider getInstance(){
        return factoryProvider;
    }
}
