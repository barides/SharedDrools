
namespace Drools.Common.Entities
{
    interface IDistributedCacheEntity
    {
        /// <summary>
        /// The id of the entity is needed in order to find it in cache.
        /// </summary>
        string Id { get; }

        /// <summary>
        /// Use to determines under which cache partition to store this instance.
        /// </summary>
        int RoutingId { get; set; }

        /// <summary>
        /// Use by the cache platform to track the changes of this instance.
        /// </summary>
        int Version { get; set; }
    }
}
