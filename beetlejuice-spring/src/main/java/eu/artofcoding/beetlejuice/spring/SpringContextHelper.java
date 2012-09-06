/*
 * beetlejuice
 * beetlejuice-spring
 * Copyright (C) 2011-2012 art of coding UG, http://www.art-of-coding.eu/
 *
 * Alle Rechte vorbehalten. Nutzung unterliegt Lizenzbedingungen.
 * All rights reserved. Use is subject to license terms.
 *
 * rbe, 06.09.12 09:38
 */

package eu.artofcoding.beetlejuice.spring;

import org.apache.commons.dbcp.BasicDataSource;
import org.slf4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
public class SpringContextHelper implements ApplicationContextAware {

    @Slf4j
    private Logger logger;

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    /**
     * http://forum.springsource.org/showthread.php?125695-Orderly-shutdown-how-to-know-when-downstream-executor-is-idle
     * @param shutdownForced
     */
    public void stopSchedulers(boolean shutdownForced) {
        logger.info(String.format("Stopping schedulers %s", shutdownForced ? "(force)" : ""));
        List<ExecutorService> executorServices = new ArrayList<ExecutorService>();
        Map<String, ThreadPoolTaskScheduler> schedulers = applicationContext.getBeansOfType(ThreadPoolTaskScheduler.class);
        for (Map.Entry<String, ThreadPoolTaskScheduler> entry : schedulers.entrySet()) {
            ThreadPoolTaskScheduler scheduler = entry.getValue();
            logger.info(String.format("Stopping scheduler %s", scheduler.getThreadNamePrefix()));
            ExecutorService executorService = scheduler.getScheduledExecutor();
            executorServices.add(executorService);
            if (shutdownForced) {
                executorService.shutdownNow();
            } else {
                executorService.shutdown();
            }
        }
        waitForExecutors(executorServices);
        logger.info("Stopped schedulers");
    }

    private void waitForExecutors(List<ExecutorService> executorServices) {
        long shutdownDeadline = System.currentTimeMillis() + 1000;
        for (ExecutorService executorService : executorServices) {
            try {
                if (!executorService.awaitTermination(shutdownDeadline - System.currentTimeMillis(), TimeUnit.MILLISECONDS)) {
                    logger.info(String.format("Executor service %s failed to terminate", executorService));
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                logger.warn(String.format("Interrupted while shutting down executor service %s", executorService));
                throw new RuntimeException("Interrupted while shutting down", e);
            }
            if (System.currentTimeMillis() > shutdownDeadline) {
                logger.warn("Timed out before waiting for all executor services");
            }
        }
    }

    private void stopExecutors() {
        ThreadPoolTaskExecutor springTaskExecutor = applicationContext.getBean("taskExecutor", ThreadPoolTaskExecutor.class);
        ThreadPoolExecutor springExecutor = springTaskExecutor.getThreadPoolExecutor();
        springExecutor.shutdownNow();
        Map<String, ThreadPoolTaskExecutor> map = applicationContext.getBeansOfType(ThreadPoolTaskExecutor.class);
        ThreadPoolTaskExecutor t = null;
        for (String key : map.keySet()) {
            t = map.get(key);
            final ThreadPoolExecutor executor = t.getThreadPoolExecutor();
            executor.shutdownNow();
            logger.info(String.format("%s: active after shutdown: %d", executor.toString(), executor.getActiveCount()));
            logger.info(String.format("%s: completed after shutdown: %d", executor.toString(), executor.getCompletedTaskCount()));
        }
    }

    private void closeDataSources() {
        Map<String, BasicDataSource> map2 = applicationContext.getBeansOfType(BasicDataSource.class);
        for (String key : map2.keySet()) {
            BasicDataSource dataSource = map2.get(key);
            try {
                dataSource.close();
            } catch (SQLException e) {
                logger.error(String.format("Could not close dataSource %s: %s", dataSource, e.getLocalizedMessage()), e);
            }
        }
    }

    public void shutdownDueToException(Throwable throwable) {
        if (null != throwable) {
            logger.error(String.format("Stopping system due to exception: %s", throwable));
        } else {
            logger.error("Stopping system, reason unknown");
        }
        /*
        closeDataSources();
        stopExecutors();
        */
        // Application specific shutdown here...
    }

}
