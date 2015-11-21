
package hudson.plugins.promoted_builds.inheritance;

import org.apache.log4j.Logger;

import hudson.Extension;
import hudson.model.JobProperty;

import hudson.plugins.project_inheritance.projects.InheritanceProject;
import hudson.plugins.project_inheritance.projects.inheritance.InheritanceSelector;

import hudson.plugins.promoted_builds.JobPropertyImpl;

/**
 *  
 * @author Jacek Tomaka
 */
@Extension(optional=true)
public class JobPropertyImplSelector extends InheritanceSelector<JobProperty<?>> {
	private static final long serialVersionUID = 6297336734737164557L;
	private static Logger logger = Logger.getLogger(JobPropertyImplSelector.class);

	@Override
	public boolean isApplicableFor(Class<?> clazz){
		return JobProperty.class.isAssignableFrom(clazz);
	}
	
	@Override
	public InheritanceSelector.MODE getModeFor(Class<?> clazz){
		if (JobPropertyImpl.class.isAssignableFrom(clazz)) return MODE.USE_LAST;
		return MODE.NOT_RESPONSIBLE;
	}
	
	@Override
	public String getObjectIdentifier(JobProperty<?> obj){
		if (JobPropertyImpl.class.getName().equals(obj.getClass().getName())){
			return "JobPropertyImpl";
		}
		return null;
	}
	
	@Override
	public JobPropertyImpl merge(JobProperty<?> prior, JobProperty<?> latter, InheritanceProject caller){
		return null;
	}
	
	@Override
	public JobProperty<?> handleSingleton(JobProperty<?> object, InheritanceProject caller){
			if (!isApplicableFor(object.getClass())) return object;
			if (caller.isAbstract) return object;
			
			JobPropertyImpl jobProperty = (JobPropertyImpl)object;
			
			try {
				JobPropertyImpl newJobProperty = new JobPropertyImpl(jobProperty, caller);
				return newJobProperty;
			} catch (Exception ex){
				logger.error("inheritance-extension-for-promoted-builds: Error during hacking up JobPropertyImpl", ex );
			}
			return object;
	}
	
	
	
}

