using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using GigaSpaces.Core.Metadata;

namespace Drools.Common
{

    [SpaceClass(AliasName = "com.c123.demo.real.Customer")]
    [Serializable]
    public class Customer
    {
        [SpaceID(AutoGenerate = false)]
        [SpaceProperty(AliasName = "id")]
        public int? Id { get; set; }

        [SpaceProperty(AliasName = "name")]
        public string Name { get; set; }

        [SpaceProperty(AliasName = "address")]
        public string Address { get; set; }

        [SpaceProperty(AliasName = "mobilePhone")]
        public string MobilePhone { get; set; }

        [SpaceRouting]
        [SpaceProperty(AliasName = "networkId")]
        public int? NetworkId { get; set; }

        public override string ToString()
        {
            return "Customer (Id=" + Id + ",Name=" + Name + ",Address=" + Address + ",MobilePhone=" + MobilePhone + ")";
        }

    }
}
