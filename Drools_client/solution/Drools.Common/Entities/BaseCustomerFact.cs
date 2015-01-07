using GigaSpaces.Core.Metadata;
using System;

namespace Drools.Common.Entities
{
    [Serializable]
    [SpaceClass(AliasName = "com.c123.demo.real.BaseCustomerFact")]
    public class BaseCustomerFact : BaseFact
    {
        [SpaceProperty(AliasName = "customerId")]
        public int? CustomerID { get; set; }

        public BaseCustomerFact()
        {

        }

        public BaseCustomerFact(BaseAccountOperationFact originFact)
            : base(originFact)
        {
            CustomerID = originFact.CustomerID;
        }


        // To verify that we will not duplicate facts, this is the unique definition of a fact.
        protected override object[] IdComponents
        {
            get
            {
                return new object[]
                {
                    this.NetworkID, this.RequestReference
                };
            }
        }
    }
}
