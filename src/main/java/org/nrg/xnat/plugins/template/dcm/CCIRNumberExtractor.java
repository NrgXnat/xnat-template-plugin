package org.nrg.xnat.plugins.template.dcm;

import java.util.regex.Pattern;
import org.dcm4che2.data.DicomObject;
import org.nrg.dcm.MatchedPatternExtractor;

public class CCIRNumberExtractor extends MatchedPatternExtractor {
	
	public CCIRNumberExtractor(int tag, String regEx, int group) {
		super(tag, Pattern.compile(regEx), group);
	}

    public String extract(final DicomObject o) {
       String s = super.extract(o);
       return (s == null) ? null : s.replace("-", "_");
    }
}
