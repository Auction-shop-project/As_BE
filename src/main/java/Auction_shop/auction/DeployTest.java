package Auction_shop.auction;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DeployTest {
    @GetMapping("/Deploytest")
    public String MyDeployTest() {
        return "Deploy Success :)";
    }
}
