package util;

import org.drools.compiler.kie.builder.impl.InternalKieModule;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.ReleaseId;
import org.kie.api.io.Resource;


public class JarDeployer 
{
    
	
	
	public  KieModule createAndDeployJar(KieServices ks, ReleaseId releaseId, String drl)
	{
        byte[] jar = createJar(ks, releaseId, drl);

        // Deploy jar into the repository
        KieModule km = deployJarIntoRepository(ks, jar);
       
        return km;
    }
	
	
    public byte[] createJar(KieServices ks,ReleaseId releaseId,String drl)
    {
        KieFileSystem kfs = ks.newKieFileSystem().generateAndWritePomXML(releaseId);
       
        
        kfs.write("src/main/resources/drools/spring/rules/userSessionRules/test.drl", drl);
        
        
        KieBuilder kb = ks.newKieBuilder(kfs).buildAll();
        
        InternalKieModule kieModule = (InternalKieModule) ks.getRepository().getKieModule(releaseId);
        
        byte[] jar = kieModule.getBytes();
        
        return jar;
    }
    
    
    private KieModule deployJarIntoRepository(KieServices ks, byte[] jar)
    {
        Resource jarRes = ks.getResources().newByteArrayResource(jar);
        KieModule km = ks.getRepository().addKieModule(jarRes);
        return km;
    }
}
