package org.academiadecodigo.javabank.controller.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller responsible for retrieving the API description
 */
@RestController
@RequestMapping("/api")
public class RestIndexController {

    /**
     * Retrieves the API name and version
     *
     * @return the response
     */
    @RequestMapping(method = RequestMethod.GET, path = {"/", ""})
    @ResponseBody
    protected ApiVersion showVersion() {

        ApiVersion version = new ApiVersion();
        version.setName("JavaBank API");
        version.setVersion("v0.1");

        return version;

    }

    private static class ApiVersion {

        private String name;
        private String version;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }
}
