package onlytest;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

/**
 * Created by devoo-kim on 16. 11. 2.
 */
@Configuration
public class HelloConfig {

    static ApplicationContext ctx = new AnnotationConfigApplicationContext(HelloConfig.class);
    @Scope(value = "prototype")
    @Bean(initMethod = "begin", destroyMethod = "destroy")
    public HelloBean helloBean(){
        return new HelloBean();

    }
    @Scope(value = "prototype")
    @Bean(initMethod = "begin", destroyMethod = "destroy")
    public HelloBean2 helloBean2(){
        return new HelloBean2();

    }

    public static void main(String[] args){
        HelloBean helloBean = (HelloBean) ctx.getBean("helloBean");
        helloBean.doSomething();
        HelloBean2 helloBean2 = (HelloBean2) ctx.getBean(HelloBean2.class);
        helloBean.doSomething();
    }
}
