using System.Xml.Serialization;
using GigaSpaces.Core.Document;
using GigaSpaces.Core.Metadata;
using System;

namespace Drools.Common.Entities
{
    /// <summary>
    /// Provides basic functionality to all classes which reside in GigaSpace.
    /// </summary>
    [Serializable]
    [SpaceClass(AliasName = "com.c123.demo.real.BaseJavaSpaceEntity")]
    public class BaseJavaSpaceEntity
    {
               /// <summary>
        /// Initializes a new instance of the BaseSpaceEntity class.
        /// </summary>
        protected BaseJavaSpaceEntity()
        {
            // ReSharper disable DoNotCallOverridableMethodsInConstructor - 
            // There's no problem initializing the dictionary here because the inheriting classes will only modify its attributes
            // this.DocumentProperties = new DocumentProperties();
            // ReSharper restore DoNotCallOverridableMethodsInConstructor
        }

        /// <summary>
        /// The id to use when working with the space.
        /// <para>This value is built from the properties defined in SpaceIdComponents.</para>
        /// </summary>
        [SpaceID(AutoGenerate = false)]
        [SpaceProperty(AliasName = "id")]
        public string Id
        {
            get { return string.Join("|", this.IdComponents); }
        }

        /// <summary>
        /// Gets the routing id which use to determines which GigaSpace partition to store this instance.
        /// </summary>
        [SpaceRouting]
        [SpaceProperty(AliasName = "routingId")]
        public virtual int? RoutingId { get; set; }
       
        /// <summary>
        /// This property is used by the space proxy to track the changes of the entity.
        /// It should not be updated manually.
        /// </summary>
        [SpaceExclude]
        public int Version { get; set; }

        /// <summary>
        /// A dictionary of dynamic properties.
        /// <para>Properties in this dictionary do not affect the entity schema, meaning we can add and remove properties without having to restart the cluster.</para>
        /// <para>This property can also be overridden in order to add indexes using the SpaceIndex attribute. </para>
        /// <para>For example this attribute will add an index for the property "Key":</para>
        /// <para>[SpaceIndex(Path = "Key", Type = SpaceIndexType.Basic)]</para>
        /// </summary>

        [XmlIgnore]
        [SpaceDynamicProperties]
         public DocumentProperties DocumentProperties { get; set; }

        /// <summary>
        /// Gets or sets the properties that compose the SpaceID. The default is routing id.
        /// </summary>
        [SpaceExclude]
        protected virtual object[] IdComponents
        {
            get { return new object[] { this.RoutingId }; }
        }

        
        //#region GettersAndSetters for using DocumentProperties
        
        ///// <summary>
        ///// Gets a value from DocumentProperties dictionary for the specified property key.
        ///// </summary>
        ///// <param name="key">The property key.</param>
        ///// <returns>The value.</returns>
        ///// 
        //protected object GetDocumentProperty([CallerMemberName]string key = null)
        //{
        //    if(string.IsNullOrWhiteSpace(key)) return null;
        //    if(!this.DocumentProperties.ContainsKey(key))
        //    {
        //        return null;
        //    }
        //    return this.DocumentProperties[key];
        //}
        
        ///// <summary>
        ///// Sets the specified value of the specified property key in DocumentProperties dictionary.
        ///// </summary>
        ///// <param name="value">The value to be set.</param>
        ///// <param name="key">The property key.</param>
        //protected void SetDocumentProperty(object value, [CallerMemberName]string key = null)
        //{
        //    if(key != null && !this.DocumentProperties.ContainsKey(key))
        //    {
        //        this.DocumentProperties.Add(key, value);
        //    }
        //    else
        //    {
        //        if (key != null)
        //        {
        //            this.DocumentProperties[key] = value;
        //        }
        //    }
        //}

        //#endregion

    }

    /// <summary>
    /// Provides basic functionality to all classes which reside in GigaSpace.
    /// </summary>
    /// <typeparam name="TId">The type of id.</typeparam>
    [Serializable]
    [SpaceClass]
    public abstract class BaseJavaSpaceEntity<TId> : BaseSpaceEntity
    {
        private TId id;

        /// <summary>
        /// Gets or sets the id of the entity to be used under GigaSpace.
        /// </summary>
        public new virtual TId Id
        {
            get
            {
                return this.id;
            }
            set
            {
                this.id = value;
            }
        }

        /// <summary>
        /// Gets or sets the properties that compose the SpaceID. The default set are routing id and id.
        /// </summary>
        [SpaceExclude]
        protected override object[] IdComponents
        {
            get { return new object[] { this.RoutingId, this.Id }; }
            // get { return new object[] { 3, this.Id }; }
        }
    }
}
