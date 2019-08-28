package com.ali;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.ali.account.web.AccountController;
import com.ali.account.AccountService;
import com.ali.account_view.web.AccountViewController;
import com.ali.account_view.event.handler.AccountViewEventHandlerChain;
import com.ali.account_view.AccountViewObserver;
import com.ali.account_view.AccountViewRepository;
import com.ali.customer.web.CustomerController;
import com.ali.customer.CustomerService;
import com.ali.customer_view.web.CustomerViewController;
import com.ali.customer_view.event.handler.CustomerViewEventHandlerChain;
import com.ali.customer_view.CustomerViewObserver;
import com.ali.customer_view.CustomerViewRepository;
import com.ali.infrastructure.EventStore;
import com.ali.infrastructure.EventStoreController;
import com.ali.money_transfer.web.MoneyTransferController;
import com.ali.money_transfer.MoneyTransferServie;

import com.google.inject.Injector;
import com.netflix.governator.InjectorBuilder;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.swagger.v3.jaxrs2.integration.resources.OpenApiResource;
import io.swagger.v3.oas.integration.SwaggerConfiguration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

public class MoneytransferApplication extends Application<MoneytransferConfiguration> {

    public static void main(final String[] args) throws Exception {
        new MoneytransferApplication().run(args);
    }

    @Override
    public String getName() {
        return "moneytransfer";
    }

    @Override
    public void initialize(final Bootstrap<MoneytransferConfiguration> bootstrap) {

    }

    @Override
    public void run(final MoneytransferConfiguration configuration,
                    final Environment environment) {

        Injector injector = InjectorBuilder.fromModule(new ServerModule()).createInjector();

        OpenAPI oas = new OpenAPI();
        Info info = new Info()//.title("Hello API")
                .title("Money Transfer API")
                .description("RESTful API for Money Transfer Application");
     
        oas.info(info);
        SwaggerConfiguration oasConfig = new SwaggerConfiguration()
                .openAPI(oas)
                .prettyPrint(true)
                .resourcePackages(Stream.of("com.ali")
                                  .collect(Collectors.toSet()));
        environment.jersey().register(new OpenApiResource()
                .openApiConfiguration(oasConfig));



        injector.getInstance(CustomerViewEventHandlerChain.class);
        injector.getInstance(AccountViewEventHandlerChain.class);
        injector.getInstance(CustomerViewObserver.class);
        injector.getInstance(AccountViewObserver.class);

        environment.jersey().register(new EventStoreController(injector.getInstance(EventStore.class)));
        environment.jersey().register(new CustomerController(injector.getInstance(CustomerService.class)));
        environment.jersey().register(new CustomerViewController(injector.getInstance(CustomerViewRepository.class)));
        environment.jersey().register(new AccountController(injector.getInstance(AccountService.class)));
        environment.jersey().register(new AccountViewController(injector.getInstance(AccountViewRepository.class)));
        environment.jersey().register(new MoneyTransferController(injector.getInstance(MoneyTransferServie.class)));

              
    }
}
