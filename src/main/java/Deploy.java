import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClient;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Deploy {


    private static final Logger log = LogManager.getLogger(Deploy.class);


    public static void main(String[] args) {
        try (KubernetesClient k8s = new DefaultKubernetesClient()) {
            // Load Deployment YAML Manifest into Java object
            Deployment deploy1 = k8s.apps().deployments()
                    .load(Deploy.class.getResourceAsStream("/consumer100.yaml"))
                    .get();

            Deployment deploy2 = k8s.apps().deployments()
                    .load(Deploy.class.getResourceAsStream("/consumer50.yaml"))
                    .get();
            // Apply it to Kubernetes Cluster
            log.info("creating deployement consumer 50");
            k8s.apps().deployments().inNamespace("default").create(deploy2);
            log.info("creating deployment consumer 100");
            k8s.apps().deployments().inNamespace("default").create(deploy1);

            Pod pod = k8s.pods()
                    .load(Deploy.class.getResourceAsStream("/producer.yaml"))
                    .get();
            k8s.pods().inNamespace("default").create(pod);


        }
    }
}
