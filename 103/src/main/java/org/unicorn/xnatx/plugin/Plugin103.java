/*
 * xnat-template-plugin: org.nrg.xnat.plugins.template.plugin.XnatTemplatePlugin
 * XNAT https://www.xnat.org
 * Copyright (c) 2005-2021, Washington University School of Medicine
 * All Rights Reserved
 *
 * Released under the Simplified BSD.
 */

package org.unicorn.xnatx.plugin;

import lombok.extern.slf4j.Slf4j;
import org.nrg.framework.annotations.XnatPlugin;
import org.springframework.context.annotation.*;
import org.springframework.context.annotation.ComponentScan.Filter;

import java.util.Arrays;

@XnatPlugin(value = "Plugin103", name = "XNAT Plugin 103 - Example Custom Page")
@Slf4j
public class Plugin103 {

    public Plugin103() {
        log.error("Creating the Plugin103 configuration class (logging as ERROR)");
        log.warn("Creating the Plugin103 configuration class (logging as WARN)");
        log.info("Creating the Plugin103 configuration class (logging as INFO)");

        log.error(log.toString());
        log.error(this.toString());
        log.error(Plugin103.class.toString());

        log.info(log.toString());
        log.info(this.toString());
        log.info(Plugin103.class.toString());

        String z = Arrays.toString(Thread.currentThread().getStackTrace());
        log.error(z);
    }

    @Bean
    public String templatePluginMessage() {
        return "This comes from deep within the Plugin 103 example.";
    }
}
