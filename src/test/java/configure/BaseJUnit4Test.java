package configure;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;



@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { JpaConfigure.class, HaRedisConfigure.class, ClusterRedisConfigure.class, LocalRedisConfigure.class })
public class BaseJUnit4Test {

}
