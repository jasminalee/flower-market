package vtc.xueqing.flower;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;


@Slf4j
@SpringBootApplication
@MapperScan("vtc.xueqing.flower.mapper")
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class FlowerMarket  implements ApplicationRunner {
    @Value("${server.port}")
    private String port;
    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("Started Successfully, Address: http://localhost:{}/doc.html", port);
        log.info("Server address: http://localhost:{}/", port);
    }
    public static void main(String[] args) {
        SpringApplication.run(FlowerMarket.class, args);
    }

}