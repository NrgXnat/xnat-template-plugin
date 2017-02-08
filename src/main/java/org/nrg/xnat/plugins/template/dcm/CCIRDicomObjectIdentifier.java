package org.nrg.xnat.plugins.template.dcm;

import com.google.common.collect.ImmutableList;
import org.dcm4che2.data.Tag;
import org.nrg.dcm.ChainExtractor;
import org.nrg.dcm.ContainedAssignmentExtractor;
import org.nrg.dcm.Extractor;
import org.nrg.dcm.id.CompositeDicomObjectIdentifier;

import java.util.List;
import java.util.regex.Pattern;


public final class CCIRDicomObjectIdentifier extends CompositeDicomObjectIdentifier {

    private static final List<Extractor> attributeExtractors = new ImmutableList.Builder<Extractor>().add(new ContainedAssignmentExtractor(Tag.PatientComments, "AA", Pattern.CASE_INSENSITIVE))
            .add(new ContainedAssignmentExtractor(Tag.StudyComments, "AA", Pattern.CASE_INSENSITIVE))
            .build();

    public CCIRDicomObjectIdentifier(CCIRProjectIdentifier project,
                                     CCIRAttributeExtractor subject,
                                     CCIRAttributeExtractor session) {
        super(project, subject, session, new ChainExtractor(attributeExtractors));
    }
}
