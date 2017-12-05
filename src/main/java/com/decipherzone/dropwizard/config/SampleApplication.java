package com.decipherzone.dropwizard.config;

import com.decipherzone.dropwizard.auth.AppAuthenticationFilter;
import com.decipherzone.dropwizard.dao.ApplicationDao;
import com.decipherzone.dropwizard.dao.impl.ApplicationDaoImpl;
import com.decipherzone.dropwizard.domain.models.db.MongoDb;
import com.decipherzone.dropwizard.domain.mongo.MongoManager;
import com.decipherzone.dropwizard.domain.repositories.MusicRepository;
import com.decipherzone.dropwizard.domain.repositories.UserRepository;
import com.decipherzone.dropwizard.domain.repositories.impl.MusicRepositoryImpl;
import com.decipherzone.dropwizard.domain.repositories.impl.UserRepositoryImpl;
import com.decipherzone.dropwizard.exceptions.mappers.*;
import com.decipherzone.dropwizard.health.ApplicationHealthCheck;
import com.decipherzone.dropwizard.resources.AuthResource;
import com.decipherzone.dropwizard.resources.MusicResource;
import com.decipherzone.dropwizard.services.ApplicationService;
import com.decipherzone.dropwizard.services.AuthService;
import com.decipherzone.dropwizard.services.MusicService;
import com.decipherzone.dropwizard.services.impl.ApplicationServiceImpl;
import com.decipherzone.dropwizard.services.impl.AuthServiceImpl;
import com.decipherzone.dropwizard.services.impl.MusicServiceImpl;
import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.mongodb.MongoClient;
import io.dropwizard.Application;
import io.dropwizard.jersey.errors.EarlyEofExceptionMapper;
import io.dropwizard.jersey.errors.LoggingExceptionMapper;
import io.dropwizard.server.DefaultServerFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

class SampleApplication extends Application<ApplicationConfiguration> {
    static MongoManager mongoDBManager;

    public static void main(final String[] args) throws Exception {
        String[] param = {"server", "config.yml"};
        new SampleApplication().run(param);
    }

    @Override
    public void initialize(final Bootstrap<ApplicationConfiguration> bootstrap) {
        bootstrap.addBundle(new SwaggerBundle<ApplicationConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(ApplicationConfiguration configuration) {
                return configuration.getSwaggerBundleConfiguration();
            }
        });
    }

    @Override
    public void run(final ApplicationConfiguration config, final Environment env) {
        final FilterRegistration.Dynamic cors = env.servlets().addFilter("CORS", CrossOriginFilter.class);

        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

        ((DefaultServerFactory) config.getServerFactory()).setRegisterDefaultExceptionMappers(false);
        env.jersey().register(new LoggingExceptionMapper<Throwable>(){});

        Injector injector = createInjector(config);

        env.jersey().register(injector.getInstance(MusicResource.class));
        env.jersey().register(injector.getInstance(AuthResource.class));
        env.healthChecks().register(config.getAppConfig().getAppName(), new ApplicationHealthCheck(config.getAppConfig().getAppName()));
        env.jersey().register(injector.getInstance(AppAuthenticationFilter.class));

        env.jersey().register(new BaseExceptionMapper());
        env.jersey().register(new JsonProcessingExceptionMapper());
        env.jersey().register(new BaseConstraintMapper());
        // restore other default exception mappers
        env.jersey().register(new EarlyEofExceptionMapper());

        env.jersey().register(new RuntimeExceptionMapper());
        env.jersey().register(new BaseLoggingExceptionMapper());

        ApplicationService instance = injector.getInstance(ApplicationService.class);
        instance.loadInitialData();

    }

    /**
     * Binding services and DAOs for dependency injections
     * @param config ApplicationConfiguration.class
     * @return com.google.inject.Injector
     */
    private Injector createInjector(ApplicationConfiguration config) {
        return Guice.createInjector(new AbstractModule() {
            @Override
            protected void configure() {
                // services
                bind(ApplicationService.class).to(ApplicationServiceImpl.class);
                bind(MusicService.class).to(MusicServiceImpl.class);
                bind(AuthService.class).to(AuthServiceImpl.class);

                // Repositories
                bind(MusicRepository.class).to(MusicRepositoryImpl.class);
                bind(UserRepository.class).to(UserRepositoryImpl.class);

                // dao
                bind(ApplicationDao.class).to(ApplicationDaoImpl.class);

                bind(AppConfiguration.class).toInstance(config.getAppConfig());

                bind(MongoClient.class).toInstance(config.getAppConfig().getDataSource());

                bind(MongoDb.class).to(MongoManager.class);

            }
        });
    }



}
