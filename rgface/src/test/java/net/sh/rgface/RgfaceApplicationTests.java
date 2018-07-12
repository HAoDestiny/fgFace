package net.sh.rgface;

import com.ha.facecamera.configserver.ConfigServer;
import com.ha.facecamera.configserver.ConfigServerConfig;
import com.ha.facecamera.configserver.DataServer;
import com.ha.facecamera.configserver.DataServerConfig;
import com.ha.facecamera.configserver.events.CameraConnectedEventHandler;
import com.ha.facecamera.configserver.events.CameraDisconnectedEventHandler;
import com.ha.facecamera.configserver.events.CaptureCompareDataReceivedEventHandler;
import com.ha.facecamera.configserver.pojo.AppConfig;
import com.ha.facecamera.configserver.pojo.CaptureCompareData;
import com.ha.facecamera.configserver.pojo.FacePage;
import com.ha.facecamera.configserver.pojo.FaceToUpload;
import com.ha.facecamera.configserver.pojo.ListFaceCriteria;
import com.ha.facecamera.configserver.pojo.Time;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RgfaceApplicationTests {

    @Test
    public void contextLoads() {
        ConfigServer configServer = new ConfigServer();

        ConfigServerConfig csc = new ConfigServerConfig();
        csc.heartBeatInterval = 10;
       // boolean ret = configServer.start(Integer.parseInt(textField.getText()), csc);

    }

}
