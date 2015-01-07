using GigaSpaces.Core.Metadata;
using System;

namespace Drools.Common.Entities
{
    [Serializable]
    [SpaceClass(AliasName = "com.c123.demo.real.BaseAccountOperationFact")]
    public class BaseAccountOperationFact : BaseCustomerFact
    {
        [SpaceProperty(AliasName = "actualAmount")]
        public decimal? ActualAmount { get; set; }


        public BaseAccountOperationFact()
        {

        }

        public BaseAccountOperationFact(BaseAccountOperationFact originFact)
            : base(originFact)
        {
            ActualAmount = originFact.ActualAmount;
            DateTime = originFact.DateTime;
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
