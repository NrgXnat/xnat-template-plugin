package org.nrg.xnat.plugins.template.dcm;

import org.dcm4che2.data.DicomObject;
import org.nrg.dcm.MatchedPatternExtractor;
import org.nrg.dcm.TextExtractor;
import com.google.common.base.Strings;

public final class CCIRAttributeExtractor extends TextExtractor {
	
	private final MatchedPatternExtractor ccirNumExt; 
	private final CCIRAttributeExtractor otherTextExt;
	
	public CCIRAttributeExtractor(int tag, MatchedPatternExtractor ccirNumber) {
		super(tag);
		this.ccirNumExt = ccirNumber;
		this.otherTextExt = null;
	}
	
	public CCIRAttributeExtractor(int tag, CCIRAttributeExtractor otherText) {
		super(tag);
		this.otherTextExt = otherText;
		this.ccirNumExt = null;
	}
	
	public CCIRAttributeExtractor(int tag) {
		super(tag);
		this.otherTextExt = null;
		this.ccirNumExt = null;
	}
	
	@Override
	public String extract(DicomObject o) {
		
		// If the tag is empty, return null. 
		String content = null;
	    content = o.getString(this.getTag());
		
		if(!Strings.isNullOrEmpty(content)){ 
			if (this.ccirNumExt != null) {
				String num = this.ccirNumExt.extract(o);
				if(!Strings.isNullOrEmpty(num)) {
					return num + '_' + content;
				} else {
					return content;
				}
			} else if (this.otherTextExt != null) {
				String str = this.otherTextExt.extract(o);
				if(!Strings.isNullOrEmpty(str)) {
					return str + '_' + content;
				} else {
					return content;
				}
			} else {
				return content;
			}
		} else if (this.ccirNumExt != null) {
			String num = this.ccirNumExt.extract(o);
			if(!Strings.isNullOrEmpty(num)) {
				return num;
			} else {
				return new String("unknown");
			}
		} else {
			return new String("unknown");
		}
	}
	
	
	private int getTag(){ return this.getTags().first(); }
}