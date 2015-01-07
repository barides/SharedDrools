using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using GigaSpaces.Core.Metadata;

namespace Drools.Common.Entities
{

    [SpaceClass(AliasName = "com.c123.demo.real.FactContainer")]
    [Serializable]
    class FactContainer
    {
        [SpaceProperty(AliasName = "fact", StorageType = StorageType.Object)]
        public BaseFact Fact { get; set; }

        [SpaceProperty(AliasName = "customer", StorageType = StorageType.Object)]
        public Customer Customer { get; set; }
    }
}
