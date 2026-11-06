package com.aabid.aisandbox.sandbox;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.io.*;import java.nio.charset.StandardCharsets;import java.util.concurrent.TimeUnit;
@Component
public class DockerSandboxRunner {
 private final boolean enabled;public DockerSandboxRunner(@Value("${app.sandbox.enabled:true}")boolean enabled){this.enabled=enabled;}
 public String smokeTest(){
  if(!enabled)throw new IllegalStateException("Sandbox is disabled");
  try{
   Process p=new ProcessBuilder("docker","run","--rm","--network","none","--memory=128m","--cpus=0.5","--pids-limit=64","--read-only","--tmpfs","/tmp:rw,noexec,nosuid,size=16m","--cap-drop=ALL","--security-opt=no-new-privileges","alpine:3.21","sh","-c","echo Sandbox is working").redirectErrorStream(true).start();
   if(!p.waitFor(35,TimeUnit.SECONDS)){p.destroyForcibly();throw new IllegalStateException("Docker smoke test timed out");}
   String output=new String(p.getInputStream().readAllBytes(),StandardCharsets.UTF_8).trim();
   if(p.exitValue()!=0)throw new IllegalStateException("Docker smoke test failed: "+output);return output;
  }catch(IOException e){throw new IllegalStateException("Could not start Docker. Check Docker Desktop and CLI.",e);}
   catch(InterruptedException e){Thread.currentThread().interrupt();throw new IllegalStateException("Docker test interrupted",e);}
 }
}
