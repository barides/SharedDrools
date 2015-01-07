using GigaSpaces.Core.Metadata;
using System;

namespace Drools.Common.Entities
{
    [Serializable]
    [SpaceClass(AliasName = "com.c123.demo.real.BaseFact")]
    public class BaseFact : BaseJavaSpaceEntity
    {
            public BaseFact()
            {
               
            }

            public BaseFact(BaseFact originFact)
            {
                CorrolationID = originFact.CorrolationID;
                NetworkID = originFact.NetworkID;
                RequestReference = originFact.RequestReference;
                SkinID = originFact.SkinID;
                this.FactState = Drools.Common.Entities.FactState.NEW;
                this.DateTime = originFact.DateTime;
            }

            #region Fields

            #endregion Fields

            #region Properties

            [SpaceProperty(AliasName = "corrolationId")]
            public Guid? CorrolationID { get; set; }
            [SpaceProperty(AliasName = "requestReference")]
            public Guid? RequestReference { get; set; }
            [SpaceProperty(AliasName = "networkId")]
            public int? NetworkID { get; set; }
            [SpaceProperty(AliasName = "skinId")]
            public int? SkinID { get; set; }
            [SpaceProperty(AliasName = "state")]
            public FactState? FactState { get; set; }
            [SpaceProperty(AliasName = "date")]
            public DateTime? DateTime { get; set; }

            #endregion Properties
        }
}
