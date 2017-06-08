package niicta.trollslator.view.creators.factories;

import niicta.trollslator.view.creators.factories.impl.StandardViewCreatorFactory;

/**
 * Created by niict on 08.06.2017.
 */

public class ViewCreatorFactoryProvider {
    public static final int STANDARD_FACTORY = 1;

    private static ViewCreatorFactoryProvider viewCreatorFactoryProvider = new ViewCreatorFactoryProvider();

    private ViewCreatorFactoryProvider(){};

    public ViewCreatorFactory provideFactory(int type){
        if (type == STANDARD_FACTORY)
            return new StandardViewCreatorFactory();
        else
            throw new RuntimeException("Unsupported ModelFactory type " + type);
    }

    public ViewCreatorFactory provideFactory() {
        return provideFactory(STANDARD_FACTORY);
    }

    public static ViewCreatorFactoryProvider getInstance(){
        return viewCreatorFactoryProvider;
    }
}
