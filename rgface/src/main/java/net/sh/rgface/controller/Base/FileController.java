package net.sh.rgface.controller.Base;

import net.sh.rgface.annotations.NotAspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Paths;

/**
 * Created by DESTINY on 2018/4/18.
 */

@RestController
public class FileController {

    @Value("${face.recognition.filePath}")
    private String FILE_ROOT;

    @Value("${face.recognition.facePath}")
    private String FACE_ROOT;

    @Value("${face.recognition.environmentPath}")
    private String ENVIRONMENT_ROOT;

    private final ResourceLoader resourceLoader;

    @Autowired
    public FileController(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @NotAspect
    @RequestMapping(method = RequestMethod.GET, value = "/images/{type}/{filename:.+}")
    @ResponseBody
    public ResponseEntity<?> getFile(@PathVariable String type, @PathVariable String filename) {

        Resource resource = null;
        if ("file".equals(type)) {
            resource = resourceLoader.getResource("file:" + Paths.get(FILE_ROOT, filename).toString());
        }

        if ("face".equals(type)) {
            resource = resourceLoader.getResource("file:" + Paths.get(FACE_ROOT, filename).toString());
        }

        if ("environment".equals(type)) {
            resource = resourceLoader.getResource("file:" + Paths.get(ENVIRONMENT_ROOT, filename).toString());
        }

        if (resource.exists()) {
            return ResponseEntity.ok(resource);
        }

        return ResponseEntity.ok(resourceLoader.getResource("classpath:static/img/d_face.jpg"));

    }

}
