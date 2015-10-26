

package qanda;

import java.util.*;
import java.io.Serializable;
import de.hybris.platform.util.*;
import de.hybris.platform.core.*;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.type.*;
import de.hybris.platform.persistence.type.*;
import de.hybris.platform.persistence.enumeration.*;
import de.hybris.platform.persistence.property.PersistenceManager;
import de.hybris.platform.persistence.*;

/**
 * Generated by hybris Platform.
 */
@SuppressWarnings({"cast","unused","boxing","null", "PMD"})
public class GeneratedTypeInitializer extends AbstractTypeInitializer
{
	/**
	 * Generated by hybris Platform.
	 */
	public GeneratedTypeInitializer( ManagerEJB manager, Map params )
	{
		super( manager, params );
	}


	/**
	 * Generated by hybris Platform.
	 */
	@Override
	protected void performRemoveObjects( ManagerEJB manager, Map params ) throws JaloBusinessException
	{
		// no-op by now
	}

	/**
	 * Generated by hybris Platform.
	 */
	@Override
	protected final void performCreateTypes( final ManagerEJB manager, Map params ) throws JaloBusinessException
	{
		// performCreateTypes
	
	
		createItemType(
			"RandomImageParagraphComponent",
			"CMSParagraphComponent",
			com.aimprosoft.platform.addons.qanda.jalo.RandomImageParagraphComponent.class,
			null,
			false,
			null,
			false
		);
	
		createEnumerationType(
			"RandomPictureTopic",
			null
		);
	
	}

	/**
	 * Generated by hybris Platform.
	 */
	@Override
	protected final void performModifyTypes( final ManagerEJB manager, Map params ) throws JaloBusinessException
	{
		// performModifyTypes
	

	
	
				single_createattr_RandomImageParagraphComponent_topic();
			
				single_createattr_RandomImageParagraphComponent_width();
			
				single_createattr_RandomImageParagraphComponent_height();
			
				single_createattr_RandomImageParagraphComponent_text();
			

	}

	
	public void single_createattr_RandomImageParagraphComponent_topic() throws JaloBusinessException
	{
		
						Map sqlColumnDefinitions = null;
					
				createPropertyAttribute(
					"RandomImageParagraphComponent", 
					"topic",  
					null,
					"RandomPictureTopic",
					de.hybris.platform.jalo.type.AttributeDescriptor.READ_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.WRITE_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.OPTIONAL_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.REMOVE_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.SEARCH_FLAG,
					null,
					sqlColumnDefinitions
				);
			
	}
	
	public void single_createattr_RandomImageParagraphComponent_width() throws JaloBusinessException
	{
		
						Map sqlColumnDefinitions = null;
					
				createPropertyAttribute(
					"RandomImageParagraphComponent", 
					"width",  
					null,
					"java.lang.Integer",
					de.hybris.platform.jalo.type.AttributeDescriptor.READ_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.WRITE_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.REMOVE_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.SEARCH_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.PRIMITIVE_FLAG,
					null,
					sqlColumnDefinitions
				);
			
	}
	
	public void single_createattr_RandomImageParagraphComponent_height() throws JaloBusinessException
	{
		
						Map sqlColumnDefinitions = null;
					
				createPropertyAttribute(
					"RandomImageParagraphComponent", 
					"height",  
					null,
					"java.lang.Integer",
					de.hybris.platform.jalo.type.AttributeDescriptor.READ_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.WRITE_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.REMOVE_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.SEARCH_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.PRIMITIVE_FLAG,
					null,
					sqlColumnDefinitions
				);
			
	}
	
	public void single_createattr_RandomImageParagraphComponent_text() throws JaloBusinessException
	{
		
						Map sqlColumnDefinitions = null;
					
				createPropertyAttribute(
					"RandomImageParagraphComponent", 
					"text",  
					null,
					"localized:java.lang.String",
					de.hybris.platform.jalo.type.AttributeDescriptor.READ_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.WRITE_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.OPTIONAL_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.REMOVE_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.SEARCH_FLAG,
					null,
					sqlColumnDefinitions
				);
			
	}
	


	/**
	 * Generated by hybris Platform.
	 */
	@Override
	protected final void performCreateObjects( final ManagerEJB manager, Map params ) throws JaloBusinessException
	{
		// performCreateObjects
	
	
		createEnumerationValues(
			"RandomPictureTopic",
			true,
			Arrays.asList( new String[] {
			
				"fashion",
				"sports",
				"abstract",
				"animals",
				"city",
				"food",
				"nightlife",
				"people",
				"nature",
				"technics",
				"transport"
			} )
		);
	
				{
				Map customPropsMap = new HashMap();
				
				setItemTypeProperties(
					"RandomImageParagraphComponent",
					false,
					true,
					true,
					null,
					customPropsMap
				);
				}
			
			single_setAttributeProperties_RandomImageParagraphComponent_topic();
		
			single_setAttributeProperties_RandomImageParagraphComponent_width();
		
			single_setAttributeProperties_RandomImageParagraphComponent_height();
		
			single_setAttributeProperties_RandomImageParagraphComponent_text();
		
				setDefaultProperties(
					"RandomPictureTopic",
					true,
					true,
					null
				);
			
	}


		
						public void single_setAttributeProperties_RandomImageParagraphComponent_topic() throws JaloBusinessException
						{
							
							
							
							Map customPropsMap = new HashMap();
							
							setAttributeProperties(
								"RandomImageParagraphComponent", 
								"topic",
								false, 
								null,
								null,
								null,
								true,
								true,
								null,
								customPropsMap,
								null
							);
						}
					
						public void single_setAttributeProperties_RandomImageParagraphComponent_width() throws JaloBusinessException
						{
							
							
							
							Map customPropsMap = new HashMap();
							
							setAttributeProperties(
								"RandomImageParagraphComponent", 
								"width",
								false, 
								Integer.valueOf(228),
								"Integer.valueOf(228)",
								null,
								true,
								true,
								null,
								customPropsMap,
								null
							);
						}
					
						public void single_setAttributeProperties_RandomImageParagraphComponent_height() throws JaloBusinessException
						{
							
							
							
							Map customPropsMap = new HashMap();
							
							setAttributeProperties(
								"RandomImageParagraphComponent", 
								"height",
								false, 
								Integer.valueOf(228),
								"Integer.valueOf(228)",
								null,
								true,
								true,
								null,
								customPropsMap,
								null
							);
						}
					
						public void single_setAttributeProperties_RandomImageParagraphComponent_text() throws JaloBusinessException
						{
							
							
							
							Map customPropsMap = new HashMap();
							
							setAttributeProperties(
								"RandomImageParagraphComponent", 
								"text",
								false, 
								null,
								null,
								null,
								true,
								true,
								null,
								customPropsMap,
								null
							);
						}
					
}

	