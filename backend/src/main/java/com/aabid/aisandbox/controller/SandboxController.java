package com.aabid.aisandbox.controller;
import com.aabid.aisandbox.sandbox.DockerSandboxRunner;import org.springframework.web.bind.annotation.*;import java.util.Map;
@RestController @RequestMapping("/api/sandbox") public class SandboxController{
 private final DockerSandboxRunner runner;public SandboxController(DockerSandboxRunner r){runner=r;}
 @PostMapping("/smoke-test") public Map<String,String> test(){return Map.of("status","PASSED","output",runner.smokeTest());}
}
