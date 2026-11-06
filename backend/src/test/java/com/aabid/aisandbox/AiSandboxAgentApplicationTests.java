package com.aabid.aisandbox;
import org.junit.jupiter.api.Test;import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest(properties={"spring.datasource.url=jdbc:h2:mem:testdb","app.username=test","app.password=test-password","app.sandbox.enabled=false"})
class AiSandboxAgentApplicationTests{@Test void contextLoads(){}}
