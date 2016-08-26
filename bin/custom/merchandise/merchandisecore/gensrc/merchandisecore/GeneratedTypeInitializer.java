

package merchandisecore;

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
			"ApparelProduct",
			"Product",
			de.hybris.merchandise.core.jalo.ApparelProduct.class,
			null,
			false,
			null,
			false
		);
	
		createItemType(
			"ApparelStyleVariantProduct",
			"VariantProduct",
			de.hybris.merchandise.core.jalo.ApparelStyleVariantProduct.class,
			null,
			false,
			null,
			false
		);
	
		createItemType(
			"ApparelSizeVariantProduct",
			"ApparelStyleVariantProduct",
			de.hybris.merchandise.core.jalo.ApparelSizeVariantProduct.class,
			null,
			false,
			null,
			false
		);
	
		createItemType(
			"ElectronicsColorVariantProduct",
			"VariantProduct",
			de.hybris.merchandise.core.jalo.ElectronicsColorVariantProduct.class,
			null,
			false,
			null,
			false
		);
	
		createItemType(
			"TurnToStaticContents",
			"GenericItem",
			de.hybris.merchandise.core.jalo.TurnToStaticContents.class,
			"de.hybris.platform.persistence.merchandisecore_TurnToStaticContents",
			false,
			null,
			false
		);
	
		createItemType(
			"TurnToColorVariantProduct",
			"VariantProduct",
			de.hybris.merchandise.core.jalo.TurnToColorVariantProduct.class,
			null,
			false,
			null,
			false
		);
	
		createEnumerationType(
			"SwatchColorEnum",
			null
		);
	
		createCollectionType(
			"GenderList",
			"Gender",
			CollectionType.LIST
		);
	
		createCollectionType(
			"SwatchColorSet",
			"SwatchColorEnum",
			CollectionType.SET
		);
	
	}

	/**
	 * Generated by hybris Platform.
	 */
	@Override
	protected final void performModifyTypes( final ManagerEJB manager, Map params ) throws JaloBusinessException
	{
		// performModifyTypes
	

	
	
				single_createattr_ApparelProduct_genders();
			
				single_createattr_ApparelStyleVariantProduct_style();
			
				single_createattr_ApparelStyleVariantProduct_swatchColors();
			
				single_createattr_ApparelSizeVariantProduct_size();
			
				single_createattr_ElectronicsColorVariantProduct_color();
			
				single_createattr_Product_internalOnly();
			
				single_createattr_Customer_isInternal();
			
				single_createattr_TurnToStaticContents_productId();
			
				single_createattr_TurnToStaticContents_timestamp();
			
				single_createattr_TurnToStaticContents_qaContent();
			
				single_createattr_TurnToStaticContents_reviewsContent();
			
				single_createattr_TurnToColorVariantProduct_color();
			

	}

	
	public void single_createattr_ApparelProduct_genders() throws JaloBusinessException
	{
		
						Map sqlColumnDefinitions = null;
					
				createPropertyAttribute(
					"ApparelProduct", 
					"genders",  
					null,
					"GenderList",
					de.hybris.platform.jalo.type.AttributeDescriptor.READ_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.WRITE_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.OPTIONAL_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.REMOVE_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.SEARCH_FLAG,
					null,
					sqlColumnDefinitions
				);
			
	}
	
	public void single_createattr_ApparelStyleVariantProduct_style() throws JaloBusinessException
	{
		
						Map sqlColumnDefinitions = null;
					
				createPropertyAttribute(
					"ApparelStyleVariantProduct", 
					"style",  
					null,
					"localized:java.lang.String",
					de.hybris.platform.jalo.type.AttributeDescriptor.READ_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.WRITE_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.OPTIONAL_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.REMOVE_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.SEARCH_FLAG,
					"VariantAttributeDescriptor",
					sqlColumnDefinitions
				);
			
	}
	
	public void single_createattr_ApparelStyleVariantProduct_swatchColors() throws JaloBusinessException
	{
		
						Map sqlColumnDefinitions = null;
					
				createPropertyAttribute(
					"ApparelStyleVariantProduct", 
					"swatchColors",  
					null,
					"SwatchColorSet",
					de.hybris.platform.jalo.type.AttributeDescriptor.READ_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.WRITE_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.OPTIONAL_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.REMOVE_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.SEARCH_FLAG,
					null,
					sqlColumnDefinitions
				);
			
	}
	
	public void single_createattr_ApparelSizeVariantProduct_size() throws JaloBusinessException
	{
		
						Map sqlColumnDefinitions = null;
					
				createPropertyAttribute(
					"ApparelSizeVariantProduct", 
					"size",  
					null,
					"localized:java.lang.String",
					de.hybris.platform.jalo.type.AttributeDescriptor.READ_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.WRITE_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.OPTIONAL_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.REMOVE_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.SEARCH_FLAG,
					"VariantAttributeDescriptor",
					sqlColumnDefinitions
				);
			
	}
	
	public void single_createattr_ElectronicsColorVariantProduct_color() throws JaloBusinessException
	{
		
						Map sqlColumnDefinitions = null;
					
				createPropertyAttribute(
					"ElectronicsColorVariantProduct", 
					"color",  
					null,
					"localized:java.lang.String",
					de.hybris.platform.jalo.type.AttributeDescriptor.READ_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.WRITE_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.OPTIONAL_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.REMOVE_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.SEARCH_FLAG,
					"VariantAttributeDescriptor",
					sqlColumnDefinitions
				);
			
	}
	
	public void single_createattr_Product_internalOnly() throws JaloBusinessException
	{
		
						Map sqlColumnDefinitions = null;
					
				createPropertyAttribute(
					"Product", 
					"internalOnly",  
					null,
					"java.lang.Boolean",
					de.hybris.platform.jalo.type.AttributeDescriptor.READ_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.WRITE_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.OPTIONAL_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.REMOVE_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.SEARCH_FLAG,
					null,
					sqlColumnDefinitions
				);
			
	}
	
	public void single_createattr_Customer_isInternal() throws JaloBusinessException
	{
		
				createDynamicAttribute(
					"Customer", 
					"isInternal",  
					"java.lang.Boolean",
					null,
					de.hybris.platform.jalo.type.AttributeDescriptor.READ_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.WRITE_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.OPTIONAL_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.REMOVE_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.SEARCH_FLAG
				);
			
	}
	
	public void single_createattr_TurnToStaticContents_productId() throws JaloBusinessException
	{
		
						Map sqlColumnDefinitions = null;
					
				createPropertyAttribute(
					"TurnToStaticContents", 
					"productId",  
					null,
					"java.lang.String",
					de.hybris.platform.jalo.type.AttributeDescriptor.READ_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.WRITE_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.REMOVE_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.SEARCH_FLAG,
					null,
					sqlColumnDefinitions
				);
			
	}
	
	public void single_createattr_TurnToStaticContents_timestamp() throws JaloBusinessException
	{
		
						Map sqlColumnDefinitions = null;
					
				createPropertyAttribute(
					"TurnToStaticContents", 
					"timestamp",  
					null,
					"java.lang.Long",
					de.hybris.platform.jalo.type.AttributeDescriptor.READ_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.WRITE_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.REMOVE_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.SEARCH_FLAG,
					null,
					sqlColumnDefinitions
				);
			
	}
	
	public void single_createattr_TurnToStaticContents_qaContent() throws JaloBusinessException
	{
		
						Map sqlColumnDefinitions = new HashMap();
						
							sqlColumnDefinitions.put(
								"mysql",
								"LONGTEXT"
							);
						
				createPropertyAttribute(
					"TurnToStaticContents", 
					"qaContent",  
					null,
					"java.lang.String",
					de.hybris.platform.jalo.type.AttributeDescriptor.READ_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.WRITE_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.REMOVE_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.SEARCH_FLAG,
					null,
					sqlColumnDefinitions
				);
			
	}
	
	public void single_createattr_TurnToStaticContents_reviewsContent() throws JaloBusinessException
	{
		
						Map sqlColumnDefinitions = new HashMap();
						
							sqlColumnDefinitions.put(
								"mysql",
								"LONGTEXT"
							);
						
				createPropertyAttribute(
					"TurnToStaticContents", 
					"reviewsContent",  
					null,
					"java.lang.String",
					de.hybris.platform.jalo.type.AttributeDescriptor.READ_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.WRITE_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.REMOVE_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.SEARCH_FLAG,
					null,
					sqlColumnDefinitions
				);
			
	}
	
	public void single_createattr_TurnToColorVariantProduct_color() throws JaloBusinessException
	{
		
						Map sqlColumnDefinitions = null;
					
				createPropertyAttribute(
					"TurnToColorVariantProduct", 
					"color",  
					null,
					"localized:java.lang.String",
					de.hybris.platform.jalo.type.AttributeDescriptor.READ_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.WRITE_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.OPTIONAL_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.REMOVE_FLAG|de.hybris.platform.jalo.type.AttributeDescriptor.SEARCH_FLAG,
					"VariantAttributeDescriptor",
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
			"SwatchColorEnum",
			true,
			Arrays.asList( new String[] {
			
				"BLACK",
				"BLUE",
				"BROWN",
				"GREEN",
				"GREY",
				"ORANGE",
				"PINK",
				"PURPLE",
				"RED",
				"SILVER",
				"WHITE",
				"YELLOW"
			} )
		);
	
				{
				Map customPropsMap = new HashMap();
				
				setItemTypeProperties(
					"ApparelProduct",
					false,
					true,
					true,
					null,
					customPropsMap
				);
				}
			
			single_setAttributeProperties_ApparelProduct_genders();
		
				{
				Map customPropsMap = new HashMap();
				
				setItemTypeProperties(
					"ApparelStyleVariantProduct",
					false,
					true,
					true,
					null,
					customPropsMap
				);
				}
			
			single_setAttributeProperties_ApparelStyleVariantProduct_style();
		
			single_setAttributeProperties_ApparelStyleVariantProduct_swatchColors();
		
				{
				Map customPropsMap = new HashMap();
				
				setItemTypeProperties(
					"ApparelSizeVariantProduct",
					false,
					true,
					true,
					null,
					customPropsMap
				);
				}
			
			single_setAttributeProperties_ApparelSizeVariantProduct_size();
		
				{
				Map customPropsMap = new HashMap();
				
				setItemTypeProperties(
					"ElectronicsColorVariantProduct",
					false,
					true,
					true,
					null,
					customPropsMap
				);
				}
			
			single_setAttributeProperties_ElectronicsColorVariantProduct_color();
		
				{
				Map customPropsMap = new HashMap();
				
				changeMetaType(
					"Product",
					null,
					customPropsMap
				);
				}
			
			single_setAttributeProperties_Product_internalOnly();
		
				{
				Map customPropsMap = new HashMap();
				
				changeMetaType(
					"Customer",
					null,
					customPropsMap
				);
				}
			
			single_setAttributeProperties_Customer_isInternal();
		
				{
				Map customPropsMap = new HashMap();
				
				setItemTypeProperties(
					"TurnToStaticContents",
					false,
					true,
					true,
					null,
					customPropsMap
				);
				}
			
			single_setAttributeProperties_TurnToStaticContents_productId();
		
			single_setAttributeProperties_TurnToStaticContents_timestamp();
		
			single_setAttributeProperties_TurnToStaticContents_qaContent();
		
			single_setAttributeProperties_TurnToStaticContents_reviewsContent();
		
				{
				Map customPropsMap = new HashMap();
				
				setItemTypeProperties(
					"TurnToColorVariantProduct",
					false,
					true,
					true,
					null,
					customPropsMap
				);
				}
			
			single_setAttributeProperties_TurnToColorVariantProduct_color();
		
				setDefaultProperties(
					"GenderList",
					true,
					true,
					null
				);
			
				setDefaultProperties(
					"SwatchColorSet",
					true,
					true,
					null
				);
			
				setDefaultProperties(
					"SwatchColorEnum",
					true,
					true,
					null
				);
			
	}


		
						public void single_setAttributeProperties_ApparelProduct_genders() throws JaloBusinessException
						{
							
							
							
							Map customPropsMap = new HashMap();
							
							setAttributeProperties(
								"ApparelProduct", 
								"genders",
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
					
						public void single_setAttributeProperties_ApparelStyleVariantProduct_style() throws JaloBusinessException
						{
							
							
							
							Map customPropsMap = new HashMap();
							
							setAttributeProperties(
								"ApparelStyleVariantProduct", 
								"style",
								false, 
								null,
								null,
								null,
								true,
								true,
								"VariantAttributeDescriptor",
								customPropsMap,
								null
							);
						}
					
						public void single_setAttributeProperties_ApparelStyleVariantProduct_swatchColors() throws JaloBusinessException
						{
							
							
							
							Map customPropsMap = new HashMap();
							
							setAttributeProperties(
								"ApparelStyleVariantProduct", 
								"swatchColors",
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
					
						public void single_setAttributeProperties_ApparelSizeVariantProduct_size() throws JaloBusinessException
						{
							
							
							
							Map customPropsMap = new HashMap();
							
							setAttributeProperties(
								"ApparelSizeVariantProduct", 
								"size",
								false, 
								null,
								null,
								null,
								true,
								true,
								"VariantAttributeDescriptor",
								customPropsMap,
								null
							);
						}
					
						public void single_setAttributeProperties_ElectronicsColorVariantProduct_color() throws JaloBusinessException
						{
							
							
							
							Map customPropsMap = new HashMap();
							
							setAttributeProperties(
								"ElectronicsColorVariantProduct", 
								"color",
								false, 
								null,
								null,
								null,
								true,
								true,
								"VariantAttributeDescriptor",
								customPropsMap,
								null
							);
						}
					
						public void single_setAttributeProperties_Product_internalOnly() throws JaloBusinessException
						{
							
							
							
							Map customPropsMap = new HashMap();
							
							setAttributeProperties(
								"Product", 
								"internalOnly",
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
					
						public void single_setAttributeProperties_Customer_isInternal() throws JaloBusinessException
						{
							
							
							
							Map customPropsMap = new HashMap();
							
							setAttributeProperties(
								"Customer", 
								"isInternal",
								false, 
								null,
								null,
								null,
								true,
								true,
								null,
								customPropsMap,
								"dynamicHybrisCustomerAttributeBean"
							);
						}
					
						public void single_setAttributeProperties_TurnToStaticContents_productId() throws JaloBusinessException
						{
							
							
							
							Map customPropsMap = new HashMap();
							
							setAttributeProperties(
								"TurnToStaticContents", 
								"productId",
								true, 
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
					
						public void single_setAttributeProperties_TurnToStaticContents_timestamp() throws JaloBusinessException
						{
							
							
							
							Map customPropsMap = new HashMap();
							
							setAttributeProperties(
								"TurnToStaticContents", 
								"timestamp",
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
					
						public void single_setAttributeProperties_TurnToStaticContents_qaContent() throws JaloBusinessException
						{
							
							
							
							Map customPropsMap = new HashMap();
							
							setAttributeProperties(
								"TurnToStaticContents", 
								"qaContent",
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
					
						public void single_setAttributeProperties_TurnToStaticContents_reviewsContent() throws JaloBusinessException
						{
							
							
							
							Map customPropsMap = new HashMap();
							
							setAttributeProperties(
								"TurnToStaticContents", 
								"reviewsContent",
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
					
						public void single_setAttributeProperties_TurnToColorVariantProduct_color() throws JaloBusinessException
						{
							
							
							
							Map customPropsMap = new HashMap();
							
							setAttributeProperties(
								"TurnToColorVariantProduct", 
								"color",
								false, 
								null,
								null,
								null,
								true,
								true,
								"VariantAttributeDescriptor",
								customPropsMap,
								null
							);
						}
					
}

	