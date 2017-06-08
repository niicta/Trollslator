package niicta.trollslator.model.factories;

import niicta.trollslator.model.factories.impl.StandardModelFactory;

/**
 * Created by niict on 07.06.2017.
 */

public class ModelFactoryProvider {
    public static final int STANDARD_FACTORY = 1;

    private static ModelFactoryProvider modelFactoryProvider = new ModelFactoryProvider();

    private ModelFactoryProvider(){};

    public ModelFactory provideFactory(int type){
        if (type == STANDARD_FACTORY)
            return new StandardModelFactory();
        else
            throw new RuntimeException("Unsupported ModelFactory type " + type);
    }

    public ModelFactory provideFactory() {
        return provideFactory(STANDARD_FACTORY);
    }

    public static ModelFactoryProvider getInstance(){
        return modelFactoryProvider;
    }
}
