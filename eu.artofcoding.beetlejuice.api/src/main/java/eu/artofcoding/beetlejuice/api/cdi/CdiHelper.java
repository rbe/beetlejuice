package eu.artofcoding.beetlejuice.api.cdi;

import javax.enterprise.inject.spi.Annotated;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.InjectionPoint;
import java.util.logging.Logger;

public final class CdiHelper {
    
    private static final Logger LOGGER = Logger.getLogger(CdiHelper.class.getName());

    private CdiHelper() {
        throw new AssertionError();
    }

    public void dumpInjectionPoint(InjectionPoint injectionPoint) {
        LOGGER.info("annotated " + injectionPoint.getAnnotated());
        LOGGER.info("bean " + injectionPoint.getBean());
        LOGGER.info("member " + injectionPoint.getMember());
        LOGGER.info("qualifiers " + injectionPoint.getQualifiers());
        LOGGER.info("type " + injectionPoint.getType());
        LOGGER.info("isDelegate " + injectionPoint.isDelegate());
        LOGGER.info("isTransient " + injectionPoint.isTransient());
        Bean<?> bean = injectionPoint.getBean();
        LOGGER.info("bean.beanClass " + bean.getBeanClass());
        LOGGER.info("bean.injectionPoints " + bean.getInjectionPoints());
        LOGGER.info("bean.name " + bean.getName());
        LOGGER.info("bean.qualifiers " + bean.getQualifiers());
        LOGGER.info("bean.scope " + bean.getScope());
        LOGGER.info("bean.stereotypes " + bean.getStereotypes());
        LOGGER.info("bean.types " + bean.getTypes());
        Annotated annotated = injectionPoint.getAnnotated();
        LOGGER.info("annotated.annotations " + annotated.getAnnotations());
        LOGGER.info("annotated.annotations " + annotated.getBaseType());
        LOGGER.info("annotated.typeClosure " + annotated.getTypeClosure());
    }

}
