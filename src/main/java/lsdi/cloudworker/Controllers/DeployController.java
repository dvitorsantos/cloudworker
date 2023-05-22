package lsdi.cloudworker.Controllers;

import com.espertech.esper.runtime.client.EPUndeployException;
import lsdi.cloudworker.DataTransferObjects.DeployRequest;
import lsdi.cloudworker.Services.DeployService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
public class DeployController {
    @Autowired
    private DeployService deployService;

    @PostMapping("/deploy")
    public void deploy(@RequestBody DeployRequest deployRequest) {
        try {
            deployService.deploy(deployRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @PostMapping("/undeploy")
    public void undeploy(@PathVariable String deployId) {
        try {
            deployService.undeploy(deployId);
        } catch (EPUndeployException e) {
            e.printStackTrace();
        }
    }
}
