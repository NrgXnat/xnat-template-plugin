package org.nrg.xnat.plugins.template.dcm;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.SortedSet;

import org.dcm4che2.data.DicomObject;
import org.dcm4che2.data.Tag;

import org.nrg.xdat.XDAT;
import org.nrg.xdat.om.XnatProjectdata;
import org.nrg.xdat.security.XDATUser;
import org.nrg.config.services.ConfigService;
import org.nrg.dcm.id.DicomProjectIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSortedSet;

public final class CCIRProjectIdentifier implements DicomProjectIdentifier {
   
   private static final ImmutableSortedSet<Integer> tags = ImmutableSortedSet.of();
   
   private static final ConfigService _configService = XDAT.getConfigService();
   
   private static final String _ccirConfig     = "CCIR";
   private static final String _ccirConfigFile = "projectMapConfig";
   
   private final int tag;
    
   /**
    * Class constructor - Sets the _aeTitle for this class. 
    * @param aeTitle - The DICOM aeTitle this ProjectIdentifier is associated with. 
    */
   public CCIRProjectIdentifier(int tag) { this.tag = tag; }

   @Override
   public SortedSet<Integer> getTags() { return tags; }

   @Override
   public XnatProjectdata apply(final XDATUser user, final DicomObject o) {
      return XnatProjectdata.getProjectByIDorAlias(getProjectId(o), user, false);
   }
   
   private String getProjectId(final DicomObject o){

      final String proj = o.getString(this.tag);
      
      try{
         final HashMap<String, String> projectMap = getProjectMap();
         if(projectMap.containsKey(proj)){
            return projectMap.get(proj);
         }
      }catch(Exception e) { /* couldn't get project map */ }
      
      return Strings.isNullOrEmpty(proj) ? null : proj;
   }
   
   private HashMap<String, String> getProjectMap() throws Exception{
      final String conf = _configService.getConfigContents(_ccirConfig, _ccirConfigFile);
      HashMap<String,String> pMap = new HashMap<String,String>();
      if(conf != null && !conf.equals("")){ 
          String [] keyValue;
          for(String line : conf.split("\n")){
             keyValue = line.split("=");
             if(keyValue[0] != null && !keyValue[0].equals("") && keyValue[1] != null && !keyValue[1].equals("")){
                pMap.put(keyValue[0], keyValue[1]);
             }
          }
      }
      if(pMap == null || pMap.isEmpty()){ 
         throw new Exception("Couldn't retrieve project map from config file"); 
      }
      return pMap;
   }
}