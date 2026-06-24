package org.nrg.xnat.plugins.template.dcm;

import java.util.regex.Pattern;

import org.dcm4che3.data.Attributes;
import org.nrg.dcm.MatchedPatternExtractor;

public class ConfigurableMappedNumberExtractor extends MatchedPatternExtractor {
    public ConfigurableMappedNumberExtractor(final int tag, final String regex, final int group) {
        super(tag, Pattern.compile(regex), group);
    }

    public String extract(final Attributes dicom) {
        final String value = super.extract(dicom);
        return value == null ? null : value.replace("-", "_");
    }
}
