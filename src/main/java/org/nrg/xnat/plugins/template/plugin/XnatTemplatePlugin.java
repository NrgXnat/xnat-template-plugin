/*
 * xnat-template: org.nrg.xnat.plugins.template.plugin.XnatTemplatePlugin
 * XNAT http://www.xnat.org
 * Copyright (c) 2017, Washington University School of Medicine
 * All Rights Reserved
 *
 * Released under the Simplified BSD.
 */

package org.nrg.xnat.plugins.template.plugin;

import org.dcm4che2.data.Tag;
import org.nrg.dcm.Extractor;
import org.nrg.framework.annotations.XnatDataModel;
import org.nrg.framework.annotations.XnatPlugin;
import org.nrg.xdat.bean.TemplateSampleBean;
import org.nrg.xnat.plugins.template.dcm.CCIRAttributeExtractor;
import org.nrg.xnat.plugins.template.dcm.CCIRDicomObjectIdentifier;
import org.nrg.xnat.plugins.template.dcm.CCIRNumberExtractor;
import org.nrg.xnat.plugins.template.dcm.CCIRProjectIdentifier;
import org.nrg.xnat.utils.XnatUserProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

@XnatPlugin(value = "templatePlugin", name = "XNAT 1.7 Template Plugin",
            entityPackages = "org.nrg.xnat.plugins.template.entities",
            dataModels = {@XnatDataModel(value = TemplateSampleBean.SCHEMA_ELEMENT_NAME,
                                         singular = "Template",
                                         plural = "Templates",
                                         code = "TM")})
@ComponentScan({"org.nrg.xnat.plugins.template.preferences",
                "org.nrg.xnat.plugins.template.repositories",
                "org.nrg.xnat.plugins.template.rest",
                "org.nrg.xnat.plugins.template.services.impl"})
public class XnatTemplatePlugin {
    public XnatTemplatePlugin() {
        _log.info("Creating the XnatTemplatePlugin configuration class");
    }

    @Bean
    public String templatePluginMessage() {
        return "This comes from deep within the template plugin.";
    }

    /**
     * Extracts the CCIR number from the specified DICOM tag using the specified regex.
     *
     * @return An extractor that gets numbers from the study description.
     */
    @Bean
    public CCIRNumberExtractor ccirNumberExtractor() {
        return new CCIRNumberExtractor(Tag.StudyDescription, ".*\\^([A-Za-z0-9-_]+).*", 1);
    }

    /**
     * Builds the subject label by extracting the contents of StudyDescription between the ^ and the first space.
     *
     * @return An extractor that gets the subject label.
     */
    @Bean
    public Extractor ccirSubjectExtractor(final CCIRNumberExtractor ccirNumberExtractor) {
        return new CCIRAttributeExtractor(0, ccirNumberExtractor);
    }

    /**
     * Gets the study date.
     *
     * @return An extractor to get the study date.
     */
    @Bean
    public CCIRAttributeExtractor ccirStudyDateExtractor() {
        return new CCIRAttributeExtractor(Tag.StudyDate);
    }

    /**
     * Builds the subject label by concatenating study date and time.
     *
     * @param ccirStudyDateExtractor An extractor to get the study date.
     *
     * @return An extractor that gets the session label.
     */
    @Bean
    public CCIRAttributeExtractor ccirSessionExtractor(final CCIRAttributeExtractor ccirStudyDateExtractor) {
        return new CCIRAttributeExtractor(Tag.StudyTime, ccirStudyDateExtractor);
    }

    /**
     * Determines the project by extracting the contents from the specified DICOM tag.
     *
     * @return The CCIR project identifier.
     */
    @Bean
    public CCIRProjectIdentifier ccirProjectIdentifier() {
        return new CCIRProjectIdentifier(Tag.StationName);
    }

    /**
     * Creates the DICOM object identifier for CCIR.
     *
     * @param receivedFileUserProvider The user provider for writing files.
     * @param ccirProjectIdentifier    The project identifier.
     * @param ccirSubjectExtractor     The subject extractor.
     * @param ccirSessionExtractor     The session extractor.
     *
     * @return The DICOM object identifier.
     */
    @SuppressWarnings("SpringJavaAutowiringInspection")
    @Bean
    public CCIRDicomObjectIdentifier ccirObjectIdent(final XnatUserProvider receivedFileUserProvider,
                                                     final CCIRProjectIdentifier ccirProjectIdentifier,
                                                     final CCIRAttributeExtractor ccirSubjectExtractor,
                                                     final CCIRAttributeExtractor ccirSessionExtractor) {
        final CCIRDicomObjectIdentifier identifier = new CCIRDicomObjectIdentifier(ccirProjectIdentifier, ccirSubjectExtractor, ccirSessionExtractor);
        identifier.setUserProvider(receivedFileUserProvider);
        return identifier;
    }

    private static final Logger _log = LoggerFactory.getLogger(XnatTemplatePlugin.class);
}
